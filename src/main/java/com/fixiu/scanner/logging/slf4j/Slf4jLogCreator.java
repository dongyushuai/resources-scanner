package com.fixiu.scanner.logging.slf4j;

import org.slf4j.LoggerFactory;

import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogCreator;

/**
 * Log Creator for Slf4j.
 */
public class Slf4jLogCreator implements LogCreator {
    public Log createLogger(Class<?> clazz) {
        return new Slf4jLog(LoggerFactory.getLogger(clazz.getName()));
    }
}