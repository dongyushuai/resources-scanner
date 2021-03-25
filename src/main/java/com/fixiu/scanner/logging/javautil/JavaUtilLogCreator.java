package com.fixiu.scanner.logging.javautil;

import java.util.logging.Logger;

import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogCreator;

/**
 * Log Creator for java.util.logging.
 */
public class JavaUtilLogCreator implements LogCreator {
    public Log createLogger(Class<?> clazz) {
        return new JavaUtilLog(Logger.getLogger(clazz.getName()));
    }
}