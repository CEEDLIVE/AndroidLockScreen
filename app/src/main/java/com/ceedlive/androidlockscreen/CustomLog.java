package com.ceedlive.androidlockscreen;

import android.util.Log;

public class CustomLog {

    private static String TAG = "CEEDLIVE";

    private static boolean isLoggable() {
        boolean isLoggable;
        try {
            isLoggable = Log.isLoggable(TAG, Log.DEBUG);
        } catch (IllegalArgumentException e) {
            isLoggable = false;
        }

        return isLoggable;
    }

    /**
     * Log Level Verbose
     * @param message
     */
    @SuppressWarnings("unused")
    public static void verbose(String message) {
        if (BuildConfig.DEBUG) {
            writeLog(Log.VERBOSE, TAG, message);
        }
    }

    /**
     * Log Level Debug
     * @param message
     */
    @SuppressWarnings("unused")
    public static void debug(String message) {
        if (BuildConfig.DEBUG) {
            writeLog(Log.DEBUG, TAG, message);
        }
    }

    /**
     * Log Level Information
     * @param message
     */
    @SuppressWarnings("unused")
    public static void info(String message) {
        if (BuildConfig.DEBUG) {
            writeLog(Log.INFO, TAG, message);
        }
    }

    /**
     * Log Level Warning
     * @param message
     */
    @SuppressWarnings("unused")
    public static void warning(String message) {
        if (BuildConfig.DEBUG) {
            writeLog(Log.WARN, TAG, message);
        }
    }

    /**
     * Log Level Error
     * @param message
     */
    @SuppressWarnings("unused")
    public static void error(String message) {
        if (BuildConfig.DEBUG) {
            writeLog(Log.ERROR, TAG, message);
        }
    }

    private static void writeLog(int logLevel, String tag, String message) {
//        boolean isLoggable = isLoggable();
//        Log.e(TAG, "writeLog > isLoggable: " + isLoggable);
        if (true) {
            switch (logLevel) {
                case Log.VERBOSE:
                    Log.v(tag, message);
                    break;
                case Log.DEBUG:
                    Log.d(tag, message);
                    break;
                case Log.INFO:
                    Log.i(tag, message);
                    break;
                case Log.WARN:
                    Log.w(tag, message);
                    break;
                case Log.ERROR:
                    Log.e(tag, message);
            }
        }
    }

}
