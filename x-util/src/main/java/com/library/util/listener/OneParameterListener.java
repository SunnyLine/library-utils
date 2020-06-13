package com.library.util.listener;

/**
 * FileName: OneParameterListener.java
 * PackageName: com.pullein.common.listener
 * Author: XG
 * CreateDate: 2019/12/28 23:59
 * Description:有一个参数回调
 */
public interface OneParameterListener<T> {
    void onResult(T t);
}
