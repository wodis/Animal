package com.openwudi.animal.utils;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.openwudi.animal.BuildConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 日志工具类. 有5种日志级别(v, d, w, e, wtf), 可以输入格式化字符串.
 * 调试时，应当将日志级别设置低一些，发布时，应当将日志级别设置到最高.
 * <p>
 * <pre>
 *     使用示例:
 *     L logger = L.get("Ice");
 *     logger.d("Start tracking: %s", "Background");
 * </pre>
 * <p>
 * Created by diwu on 16/12/1.
 */

public class L {
    private static final String DEFAULT_TAG = "Ever";
    private static Map<String, L> sLoggerMap = new HashMap<String, L>();
    private static Map<String, Integer> sLoggerLevel = new HashMap<String, Integer>();
    private static int sLogLevel = Log.VERBOSE;
    private static boolean sIsEnabled = BuildConfig.DEBUG;
    private static final String DEFAULT_LOG_FORMAT = "[%t] %c: %m";

    /**
     * Log Format: default is "[%t] %c: %m"
     * <ul>
     * <li>%t: thread id</li>
     * <li>%c: caller method name</li>
     * <li>%m: msg</li>
     * </ul>
     */
    private static String sLogFormat = DEFAULT_LOG_FORMAT;

    public static L get() {
        return get(DEFAULT_TAG);
    }

    public static L get(String tag) {
        if (TextUtils.isEmpty(tag)) {
            throw new IllegalArgumentException("tag cannot be empty!");
        }
        L log = sLoggerMap.get(tag);
        if (log == null) {
            log = new L(tag);
            sLoggerMap.put(tag, log);
        }
        return log;
    }

    public static void setLevel(int level) {
        sLogLevel = level;
    }

    public static void setLevel(String tag, int level) {
        if (TextUtils.isEmpty(tag)) {
            throw new IllegalArgumentException("tag cannot be empty!");
        }
        sLoggerLevel.put(tag, level);
    }

    public static boolean isEnabled() {
        return sIsEnabled;
    }

    public static void setEnabled(boolean enabled) {
        sIsEnabled = enabled;
    }

    public static void setFormat(String format) {
        sLogFormat = format;
        if (format == null) {
            sLogFormat = DEFAULT_LOG_FORMAT;
        }
    }

    public static boolean isLoggable() {
        return BuildConfig.DEBUG;
    }
//    public static boolean isLoggable(String tag, int level) {
//        if (!sIsEnabled) {
//            return false;
//        }
//        if (sLoggerLevel.get(tag) == null) {
//            return level >= sLogLevel;
//        }
//        return level >= sLogLevel && level >= sLoggerLevel.get(tag);
//    }

    private String tag;

    L(String tag) {
        this.tag = tag;
    }

    public void v(String format, Object... args) {
        if (isLoggable()) {
            String[] parts = split(buildMessage(format, args), MAX_MSG_LENGTH);
            for (int i = 0; i < parts.length; i++) {
                Log.v(tag, parts[i]);
            }
        }
    }

    public void d(String format, Object... args) {
        if (isLoggable()) {
            String[] parts = split(buildMessage(format, args), MAX_MSG_LENGTH);
            for (int i = 0; i < parts.length; i++) {
                Log.d(tag, parts[i]);
            }
        }
    }

    public void w(String format, Object... args) {
        if (isLoggable()) {
            String[] parts = split(buildMessage(format, args), MAX_MSG_LENGTH);
            for (int i = 0; i < parts.length; i++) {
                Log.w(tag, parts[i]);
            }
        }
    }

    public void e(String format, Object... args) {
        if (isLoggable()) {
            String[] parts = split(buildMessage(format, args), MAX_MSG_LENGTH);
            for (int i = 0; i < parts.length; i++) {
                Log.e(tag, parts[i]);
            }
        }
    }

    public void e(Throwable tr, String format, Object... args) {
        if (isLoggable()) {
            Log.e(tag, buildMessage(format, args), tr);
        }
    }

    public void wtf(String format, Object... args) {
        if (isLoggable()) {
            Log.wtf(tag, buildMessage(format, args));
        }
    }

    public void wtf(Throwable tr, String format, Object... args) {
        if (isLoggable()) {
            Log.wtf(tag, buildMessage(format, args), tr);
        }
    }

    private static final int MAX_MSG_LENGTH = 4000;

    private static String[] split(String msg, int maxLength) {
        if (msg == null || msg.length() == 0) {
            return new String[0];
        }
        String[] parts = new String[(msg.length() - 1) / maxLength + 1];
        for (int i = 0; i < parts.length; i++) {
            parts[i] = msg.substring(i * maxLength, Math.min((i + 1) * maxLength, msg.length()));
        }
        return parts;
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String format, Object... args) {
        String msg = (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        // Walk up the stack looking for the first caller outside of HttpManagerLog.
        // It will be at least two frames up, so start there.
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(L.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }

        return String.format(Locale.US, "[%d] %s: ",
                Thread.currentThread().getId(), caller) + msg;
    }

    /**
     * A simple event log with records containing a name, thread ID, and timestamp.
     */
    public static class MarkerLog {
        /**
         * Minimum duration from first marker to last in an marker log to warrant logging.
         */
        private static final long MIN_DURATION_FOR_LOGGING_MS = 0;

        private static class Marker {
            public final String name;
            public final long thread;
            public final long time;

            public Marker(String name, long thread, long time) {
                this.name = name;
                this.thread = thread;
                this.time = time;
            }
        }

        private final List<Marker> mMarkers = new ArrayList<Marker>();
        private boolean mFinished = false;
        private L logger;

        public MarkerLog(L logger) {
            this.logger = logger;
        }

        /**
         * Adds a marker to this log with the specified name.
         */
        public synchronized void add(String name, long threadId) {
            if (mFinished) {
                throw new IllegalStateException("Marker added to finished log");
            }

            mMarkers.add(new Marker(name, threadId, SystemClock.elapsedRealtime()));
        }

        /**
         * Closes the log, dumping it to logcat if the time difference between
         * the first and last markers is greater than {@link #MIN_DURATION_FOR_LOGGING_MS}.
         *
         * @param header Header string to print above the marker log.
         */
        public synchronized void finish(String header) {
            mFinished = true;

            long duration = getTotalDuration();
            if (duration <= MIN_DURATION_FOR_LOGGING_MS) {
                return;
            }

            long prevTime = mMarkers.get(0).time;
            logger.d("(%-4d ms) %s", duration, header);
            for (Marker marker : mMarkers) {
                long thisTime = marker.time;
                logger.d("(+%-4d) [%2d] %s", (thisTime - prevTime), marker.thread, marker.name);
                prevTime = thisTime;
            }
        }

        @Override
        protected void finalize() throws Throwable {
            // Catch requests that have been collected (and hence end-of-life)
            // but had no debugging output printed for them.
            if (!mFinished) {
                finish("Request on the loose");
                logger.e("Marker log finalized without finish() - uncaught exit point for request");
            }
        }

        /**
         * Returns the time difference between the first and last events in this log.
         */
        private long getTotalDuration() {
            if (mMarkers.size() == 0) {
                return 0;
            }

            long first = mMarkers.get(0).time;
            long last = mMarkers.get(mMarkers.size() - 1).time;
            return last - first;
        }
    }
}
