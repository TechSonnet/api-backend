package com.techsonet.apiinterface.controller;

import cn.hutool.core.util.StrUtil;
import com.techsonet.apiinterface.domain.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/name")
public class UserController {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 获取一个参数名字
     * @param name
     * @return
     */
    @GetMapping("/")
    public String getNameByGet(String name){
        return "get: your name is " + name;
    }

    /**
     * 获取一个参数名字
     * @param name
     * @return
     */
    @PostMapping("/")
    public String getNameByPost(@RequestParam String name){
        return "post: your name is " + name;
    }


    /**
     * 获取一个参数名字
     * @param user
     * @return
     */
    @PostMapping("/user")
    public String getNameByPost(@RequestBody User user, HttpServletRequest request){

        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");

        // 服务端验签
        // 1. 基本校验：确保参数都不为空
        if (StrUtil.hasBlank(accessKey, nonce, timestamp, sign)) {
            throw new RuntimeException("无权访问：请求参数不完整");
        }
        // 2. 查出真实的 SecretKey（假设从数据库查）
        // 注意：不能从 Header 取，要从服务端根据 accessKey 查
        String serverSecretKey = getSecretKeyFromDb(accessKey);
        if (serverSecretKey == null) {
            throw new RuntimeException("无权访问：AccessKey 无效");
        }
        // 3. 时间戳校验：防过期（允许前后 5 分钟误差）
        long currentTime = System.currentTimeMillis() / 1000;
        long requestTime = Long.parseLong(timestamp);
        if (Math.abs(currentTime - requestTime) > 300) {
            throw new RuntimeException("无权访问：请求已过期");
        }
        // 4. Nonce 校验：防重放（借助 Redis）
        String redisKey = "api:nonce:" + nonce;
        Boolean isAbsent = redisTemplate.opsForValue().setIfAbsent(redisKey, "1", 5, TimeUnit.MINUTES);
        if (isAbsent == null || !isAbsent) {
            throw new RuntimeException("无权访问：请求重复");
        }
        // 5. 服务端复算签名
        // 这里的拼接规则必须和客户端完全一致，建议写成工具类
        String content = user.getUserAccount() + "." + timestamp + "." + nonce + "." + serverSecretKey;
        String serverSign = DigestUtils.md5DigestAsHex(content.getBytes());
        // 6. 比对签名
        if (!serverSign.equals(sign)) {
            throw new RuntimeException("无权访问：签名校验失败");
        }

        return "post: your name is " + user.getUserName();
    }

    private String getSecretKeyFromDb(String accessKey) {
        return "c6b1e5e953cc920fdb2c12bc9c78ed81";
    }


}
