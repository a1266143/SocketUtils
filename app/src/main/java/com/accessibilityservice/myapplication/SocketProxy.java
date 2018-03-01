package com.accessibilityservice.myapplication;

import android.text.TextUtils;

import com.accessibilityservice.util.LogUtils;
import com.accessibilityservice.util.SocketUtils;

/**
 * Function:
 * Project:MyApplication
 * Date:2018/3/1
 * Created by xiaojun .
 */

public class SocketProxy {

    private SocketUtils socketUtils;
    private boolean flag = true;
    private static SocketProxy socketProxy;
    private SocketProxyCallback proxyCallback;

    private SocketProxy() {

    }

    public void setCallback(SocketProxyCallback callback) {
        this.proxyCallback = callback;
    }

    public static SocketProxy getInstance() {
        if (socketProxy == null)
            socketProxy = new SocketProxy();
        return socketProxy;
    }

    private SocketUtils.SocketCallback callback = new SocketUtils.SocketCallback() {
        @Override
        public void connectSuc() {
            if (proxyCallback != null)
                proxyCallback.connectSuc();
        }

        @Override
        public void connectBreak() {
            socketUtils = null;
            flag = false;
            if (proxyCallback != null)
                proxyCallback.connectBrk();
        }
    };

    public void connect(final String ip, final int port) {
        flag = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketUtils = new SocketUtils();
                socketUtils.setConnectionListener(callback);
                socketUtils.connect(ip, port);
                readStr();
            }
        }).start();

    }

    private void readStr() {
        String str = "1";
        while (socketUtils != null && !TextUtils.isEmpty(str)) {
            str = socketUtils.readData();
        }
        LogUtils.e("socket已经断开");
        callback.connectBreak();
    }

    public void sendStr(String str) {
        if (socketUtils != null)
            socketUtils.sendData(str);
        else
            LogUtils.e("socket已经断开，不能发送数据");
    }

    public interface SocketProxyCallback {
        void connectSuc();//网络连接成功

        void connectBrk();//网络断开
    }
}
