package com.apigate.customer_info_service.service;

import com.apigate.customer_info_service.entities.Routing;
import com.apigate.logging.ServicesLog;
import org.apache.commons.lang3.StringUtils;
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
        try{
            redisTemplate.opsForValue().set(key, value);
            redisTemplate.expire(key, Duration.ofSeconds(expireInSeconds));
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
    }

    public void createRoutingResponseCache(Routing routing, String msisdn, String responseBody) throws IllegalArgumentException{
        createCache(getRoutingResponseCacheRedisKey(routing, msisdn), responseBody, routing.getCachePeriod());
    }

    public String getFromCache(String key){
        try{
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
        return "";
    }

    public String getRoutingResponseCache(Routing routing, String msisdn) throws IllegalArgumentException{
        return getFromCache(getRoutingResponseCacheRedisKey(routing, msisdn));
    }

    private String getRoutingResponseCacheRedisKey(Routing routing, String msisdn) throws IllegalArgumentException{
        if(StringUtils.isBlank(msisdn)){
            throw new IllegalArgumentException("getRoutingResponseCacheRedisKey doesn't allow empty msisdn");
        }
        return routing.getRedisKey()+":"+msisdn;
    }

    public boolean removeCache(String key){
        try{
            return redisTemplate.delete(key);
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
        return false;
    }

    public Duration getRemainingTTL(String key){
        try{
            long remainingTTL = redisTemplate.getExpire(key);
            if(remainingTTL < 1) {
                return Duration.ZERO;
            }
            return Duration.ofSeconds(remainingTTL);
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
        return Duration.ZERO;
    }
}
