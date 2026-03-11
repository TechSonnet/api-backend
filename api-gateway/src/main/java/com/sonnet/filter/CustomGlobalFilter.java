package com.sonnet.filter;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.sonnet.apicommon.model.entity.User;
import com.sonnet.apicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;

    // 这里应该用前端传过来的参数进行后端查询，此处为了快速测试 RPC 使用测试数据
    private final String accessKey = "0aa00d375d34cdcdba0a6201692fc5f0";
    String secretKey = "c6b1e5e953cc920fdb2c12bc9c78ed81";
    String nonce = RandomUtil.randomNumbers(4);
    String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
    String userAccount = "admin";
    String content_01 = userAccount + "." + timestamp + "." + nonce + "." + secretKey;
    String sign = DigestUtils.md5DigestAsHex(content_01.getBytes());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 记录请求日志
        recordLogs(exchange);

        // RPC 获取对应用户
        User invokeUser = innerUserService.getInvokeUser(accessKey);

        // 用户鉴权
        checkAuth(invokeUser);


        return chain.filter(exchange);
    }

    /**
     * 记录日志
     *
     * @param exchange
     */
    private static void recordLogs(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + path);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);
        log.info("请求来源地址：" + request.getRemoteAddress());
        ServerHttpResponse response = exchange.getResponse();
    }

    /**
     * 用户鉴权
     *
     * @param invokeUser
     */
    private void checkAuth(User invokeUser) {
        // 1. 基本校验：确保参数都不为空
        if (StrUtil.hasBlank(accessKey, nonce, timestamp, sign)) {
            throw new RuntimeException("无权访问：请求参数不完整");
        }
        // 2. 查出真实的 SecretKey（假设从数据库查）
        // 注意：不能从 Header 取，要从服务端根据 accessKey 查
        String serverSecretKey = invokeUser.getSecretKey();
        if (serverSecretKey == null) {
            throw new RuntimeException("无权访问：AccessKey 无效");
        }
        // 3. 时间戳校验：防过期（允许前后 5 分钟误差）
        long currentTime = System.currentTimeMillis() / 1000;
        long requestTime = Long.parseLong(timestamp);
        if (Math.abs(currentTime - requestTime) > 300) {
            throw new RuntimeException("无权访问：请求已过期");
        }
        // 4. Nonce 校验：防重放（可借助 Redis）
        String redisKey = "api:nonce:" + nonce;

        // 5. 服务端复算签名
        // 这里的拼接规则必须和客户端完全一致，建议写成工具类
        String content = invokeUser.getUserAccount() + "." + timestamp + "." + nonce + "." + serverSecretKey;
        String serverSign = DigestUtils.md5DigestAsHex(content.getBytes());
        // 6. 比对签名
        if (!serverSign.equals(sign)) {
            throw new RuntimeException("无权访问：签名校验失败");
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }


    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long userId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7. 调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            log.info("invokeCount by RPC");
                                        } catch (Exception e) {
                                            log.error("invokeCount error", e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}
