package com.naxy.xytodo.tool;

import android.content.Context;
import android.content.SharedPreferences;

//应用配置工具
public class ToolSharedPreferences {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public ToolSharedPreferences(Context c, String name) {
        sp = c.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    // 向SharedPreferences中注入数据
    public void PutStringValue(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        // 这个提交很重要，别忘记，对xml修改一定别忘了commit()
        editor.apply();
    }

    // 根据Key获取对应的Value
    public String GetStringValue(String key) {
        return sp.getString(key, "");
    }

    // 向SharedPreferences中注入数据
    public void PutBooleanValue(String key, boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        // 这个提交很重要，别忘记，对xml修改一定别忘了commit()
        editor.apply();
    }

    // 根据Key获取对应的Value
    public boolean GetBooleanValue(String key) {
        return sp.getBoolean(key, false);
    }

    // 根据Key获取对应的Value
    public int GetIntValue(String key){
        return sp.getInt(key,0);
    }
    // 向SharedPreferences中注入数据
    public void SetIntValue(String key, int value){
        editor = sp.edit();
        editor.putInt(key, value);
        // 这个提交很重要，别忘记，对xml修改一定别忘了commit()
        editor.apply();
    }

    // 清除SharedPreferences中的数据，比如点击“忘记密码”
    public void Clear() {
        editor = sp.edit();
        editor.clear();
        editor.apply();
    }
}
