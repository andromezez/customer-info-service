package com.apigate.utils;

import com.apigate.exceptions.internal.ExhaustedResourceException;
import com.apigate.config.Config;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class ObjectMapperUtils {
    private static final int MAX_POOL_SIZE = Config.getApigateServerThreadsMax()+10;
    private static final ObjectPool<ObjectMapper> MAPPER_POOL = new GenericObjectPool<>(new ObjectMapperPoolFactory(),
            new MapperPoolConfig(getMaxPoolSize()),
            new AbandonedPoolConfig());

    private ObjectMapperUtils(){}

    public static ObjectMapper getMapperInstance() throws ExhaustedResourceException {
        try {
            return MAPPER_POOL.borrowObject();
        } catch (Exception e) {
            throw new ExhaustedResourceException(e);
        }
    }

    public static void returnToPool(ObjectMapper objectMapper){
        try {
            if(objectMapper != null){
                MAPPER_POOL.returnObject(objectMapper);
            }
        } catch (Exception e) {
            // ignored
        }
    }

    public static String parseObjectToJsonString(Object obj) throws ExhaustedResourceException, JsonProcessingException {
        if(obj instanceof String) {
            return (String) obj;
        }
        var mapper = getMapperInstance();
        var result =  "";
        try {
            result = mapper.writeValueAsString(obj);
        }finally {
            returnToPool(mapper);
        }
        return result;
    }

    public static int getNumIdle(){
        return MAPPER_POOL.getNumIdle();
    }

    public static int getNumActive(){
        return MAPPER_POOL.getNumActive();
    }

    public static int getMaxPoolSize() {
        return MAX_POOL_SIZE;
    }
}
