package com.library.util.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ProjectName: X-Util
 * ClassName: com.pullein.common.util.CollectionUtil
 * Author: XG
 * CreateDate: 2019/12/15 10:38
 * Description:集合工具类
 */
public class CollectionUtil {
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isEmpty(T... ts) {
        return ts == null || ts.length == 0;
    }

    public static <K,V> boolean isEmpty(Map<K,V> map) {
        return map == null || map.isEmpty();
    }

    public static <T> boolean notEmpty(Collection<T> collection) {
        return collection != null && collection.size() > 0;
    }

    public static <T> boolean notEmpty(T... ts) {
        return ts != null && ts.length > 0;
    }

    public static <K,V> boolean notEmpty(Map<K,V> map) {
        return map != null && map.size() > 0;
    }

    public static <T> void printList(Collection<T> collection) {
        if (isEmpty(collection)) {
            LogUtil.d("list is Empty");
            return;
        }
        for (T t : collection) {
            if (t != null) {
                LogUtil.d(t.toString());
            }
        }
    }

    public static <K, V> void printMap(Map<K, V> map) {
        if (isEmpty(map)) {
            LogUtil.d("map is empty");
            return;
        }
        for (Map.Entry<K, V> entry : map.entrySet()) {
            LogUtil.d("key = " + entry.getKey() + "\tvalue = " + entry.getValue());
        }
    }

    /**
     * FileName: CollectionUtil.java
     * Author: XG
     * CreateDate: 2019/12/28 23:35
     * Description:快速去重，得到一个新的list
     */
    public static <T> Collection<T> removeDuplicateData(Collection<T> list) {
        if (list != null) {
            Set<T> uniques = new HashSet<>(list);
            return new ArrayList<>(uniques);
        }
        return null;
    }
}
