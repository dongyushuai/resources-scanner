package com.fixiu.scanner.logging.android;

import com.fixiu.scanner.logging.Log;
import com.fixiu.scanner.logging.LogCreator;

/**
 * Log Creator for Android.
 */
public class AndroidLogCreator implements LogCreator {
    public Log createLogger(Class<?> clazz) {
        return new AndroidLog();
    }
}