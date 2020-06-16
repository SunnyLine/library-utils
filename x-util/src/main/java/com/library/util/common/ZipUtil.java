package com.library.util.common;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ProjectName: X-Util
 * ClassName: ZipUtil
 * Author: XG
 * CreateDate: 2020/1/14 22:50
 * Description:压缩工具类
 */
public class ZipUtil {
    private ZipUtil() {
    }

    public static byte[] gzip(String str, String encoding) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseableUtil.close(gzip, out);
        }
        return out.toByteArray();
    }

    public static String unGzip(String gzipStr) {
        return unGzip(gzipStr, "UTF-8");
    }

    public static String unGzip(String gzipStr, String encoding) {
        byte[] data = Base64Util.decode(gzipStr);
        return unGzip(data, encoding);
    }

    public static String unGzip(byte[] gzipData, String encoding) {
        if (CollectionUtil.isEmpty(gzipData)) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        ByteArrayInputStream bais = null;
        GZIPInputStream gis = null;
        try {
            //建立gzip压缩文件输出流
            baos = new ByteArrayOutputStream();
            bais = new ByteArrayInputStream(gzipData);
            gis = new GZIPInputStream(bais);
            byte[] buffer = new byte[256];
            int num;
            while ((num = gis.read(buffer)) != -1) {
                baos.write(buffer, 0, num);
            }
            baos.flush();
            return baos.toString(encoding);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseableUtil.close(baos, gis, bais);
        }
    }

    public static boolean unZip(String sourceDir, String outDir) {
        ZipFile zipFile = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Charset CP866 = Charset.forName("CP866");
                zipFile = new ZipFile(sourceDir, CP866);
            } else {
                zipFile = new ZipFile(sourceDir);
            }
            FileUtil.createFolder(outDir);
            Enumeration<?> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                if (entry.isDirectory()) {
                    FileUtil.createFolder(outDir + File.separator + entry.getName());
                    continue;
                }
                fos = new FileOutputStream(new File(outDir, entry.getName()));
                bos = new BufferedOutputStream(fos);
                bis = new BufferedInputStream(zipFile.getInputStream(entry));
                FileUtil.write(bis, bos);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseableUtil.close(bos, fos, zipFile, bis);
        }
        return true;
    }
}
