package com.library.util.listener;

/**
 * FileName: MulResultListener.java
 * PackageName: com.pullein.common.listener
 * Author: XG
 * CreateDate: 2019/12/28 23:59
 * Description:两种结果回调监听
 */
public interface MulResultListener<S, F> {
    void onResult(S s);

    void onFail(F f);
}
