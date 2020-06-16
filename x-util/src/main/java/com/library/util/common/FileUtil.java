package com.library.util.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;

/**
 * ProjectName: X-Util
 * ClassName: FileUtil
 * Author: XG
 * CreateDate: 2020/1/14 22:51
 * Description:文件操作
 */
public class FileUtil {
    private FileUtil() {
    }

    /**
     * 初始化文件夹
     */
    public static boolean createFolder(String folderPath) {
        File file = new File(folderPath);
        return file.exists() || file.mkdirs();
    }

    /**
     * 获取文件名称
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        if (url.contains("/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        }
        return "";
    }

    /**
     * 计算文件MD5值
     *
     * @param inputStream
     * @return
     */
    public static String getFileMD5(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        MessageDigest digest = null;
        byte[] data = new byte[8 * 1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((len = inputStream.read(data)) != -1) {
                digest.update(data, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseableUtil.close(inputStream);
        }
    }

    /**
     * 计算文件SHA1值
     *
     * @param inputStream
     * @return
     */
    public static String getFileSha1(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        MessageDigest digest = null;
        byte[] data = new byte[8 * 1024];
        int len;
        try {
            digest = MessageDigest.getInstance("SHA-1");
            while ((len = inputStream.read(data)) != -1) {
                digest.update(data, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseableUtil.close(inputStream);
        }
    }

    /**
     * 写入文件
     *
     * @param is
     * @param os
     */
    public static void write(InputStream is, OutputStream os) throws IOException {
        int num;
        byte[] buf = new byte[1024];
        while ((num = is.read(buf, 0, buf.length)) != -1) {
            os.write(buf, 0, num);
        }
        os.flush();
    }

    /**
     * 读取本地文件
     */
    public static byte[] read(String filePath) {
        File file = new File(filePath);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            CloseableUtil.close(buf);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 创建文件
     */
    public static File createFile(String filePath, InputStream netInputStream) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = null;
        try {
            file.createNewFile();

            fileOutputStream = new FileOutputStream(file);
            byte[] b = new byte[1024];

            int length;
            while ((length = netInputStream.read(b)) != -1) {
                fileOutputStream.write(b, 0, length);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            CloseableUtil.close(fileOutputStream, netInputStream);
        }
        return file;
    }

    /**
     * 转换文件大小
     */
    public static String fileSize2String(long fileLen) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileLen < 1024L) {
            fileSizeString = df.format((double) fileLen) + "B";
        } else if (fileLen < 1048576L) {
            fileSizeString = df.format((double) fileLen / 1024.0D) + "K";
        } else if (fileLen < 1073741824L) {
            fileSizeString = df.format((double) fileLen / 1048576.0D) + "M";
        } else {
            fileSizeString = df.format((double) fileLen / 1.073741824E9D) + "G";
        }

        return fileSizeString;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(File file) {
        return file != null && file.exists() && file.delete();
    }

    /**
     * 删除文件夹及子文件
     */
    public static boolean deleteFolder(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles != null && childFiles.length > 0) {
                File[] var3 = childFiles;
                int var4 = childFiles.length;

                for (int var5 = 0; var5 < var4; ++var5) {
                    File child = var3[var5];
                    if (!deleteFolder(child)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * 读取文件文本
     */
    public static String readText(Reader reader) {
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(reader, 8 * 1024);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseableUtil.close(bufferedReader);
        }
        return sb.toString();
    }

    /**
     * 读取文件文本
     */
    public static String readText(String path) {
        try {
            return readText(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取文件文本
     */
    public static String readText(InputStream inputStream) {
        try {
            return readText(new InputStreamReader(inputStream));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
