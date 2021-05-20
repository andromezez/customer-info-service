package com.apigate.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class MapperPoolConfig extends GenericObjectPoolConfig<ObjectMapper> {
    public MapperPoolConfig(int max){
        super();
        setMaxTotal(max);
        setMaxIdle(max);
        setMaxWaitMillis(1000);
    }
}
