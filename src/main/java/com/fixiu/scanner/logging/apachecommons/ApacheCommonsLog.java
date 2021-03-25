package com.fixiu.scanner.logging.apachecommons;

import com.fixiu.scanner.logging.Log;

/**
 * Wrapper for an Apache Commons Logging logger.
 */
public class ApacheCommonsLog implements Log {
    /**
     * Apache Commons Logging Logger.
     */
    private final org.apache.commons.logging.Log logger;

    /**
     * Creates a new wrapper around this logger.
     *
     * @param logger The original Apache Commons Logging Logger.
     */
    public ApacheCommonsLog(org.apache.commons.logging.Log logger) {
        this.logger = logger;
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void warn(String message) {
        logger.warn(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Exception e) {
        logger.error(message, e);
    }
}