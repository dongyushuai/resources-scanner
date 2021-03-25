package com.fixiu.scanner.logging.apachecommons;

import org.apache.commons.logging.LogFactory;

import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogCreator;

/**
 * Log Creator for Apache Commons Logging.
 */
public class ApacheCommonsLogCreator implements LogCreator {
    public Log createLogger(Class<?> clazz) {
        return new ApacheCommonsLog(LogFactory.getLog(clazz));
    }
}