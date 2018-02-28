package com.etranslate.yidiantong;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
/**
 * Created by Alex on 2018/1/10.
 */

public class Login_User_Fragment extends Fragment {
    private EditText loginname;
    private EditText passwd;
    private Button login;
    private Button register;
    private View view;
    private TextView title;
    final String APP_KEY = "24749105";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_user, container,false);
        title=(TextView) view.findViewById(R.id.title_text);
        title.setText(R.string.login);
        loginname=(EditText) view.findViewById(R.id.loginname);
        passwd=(EditText) view.findViewById(R.id.passwd);
        login=(Button) view.findViewById(R.id.login);
        register=(Button) view.findViewById(R.id.register);
        final Intent intent = new Intent();
        intent.setClass(this.getContext(),UserActivity.class);
        login.setOnClickListener(new View.OnClickListener(){
            //登录操作
            @Override
            public void onClick(View view)
            {

                YWIMCore imCore = YWAPI.createIMCore(loginname.getText().toString().trim(),APP_KEY);
                IYWLoginService loginService = imCore.getLoginService();
                YWLoginParam Param = YWLoginParam.createLoginParam(loginname.getText().toString().trim(), passwd.getText().toString().trim());

                loginService.login(Param, new IWxCallback() {

                    @Override
                    public void onSuccess(Object... arg0) {
                        //登录成功
                        Data.userid=loginname.getText().toString().trim();
                        Data.passwd=passwd.getText().toString().trim();
                        Data.setRole("User");
                        YWIMKit mIMKit = YWAPI.getIMKitInstance(loginname.getText().toString().trim(), APP_KEY);
                        Data.mIMKit=mIMKit;
                        Data.getProfile();
                        startActivity(intent);
                        Log.d("test switch","fff");
                        Data.m_agoraAPI.login2(Data.voiceAPP, Data.userid, "_no_need_token", 0, "" ,5,1);

                    }

                    @Override
                    public void onProgress(int arg0) {
                        // TODO Auto-generated method stub
                        Log.d("test switch","processing"+loginname.getText().toString().trim()+passwd.getText().toString().trim());
                    }

                    @Override
                    public void onError(int errCode, String description) {
                        //如果登录失败，errCode为错误码,description是错误的具体描述信息
                        Log.d("test switch",description);
                    }
                });

            }

        });
        register.setOnClickListener(new View.OnClickListener(){
            //注册操作
            @Override
            public void onClick(View view)
            {
                RegisterFragment fragment_register = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.login_user,fragment_register);
                transaction.addToBackStack("fragment_register");
                transaction.commit();

            }

        });
            return view;
    }


}
