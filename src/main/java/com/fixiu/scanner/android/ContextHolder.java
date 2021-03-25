package com.fixiu.scanner.android;

import android.content.Context;

/**
 * Holds an Android context. The context must be set for Scanner to be able to scan assets and classes.
 *
 * <p>
 *     You can set this within an activity using ContextHolder.setContext(this);
 * </p>
 */
public class ContextHolder {
    private ContextHolder() {}

    /**
     * The Android context to use.
     */
    private static Context context;

    /**
     * @return The Android context to use to be able to scan assets and classes.
     */
    public static Context getContext() {
        return context;
    }

    /**
     * @param context The Android context to use to be able to scan assets and classes.
     */
    public static void setContext(Context context) {
        ContextHolder.context = context;
    }
}