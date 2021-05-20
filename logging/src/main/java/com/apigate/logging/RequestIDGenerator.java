package com.apigate.logging;

import java.util.UUID;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class RequestIDGenerator {

    public static String generateId(){
        return UUID.randomUUID().toString();
    }
}
