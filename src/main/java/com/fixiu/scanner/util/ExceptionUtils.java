package com.fixiu.scanner.util;

import java.sql.SQLException;

/**
 * Utility class for dealing with exceptions.
 */
public class ExceptionUtils {
    /**
     * Prevents instantiation.
     */
    private ExceptionUtils() {
        //Do nothing
    }

    /**
     * Returns the root cause of this throwable.
     *
     * @param throwable The throwable to inspect.
     * @return The root cause or the throwable itself if it doesn't have a cause.
     */
    public static Throwable getRootCause(Throwable throwable) {
        if (throwable == null) {
            return null;
        }

        Throwable cause = throwable;
        Throwable rootCause;
        while ((rootCause = cause.getCause()) != null) {
            cause = rootCause;
        }

        return cause;
    }

    /**
     * Retrives the exact location where this exception was thrown.
     *
     * @param e The exception.
     * @return The location, suitable for a debug message.
     */
    public static String getThrowLocation(Throwable e) {
        StackTraceElement element = e.getStackTrace()[0];
        int lineNumber = element.getLineNumber();
        return element.getClassName() + "." + element.getMethodName()
                + (lineNumber < 0 ? "" : ":" + lineNumber)
                + (element.isNativeMethod() ? " [native]" : "");
    }

    /**
     * Transforms the details of this SQLException into a nice readable message.
     *
     * @param e The exception.
     * @return The message.
     */
    public static String toMessage(SQLException e) {
        SQLException cause = e;
        while (cause.getNextException() != null) {
            cause = cause.getNextException();
        }

        String message = "SQL State  : " + cause.getSQLState() + "\n"
                + "Error Code : " + cause.getErrorCode() + "\n";
        if (cause.getMessage() != null) {
            message += "Message    : " + cause.getMessage().trim() + "\n";
        }

        return message;

    }
}