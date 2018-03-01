package com.accessibilityservice.myapplication;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.accessibilityservice.util.Constant;
import com.accessibilityservice.util.SocketUtils;
import com.accessibilityservice.util.SpUtils;

public class MainActivity extends AppCompatActivity {

    private TextView tvInfo;
    private EditText edtIP, edtPort, edtContent;
    private Button btnConnect, btnSend;
    private final int MSG_SUC = 0x123;
    private final int MSG_FAIL = 0x124;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_SUC:
                    tvInfo.setText("服务端IP:"+edtIP.getText().toString()+":"+edtPort.getText().toString()+"连接成功");
                    tvInfo.setTextColor(Color.GREEN);
                    btnConnect.setEnabled(false);
                    btnSend.setEnabled(true);
                    break;
                case MSG_FAIL:
                    tvInfo.setText("服务端IP:"+edtIP.getText().toString()+":"+edtPort.getText().toString()+"连接断开");
                    tvInfo.setTextColor(Color.RED);
                    btnConnect.setEnabled(true);
                    btnSend.setEnabled(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        init();
    }

    private void findViews() {
        tvInfo = findViewById(R.id.tvInfo);
        edtIP = findViewById(R.id.edtIP);
        edtPort = findViewById(R.id.edtPort);
        edtContent = findViewById(R.id.edtContent);
        btnConnect = findViewById(R.id.btnConnect);
        btnSend = findViewById(R.id.btnSend);
        //连接服务端
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = edtIP.getText().toString();
                String port = edtPort.getText().toString();
                SocketProxy.getInstance().setCallback(callback);
                SocketProxy.getInstance().connect(ip,Integer.parseInt(port));
            }
        });
        //发送内容
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString();
                SocketProxy.getInstance().sendStr(content);
            }
        });
    }

    private void init() {
        SpUtils spUtils = SpUtils.getInstance();
        String localIP = (String) spUtils.getValue(Constant.SP_KEY_IP, "");
        int localPort = (int) spUtils.getValue(Constant.SP_KEY_PORT, 0);
        //如果本地存储了IP地址，就重新填充
        if (!TextUtils.isEmpty(localIP)) {
            edtIP.setText(localIP);
        }
        //如果本地存储了端口，重新填充
        if (localPort != 0) {
            edtPort.setText(""+localPort);
        }
    }

    private SocketProxy.SocketProxyCallback callback = new SocketProxy.SocketProxyCallback() {
        @Override
        public void connectSuc() {
            SpUtils.getInstance().putValue(Constant.SP_KEY_IP,edtIP.getText().toString());
            SpUtils.getInstance().putValue(Constant.SP_KEY_PORT,Integer.parseInt(edtPort.getText().toString()));

            handler.sendEmptyMessage(MSG_SUC);
        }

        @Override
        public void connectBrk() {
            handler.sendEmptyMessage(MSG_FAIL);
        }
    };
}
