package com.sonnet.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 */
@Slf4j
@Configuration
public class RedisConfig {

    /**
     * 实例化 RedisTemplate 对象
     * 设置序列化方式
     * 默认序列化为：JdkSerializationRedisSerializer
     * JdkSerializationRedisSerializer       占用空间小，序列化速度慢，客户端可读性差
     * GenericJackson2JsonRedisSerializer    占用空间大，序列化速度快，客户端可读性好
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        log.info("redisTemplate init. serializer=GenericJackson2JsonRedisSerializer");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置 key 的序列化器 StringRedisSerializer
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // 设置 value 的序列化器 GenericJackson2JsonRedisSerializer
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }
}
