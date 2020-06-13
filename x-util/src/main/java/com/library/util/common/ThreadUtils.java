package com.library.util.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ProjectName: X-Util
 * ClassName: com.pullein.common.util.ThreadUtils
 * Author: XG
 * CreateDate: 2019/12/28 23:29
 * Description: 线程工具类，默认开容量为10的线程池
 */
public class ThreadUtils {
    private static ScheduledExecutorService executor;

    private static synchronized ScheduledExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(10);
        }
        return executor;
    }

    /**
     * FileName: ThreadUtils.java
     * Author: XG
     * CreateDate: 2019/12/28 23:31
     * Description:立即执行任务
     */
    public static void runOnBackgroundThread(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    /**
     * FileName: ThreadUtils.java
     * Author: XG
     * CreateDate: 2019/12/28 23:31
     * Description:delay一段时间执行任务
     */
    public static void runOnBackgroundThread(Runnable runnable, long delay) {
        getExecutor().schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }
}
