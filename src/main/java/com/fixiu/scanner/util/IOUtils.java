package com.fixiu.scanner.util;

/**
 * General IO-related utilities.
 */
public class IOUtils {
    private IOUtils() {
    }

    /**
     * Closes this closeable and never fail while doing so.
     *
     * @param closeable The closeable to close. Can be {@code null}.
     */
    public static void close(AutoCloseable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (Exception e) {
            // Ignore
        }
    }
}