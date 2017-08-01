package com.naxy.xytodo.tool;

import android.content.Context;
import android.widget.Toast;

//弹出消息工具
public class ToolToast implements ToolConstant
{
    // 显示Toast消息，使用默认时长,默认类型
    public static void makeToast(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
