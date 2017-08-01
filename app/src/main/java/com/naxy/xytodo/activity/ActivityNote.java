package com.naxy.xytodo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.naxy.xytodo.R;
import com.naxy.xytodo.tool.ToolConstant;
import com.naxy.xytodo.tool.ToolFunction;


public class ActivityNote extends ActivityBase implements ToolConstant
{
    RelativeLayout btnAction;//保存按钮
    EditText edtContent;//输入框

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        //设置ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getText(R.string.activity_title_note));
        }
        // 获取组件
        btnAction = (RelativeLayout) findViewById(R.id.rlyt_btnAction);
        edtContent = (EditText) findViewById(R.id.edt_content);
        //设置属性
        // 接收ID
        Intent intent = getIntent();
        String note = intent.getStringExtra("note");
        edtContent.setText(note);
        //设置监听
        btnAction.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // 隐藏软键盘
                ((InputMethodManager) getSystemService(
                        INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(
                                ActivityNote.this
                                        .getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                //然后再返回原页面替换
                Intent i = new Intent();
                i.putExtra("note", edtContent.getText().toString());
                setResult(TYPE_ACTION_SUCCESS, i);
                ActivityNote.this.onBackPressed();
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
        ToolFunction.SetTransitionAnimation(this, TYPE_FALLDOWN_EXIT);
    }
}
