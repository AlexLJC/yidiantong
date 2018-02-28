package com.etranslate.yidiantong;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageOperateion;
import com.alibaba.mobileim.aop.model.ReplyBarItem;
import com.alibaba.mobileim.aop.model.YWChattingPlugin;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.conversation.YWMessageChannel;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;


/**
 * Created by Alex on 2018/1/16.
 */

public class ChattingOperation extends IMChattingPageOperateion {
    // 默认写法
    public ChattingOperation(Pointcut pointcut) {
        super(pointcut);
    }

    private RtcEngine mRtcEngine;// Tutorial Step 1
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        @Override
        public void onUserOffline(final int uid, final int reason) { // Tutorial Step 4
            mRtcEngine.leaveChannel();
        }


        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) { // Tutorial Step 6

        }
    };

    @Override
    public List<ReplyBarItem> getCustomReplyBarItemList(final Fragment pointcut,
                                                        final YWConversation conversation, List<ReplyBarItem> replyBarItemList) {
        List<ReplyBarItem> replyBarItems = new ArrayList<ReplyBarItem>();
        for (ReplyBarItem replyBarItem : replyBarItemList) {
            if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_CAMERA) {
                //是否隐藏ReplyBarItem中的拍照选项
                replyBarItem.setNeedHide(false);
                //不自定义ReplyBarItem中的拍照的点击事件,设置OnClicklistener(null);
                replyBarItem.setOnClicklistener(null);
                //自定义ReplyBarItem中的拍照的点击事件,设置OnClicklistener
//                开发者在自己实现拍照逻辑时，可以在{@link #onActivityResult(int, int, Intent, List<YWMessage>)}中处理拍照完成后的操作
//                replyBarItem.setOnClicklistener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                    }
//                });
            } else if (replyBarItem.getItemId() == YWChattingPlugin.ReplyBarItem.ID_ALBUM) {
                //是否隐藏ReplyBarItem中的选择照片选项
                replyBarItem.setNeedHide(false);
                //不自定义ReplyBarItem中的相册的点击事件,设置OnClicklistener（null）
                replyBarItem.setOnClicklistener(null);
                //自定义ReplyBarItem中的相册的点击事件,设置OnClicklistener
//                replyBarItem.setOnClicklistener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        IMNotificationUtils.getInstance().showToastLong(pointcut.getActivity(), "用户点击了选择照片");
//                    }
//                });
            }
            replyBarItems.add(replyBarItem);
        }
        if (conversation.getConversationType() == YWConversationType.P2P) {
            ReplyBarItem replyBarItem = new ReplyBarItem();
            replyBarItem.setItemId(1);
            replyBarItem.setItemImageRes(R.drawable.videocall);
            replyBarItem.setItemLabel("视频通话");

            SearchActivity.mInstance.addCallback(pointcut);
            replyBarItem.setOnClicklistener(new View.OnClickListener() {
                //voice call
                @Override
                public void onClick(View view) {
                    try {
                        //mRtcEngine = RtcEngine.create(pointcut.getContext(), Data.voiceAPP, mRtcEventHandler);
                        //Data.mRtcEngine=mRtcEngine;
                        //Data.mRtcEngine.joinChannel(null, "test", "Extra Optional Data",0); // if you do not specify the uid, we will generate the uid for you
                        Data.call_flg = false;
                        Data.call_type = 2;
                        Data.m_agoraAPI.queryUserStatus(Data.remoteuserid);
                    } catch (Exception e) {
                        throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
                    }
                }

            });
            replyBarItems.add(replyBarItem);

            //voice call item
            ReplyBarItem replyBarItem2 = new ReplyBarItem();
            replyBarItem2.setItemId(1);
            replyBarItem2.setItemImageRes(R.drawable.voicecall);
            replyBarItem2.setItemLabel("语音通话");

            SearchActivity.mInstance.addCallback(pointcut);
            replyBarItem2.setOnClicklistener(new View.OnClickListener() {
                //voice call
                @Override
                public void onClick(View view) {
                    try {
                        //mRtcEngine = RtcEngine.create(pointcut.getContext(), Data.voiceAPP, mRtcEventHandler);
                        //Data.mRtcEngine=mRtcEngine;
                        //Data.mRtcEngine.joinChannel(null, "test", "Extra Optional Data",0); // if you do not specify the uid, we will generate the uid for you
                        Data.call_flg = true;
                        Data.call_type = 1;
                        Data.m_agoraAPI.queryUserStatus(Data.remoteuserid);

                    } catch (Exception e) {
                        throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
                    }
                }

            });
            replyBarItems.add(replyBarItem2);

        }
        return replyBarItems;
    }

    /**
     * 自定义viewType，viewType的值必须从0开始，然后依次+1递增，且viewType的个数必须等于typeCount，切记切记！！！
     ***/
    //聊天时长消息
    private final int type_0 = 0;//voice
    private final int type_1 = 1;//video

    @Override
    public boolean needHideHead(int viewType) {
        if (viewType == type_0) {
            return true;
        }
        if (viewType == type_1) {
            return true;
        }
        return super.needHideHead(viewType);
    }

    public class CustomMessageType {
        private static final String VoiceTime = "VoiceTime";
        private static final String VideoTime = "VideoTime";

    }

    private final int typeCount = 2;

    @Override
    public int getCustomViewTypeCount() {
        return typeCount;
    }

    /**
     * 自定义消息view的类型，开发者可以根据自己的需求定制多种自定义消息view，这里需要根据消息返回view的类型
     *
     * @param message 需要自定义显示的消息
     * @return 自定义消息view的类型
     */
    @Override
    public int getCustomViewType(YWMessage message) {
        if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_GEO) {
//            return type_0;
            return super.getCustomViewType(message);
        } else if (message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_P2P_CUS || message.getSubType() == YWMessage.SUB_MSG_TYPE.IM_TRIBE_CUS) {
            String msgType = null;
            try {
                String content = message.getMessageBody().getContent();
                JSONObject object = new JSONObject(content);
                msgType = object.getString("type");
            } catch (Exception e) {

            }
            if (!TextUtils.isEmpty(msgType)) {
                if (msgType.equals(CustomMessageType.VoiceTime)) {
                    return type_0;
                } else if (msgType.equals(CustomMessageType.VideoTime)) {
                    return type_1;
                }
            }
        }
        return super.getCustomViewType(message);
    }

    private static final String TAG = "ChattingOperation";

    /**
     * 根据viewType获取自定义view
     *
     * @param fragment       聊天窗口fragment
     * @param msg            当前需要自定义view的消息
     * @param convertView    自定义view
     * @param viewType       自定义view的类型
     * @param headLoadHelper 头像加载管理器，用户可以调用该对象的方法加载头像
     * @return 自定义view
     */
    @Override
    public View getCustomView(Fragment fragment, YWMessage msg, View convertView, int viewType, YWContactHeadLoadHelper headLoadHelper) {
        String data = "";
        String type = "";
        try {
            if (msg.getMessageBody().getExtraData() != null) {
                // 这里的ExtraData主要是方便用户在内存中存储数据，这样有些复杂的消息就不需要反复地去解析
                data = (String) msg.getMessageBody().getExtraData();
            } else {
                // 没有解析过，则解析一遍，然后临时存储到Extradata中
                String content = msg.getMessageBody().getContent();
                JSONObject object = new JSONObject(content);
                data = object.getString("data");
                type = object.getString("type");
                msg.getMessageBody().setExtraData(data);

            }
        } catch (Exception e) {

        }
        Log.d("CustumeMsg Test", msg.getContent());
        if (viewType == type_0) {
            //LinearLayout layout = (LinearLayout) View.inflate(
            //          fragment.getActivity(),
            //          R.layout.voicetime_msg, null);
            convertView = View.inflate(fragment.getActivity(), R.layout.voicetime_msg, null);
            TextView textView = (TextView) convertView.findViewById(R.id.content_vtime);
            textView.setText(data);
            return convertView;

        } else if (viewType == type_1) {
            convertView = View.inflate(fragment.getActivity(), R.layout.voicetime_msg, null);
            TextView textView = (TextView) convertView.findViewById(R.id.content);
            textView.setText(data);
            return convertView;
        }
        return super.getCustomView(fragment, msg, convertView, viewType, headLoadHelper);
    }

    //第一条发送需求
    @Override
    public YWMessage ywMessageToSendWhenOpenChatting(Fragment fragment, YWConversation conversation, boolean isConversationFirstCreated) {
//        YWMessageBody messageBody = new YWMessageBody();
//        messageBody.setSummary("WithoutHead");
//        messageBody.setContent("hi，我是单聊消息之好友名片");
//        YWMessage message = YWMessageChannel.createCustomMessage(messageBody);
//        return message;

        if (Data.getRole().equals("User")) {

            YWMessage textMessage = YWMessageChannel.createTextMessage("你好，我的需求是："+Data.demand);
            //添加发送的消息不显示在对方界面上的本地标记（todo 仅支持本地隐藏。当用户切换手机或清楚数据后，会漫游消息下这些消息并出现在用户的聊天界面上！！）
            textMessage.setLocallyHideMessage(false);
            return textMessage;
        }
        else
        {
        //返回null,则不发送
        return null;
            }
}

}
