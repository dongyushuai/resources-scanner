package com.fixiu.scanner.logging;

import com.fixiu.scanner.logging.android.AndroidLogCreator;
import com.fixiu.scanner.logging.apachecommons.ApacheCommonsLogCreator;
import com.fixiu.scanner.logging.javautil.JavaUtilLogCreator;
import com.fixiu.scanner.logging.slf4j.Slf4jLogCreator;
import com.fixiu.scanner.util.ClassUtils;
import com.fixiu.scanner.util.FeatureDetector;

public class LogCreatorFactory {
    /**
     * Prevent instantiation.
     */
    private LogCreatorFactory() {
        // Do nothing
    }

    public static LogCreator getLogCreator(ClassLoader classLoader, LogCreator fallbackLogCreator) {
        FeatureDetector featureDetector = new FeatureDetector(classLoader);
        if (featureDetector.isAndroidAvailable()) {
            return ClassUtils.instantiate(AndroidLogCreator.class.getName(), classLoader);
        }
        if (featureDetector.isSlf4jAvailable()) {
            return ClassUtils.instantiate(Slf4jLogCreator.class.getName(), classLoader);
        }
        if (featureDetector.isApacheCommonsLoggingAvailable()) {
            return ClassUtils.instantiate(ApacheCommonsLogCreator.class.getName(), classLoader);
        }
        if (fallbackLogCreator == null) {
            return new JavaUtilLogCreator();
        }
        return fallbackLogCreator;
    }
}