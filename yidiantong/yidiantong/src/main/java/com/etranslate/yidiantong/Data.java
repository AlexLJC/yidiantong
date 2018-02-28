package com.etranslate.yidiantong;

import android.graphics.Bitmap;
import android.util.Log;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import com.alibaba.mobileim.channel.event.IWxCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.agora.AgoraAPIOnlySignal;
import io.agora.rtc.RtcEngine;
/**
 * Created by Alex on 2018/1/12.
 */

public class Data {
    private static String role;
    public  static String Icon;
    public static YWIMKit mIMKit ;
    public static String AppKey="24749105";
    public static String userid;
    public static String remoteuserid;
    public static String getRole(){
        return role;
    }
    public static void setRole(String t){
       role=t;
    }
    public static AgoraAPIOnlySignal m_agoraAPI;
    public static RtcEngine mRtcEngine;
    public static String voiceAPP="a2009e9198234a4ca34e6a23b0955ac6";
    public static Bitmap picture=null;
    public static int call_type;
    public static DateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
    public static Date start_time ;
    public static Date end_time;
    public static boolean call_flg=false;//true is voice false is video
    public static String demand;
    public static String passwd;
    public static String firstname;
    public static String lastname;
    public static String gender;
    public static void getProfile(){
        ArrayList uids = new ArrayList<>();
        uids.add(userid);
        mIMKit.getIMCore().getContactManager().fetchUserProfile(uids, AppKey, new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                if (result != null && result.length > 0) {
                    List infos = (List) result[0];
                    if(infos  == null)
                        return;
                    for (Object info : infos) {
                        YWProfileInfo profileInfo =(YWProfileInfo) info;
                        lastname=profileInfo.nick;
                        //获取到的头像以及昵称
                        Log.e("profile test", "onSuccess ，AvatarPath:" + profileInfo.icon + "; ShowName:" + profileInfo.nick + "; getUserId:" + profileInfo.userId);
                    }
                }
            }
            @Override
            public void onError(int i, String s) {
                Log.e("profile", "onError   ErrorCode:" +i +"   , ErrorInfo: "+s);
            }
            @Override
            public void onProgress(int i) {
            }
        });

    }



}
