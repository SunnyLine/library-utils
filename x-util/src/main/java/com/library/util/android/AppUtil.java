package com.library.util.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.telephony.SmsManager;
import android.text.TextUtils;

import java.io.File;
import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * ProjectName: X-Util
 * ClassName: AppUtil
 * Author: XG
 * CreateDate: 2020/1/14 23:17
 * Description:APP 系统 工具
 */
public class AppUtil {
    public static boolean isMainProcess(Context context) {
        String processName = getCurrentProcessName(context);
        return processName == null || processName.equalsIgnoreCase(context.getPackageName());
    }

    public static String getCurrentProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : infoList) {
            if (processInfo.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;

        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException var5) {
            var5.printStackTrace();
        }

        return packageInfo;
    }

    public static void startApp(Context context, String pkg) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkg);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开系统设置页面
     *
     * @param action {@link Settings}
     * @see Settings#ACTION_SETTINGS,Settings#ACTION_BLUETOOTH_SETTINGS,Settings#ACTION_DEVICE_INFO_SETTINGS
     */
    public static void openSetting(Context context, String action) {
        Intent mIntent = new Intent(action);
        context.startActivity(mIntent);
    }

    /**
     * 打开悬浮框权限设置页面
     *
     * @param context
     * @param packageName BuildConfig.APPLICATION_ID
     */
    public static void openOverlayPermissionSetting(Context context, String packageName) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        try {
            if (!TextUtils.isEmpty(packageName)) {
                String uri = "package:" + packageName;
                intent.setData(Uri.parse(uri));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.startActivity(intent);
    }

    /**
     * 打开某应用的详情页面
     */
    public static void openAppInfo(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
        context.startActivity(intent);
    }

    public static void openCamera(Activity activity, Uri takePhotoPath, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoPath);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openGallery(Activity activity, int requestCode) {
        Intent choiceFromAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        // 设置数据类型为图片类型
        choiceFromAlbumIntent.setType("image/*");
        activity.startActivityForResult(Intent.createChooser(choiceFromAlbumIntent, "Select File"), requestCode);
    }

    public static void openBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    /**
     * 直接拨打电话，需要权限<br>
     * 需要权限：android.permission.CALL_PHONE
     */
    @SuppressLint("MissingPermission")
    public static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri url = Uri.parse("tel:" + phone);
        intent.setData(url);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 打开系统拨号盘
     *
     * @param context
     * @param phone
     */
    public static void dial(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri url = Uri.parse("tel:" + phone);
        intent.setData(url);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void sendMsg(Context context, String number, String msg) {
        sendMsg(context, number, msg, true);
    }

    public static void sendMsg(Context context, String number, String msg, boolean show) {
        if (show) {
            Uri uri1 = Uri.parse("smsto:" + number);
            Intent i = new Intent(Intent.ACTION_SENDTO, uri1);
            i.putExtra("sms_body", msg);
            context.startActivity(i);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
        }
    }

    public static boolean isAppInstalled(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    public static void installAPK(Context context, String filePath) {
        File file = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
