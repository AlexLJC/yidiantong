package com.etranslate.yidiantong;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.wxlib.util.SysUtil;

import io.agora.AgoraAPIOnlySignal;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;

/**
 * Created by Alex on 2018/1/11.
 */

public class MyApplication extends Application {
    private  final String TAG = MyApplication.class.getSimpleName();
    private static Context sContext;
    public static Context getContext(){
        return sContext;
    }
    final String APP_KEY = "24749105";
    private OnAgoraEngineInterface onAgoraEngineInterface;
    private static MyApplication mInstance ;
    @Override
    public void onCreate() {
        super.onCreate();
        SysUtil.setApplication(this);
        if(SysUtil.isTCMSServiceProcess(this)){
            return;
        }
        YWAPI.init(this,APP_KEY);
      try {
          Data.mRtcEngine = RtcEngine.create(this, Data.voiceAPP, mRtcEventHandler);
          Data.m_agoraAPI = AgoraAPIOnlySignal.getInstance(this, Data.voiceAPP);
      }
      catch(Exception e)
        {

        }

       //第一个参数是Application Context
       //这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_UI_POINTCUT,ChattingUICustom.class);
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_OPERATION_POINTCUT,ChattingOperation.class);
    }
    public static MyApplication the() {
        return mInstance;
    }
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        @Override
        public void onUserOffline(final int uid, final int reason) { // Tutorial Step 4
            Data.mRtcEngine.leaveChannel();
            Log.i(TAG, "onUserOffline uid: " + uid +" reason:" + reason);
            if (onAgoraEngineInterface != null){
                onAgoraEngineInterface.onUserOffline(uid ,reason);
            }
        }


        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6

        }
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            if (onAgoraEngineInterface != null){
                onAgoraEngineInterface.onFirstRemoteVideoDecoded(uid ,width,height,elapsed);
            }
        }



        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            if (onAgoraEngineInterface != null){
                onAgoraEngineInterface.onUserMuteVideo(uid, muted);
            }

        }
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            Log.i(TAG ,"onJoinChannelSuccess channel:" + channel+ " uid:" + uid);
            if (onAgoraEngineInterface != null){
                onAgoraEngineInterface.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }
    };
    public interface OnAgoraEngineInterface{
        void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed);


        void onUserOffline(int uid, int reason) ;


        void onUserMuteVideo(final int uid, final boolean muted);

        void onJoinChannelSuccess(String channel, int uid, int elapsed);


    }
    public void setOnAgoraEngineInterface(OnAgoraEngineInterface onAgoraEngineInterface) {
        this.onAgoraEngineInterface = onAgoraEngineInterface;
    }
    //private boolean mustRunFirstInsideApplicationOnCreate() {
    //    //必须的初始化
     //   SysUtil.setApplication(this);
    //    sContext = getApplicationContext();
   //     return SysUtil.isTCMSServiceProcess(sContext);
  //  }
    public MyApplication() {
        mInstance = this;
    }
}
