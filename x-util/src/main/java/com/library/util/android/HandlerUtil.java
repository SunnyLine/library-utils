package com.library.util.android;

import android.os.Handler;
import android.os.Looper;

/**
 * ProjectName: X-Util
 * ClassName: HandlerUtil
 * Author: XG
 * CreateDate: 2020/1/14 21:34
 * Description:Handler工具，抛到主线程执行任务
 */
public class HandlerUtil {
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private HandlerUtil() {
    }

    public static void runOnUiThread(Runnable runnable) {
        MAIN_HANDLER.post(runnable);
    }

    public static void runOnUiThreadDelay(Runnable runnable, long delayMillis) {
        MAIN_HANDLER.postDelayed(runnable, delayMillis);
    }

    public static void removeRunable(Runnable runnable) {
        MAIN_HANDLER.removeCallbacks(runnable);
    }
}
