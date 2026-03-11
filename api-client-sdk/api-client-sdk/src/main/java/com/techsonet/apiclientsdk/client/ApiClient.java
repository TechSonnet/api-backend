package com.techsonet.apiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.techsonet.apiclientsdk.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * 调用第三方接口的客户端
 *
 * @author chang
 */

@Data
public class ApiClient {

    private String accessKey;

    private String secretKey;

    public ApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    // 使用GET方法从服务器获取名称信息
    public String getNameByGet(String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        //HashMap<String, Object> paramMap = new HashMap<>();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        // 将"name"参数添加到映射中
        paramMap.put("name", name);
        // 使用HttpUtil工具发起GET请求，并获取服务器返回的结果
        String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        // 打印服务器返回的结果
        System.out.println(result);
        // 返回服务器返回的结果
        return result;
    }

    // 使用POST方法从服务器获取名称信息
    public String getNameByPost(String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        //HashMap<String, Object> paramMap = new HashMap<>();
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name", name);
        // 使用HttpUtil工具发起POST请求，并获取服务器返回的结果
        String result= HttpUtil.post("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    // 使用POST方法向服务器发送User对象，并获取服务器返回的结果
    /**
     * 调用接口的方法
     */
    public String getUserNameByPost(User user) {
        // 1. 准备随机数和时间戳
        String nonce = RandomUtil.randomNumbers(4);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        // 2. 构造 Header 并计算签名
        // 注意：拼接顺序必须和服务端完全一致！
        //Map<String, String> headers = new HashMap<>();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("accessKey", accessKey);
        headers.put("nonce", nonce);
        headers.put("timestamp", timestamp);

        // 计算签名：userAccount + . + timestamp + . + nonce + . + secretKey
        String content = user.getUserAccount() + "." + timestamp + "." + nonce + "." + secretKey;
        String sign = DigestUtil.md5Hex(content);
        headers.put("sign", sign);

        // 3. 发送 POST 请求 (使用 Hutool)
        return HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(headers)
                .body(JSONUtil.toJsonStr(user)) // 传递完整的 User 对象给 @RequestBody
                .execute()
                .body();
    }
}
