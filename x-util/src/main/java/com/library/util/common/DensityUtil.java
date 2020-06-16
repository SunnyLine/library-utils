package com.library.util.common;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * ProjectName: X-Util
 * ClassName: com.pullein.common.util.DensityUtil
 * Author: XG
 * CreateDate: 2019/12/28 23:43
 * Description:显示相关工具栏，获取屏幕信息和计量单位转换
 */
public class DensityUtil {
    /**
     * 将 dp/dip  转换成 px
     */
    public static int dip2px(Context context, int dip) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 将 px  转换成 dp/dip
     */
    public static int px2dip(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f * (px >= 0 ? 1 : -1));
    }

    /**
     * 将 sp  转换成 px
     */
    public static int sp2px(Context context, int sp) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    /**
     * 将 px  转换成 sp
     */
    public static int px2sp(Context context, int px) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / fontScale + 0.5f);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager == null){return 0;}
        DisplayMetrics metric = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager == null){return 0;}
        DisplayMetrics metric = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 获得密度
     */
    public static float getDensity(Activity activity) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(
                mDisplayMetrics);
        return mDisplayMetrics.density;
    }
    /**
     * 获取屏幕相关参数
     */
    public static String getScreenParams(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        int heightPixels = getScreenHeight(context);
        int widthPixels = getScreenWidth(context);
        float xdpi = dm.xdpi;
        float ydpi = dm.ydpi;
        int densityDpi = dm.densityDpi;
        float density = dm.density;
        float scaledDensity = dm.scaledDensity;
        float heightDP = heightPixels / density;
        float widthDP = widthPixels / density;
        float smallestWidthDP;
        if (widthDP < heightDP) {
            smallestWidthDP = widthDP;
        } else {
            smallestWidthDP = heightDP;
        }
        String str = "=== screen params ===";
        str += "\nheightPixels: " + heightPixels + "px";
        str += "\nwidthPixels: " + widthPixels + "px";
        str += "\nxdpi: " + xdpi + "dpi";
        str += "\nydpi: " + ydpi + "dpi";
        str += "\ndensityDpi: " + densityDpi + "dpi";
        str += "\ndensity: " + density;
        str += "\nscaledDensity: " + scaledDensity;
        str += "\nheightDP: " + heightDP + "dp";
        str += "\nwidthDP: " + widthDP + "dp";
        str += "\nsmallestWidthDP: " + smallestWidthDP + "dp";
        return str;
    }
}
