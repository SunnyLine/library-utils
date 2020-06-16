package com.library.util.android;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Method;

import static android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT;

/**
 * ProjectName: X-Util
 * ClassName: KeyboardUtil
 * Author: XG
 * CreateDate: 2020/1/14 21:40
 * Description:操作键盘工具类
 */
public class KeyboardUtil {
    private KeyboardUtil(){}

    /**
     * 关闭软键盘
     */
    public static void closeKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设置系统键盘不可见
     *
     * @param editText
     */
    public static void setKeyBoardHide(Context context, EditText editText) {
        int sdkInt = Build.VERSION.SDK_INT;
        int sdkVersion = 11;
        if (sdkInt >= sdkVersion) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 如果软键盘已经显示，则隐藏
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 显示虚拟键盘
     *
     * @param v     EditView
     * @param flags 建议使用:InputMethodManager.SHOW_IMPLICIT
     */
    public static void showKeyboard(final View v, final int flags) {
        //加一个Delay 是因为系统键盘不能够在页面初始化立即弹出，需要延时
        HandlerUtil.runOnUiThreadDelay(new Runnable() {
            @Override
            public void run() {
                v.requestFocus();
                if (v instanceof EditText) {
                    String text = ((EditText) v).getText().toString().trim();
                    if (!TextUtils.isEmpty(text)) {
                        ((EditText) v).setSelection(text.length());
                    }
                }
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, flags);
            }
        }, 200);
    }

    /**
     * 显示虚拟键盘
     *
     * @param v EditView
     */
    public static void showKeyboard(final View v) {
        showKeyboard(v, SHOW_IMPLICIT);
    }
}
