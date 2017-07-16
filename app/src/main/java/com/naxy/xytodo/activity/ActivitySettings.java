package com.naxy.xytodo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;


import com.naxy.xytodo.R;
import com.naxy.xytodo.component.DialogXy;
import com.naxy.xytodo.tool.AppNaxy;
import com.naxy.xytodo.tool.ToolConstant;
import com.naxy.xytodo.tool.ToolFunction;


public class ActivitySettings extends ActivityBase implements ToolConstant{

    AppNaxy app;// 全局缓存

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //自定义ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getText(R.string.activity_title_settings));
        }
        // 装填缓存数据
        app = (AppNaxy) this.getApplication();
        //加载组件
        RelativeLayout btn0 = (RelativeLayout)findViewById(R.id.btn_clearData);
        //设置监听
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出清空数据对话框
                DialogXy dlgClearData = DialogXy.from(
                        ActivitySettings.this)
                        .setTheme(
                                R.style.DialogCardLight)
                        .setTitle(
                                R.string.dialog_title_clear_data)
                        .setAnimations(
                                R.style.WindowAnimationDropThrough)
                        .setContent(
                                R.string.dialog_content_clear_data)
                        .setPositiveID(
                                R.string.button_ok)
                        .setPositiveColorID(R.color.PURE_RED_500)
                        .setNegativieID(
                                R.string.button_cancel)
                        .setNegativeListener(
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveListener(
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        //清空数据
                                        app.DBMGet().DBClear();
                                        // 设置返回代码
                                        Intent i = new Intent();
                                        setResult(TYPE_ACTION_SUCCESS, i);
                                        ActivitySettings.this.onBackPressed();
                                        dialog.dismiss();
                                    }
                                })
                        .build();
                dlgClearData.show();
            }
        });
    }

    // 菜单按钮监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 返回键动画
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 设置跳转动画
        ToolFunction.SetTransitionAnimation(this, TYPE_SWIPELEFT_EXIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == TYPE_SETTING) {
            if (resultCode == TYPE_ACTION_SUCCESS) {
                // 设置返回代码
                Intent i = new Intent();
                setResult(TYPE_ACTION_SUCCESS, i);
            }
        }
    }
}
