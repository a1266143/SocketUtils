package com.accessibilityservice.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.accessibilityservice.myapplication.XjApplication;

import java.util.Set;

/**
 * Function:SharedPreferences工具类
 * Project:MyApplication
 * Date:2018/3/1
 * Created by xiaojun .
 */

public class SpUtils {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private static SpUtils spUtils;

    private SpUtils() {
        sp = XjApplication.context.getSharedPreferences(Constant.SP_FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static SpUtils getInstance() {
        if (spUtils == null)
            spUtils = new SpUtils();
        return spUtils;
    }

    public void putValue(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.apply();
    }

    public Object getValue(String key, Object defaultValue) {
        if (defaultValue instanceof String) {
            return sp.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            return sp.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            return sp.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return sp.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Set) {
            return sp.getStringSet(key, (Set<String>) defaultValue);
        } else {
            return sp.getString(key, null);
        }
    }
}
