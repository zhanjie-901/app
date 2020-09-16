package com.king.anetty.app.tools;

/**
 * Created by 高金磊 on 2018/10/2.
 * 自定义网络传输协议
 * 第7次修改--2019-12-17
 */

public class Stringmake {
    private static String head="0x001startfromsevlet";
    private static String foot="0x001endfromsevlet";
    private static String open="0x001openfromsevlet";
    private static String close="0x001closefromsevlet";
    public static String getSevletString(String basicdata){//解从服务器返回的数据的文件头尾(解协议)
        return basicdata.substring(basicdata.indexOf(head)+20, basicdata.indexOf(foot));
    }
    public static String sendSevletString(String basicdata){//服务器打包要向客户端发送的数据
        return head+basicdata+foot;
    }
    public static String getFoot() {
        return foot;
    }

    public static String getHead() {
        return head;
    }

    public static String getOpen() {
        return open;
    }

    public static String getClose() {
        return close;
    }
}
