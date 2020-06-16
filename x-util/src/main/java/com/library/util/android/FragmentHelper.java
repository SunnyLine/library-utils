package com.library.util.android;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * ProjectName: X-Util
 * ClassName: FragmentHelper
 * Author: XG
 * CreateDate: 2020/1/14 23:21
 * Description:Fragment工具类
 */
public class FragmentHelper {
    private FragmentHelper(){}

    public static void popBackStack(FragmentManager manager) {
        manager.popBackStack();
    }

    /**
     * 弹出栈
     *
     * @param name  目标Fragment
     * @param flags 0 弹出目标Fragment 以上的所有Fragment，不包含目标Fragment;<br>{@link FragmentManager#POP_BACK_STACK_INCLUSIVE}将目标Fragment及其以上的Fragment都弹出栈
     */
    public static void popBackStack(FragmentManager manager, String name, int flags) {
        manager.popBackStack(name, flags);
    }

    public static void add(FragmentManager manager, int id, Fragment fragment) {
        manager.beginTransaction().add(id, fragment).show(fragment).commitAllowingStateLoss();
    }

    public static void addToBackStack(FragmentManager manager, int id, Fragment fragment) {
        manager.beginTransaction().add(id, fragment).show(fragment).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
    }

    public static void replace(FragmentManager manager, int id, Fragment fragment) {
        manager.beginTransaction().replace(id, fragment).commitAllowingStateLoss();
    }

    public static void replaceToBackStack(FragmentManager manager, int id, Fragment fragment) {
        manager.beginTransaction().replace(id, fragment).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
    }

    public static void remove(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    public static void hide(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction().hide(fragment).commitAllowingStateLoss();
    }

    public static void show(FragmentManager manager, Fragment fragment) {
        manager.beginTransaction().show(fragment).commitAllowingStateLoss();
    }

    public static void registerFragmentLifecycleCallbacks(FragmentManager manager, FragmentManager.FragmentLifecycleCallbacks cb, boolean recursive) {
        manager.registerFragmentLifecycleCallbacks(cb, recursive);
    }

    public static void unregisterFragmentLifecycleCallbacks(FragmentManager manager, FragmentManager.FragmentLifecycleCallbacks cb) {
        manager.unregisterFragmentLifecycleCallbacks(cb);
    }

    public static void addOnBackStackChangedListener(FragmentManager manager, FragmentManager.OnBackStackChangedListener listener) {
        manager.addOnBackStackChangedListener(listener);
    }

    public static void removeOnBackStackChangedListener(FragmentManager manager, FragmentManager.OnBackStackChangedListener listener) {
        manager.removeOnBackStackChangedListener(listener);
    }
}
