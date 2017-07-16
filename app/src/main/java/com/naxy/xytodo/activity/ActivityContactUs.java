package com.naxy.xytodo.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.naxy.xytodo.R;
import com.naxy.xytodo.tool.ToolConstant;
import com.naxy.xytodo.tool.ToolFunction;

public class ActivityContactUs extends ActivityBase implements ToolConstant {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        //自定义ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getText(R.string.activity_title_contact_us));
        }
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
}
