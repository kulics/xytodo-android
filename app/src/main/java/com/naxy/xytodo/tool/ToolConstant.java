package com.naxy.xytodo.tool;


//常量工具
public interface ToolConstant
{
    // 跳转动画类型
    int TYPE_SWIPERIGHT_ENTER = 0;// 右滑返回进入
    int TYPE_SWIPERIGHT_EXIT = 1;// 右滑返回退出
    int TYPE_SWIPELEFT_ENTER = 2;// 左滑返回进入
    int TYPE_SWIPELEFT_EXIT = 3;// 左滑返回退出
    int TYPE_FALLDOWN_ENTER = 4;// 渐隐退出
    int TYPE_FALLDOWN_EXIT = 5;// 渐隐退出

    // activity requestCode类型  (默认值为0，因此不能从0开始)
    int TYPE_ITEM_ADD = 1;
    int TYPE_ITEM_SET = 2;
    int TYPE_SETTING = 3;

    // activity resultCode类型  (默认值为0，因此不能从0开始)
    int TYPE_ACTION_SUCCESS = 1;
}
