package com.library.util.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.library.util.common.CloseableUtil;
import com.library.util.common.JsonUtil;
import com.library.util.common.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ProjectName: X-Util
 * ClassName: SPUtil
 * Author: XG
 * CreateDate: 2020/1/14 22:05
 * Description:SP工具类
 */
public class SPUtil {
    private static SharedPreferences sharedPreferences;

    private SPUtil() {
    }

    public static void init(Context context) {
        String fileName = context.getPackageName().replaceAll("\\.", "_");
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }


    public static void putInt(String key, int value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void putString(String key, String value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void putBoolean(String key, boolean value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void putFloat(String key, float value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static void putLong(String key, long value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void putStringSet(String key, Set<String> value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public static void putObjectByJson(String key, Object value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        String obj = JsonUtil.toJson(value);
        putString(key, obj);
    }

    public static <T> T getObjectByJson(String key, Class<T> tClass) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return null;
        }
        String json = getString(key);
        return JsonUtil.toBean(json, tClass);
    }

    public static <T> List<T> getListByJson(String key, Class<T> tClass) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return null;
        }
        String json = getString(key);
        return JsonUtil.toList(json, tClass);
    }

    public static <T> void putObject(String key, T value) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            String str = new String(Base64.encode(bos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, str);
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            CloseableUtil.close(oos, bos);
        }
    }

    public static <T> T getObject(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return null;
        }
        String res = sharedPreferences.getString(key, null);
        if (TextUtils.isEmpty(res)) {
            return null;
        }
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            //读取字节
            byte[] base64 = Base64.decode(res.getBytes(), Base64.DEFAULT);
            //封装到字节流
            bis = new ByteArrayInputStream(base64);
            //再次封装
            ois = new ObjectInputStream(bis);
            return (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            CloseableUtil.close(ois, bis);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> getAllMap(Context context) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return null;
        }
        return (Map<String, Object>) sharedPreferences.getAll();
    }


    public static float getFloat(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return -1;
        }
        return sharedPreferences.getFloat(key, -1);
    }

    public static float getFloat(String key, float def) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return def;
        }
        return sharedPreferences.getFloat(key, def);
    }

    public static long getLong(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return -1;
        }
        return sharedPreferences.getLong(key, -1);
    }

    public static long getLong(String key, long def) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return def;
        }
        return sharedPreferences.getLong(key, def);
    }

    public static Set<String> getStringSet(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return null;
        }
        return sharedPreferences.getStringSet(key, null);
    }

    public static Set<String> getStringSet(String key, Set<String> def) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return null;
        }
        return sharedPreferences.getStringSet(key, def);
    }

    public static int getInt(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return -1;
        }
        return sharedPreferences.getInt(key, -1);
    }

    public static int getInt(String key, int defValue) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return defValue;
        }
        return sharedPreferences.getInt(key, defValue);
    }

    public static String getString(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return "";
        }
        return sharedPreferences.getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return defaultValue;
        }
        return sharedPreferences.getString(key, defaultValue);
    }

    public static Boolean getBoolean(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return false;
        }
        return sharedPreferences.getBoolean(key, false);
    }


    public static Boolean getBoolean(String key, boolean def) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return def;
        }
        return sharedPreferences.getBoolean(key, def);
    }

    public static void clearData() {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void removeKey(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static boolean containsKey(String key) {
        if (sharedPreferences == null) {
            LogUtil.e("db is null,use SPUtil.init(getApplicationContext())");
            return false;
        }
        return sharedPreferences.contains(key);
    }
}
