package com.library.util.android.widget;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * X-Util<br>
 * describe ：限制长度过滤器
 *
 * @author xugang
 * @date 2020/1/19
 */
public class LengthFilter implements InputFilter {

    private int maxLength;

    public LengthFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        int keep = maxLength - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            onOverstep();
            return "";
        } else if (keep >= end - start) {
            // keep original
            return null;
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            onOverstep();
            return source.subSequence(start, keep);
        }
    }

    protected void onOverstep(){
        //超出最大长度回调
    }
}
