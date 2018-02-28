package com.etranslate.yidiantong;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMChattingPageUI;
import com.alibaba.mobileim.channel.util.AccountUtils;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.conversation.YWConversationType;
import com.alibaba.mobileim.conversation.YWP2PConversationBody;
import com.alibaba.mobileim.conversation.YWTribeConversationBody;
/**
 * Created by Alex on 2018/1/15.
 */

public class ChattingUICustom extends IMChattingPageUI {
    public ChattingUICustom(Pointcut pointcut) {
        super(pointcut);
    }
    public View getCustomTitleView(final Fragment fragment,
                                   final Context context, LayoutInflater inflater,
                                   final YWConversation conversation) {
        // 单聊和群聊都会使用这个方法，所以这里需要做一下区分
        // 本demo示例是处理单聊，如果群聊界面也支持自定义，请去掉此判断

        //TODO 重要：必须以该形式初始化view---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局**中的高度和宽度无效，均变为wrap_content
        View view = inflater.inflate(R.layout.common_title, new RelativeLayout(context), false);
        view.setBackgroundColor(Color.parseColor("#F78500"));
        TextView textView = (TextView) view.findViewById(R.id.title);
        String title = null;
        if (conversation.getConversationType() == YWConversationType.P2P) {
            YWP2PConversationBody conversationBody = (YWP2PConversationBody) conversation
                    .getConversationBody();
            if (!TextUtils.isEmpty(conversationBody.getContact().getShowName())) {
                title = conversationBody.getContact().getShowName();
            } else {

                YWIMKit imKit =Data.mIMKit;
                IYWContact contact = imKit.getContactService().getContactProfileInfo(conversationBody.getContact().getUserId(), conversationBody.getContact().getAppKey());
                //生成showName，According to id。
                if (contact != null && !TextUtils.isEmpty(contact.getShowName())) {
                    title = contact.getShowName();
                }
            }
            //如果标题为空，那么直接使用Id
            if (TextUtils.isEmpty(title)) {
                title = conversationBody.getContact().getUserId();
            }
        } else {
            if (conversation.getConversationBody() instanceof YWTribeConversationBody) {
                title = ((YWTribeConversationBody) conversation.getConversationBody()).getTribe().getTribeName();
                if (TextUtils.isEmpty(title)) {
                    title = "自定义的群标题";
                }
            } else {
                if (conversation.getConversationType() == YWConversationType.SHOP) { //为OpenIM的官方客服特殊定义了下、
                    title = AccountUtils.getShortUserID(conversation.getConversationId());
                }
            }
        }
        textView.setText(title);
        textView.setTextColor(Color.parseColor("#FFFFFF"));
        textView.setTextSize(15);
        TextView backView = (TextView) view.findViewById(R.id.back);
        SearchActivity.mInstance.addCallback(fragment);
        backView.setTextColor(Color.parseColor("#FFFFFF"));
        backView.setTextSize(15);
        backView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.demo_common_back_btn_white, 0, 0, 0);
        backView.setGravity(Gravity.CENTER_VERTICAL);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog dialog = new AlertDialog.Builder(arg0.getContext()).setTitle("提示")
                        .setNegativeButton("取消", null).setPositiveButton("确定", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(Data.getRole().equals("Translator"))
                                {
                                    FeedbackFragment feedbacnfrag = new FeedbackFragment();
                                    FragmentTransaction transaction = fragment.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(fragment.getId(),feedbacnfrag);
                                    transaction.addToBackStack("feedback");
                                    transaction.commit();
                                }
                                else {
                                    //fragment.getActivity().finish();
                                    Intent intent = new Intent(fragment.getActivity(), UserActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    fragment.startActivity(intent);
                                }
                                //处理确认按钮的点击事件
                            }
                        }) .setMessage("是否退出此次服务？").create();
                dialog.show();

            }
        });



        return view;
    }

}
