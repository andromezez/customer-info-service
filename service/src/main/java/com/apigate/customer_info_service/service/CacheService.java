package com.apigate.customer_info_service.service;

import com.apigate.customer_info_service.entities.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author Bayu Utomo
 * @date 23/6/2021 9:20 AM
 */
@Service
public class CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void createCache(String key, String value, int expireInSeconds){
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, Duration.ofSeconds(expireInSeconds));
    }

    public void createCache(Routing routing, String value){
        createCache(routing.getRedisKey(), value, routing.getCachePeriod());
    }

    public String getFromCache(String key){
        return redisTemplate.opsForValue().get(key);
    }
}
