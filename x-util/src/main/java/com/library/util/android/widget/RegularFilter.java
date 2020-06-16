package com.library.util.android.widget;

import android.text.InputFilter;
import android.text.Spanned;

import com.pullein.common.util.StringUtil;

/**
 * X-Util<br>
 * describe ：正则校验过滤器
 *
 * @author xugang
 * @date 2020/1/19
 */
public class RegularFilter implements InputFilter {

    private String rule;

    public RegularFilter(String rule) {
        this.rule = rule;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (dstart == dend) {
            StringBuffer buffer = new StringBuffer(source);
            if (!StringUtil.matches(buffer.toString(), rule)) {
                return onDisAccord();
            }
        }
        return source;
    }

    protected String onDisAccord() {
        return "";
    }
}
