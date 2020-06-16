package com.library.util.common;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ProjectName: X-Util
 * ClassName: Md5Util
 * Author: XG
 * CreateDate: 2020/1/14 21:42
 * Description:MD5工具类
 */
public class Md5Util {
    private static final String TAG = Md5Util.class.getSimpleName();
    private static final int STREAM_BUFFER_LENGTH = 1024;

    private Md5Util() {
    }

    public static MessageDigest getDigest(String algorithm) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorithm);
    }

    public static byte[] md5(String txt) {
        return md5(txt.getBytes());
    }

    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest digest = getDigest("MD5");
            digest.update(bytes);
            return digest.digest();
        } catch (NoSuchAlgorithmException var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static byte[] md5(InputStream is) throws NoSuchAlgorithmException, IOException {
        return updateDigest(getDigest("MD5"), is).digest();
    }

    public static MessageDigest updateDigest(MessageDigest digest, InputStream data) throws IOException {
        byte[] buffer = new byte[1024];

        for (int read = data.read(buffer, 0, 1024); read > -1; read = data.read(buffer, 0, 1024)) {
            digest.update(buffer, 0, read);
        }

        return digest;
    }
}
