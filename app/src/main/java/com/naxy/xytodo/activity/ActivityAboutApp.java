package com.naxy.xytodo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.naxy.xytodo.R;
import com.naxy.xytodo.tool.ToolConstant;
import com.naxy.xytodo.tool.ToolFunction;

public class ActivityAboutApp extends ActivityBase
        implements ToolConstant
{
    //TextView tvLicense;// 开源许可
    TextView tvWebsite;//官网

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        //设置ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getText(R.string.activity_title_about_app));
        }
        //加载控件
        tvWebsite = (TextView) findViewById(R.id.about_official_website);
        //设置监听
        tvWebsite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String strURL = ActivityAboutApp.this.getString(R.string.about_website);
                Uri uri = Uri.parse(strURL);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    // 设置菜单监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 返回键动画
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        // 设置跳转动画
        ToolFunction.SetTransitionAnimation(this, TYPE_SWIPELEFT_EXIT);
    }

}
