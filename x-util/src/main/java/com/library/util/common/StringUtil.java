package com.library.util.common;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ProjectName: X-Util
 * ClassName: com.pullein.common.util.StringUtil
 * Author: XG
 * CreateDate: 2019/12/15 10:37
 * Description:字符串工具类
 */
public class StringUtil {
    /**
     * FileName: StringUtil.java
     * Author: XG
     * CreateDate: 2019/12/28 23:27
     * Description:字符串是否为空
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    /**
     * FileName: StringUtil.java
     * Author: XG
     * CreateDate: 2019/12/28 23:26
     * Description:字符串是否不为空
     */
    public static boolean notEmpty(String s) {
        return s != null && s.length() > 0;
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 11:32
     * Description:校验字符串,常用正则串{@link RegularConstants}
     *
     * @see Pattern
     */
    public static boolean matches(String s, String regex) {
        if (notEmpty(s) && notEmpty(regex)) {
            return s.matches(regex);
        }
        return false;
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:06
     * Description:根据正则提取字符串
     *
     * @return 返回符合正则的串
     * @see Pattern
     */
    public static String extract(String s, String regex) {
        if (isEmpty(s) || isEmpty(regex)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            Matcher matcher = Pattern.compile(regex).matcher(s);
            while (matcher.find()) {
                sb.append(matcher.group());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:10
     * Description:根据正则，将符合正则的字符串过滤掉
     *
     * @return 返回根据正则处理后的数据
     * @see Pattern
     */
    public static String filter(String s, String regex) {
        if (isEmpty(s) || isEmpty(regex)) {
            return "";
        }
        return Pattern.compile(regex).matcher(s).replaceAll("").trim();
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:19
     * Description:将原始字符串中符合正则的字符串，使用目标字符串替换掉
     *
     * @return 处理后的字符串
     * @see Pattern
     */
    public static String replace(String originalStr, String fillValue, String regex) {
        if (isEmpty(originalStr) || isEmpty(regex) || fillValue == null) {
            return "";
        }
        return Pattern.compile(regex).matcher(originalStr).replaceAll(fillValue).trim();
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:36
     * Description:获取View 文案
     */
    public static String getText(TextView view) {
        if (view == null) {
            return "";
        }
        if (view.getText() == null) {
            return "";
        }
        return view.getText().toString().trim();
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:38
     * Description:将字符串utf-8进行编码
     */
    public static String encodeUtf8(String s) {
        String result = null;
        if (isEmpty(s)) {
            result = "";
        }
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:48
     * Description：通过html解析成文本
     */
    public static CharSequence fromHtml(String html) {
        if (isEmpty(html)) {
            return "";
        }
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N ? Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT) : Html.fromHtml(html);
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:51
     * Description:读取资源文件文本
     */
    public static String getString(Context context, int strRes) {
        return context.getString(strRes);
    }

    /**
     * ClassName: StringUtil
     * Author: XG
     * CreateDate: 2019/12/15 12:52
     * Description:读取资源文件文本
     */
    public static String getString(Context context, int strRes, Object... formatArgs) {
        return context.getString(strRes, formatArgs);
    }

    /**
     * FileName: StringUtil.java
     * Author: XG
     * CreateDate: 2019/12/28 23:27
     * Description:编码
     */
    public static String encodeUrl(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * FileName: StringUtil.java
     * Author: XG
     * CreateDate: 2019/12/28 23:27
     * Description:解码
     */
    public static String decodeUrl(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
