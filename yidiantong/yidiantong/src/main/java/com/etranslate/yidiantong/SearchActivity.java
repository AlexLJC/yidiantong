package com.etranslate.yidiantong;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;

import io.agora.AgoraAPI;
import io.agora.IAgoraAPI;

public class SearchActivity extends AppCompatActivity {
    private WebSocketClient wsClient;
    private String role;
    private Button cancel;
    String target=new String();
    String channelName;
    Intent intent;
    Context context;

    AlertDialog.Builder builder;
    private final int REQUEST_CODE = 0x01;
    private  final String TAG = SearchActivity.class.getSimpleName();
    public static SearchActivity mInstance=new SearchActivity() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_fragment);
        role=Data.getRole();
        cancel=(Button) findViewById(R.id.cancel);
        Log.d("userid",Data.userid+Data.getRole());
        context=this;
        builder=new AlertDialog.Builder(this).setTitle("提示");
        try
        {
            wsClient = new WebSocketClient(new URI("ws://118.25.17.193:8088"), new Draft_6455() ){
                AlertDialog dialog;
                @Override
                public void onOpen(ServerHandshake handshakedata) {

                }

                @Override
                public void onMessage(String message) {

                    target=message.split(";")[1];
                    Data.remoteuserid=target;
                    Log.d("test",message);
                    if(Data.getRole().equals("Translator"))
                    {

                        Log.d("test",message.split(";")[2]);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //处理确认按钮的点击事件
                                        intent = Data.mIMKit.getChattingActivityIntent(target, Data.AppKey);
                                        startActivity(intent);

                                    }
                                }) .setMessage(message.split(";")[2].toString());

                        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        //dialog.setMessage(message.split(";")[2].toString());
                        Log.d("AlertDialog test222",message.split(";")[2]);
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog = builder.create();
                                    dialog.show();
                                }
                            });

                        }
                        catch (Exception e)
                        {
                            Log.d("AlertDialog Exception",e.toString());
                        }


                    }
                    else {
                        intent = Data.mIMKit.getChattingActivityIntent(target, Data.AppKey);
                        startActivity(intent);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {

                }

                @Override
                public void onError(Exception ex) {

                }
            };
            wsClient.connect();
            while(true) {
                if(wsClient.isOpen()) {
                    wsClient.send("Regist;" + Data.userid + ";");
                    if (role.equals("Translator")) {
                        wsClient.send("Search_U;");
                    }
                    if (role.equals("User")) {
                        wsClient.send("Search_T;"+Data.demand+";");
                    }
                    break;
                }
            }
        }
        catch (Exception e)
        {

        };
        cancel.setOnClickListener(new View.OnClickListener(){
            //cancel
            @Override
            public void onClick(View view)
            {
                wsClient.close();
                 finish();
            }

        });
    }
    public void addCallback(final Fragment pointcut) {
        if (Data.m_agoraAPI == null){
            return;
        }

        Data.m_agoraAPI.callbackSet(new AgoraAPI.CallBack() {

            @Override
            public void onLogout(final int i) {
                Log.i(TAG ,"onLogout  i = " + i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i == IAgoraAPI.ECODE_LOGOUT_E_KICKED){ //other login the account
                            Toast.makeText(pointcut.getActivity() ,"Other login account ,you are logout." ,Toast.LENGTH_SHORT).show();

                        }else if (i == IAgoraAPI.ECODE_LOGOUT_E_NET){ //net
                            Toast.makeText( pointcut.getActivity() ,"Logout for Network can not be." ,Toast.LENGTH_SHORT).show();

                        }
                        finish();

                    }
                });

            }

            @Override
            public void onLoginFailed(int i) {
                Log.i(TAG ,"onLoginFailed  i = " + i);
            }

            @Override
            public void onInviteReceived(final String channelID, final String account,int uid, final String s2) { //call out other remote receiver
                Log.i(TAG ,"onInviteReceived  channelID = " + channelID + "  account = " + account);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int call_type=1;
                        try {
                            JSONObject obj = new JSONObject(s2);
                            call_type=obj.getInt("call_type");
                            if(call_type==1)
                            {
                                Data.call_flg=true;
                            }
                            else if(call_type==2)
                            {
                                Data.call_flg=false;
                            }
                        }
                        catch (Exception e)
                        {
                        }
                        Intent intent = new Intent(pointcut.getActivity().getApplication() ,CallActivity.class);
                        intent.putExtra("account" , Data.userid);
                        intent.putExtra("channelName" ,channelID);
                        intent.putExtra("mBeCallaccount" ,account);
                        intent.putExtra("type" ,Constant.CALL_IN);
                        intent.putExtra("call_type" ,call_type);
                        pointcut.startActivity(intent );

                    }
                });
            }

            @Override
            public void onInviteReceivedByPeer(final String channelID, final String account, int uid) {//call out other local receiver
                Log.i(TAG ,"onInviteReceivedByPeer  channelID = " + channelID + "  account = " + account);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(pointcut.getActivity().getApplication(),CallActivity.class);
                        intent.putExtra("account" ,Data.userid);
                        intent.putExtra("channelName" ,channelID);
                        intent.putExtra("mBeCallaccount" ,account);
                        intent.putExtra("type" , Constant.CALL_OUT);
                        pointcut.startActivity(intent);
                    }
                });

            }

            @Override
            public void onInviteFailed(String channelID, String account, int uid, int i1, String s2) {
                Log.i(TAG ,"onInviteFailed  channelID = " + channelID + "  account = " + account +" s2 :" + s2 +" i1:" + i1);

            }

            @Override
            public void onError(final String s, int i,final String s1) {

                Log.i(TAG ,"onError  s = " + s + " i = " + i + "  s1 = " + s1);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (s.equals("query_user_status") ){
                            Toast.makeText( pointcut.getActivity() , s1 ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onQueryUserStatusResult(final String name, final String status) {
                Log.i(TAG ,"onQueryUserStatusResult  name = " + name + "  status = " + status);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (status.equals("1")){
                            channelName = Data.remoteuserid;
                            //Data.m_agoraAPI.channelInviteUser(channelName, Data.remoteuserid, 0);
                            JSONObject json=new JSONObject();
                            try {
                                json.put("call_type", Data.call_type);
                                Data.m_agoraAPI.channelInviteUser2(channelName, Data.remoteuserid,json.toString());
                            }
                            catch(Exception e)
                            {

                            }
                        }else if (status.equals("0")){
                            Toast.makeText( pointcut.getActivity(), name +" is offline "  ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }
}
