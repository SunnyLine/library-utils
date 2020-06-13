package com.library.util.common;

import java.io.Closeable;
import java.io.IOException;

/**
 * FileName: CloseableUtil.java
 * PackageName: com.pullein.common.util
 * Author: XG
 * CreateDate: 2019/12/28 23:49
 * Description:关闭流工具类
 */
public class CloseableUtil {
    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            close(closeable);
        }
    }
}
