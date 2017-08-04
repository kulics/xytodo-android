package com.naxy.xytodo.tool;

import android.app.Activity;
import android.content.Context;

import com.naxy.xytodo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

//常用方法工具
public class ToolFunction implements ToolConstant
{
    private static long lastClickTime; // 防止连点计时

    // 防止连点判断
    public static boolean IsFastClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500)
        {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    // activity切换动画
    public static void SetTransitionAnimation(Activity acty, int type)
    {
        switch (type)
        {
            case TYPE_SWIPERIGHT_ENTER:
                acty.overridePendingTransition(R.anim.swipeback_stack_right_in, R.anim.swipeback_stack_to_back);
                break;// 右滑返回进入
            case TYPE_SWIPERIGHT_EXIT:
                acty.overridePendingTransition(R.anim.swipeback_stack_to_front, R.anim.swipeback_stack_right_out);
                break;// 右滑返回退出
            case TYPE_SWIPELEFT_ENTER:
                acty.overridePendingTransition(R.anim.swipeback_stack_left_in, R.anim.swipeback_stack_to_back);
                break;// 左滑返回进入
            case TYPE_SWIPELEFT_EXIT:
                acty.overridePendingTransition(R.anim.swipeback_stack_to_front, R.anim.swipeback_stack_left_out);
                break;// 左滑返回退出
            case TYPE_FALLDOWN_ENTER:
                acty.overridePendingTransition(R.anim.fall_down_enter, R.anim.swipeback_stack_to_back);
                break;// 渐隐退出
            case TYPE_FALLDOWN_EXIT:
                acty.overridePendingTransition(R.anim.swipeback_stack_to_front, R.anim.fall_down_exit);
                break;// 渐隐退出
        }
    }

    //日期处理方法
    public static String GetDateFromUTC(Context context, long time)
    {
        Date dateTarget = new java.util.Date(time * 1000);
        Date dateNow = new Date();
        SimpleDateFormat fYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat fDay = new SimpleDateFormat("D");

        int dYear = Integer.parseInt(fYear.format(dateNow)) - Integer.parseInt(fYear.format(dateTarget));
        int dDay = Integer.parseInt(fDay.format(dateNow)) - Integer.parseInt(fDay.format(dateTarget));
        //按格式输出时间
        String date = "";
        if (dYear > 0) //去年以前
        {
            date = new SimpleDateFormat("yyyy-MM-dd").format(dateTarget);
        }
        else if (dYear <= -1)  //明年以后
        {
            date = new SimpleDateFormat("yyyy-MM-dd").format(dateTarget);
        }
        else if (dDay > 1) //昨天以前
        {
            date = new SimpleDateFormat("MM-dd EEE").format(dateTarget);
        }
        else if (dDay > 0) //昨天
        {
            date = context.getString(R.string.yesterday) + new SimpleDateFormat("  EEE").format(dateTarget);
        }
        else if (dDay == 0) //今天
        {
            date = context.getString(R.string.today) + new SimpleDateFormat("  EEE").format(dateTarget);
        }
        else if (dDay > -2) //明天
        {
            date = context.getString(R.string.tomorrow) + new SimpleDateFormat("  EEE").format(dateTarget);
        }
        else //明天以后
        {
            date = new SimpleDateFormat("MM-dd  EEE").format(dateTarget);
        }

        return date;
    }

    //换算是否当天
    public static boolean ComputeDateToday(long time)
    {
        Date dateTarget = new java.util.Date(time * 1000);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        if (fmt.format(dateTarget).toString().equals(fmt.format(new Date()).toString())) //格式化为相同格式
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
