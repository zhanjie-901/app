/**
 * @author 高金磊
 * @version 1.0
 * @date 2019/12/14 17:34
 * @项目名 Android_last
 */
package com.king.anetty.app;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class login extends AppCompatActivity {
Switch auto_login,sever_setting;
ConstraintLayout sever_setting_layout;
EditText user_host,user_port,user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataManage.start(this);
        initViewState();
        if (auto_login.isChecked())
            gotologin(null);
    }

    public void gotologin(View view) {
        //检查基本信息
        if (user_name.getText().length()<3){
            Toast.makeText(this,"昵称太短",Toast.LENGTH_LONG).show();
            return;
        }
        DataManage.put("user_name",user_name.getText().toString());


        //添加自动登陆标识
        DataManage.put("auto_login",auto_login.isChecked());
        //添加用户服务标识
        DataManage.put("sever_setting",sever_setting.isChecked());
        if (sever_setting.isChecked()){
            //使用用户的地址
            DataManage.put("user_host",user_host.getText().toString());
            DataManage.put("user_point",user_port.getText().toString());
        }

        startActivity(new Intent("main"));
    }

    private void initViewState() {
        setContentView(R.layout.activity_login);

        user_name=findViewById(R.id.user_name);
        user_name.setText(DataManage.get("user_name",""));

        auto_login=findViewById(R.id.auto_login);
        auto_login.setChecked(DataManage.get("auto_login",false));
        auto_login.setOnClickListener(new sl());

        sever_setting=findViewById(R.id.sever_setting);
        sever_setting.setChecked(false);
        sever_setting.setOnClickListener(new sl());
        sever_setting_layout=findViewById(R.id.sever_setting_layout);
        sever_setting_layout.setVisibility(View.GONE);

        user_host=findViewById(R.id.user_host);
        user_host.setText(DataManage.get("user_host",this.getString(R.string.host_address)));
        user_port=findViewById(R.id.user_port);
        user_port.setText(DataManage.get("user_point",this.getString(R.string.point)));


    }



    class sl implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.sever_setting :
                    if (((Switch)view).isChecked())
                    {
                        sever_setting_layout.setVisibility(View.VISIBLE);
                    }
                    else
                        sever_setting_layout.setVisibility(View.GONE);

            }

        }
    }




}
