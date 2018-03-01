package com.accessibilityservice.util;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 * Function:
 * Project:MyApplication
 * Date:2018/3/1
 * Created by xiaojun .
 */

public class SocketUtils {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private BufferedReader br;
    private BufferedWriter bw;
    private SocketCallback callback;

    //设置连接监听器
    public void setConnectionListener(SocketCallback callback){
        this.callback = callback;
    }

    //连接服务端
    public void connect(String host, int port) {
        try {
            socket = new Socket(host, port);
            LogUtils.e("Socket连接服务端成功...");
            is = socket.getInputStream();
            os = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(is));
            bw = new BufferedWriter(new OutputStreamWriter(os));
            callback.connectSuc();
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("Socket连接服务端失败...");
            is = null;
            os = null;
            br = null;
            bw = null;
            if (callback!=null){
                callback.connectBreak();
            }
        }
    }

    //发送数据给服务端
    public void sendData(String data) {
        try {
            if (bw != null){
                bw.write(data);
                bw.flush();
                LogUtils.e("发送数据成功...");
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e("发送数据失败IOException...");
            if (callback!=null){
                callback.connectBreak();
            }
        }
    }

    /**
     * 读取服务端发来的数据
     * 注意：需要定义结束符
     * 结束符：回车%%%回车
     * @return 返回得到的数据
     */
    public String readData(){
        StringBuilder sb = new StringBuilder();
        try {
            if (br!=null){
                String str = "";
                while (!TextUtils.isEmpty((str = br.readLine()))&&!str.equals("%%%")){
                    sb.append(str);
                }
                String content = sb.toString();
                LogUtils.e("读取到服务端数据："+content);
                return content;
            }
        }catch (IOException e){
            e.printStackTrace();
            LogUtils.e("接收数据失败IOException...");
            if (callback!=null){
                callback.connectBreak();
            }
        }
        return null;
    }

    //回调接口，用于接收服务端发来的数据
    public interface SocketCallback{
        void connectSuc();//连接成功
        void connectBreak();//连接断开
    }

}
