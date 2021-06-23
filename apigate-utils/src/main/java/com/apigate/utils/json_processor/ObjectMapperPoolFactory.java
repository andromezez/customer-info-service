package com.apigate.utils.json_processor;

import com.apigate.config.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.text.SimpleDateFormat;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class ObjectMapperPoolFactory extends BasePooledObjectFactory<ObjectMapper> {

    @Override
    public ObjectMapper create() throws Exception {
        return new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .setDateFormat(new SimpleDateFormat(Config.TIMESTAMP_PATTERN));
    }

    @Override
    public PooledObject<ObjectMapper> wrap(ObjectMapper obj) {
        return new DefaultPooledObject<>(obj);
    }

}
