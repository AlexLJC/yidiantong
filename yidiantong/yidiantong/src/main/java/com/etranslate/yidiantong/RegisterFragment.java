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
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONStringer;

/**
 * Created by Alex on 2018/1/10.
 */

public class RegisterFragment  extends Fragment {
    private View view;
    private TextView title;
    private EditText phone;
    private EditText passwd;
    private EditText repeat_pw;
    private EditText first_name;
    private EditText last_name;
    private Spinner gender;
    private Button register;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_user, container,false);
        title=(TextView) view.findViewById(R.id.title_text);
        title.setText(R.string.register);

        phone=(EditText) view.findViewById(R.id.phone);
        passwd=(EditText) view.findViewById(R.id.password_register);
        repeat_pw=(EditText) view.findViewById(R.id.password_repeat);
        first_name=(EditText) view.findViewById(R.id.firstname);
        last_name=(EditText) view.findViewById(R.id.lastname);
        gender=(Spinner) view.findViewById(R.id.gender);
        register=(Button) view.findViewById(R.id.register_send);
        register.setOnClickListener(new View.OnClickListener(){
            //发送注册信息到服务器
            @Override
            public void onClick(View view1)
            {
                // TODO Auto-generated method stub
                //new Thread(WCFTest).start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                    /*String Retstr = WebserviceHelper.GetWebService("http://tempuri.org/",
                            "login", "UName","UPassword", "http://192.168.140.1:7755/Service1.asmx?wsdl", accoun.getText().toString(),pwd.getText().toString());
                    Log.d("this", Retstr);*/
                        JSONStringer myString;
                        try {
                            if(passwd.getText()!=repeat_pw.getText())
                            {
                                //重复输入密码不对
                            }
                            myString = new JSONStringer().object();
                            myString.key("phone");
                            myString.value(phone.getText().toString());
                            myString.key("passwd");
                            myString.value(passwd.getText().toString());
                            myString.key("firstname");
                            myString.value(first_name.getText().toString());
                            myString.key("lastname");
                            myString.value(last_name.getText().toString());
                            myString.key("gender");
                            myString.value(gender.getSelectedItem().toString());

                           // myString.key("action");
                          //  myString.value("register");
                            myString.endObject();
                            String l=myString.toString();
                            Log.d("myString", myString.toString());
                            String Retstr = WebServiceHelper.GetWebService("http://chatserver20171204045436.azurewebsites.net/",
                                    "Regist", "askStr", "http://118.25.17.193/messageControl.asmx?wsdl", myString.toString());
                            Log.d("this", Retstr);
//                        Looper.prepare();
//
                       // Toast.makeText(view.getContext(), Retstr, Toast.LENGTH_LONG).show();
                         if(Retstr.equals("success"))
                         {
                            //Looper.loop();
                            Intent intent = new Intent();
                            //在Intent对象当中添加一个键值对
                            intent.putExtra("testIntent", Retstr);
                            //设置Intent对象要启动的Activity
                            //intent.setClass(MainActivity.this, Result.class);
                            //通过Intent对象启动另外一个Activity
                            //startActivity(intent);
                             Login_User_Fragment fragment_user_login = new Login_User_Fragment();
                             FragmentTransaction transaction = getFragmentManager().beginTransaction();
                             transaction.replace(R.id.register_user_fragment,fragment_user_login);
                             //transaction.addToBackStack("fragment_user_login");
                             transaction.commit();
                         }
                         else
                         {
                            //跳转至错误页面
                         }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();

            }

        });
        return view;
    }
}
