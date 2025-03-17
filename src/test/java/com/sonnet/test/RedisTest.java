package com.sonnet.test;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    public void testRedis() {

        // add
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("key", "value222");

        // update
        valueOperations.set("key", "value555");

        // search
        Object value = valueOperations.get("key");
        System.out.println((String) value);

        // delete
//        Boolean result = redisTemplate.delete("key");
//        System.out.println(result);
    }
}
