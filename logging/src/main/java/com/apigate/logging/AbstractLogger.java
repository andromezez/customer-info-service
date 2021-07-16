package com.apigate.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public abstract class AbstractLogger implements Logger{

    protected String parseError(Exception e){
        return ExceptionUtils.getStackTrace(e);
    }
}
