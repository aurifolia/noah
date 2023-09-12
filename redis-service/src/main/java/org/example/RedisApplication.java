package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/6
 **/
@SpringBootApplication
public class RedisApplication implements ApplicationRunner {
    @Autowired
    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        String[] keys = {"a", "b", "c", "d"};
        func(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operation) throws DataAccessException {
                return null;
            }
        });
        System.out.println();
    }


    public void func(SessionCallback callback) {

    }
}
