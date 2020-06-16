package com.library.util.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ProjectName: X-Util
 * ClassName: Sha1Util
 * Author: XG
 * CreateDate: 2020/1/14 21:44
 * Description:SHA1工具类
 */
public class Sha1Util {
    private Sha1Util() {
    }

    public static String getSha1(String input) throws NoSuchAlgorithmException {
        return getSha1(input.getBytes());
    }

    public static String getSha1(byte[] input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(input);
        //加密
        byte[] result = digest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
