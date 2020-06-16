package com.library.util.android;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.library.util.common.CollectionUtil;
import com.library.util.common.LogUtil;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ProjectName: library-utils
 * ClassName: ApplicationHelper
 * Author: xugang
 * CreateDate: 2020/6/16 10:12
 * Description:Application工具，维护Activity栈，和进程状态
 */
public class ApplicationHelper {
    private List<Activity> activityList = Collections.synchronizedList(new LinkedList<Activity>());
    private AtomicInteger count = new AtomicInteger(0);
    private ProcessStateListener mProcessStateListener;
    private ActivityLifecycleListener mActivityLifecycleListener;

    /**
     * 获取栈顶的Activity
     *
     * @return
     */
    public Activity getTopActivity() {
        if (CollectionUtil.isEmpty(activityList)) {
            return null;
        }
        return activityList.get(activityList.size() - 1);
    }

    public void addActivity(Activity activity) {
        if (activityList != null) {
            activityList.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        if (activityList != null) {
            activityList.remove(activity);
        }
    }

    /**
     * 关闭所有的Activity
     */
    public void closeAllActivity() {
        try {
            if (CollectionUtil.isEmpty(activityList)) {
                return;
            }
            Iterator<Activity> iterator = activityList.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                activity.finish();
                iterator.remove();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * APP是否在前台
     *
     * @return
     */
    public boolean isAppForeground() {
        return count.get() != 0;
    }

    /**
     * 设置进程状态监听
     */
    public void setProcessStateListener(ProcessStateListener mProcessStateListener) {
        this.mProcessStateListener = mProcessStateListener;
    }

    /**
     * 设置Activity生命周期监听
     */
    public void setActivityLifecycleListener(ActivityLifecycleListener mActivityLifecycleListener) {
        this.mActivityLifecycleListener = mActivityLifecycleListener;
    }

    /**
     * 注册Activity生命周期
     */
    public void registerActivityLifecycle(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                if (activity != null) {
                    LogUtil.i(activity.getClass().getSimpleName() + "\tonActivityCreated");
                }
                if (mActivityLifecycleListener != null) {
                    mActivityLifecycleListener.onActivityCreated(activity, savedInstanceState);
                }
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (activity != null) {
                    LogUtil.i(activity.getClass().getSimpleName() + "\tonActivityStarted");
                }
                if (mActivityLifecycleListener != null) {
                    mActivityLifecycleListener.onActivityStarted(activity);
                }
                if (count.get() == 0) {
                    if (mProcessStateListener != null) {
                        mProcessStateListener.onProcessForeground();
                    }
                }
                count.addAndGet(1);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (activity != null) {
                    LogUtil.i(activity.getClass().getSimpleName() + "\tonActivityResumed");
                }
                if (mActivityLifecycleListener != null) {
                    mActivityLifecycleListener.onActivityResumed(activity);
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {
                if (activity != null) {
                    LogUtil.i(activity.getClass().getSimpleName() + "\tonActivityPaused");
                }
                if (mActivityLifecycleListener != null) {
                    mActivityLifecycleListener.onActivityPaused(activity);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (activity != null) {
                    LogUtil.i(activity.getClass().getSimpleName() + "\tonActivityStopped");
                }
                if (mActivityLifecycleListener != null) {
                    mActivityLifecycleListener.onActivityStopped(activity);
                }
                count.addAndGet(-1);
                if (count.get() == 0) {
                    if (mProcessStateListener != null) {
                        mProcessStateListener.onProcessBackground();
                    }
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                if (activity != null) {
                    LogUtil.i(activity.getClass().getSimpleName() + "\tonActivitySaveInstanceState");
                }
                if (mActivityLifecycleListener != null) {
                    mActivityLifecycleListener.onActivitySaveInstanceState(activity, outState);
                }
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity != null) {
                    LogUtil.i(activity.getClass().getSimpleName() + "\tonActivityDestroyed");
                }
                if (mActivityLifecycleListener != null) {
                    mActivityLifecycleListener.onActivityDestroyed(activity);
                }
                removeActivity(activity);
            }
        });
    }

    public static class ActivityLifecycleListener implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

    public interface ProcessStateListener {
        /**
         * 进程进入前台
         */
        void onProcessForeground();

        /**
         * 进程进入后台
         */
        void onProcessBackground();
    }
}
