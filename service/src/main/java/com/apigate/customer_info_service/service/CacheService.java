package com.apigate.customer_info_service.service;

import com.apigate.config.Config;
import com.apigate.logging.ServicesLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bayu Utomo
 * @date 23/6/2021 9:20 AM
 */
@Service
public class CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean createLock(String key){
        try{
            var result = redisTemplate.opsForValue().setIfAbsent(key, "true", Config.TOKEN_PROCESSING_LOCK_EXPIRY);
            if(Boolean.TRUE.equals(result)){
                return true;
            }
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
        return false;
    }

    public boolean removeLock(String key){
        return removeCache(key);
    }

    public boolean isLocked(String key){
        return StringUtils.isNotBlank(getFromCache(key));
    }

    public void createCache(String key, String value, int expireInSeconds){
        try{
            redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expireInSeconds));
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
    }

    public String getFromCache(String key){
        try{
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
        return "";
    }

    public boolean removeCache(String key){
        try{
            var result = redisTemplate.delete(key);
            if(Boolean.TRUE.equals(result)){
                ServicesLog.getInstance().logInfo("Cache " + key + " successfully removed");
                return true;
            }
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
        ServicesLog.getInstance().logInfo("Can't remove cache " + key);
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

    public Set<String> getKeys(String pattern){
        try{
            return redisTemplate.keys(pattern);
        }catch (Exception e){
            ServicesLog.getInstance().logError(e);
        }
        return new HashSet<>(0);
    }
}
