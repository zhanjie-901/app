/**
 * @author 高金磊
 * @version 1.0
 * @date 2019/12/10 7:34
 * @项目名 Android_last
 */
package com.king.anetty.app;

import android.app.Dialog;
import android.content.Intent;
import android.drm.ProcessedData;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import com.king.anetty.ANetty;
import com.king.anetty.Netty;
import com.king.anetty.app.tools.DateTools;
import com.king.anetty.app.tools.Stringmake;

import java.util.Date;

import io.netty.channel.ChannelHandlerContext;

public class MainActivity extends AppCompatActivity {

    private EditText etContent;
    private TextView tvContent;
    private Toast mToast;
    private String mHost;
    private int mPort;
    private String user_name;
    private Netty mNetty;

    ProgressDialog progressDialog;

    private final Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0x0001){
                //定时刷新消息队列
                sendNettyMsg(Stringmake.getFoot());
            }
            if (msg.what==0x002){
                //服务异常,回到主界面
                sendNettyMsg(Stringmake.getClose());
                startActivity(new Intent("gjlchatlogin"));
            }
            if (msg.what==0x003){
                //在不阻断主线程的时候进行进度的显示
                progressDialog.cancel();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_name=DataManage.get("user_name","");
        mHost = DataManage.get("sever_setting",false)?DataManage.get("user_host",""):this.getString(R.string.host_address);
        mPort = DataManage.get("sever_setting",false)?Integer.valueOf(DataManage.get("user_point","")):Integer.valueOf(this.getString(R.string.point));

        etContent = findViewById(R.id.etContent);
        tvContent = findViewById(R.id.tvContent);
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        initNetty();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("正在连接服务器");
        progressDialog.create();
        progressDialog.show();
        connectNetty();
        //周期性检查是否有新消息
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);//给程序充足的时间建立连接--
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                while (true){
              handler.sendEmptyMessage(0x0001);
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }}
        }.start();

    }

    /**
     * 初始化Netty
     */
    private void initNetty(){
        mNetty = new ANetty(new Netty.OnChannelHandler() {
            @Override
            public void onMessageReceived(ChannelHandlerContext ctx, String msg) {
                //接收到消息
                tvContent.setText(msg + "\n");
            }

            @Override
            public void onExceptionCaught(ChannelHandlerContext ctx,Throwable e) {
                Log.e(ANetty.TAG,e.getMessage());

            }
        }, true);

        mNetty.setOnConnectListener(new Netty.OnConnectListener() {

            @Override
            public void onSuccess() {
                //TODO 连接成功

                showToast("连接成功");
//                sendNettyMsg(Stringmake.getOpen());
                sendNettyMsg(user_name+"上线了!");
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0x003);
                    }

                }.start();
            }

            @Override
            public void onFailed() {
                //TODO 连接失败
                progressDialog.cancel();
                showToast("连接失败,请检查服务器配置");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x002);

            }

            @Override
            public void onError(Exception e) {
                progressDialog.cancel();
                //TODO 连接异常
                showToast("连接异常,请检查服务器配置");
                Log.e(ANetty.TAG,e.getMessage());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException a) {
                    a.printStackTrace();
                }
               handler.sendEmptyMessage(0x002);
            }
        });

        mNetty.setOnSendMessageListener(new Netty.OnSendMessageListener() {
            @Override
            public void onSendMessage(Object msg, boolean success) {
                //发送消息
//                if(success){
//                    showToast("发送成功");
//                }else{
//                    showToast("发送失败");
//                }
            }

            @Override
            public void onException(Throwable e) {
                //异常
                Log.e(ANetty.TAG,e.getMessage());
            }
        });

    }

    /**
     * 连接Netty
     */
    private void connectNetty(){

        //连接Netty
        mNetty.connect(mHost,mPort);
    }

    /**
     * 发送消息
     * @param msg
     */
    private void sendNettyMsg(String msg){
        if(mNetty.isConnected()){
            if (msg.equals(Stringmake.getFoot())){
                mNetty.sendMessage(msg);
            }
            else
                mNetty.sendMessage(user_name+"("+ DateTools.getDate()+"):"+msg);
        }else{
            if (!msg.equals(Stringmake.getFoot())){
            //连接不稳定的时候易出错
                showToast("连接不太稳定,请检查网络设置后重试");
//                handler.sendEmptyMessage(0x002);
            }
        }

    }

    private void showToast(String text){
        if(mToast == null){
            mToast = Toast.makeText(this,text,Toast.LENGTH_SHORT);
        }
        mToast.setText(text);
        mToast.show();

    }

    @Override
    protected void onDestroy() {
        mNetty.disconnect();
        super.onDestroy();
    }

    private void clickSend(){
        if(TextUtils.isEmpty(etContent.getText())){
            showToast("请输入消息内容");
            return;
        }

        sendNettyMsg(etContent.getText().toString());


    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSend:
                clickSend();
                break;
        }
    }

    public void close(View view) {
        sendNettyMsg(user_name+"退出谈论组!");
        Toast.makeText(this, "正在断开服务器", Toast.LENGTH_SHORT).show();

        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x002);
            }
        }.start();
        
    }
}
