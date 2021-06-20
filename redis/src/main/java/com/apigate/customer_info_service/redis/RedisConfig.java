package com.apigate.customer_info_service.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Bayu Utomo
 * @date 17/6/2021 9:42 AM
 */
@Configuration
@Component
@PropertySource("classpath:redis.properties")
public class RedisConfig {
}
