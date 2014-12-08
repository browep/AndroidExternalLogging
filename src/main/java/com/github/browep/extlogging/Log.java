package com.github.browep.extlogging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Log {

    public static final String TAG = Log.class.getCanonicalName();

    public static final int VERBOSE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static final int ASSERT = 7;

    private static File mFileToWrite;

    private static ExecutorService mExecutorService;

    private static PrintWriter mPrintWriter;

    Log() {

    }

    public static void config(File fileToWrite) {

        mFileToWrite = fileToWrite;
        mExecutorService = Executors.newSingleThreadExecutor();
        createWriter();

    }

    private static boolean createWriter() {
        try {
            mPrintWriter = new PrintWriter(new FileWriter(mFileToWrite, true), true);
            return true;
        } catch (IOException e) {
            android.util.Log.e(TAG, "could not create PrintWriter from: " + mFileToWrite, e);
            return false;
        }
    }

    private static void writeToFile(final int type, final String tag, final String msg, final Throwable throwable) {

        if( mFileToWrite == null) {
            return;
        }

        final Date date = new Date();
        mExecutorService.execute(new Runnable() {
            public void run() {

                String typeStr = null;
                switch (type) {
                    case VERBOSE:
                        typeStr = "VERBOSE";
                        break;
                    case DEBUG:
                        typeStr = "DEBUG";
                        break;
                    case INFO:
                        typeStr = "INFO";
                        break;
                    case WARN:
                        typeStr = "WARN";
                        break;
                    case ERROR:
                        typeStr = "ERROR";
                        break;
                    case ASSERT:
                        typeStr = "ASSERT";
                        break;
                }

                StringBuilder sb = new StringBuilder().append(date.toString()).append(": ").append(typeStr).append("/").
                        append(tag).append(": ").append(msg);
                if (throwable != null) {
                    sb.append(getStackTraceString(throwable));
                }
                sb.append("\n");

                if (mPrintWriter == null) {
                    if (createWriter()) {
                        doWrite(sb);
                    }

                } else {
                    doWrite(sb);
                }

            }
        });
    }

    private static void doWrite(StringBuilder sb) {
        mPrintWriter.write(sb.toString());
        mPrintWriter.flush();
    }

    public static int v(java.lang.String tag, java.lang.String msg) {
        return android.util.Log.v(tag, msg);
    }

    public static int v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return android.util.Log.v(tag, msg);
    }

    public static int d(java.lang.String tag, java.lang.String msg) {
        writeToFile(DEBUG, tag, msg, null);
        return android.util.Log.d(tag, msg);
    }

    public static int d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        writeToFile(DEBUG, tag, msg, tr);
        return android.util.Log.d(tag, msg, tr);
    }

    public static int i(java.lang.String tag, java.lang.String msg) {
        writeToFile(INFO, tag, msg, null);
        return android.util.Log.i(tag, msg);
    }

    public static int i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        writeToFile(INFO, tag, msg, tr);
        return android.util.Log.i(tag, msg, tr);
    }

    public static int w(java.lang.String tag, java.lang.String msg) {
        writeToFile(WARN, tag, msg, null);
        return android.util.Log.w(tag, msg);
    }

    public static int w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        writeToFile(WARN, tag, msg, tr);
        return android.util.Log.w(tag, msg, tr);
    }

    public static int w(java.lang.String tag, java.lang.Throwable tr) {
        writeToFile(WARN, tag, null, tr);
        return android.util.Log.w(tag, tr);
    }

    public static int e(java.lang.String tag, java.lang.String msg) {
        writeToFile(ERROR, tag, msg, null);
        return android.util.Log.e(tag, msg);
    }

    public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        writeToFile(ERROR, tag, msg, tr);
        return android.util.Log.e(tag, msg, tr);
    }

    public static java.lang.String getStackTraceString(java.lang.Throwable tr) {
        return android.util.Log.getStackTraceString(tr);
    }

    public static int println(int priority, java.lang.String tag, java.lang.String msg) {
        return android.util.Log.println(priority, tag, msg);
    }

}