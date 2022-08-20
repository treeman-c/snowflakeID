package com.treeman.snowflake;

import com.treeman.snowflake.exception.SeqException;
import com.treeman.snowflake.tool.SnowFlakeTool;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class SnowflakeApplication {

    /** 雪花ID生成bean对象*/
    @Bean
    public SnowFlakeTool getSnowFlakeTool() throws SeqException {
        return new SnowFlakeTool(5L,5L);  //修改自己对应的机器码和数据码获取对应的bean对象
    }
    /**redis的bean对象*/
    @Bean
    public RedisTemplate<String,String> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
    public static void main(String[] args) {
        SpringApplication.run(SnowflakeApplication.class, args);
    }

}
