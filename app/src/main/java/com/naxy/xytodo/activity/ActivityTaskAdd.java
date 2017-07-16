package com.naxy.xytodo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.naxy.xytodo.R;
import com.naxy.xytodo.component.AdapterTaskEdit;
import com.naxy.xytodo.component.DialogColorTag;
import com.naxy.xytodo.component.DialogDate;
import com.naxy.xytodo.model.ModelTask;
import com.naxy.xytodo.tool.AppNaxy;
import com.naxy.xytodo.tool.ToolConstant;
import com.naxy.xytodo.tool.ToolFunction;
import com.naxy.xytodo.tool.ToolSharedPreferences;

import java.util.Calendar;
import java.util.Date;


public class ActivityTaskAdd extends ActivityBase implements ToolConstant {
    private ModelTask mData;//数据

    RecyclerView mRecyclerView;// 列表
    LinearLayoutManager mLayoutManager;// 列表布局管理器
    AdapterTaskEdit mAdapter;// 列表适配器
    private RelativeLayout btnAction;// 主按钮
    private Toolbar mToolbar;//工具栏
    private AppNaxy app;// 全局缓存

    private DialogColorTag dlgColor;//设置颜色框
    private DialogDate dlgDate;//日期获取框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_edit);
        //设置ActionBar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            setTitle(getText(R.string.activity_title_task_add));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // 加载控件
        btnAction = (RelativeLayout) findViewById(R.id.rlyt_btnAction);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // 创建一个线性布局管理器
        mLayoutManager = new LinearLayoutManager(this);
        // layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 装填缓存数据
        app = (AppNaxy) this.getApplication();
        mData = new ModelTask();
        long unixTime = new Date().getTime();//获取当前时区下日期时间对应的时间戳
        mData.setTimeCreate((int) (unixTime / 1000));
        mData.setTimeTarget(mData.getTimeCreate());
        mData.setTimeSort(mData.getTimeCreate());
        //读取最后的使用色
        ToolSharedPreferences spfl = new ToolSharedPreferences(this, "set_info");
        mData.setColor(spfl.GetStringValue("color_last"));
        if (mData.getColor().length() <= 0) {
            mData.setColor("blue");
        }
        setColor(mData.getColor());
        initRecyclerView();
        //设置监听
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取输入栏消息
                if (mData.getContent().length() > 0) {
                    mData.setSub(mAdapter.DataSubGet());
                    app.DBMGet().TaskAdd(mData);
                    Intent i = new Intent();
                    setResult(TYPE_ACTION_SUCCESS, i);
                    ActivityTaskAdd.this.onBackPressed();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == TYPE_ITEM_SET) {
            if (resultCode == TYPE_ACTION_SUCCESS) {
                // 更新
                String note = data.getStringExtra("note");
                mAdapter.NoteUpdate(note);
            }
        }
    }

    // 返回键动画
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 设置跳转动画
        ToolFunction.SetTransitionAnimation(this, TYPE_SWIPERIGHT_EXIT);
    }

    //创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_add, menu);
        return true;
    }

    //监听菜单
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add:
                mAdapter.DataSubAdd();
                mRecyclerView.scrollToPosition(mAdapter.getItemCount());
                return true;
            case R.id.action_palette:
                // 设置调色对话框
                dlgColor = DialogColorTag.from(ActivityTaskAdd.this).setTheme(
                        R.style.DialogCardLight)
                        .setTitle(
                                R.string.dialog_title_palette).
                                setAnimations(
                                        R.style.WindowAnimationDropThrough)
                        .setColorTag(mData.getColor())
                        .setPositiveID(
                                R.string.button_ok)
                        .setPositiveColorID(R.color.PURE_BLUE_500)
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
                                        mData.setColor(dlgColor.getColorTag());
                                        //保存最后的使用色
                                        ToolSharedPreferences spfl = new ToolSharedPreferences(ActivityTaskAdd.this, "set_info");
                                        spfl.PutStringValue("color_last", mData.getColor());
                                        setColor(mData.getColor());
                                        dialog.dismiss();
                                    }
                                })
                        .build();
                dlgColor.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 初始化listview
    private void initRecyclerView() {
        mAdapter = new AdapterTaskEdit(this, app.DBMGet(),
                new AdapterTaskEdit.ViewHolderClicks() {
                    @Override
                    public void onViewClick(int position) {
                        if (position == 2) {
                            Intent intent = new Intent(
                                    ActivityTaskAdd.this,
                                    ActivityNote.class);
                            intent.putExtra("note", mData.getNote());
                            ActivityTaskAdd.this
                                    .startActivityForResult(intent, TYPE_ITEM_SET);
                            // 设置跳转动画
                            ToolFunction
                                    .SetTransitionAnimation(
                                            ActivityTaskAdd.this,
                                            TYPE_FALLDOWN_ENTER);
                        }
                    }
                }, new AdapterTaskEdit.ViewHolderBtnClicks() {
            @Override
            public void onViewClick(int position, View v) {
                switch (position) {
                    case 1:
                        //创建菜单弹窗
                        PopupMenu popup = new PopupMenu(ActivityTaskAdd.this, v);
                        //初始化菜单弹窗
                        popup.getMenuInflater()
                                .inflate(R.menu.menu_task_time, popup.getMenu());
                        //设置监听
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                long unixTime = new Date().getTime();//获取当前时区下日期时间对应的时间戳
                                int now = (int) (unixTime / 1000);
                                switch (item.getItemId()) {
                                    case R.id.action_today:
                                        mData.setTimeTarget(now);
                                        break;
                                    case R.id.action_tomorrow:
                                        mData.setTimeTarget(now + 86400);
                                        break;
                                    case R.id.action_next_week:
                                        mData.setTimeTarget(now + 604800);
                                        break;
                                    case R.id.action_custom:
                                        dlgDate = DialogDate.from(ActivityTaskAdd.this)
                                                .setTheme(R.style.DialogCardLight)
                                                .setAnimations(R.style.WindowAnimationDropThrough)
                                                .setPositiveID(
                                                        R.string.button_ok)
                                                .setPositiveColorID(R.color.PURE_BLUE_500)
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
                                                        }).setPositiveListener(
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface dialog, int id) {
                                                                DatePicker pk = dlgDate.PickerGet();
                                                                // 创建 Calendar 对象
                                                                Calendar calendar = Calendar.getInstance();
                                                                //设置时间
                                                                calendar.set(pk.getYear(), pk.getMonth(), pk.getDayOfMonth());
                                                                //保存
                                                                mData.setTimeTarget((int) (calendar.getTime().getTime() / 1000));
                                                                mAdapter.TimeUpdate();
                                                                dialog.dismiss();
                                                            }
                                                        }).build();
                                        dlgDate.show();
                                        break;
                                    default:
                                        break;
                                }
                                mAdapter.TimeUpdate();
                                return true;
                            }
                        });
                        popup.show(); //showing popup menu
                        break;
                    default:
                        mAdapter.DataSubDelete(position);
                        break;
                }
            }
        });
        // 设置适配器
        mRecyclerView.setAdapter(mAdapter);
        //初始化数据
        mAdapter.UpdateDataset(mData);
    }

    //设置沉浸颜色
    public void setColor(String color) {
        switch (color) {
            case "green":
                mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.PURE_GREEN_500));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.PURE_GREEN_700));
                }
                break;
            case "blue":
                mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.PURE_BLUE_500));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.PURE_BLUE_700));
                }
                break;
            case "red":
                mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.PURE_RED_500));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.PURE_RED_700));
                }
                break;
            case "yellow":
                mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.PURE_YELLOW_500));
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(ContextCompat.getColor(this, R.color.PURE_YELLOW_700));
                }
                break;
        }
    }


}