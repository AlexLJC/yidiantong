package com.etranslate.yidiantong;

/**
 * Created by Alex on 2018/1/19.
 */
public class Constant {

    public static int  CALL_IN = 0x01;
    public static int  CALL_OUT = 0x02;

    private static long timeLast;

    public static boolean isFastlyClick(){
        if (System.currentTimeMillis() - timeLast < 1500){
            timeLast = System.currentTimeMillis();
            return true;
        }else {
            timeLast = System.currentTimeMillis();
            return false;
        }

    }

}