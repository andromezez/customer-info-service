package com.apigate.logging;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public abstract class AbstractLogger implements Logger{

    protected String parseError(Exception e){ //ExceptionUtils.getStackTrace(e); replace the content of the method with this
        /*StringBuilder errors = new StringBuilder();
        errors.append("Exception : " + e.getClass() + "\n");
        errors.append("Caused by : " + e.getCause() + "\n");
        errors.append(e.getMessage() + "\n");
        for(StackTraceElement stackTraceElement : Arrays.asList(e.getStackTrace())){
            errors.append("\t"+stackTraceElement.toString()+"\n");
        }
        return errors;*/

        return ExceptionUtils.getStackTrace(e);
    }
}
