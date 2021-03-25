package com.fixiu.scanner.logging;

/**
 * Factory for loggers
 */
public class LogFactory {
    /**
     * Factory for implementation-specific loggers.
     */
    private static LogCreator logCreator;

    /**
     * The factory for implementation-specific loggers to be used as a fallback when no other suitable loggers were found.
     */
    private static LogCreator fallbackLogCreator;

    /**
     * Prevent instantiation.
     */
    private LogFactory() {
        // Do nothing
    }

    /**
     * Sets the LogCreator that will be used. 
     *
     * @param logCreator The factory for implementation-specific loggers.
     */
    public static void setLogCreator(LogCreator logCreator) {
        LogFactory.logCreator = logCreator;
    }

    /**
     * Sets the fallback LogCreator.
     * @param fallbackLogCreator The factory for implementation-specific loggers to be used as a fallback when no other
     *                           suitable loggers were found.
     */
    public static void setFallbackLogCreator(LogCreator fallbackLogCreator) {
        LogFactory.fallbackLogCreator = fallbackLogCreator;
    }

    /**
     * Retrieves the matching logger for this class.
     *
     * @param clazz The class to get the logger for.
     * @return The logger.
     */
    public static Log getLog(Class<?> clazz) {
        if (logCreator == null) {
            logCreator = LogCreatorFactory.getLogCreator(LogFactory.class.getClassLoader(), fallbackLogCreator);
        }

        return logCreator.createLogger(clazz);
    }
}