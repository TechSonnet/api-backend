package com.techsonet.apiinterface.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void testRedisConnection() {
        // 1. 尝试写入一条数据
        String key = "test:connection";
        String value = "hello redis";
        redisTemplate.opsForValue().set(key, value);

        // 2. 尝试读取出来
        Object result = redisTemplate.opsForValue().get(key);

        // 3. 断言验证
        System.out.println("Redis 返回结果: " + result);
        assert value.equals(result);
    }
}
