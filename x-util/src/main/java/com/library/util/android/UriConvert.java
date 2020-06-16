package com.library.util.android;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;


import java.io.File;

/**
 * ProjectName: X-Util
 * ClassName: UriConvert
 * Author: XG
 * CreateDate: 2019/12/28 23:38
 * Description:Uri 转换器。为了兼容7.0以上版本，Uri.from;Uri.parse等方法不再直接使用
 */
public class UriConvert {
    public static Uri file2Uri(Context context, final String filePath) {
        return file2Uri(context, new File(filePath));
    }

    public static Uri file2Uri(Context context, final File file) {
        if (Build.VERSION_CODES.M < Build.VERSION.SDK_INT) {
            return FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
