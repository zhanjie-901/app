/**
 * @author 高金磊
 * @version 1.0
 * @date 2019/12/15 7:34
 * @项目名 Android_last
 */
package com.king.anetty.app;

import android.content.Context;
import android.content.SharedPreferences;

public  final class DataManage {
    //为了使在任何地方使用SharedPreferences
    //为了方便操作全部使用字符串作为key和value
  private static SharedPreferences sharedPreferences;
  private static SharedPreferences.Editor editor;
     static void  start(Context context){
         if (sharedPreferences!=null)
             return;
        sharedPreferences=context.getSharedPreferences("user_data",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
     static void put(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }
    static void put(String key,boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }
     static String get(String key,String def)
    {
        return sharedPreferences.getString(key,def);
    }
    static boolean get(String key,boolean def)
    {
        return sharedPreferences.getBoolean(key,def);
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }

    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
