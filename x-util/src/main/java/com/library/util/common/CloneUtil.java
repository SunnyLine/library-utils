package com.library.util.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * X-Util<br>
 * describe ：克隆工具类
 *
 * @author xugang
 * @date 2020/1/19
 */
public class CloneUtil {

    public static <T> T clone(T obj) {
        T cloneObj = null;
        //写入字节流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream obs = null;
        try {
            obs = new ObjectOutputStream(out);
            obs.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableUtil.close(obs);
        }

        //分配内存，写入原始对象，生成新对象
        ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(ios);
            cloneObj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            CloseableUtil.close(ois);
        }
        return cloneObj;
    }
}
