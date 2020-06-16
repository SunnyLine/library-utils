package com.library.util.common;

import android.support.annotation.ColorInt;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * ProjectName: X-Util
 * ClassName: com.pullein.common.util.SpannableUtil
 * Author: XG
 * CreateDate: 2019/12/28 23:39
 * Description:快速展示多种颜色，字体大小文本
 */
public class SpannableUtil {
    private List<TextInfo> list = new ArrayList<>();

    public SpannableUtil addText(String text){
        return addText(text,-1);
    }

    public SpannableUtil addText(String text, @ColorInt int colorId) {
        return addText(text, colorId, 0);
    }

    public SpannableUtil addText(String text, @ColorInt int colorId, int textSizePx) {
        TextInfo textInfo = new TextInfo();
        textInfo.text = text;
        textInfo.colorId = colorId;
        textInfo.textSize = textSizePx;
        list.add(textInfo);
        return this;
    }

    public SpannableStringBuilder create() {
        StringBuilder sb = new StringBuilder();
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        Iterator<TextInfo> iterator = list.iterator();
        while (iterator.hasNext()) {
            TextInfo textInfo = iterator.next();
            if (StringUtil.isEmpty(textInfo.text)) {
                iterator.remove();
                continue;
            }
            stringBuilder.append(textInfo.text);
            if (textInfo.colorId != -1) {
                stringBuilder.setSpan(new ForegroundColorSpan(textInfo.colorId), sb.length(), sb.length() + textInfo.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (textInfo.textSize > 0) {
                stringBuilder.setSpan(new AbsoluteSizeSpan(textInfo.textSize), sb.length(), sb.length() + textInfo.text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            sb.append(textInfo.text);
        }
        if (!CollectionUtil.isEmpty(list)) {
            list.clear();
        }
        return stringBuilder;
    }

    class TextInfo {
        String text;
        int textSize;
        @ColorInt
        int colorId;
    }
}
