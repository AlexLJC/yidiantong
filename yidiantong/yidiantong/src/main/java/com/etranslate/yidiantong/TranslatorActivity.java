package com.etranslate.yidiantong;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.java_websocket.client.WebSocketClient;
public class TranslatorActivity extends AppCompatActivity  implements BottomNavigationBar.OnTabSelectedListener {
    private WebSocketClient wsClient;
    private BottomNavigationBar bottomNavigationBar;
    private SettingFragment settingfragment;
    private TranslatorFragmentMain maingragment;
    private TextView back;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);


        /*1.首先进行fvb*/
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_nav_bar);
        bottomNavigationBar.setTabSelectedListener(this);
/*2.进行必要的设置*/
        bottomNavigationBar.setBarBackgroundColor(R.color.aliwx_bg_color_white);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_DEFAULT );//适应大小
/*3.添加Tab*/
        bottomNavigationBar.addItem(new BottomNavigationItem(
                R.drawable.videocall,R.string.home)
                .setInactiveIconResource(R.drawable.videocall)
                .setActiveColorResource(R.color.yellow_design))
                .addItem(new BottomNavigationItem(
                        R.drawable.videocall,R.string.setting)
                        .setInactiveIconResource(R.drawable.videocall)
                        .setActiveColorResource(R.color.yellow_design))
                .setFirstSelectedPosition(0)//默认显示面板
                .initialise();//初始化
        onTabSelected(0);

        back=(TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

    }
    @Override
    public void onTabSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (position){
            case 0:
                if(maingragment == null){
                    maingragment = new TranslatorFragmentMain();
                }
                transaction.replace(R.id.translator_main,maingragment);
                break;
            case 1:
                if(settingfragment == null){
                    settingfragment = new SettingFragment();
                }                transaction.replace(R.id.translator_main,settingfragment);
                break;
        }
        transaction.commit();
    }
    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
