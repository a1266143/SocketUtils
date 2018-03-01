package com.accessibilityservice.myapplication;

import android.app.Application;
import android.content.Context;

/**
 * Function:
 * Project:MyApplication
 * Date:2018/3/1
 * Created by xiaojun .
 */

public class XjApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        XjApplication.context = getApplicationContext();
    }
}
