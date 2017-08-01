package com.naxy.xytodo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.naxy.xytodo.R;
import com.naxy.xytodo.component.AdapterTask;
import com.naxy.xytodo.component.DialogEditText;
import com.naxy.xytodo.model.ModelTask;
import com.naxy.xytodo.tool.AppNaxy;
import com.naxy.xytodo.tool.ToolConstant;
import com.naxy.xytodo.tool.ToolFunction;
import com.naxy.xytodo.tool.ToolSharedPreferences;
import com.naxy.xytodo.tool.ToolToast;

import java.util.Collections;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class ActivityMain extends ActivityBase implements ToolConstant
{
    private AppNaxy app;// 全局缓存
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView mRecyclerView;// 消息列表
    private AdapterTask mAdapter;// 列表适配器
    private FloatingActionButton mFloatingAction;// 浮动按钮
    private DialogEditText dlgTaskAdd;// 添加对话框
    private TextView tvTag;//顶部标签
    private String mFilter = "today";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置ActionBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(getText(R.string.activity_title_main));
        }
        //设置侧边栏
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name,
                                                  R.string.menu_settings)
        {

            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
        {
            setupDrawerContent(navigationView);
        }
        // 加载控件
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFloatingAction = (FloatingActionButton) findViewById(R.id.fab);
        tvTag = (TextView) findViewById(R.id.tv_tag);
        // 创建一个线性布局管理器
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);

        // 装填缓存数据
        app = (AppNaxy) getApplication();
        this.FABInit();
        this.AdapterInit();
        this.RecyclerViewInit();
        this.UpdateTag();
        // 设置监听
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // 当otherActivity中返回数据的时候，会响应此方法
        // requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
        if (requestCode == TYPE_ITEM_SET)
        {
            if (resultCode == TYPE_ACTION_SUCCESS)
            {
                //更新数据
                UpdateListData(mFilter);
                UpdateTag();
            }
        }
        if (requestCode == TYPE_SETTING)
        {
            if (resultCode == TYPE_ACTION_SUCCESS)
            {
                //更新数据
                UpdateListData(mFilter);
                UpdateTag();
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    // 创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        //menuItem.setChecked( true );
                        // mDrawerLayout.closeDrawers();
                        switch (menuItem.getItemId())
                        {
                            case R.id.action_setting:
                                Intent settingIntent =
                                        new Intent(ActivityMain.this, ActivitySettings.class);
                                ActivityMain.this.startActivityForResult(settingIntent, TYPE_SETTING);
                                // 设置跳转动画
                                ToolFunction.SetTransitionAnimation(ActivityMain.this,
                                                                    TYPE_SWIPELEFT_ENTER);
                                return true;
                            case R.id.action_about:
                                Intent aboutIntent =
                                        new Intent(ActivityMain.this, ActivityAboutApp.class);
                                ActivityMain.this.startActivity(aboutIntent);
                                // 设置跳转动画
                                ToolFunction.SetTransitionAnimation(ActivityMain.this,
                                                                    TYPE_SWIPELEFT_ENTER);
                                return true;
                            case R.id.action_guide:
                                Intent guideIntent =
                                        new Intent(ActivityMain.this, ActivityGuide.class);
                                ActivityMain.this.startActivity(guideIntent);
                                // 设置跳转动画
                                ToolFunction.SetTransitionAnimation(ActivityMain.this,
                                                                    TYPE_SWIPELEFT_ENTER);
                                return true;
                            case R.id.action_contact_us:
                                Intent joinIntent =
                                        new Intent(ActivityMain.this, ActivityContactUs.class);
                                ActivityMain.this.startActivity(joinIntent);
                                // 设置跳转动画
                                ToolFunction.SetTransitionAnimation(ActivityMain.this,
                                                                    TYPE_SWIPELEFT_ENTER);
                                return true;
                            default:
                                return onNavigationItemSelected(menuItem);
                        }
                    }
                });
    }

    // 菜单按键监听
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //侧边栏按钮控制
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // 连点判断
        if (ToolFunction.IsFastClick())
        {
            ToolToast.makeToast(this,
                                this.getString(R.string.toast_fast_click)
            );
            return true;
        }
        else
        {
            switch (item.getItemId())
            {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                case R.id.action_today:
                    mFilter = "today";
                    mAdapter.DataUpdate(mFilter);
                    UpdateTag();
                    return true;
                case R.id.action_all:
                    mFilter = "all";
                    mAdapter.DataUpdate(mFilter);
                    UpdateTag();
                    return true;
                case R.id.action_todo:
                    mFilter = "todo";
                    mAdapter.DataUpdate(mFilter);
                    UpdateTag();
                    return true;
                case R.id.action_done:
                    mFilter = "done";
                    mAdapter.DataUpdate(mFilter);
                    UpdateTag();
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    // 返回键
    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            exitBy2Click();
        }
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click()
    {
        Timer tExit = null;
        if (isExit == false)
        {
            isExit = true; // 准备退出
            Toast.makeText(this, this.getString(R.string.toast_quit), Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void FABInit()
    {
        //设置浮动按钮
        mFloatingAction.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //短按快捷添加
                dlgTaskAdd = DialogEditText.from(ActivityMain.this)
                        .setTheme(R.style.DialogCardLight)
                        .setTitle(R.string.activity_title_task_add)
                        .setEditHint(R.string.content)
                        .setEditErr(R.string.err_name_none)
                        .setAnimations(R.style.WindowAnimationDropThrough)
                        .setPositiveID(R.string.button_add)
                        .setPositiveColorID(R.color.PURE_BLUE_500)
                        .setNegativeID(
                                R.string.button_cancel)
                        .setNegativeListener(
                                new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id)
                                    {
                                        dialog.dismiss();
                                    }
                                })
                        .setPositiveListener(new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int id)
                            {
                                // 获取输入栏消息
                                String strContent =
                                        dlgTaskAdd.getText();
                                //过滤空内容
                                if (strContent.length() > 0)
                                {
                                    ModelTask temp = new ModelTask();
                                    temp.setContent(strContent);
                                    temp.setNote("");
                                    //读取最后的使用色
                                    ToolSharedPreferences spfl = new ToolSharedPreferences(ActivityMain.this, "set_info");
                                    temp.setColor(spfl.GetStringValue("color_last"));
                                    if (temp.getColor().length() <= 0)
                                    {
                                        temp.setColor("blue");
                                    }
                                    long unixTime = new Date().getTime();//获取当前时区下日期时间对应的时间戳
                                    temp.setTimeCreate((int) (unixTime / 1000));
                                    temp.setTimeTarget(temp.getTimeCreate());
                                    temp.setTimeDone(0);
                                    temp.setTimeSort(temp.getTimeCreate());
                                    temp.setStatus(0);
                                    int modelID = app.DBMGet().TaskAdd(temp);
                                    mAdapter.DataAdd(app.DBMGet().TaskGet(modelID));
                                    mRecyclerView.scrollToPosition(0);
                                }
                                dialog.dismiss();
                            }
                        })
                        .build();
                dlgTaskAdd.show();
                Timer timer = new Timer();
                timer.schedule(new TimerTask()
                {

                    @Override
                    public void run()
                    {
                        dlgTaskAdd.showKeyboard();
                    }
                }, 300);
            }
        });
        mFloatingAction.setLongClickable(true);
        mFloatingAction.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                //长按跳转详细编辑页面
                Intent addIntent = new Intent(ActivityMain.this,
                                              ActivityTaskAdd.class);
                ActivityMain.this
                        .startActivityForResult(addIntent, TYPE_ITEM_SET);
                // 设置跳转动画
                ToolFunction.SetTransitionAnimation(ActivityMain.this,
                                                    TYPE_SWIPERIGHT_ENTER);
                return true;
            }
        });
    }

    private void AdapterInit()
    {
        mAdapter = new AdapterTask(this, app.DBMGet(),
                                   new AdapterTask.ViewHolderClicks()
                                   {
                                       @Override
                                       public void onViewClick(
                                               int position, View text, View background)
                                       {
                                           //跳转到编辑页面
                                           Intent intent =
                                                   new Intent(ActivityMain.this, ActivityTaskEdit.class);
                                           intent.putExtra("id", mAdapter.DataGetByPosition(position).getID());
//                        ActivityOptionsCompat options = ActivityOptionsCompat
//                                .makeSceneTransitionAnimation(ActivityMain.this,
//                                        null);
//                        getWindow().setExitTransition(new Explode());
//                        startActivityForResult(intent, TYPE_ITEM_SET, options.toBundle());

                                           ActivityMain.this.startActivityForResult(intent, TYPE_ITEM_SET);
                                           ToolFunction.SetTransitionAnimation(ActivityMain.this, TYPE_SWIPERIGHT_ENTER);
                                       }
                                   }, new AdapterTask.ViewHolderCbxChange()
        {
            @Override
            public void onViewClick(int position, boolean isChecked)
            {
                ModelTask temp = mAdapter.DataGetByPosition(position);
                //必须先对当前状态做判断，因为每次装填都会触发事件，这里需要过滤
                int b = isChecked ? 1 : 0;
                if (temp.getStatus() != b)
                {
                    temp.setStatus(b);
                    long unixTime = new Date().getTime();//获取当前时区下日期时间对应的时间戳
                    temp.setTimeDone((int) (unixTime / 1000));
                    app.DBMGet().TaskCheck(temp);
                    UpdateTag();
                    mAdapter.UpdateStatus(position);
                }
            }
        });
        //更新数据
        UpdateListData(mFilter);
    }

    // 初始化listview
    private void RecyclerViewInit()
    {
        //设置拖拽
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback()
        {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
            {
                //支持上下拖动
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                if (mAdapter.getItemCount() == 0)
                {
                    return false;
                }
                //处理拖动排序
                //使用Collection对数组进行重排序，目的是把我们拖动的Item换到下一个目标Item的位置
                Collections.swap(mAdapter.DataGetAll(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                //对数据库重新排位置
                ModelTask mSelect = mAdapter.DataGetByPosition(viewHolder.getAdapterPosition());
                int selectPosition = mSelect.getTimeSort();
                ModelTask mTarget = mAdapter.DataGetByPosition(target.getAdapterPosition());
                int targetPosition = mTarget.getTimeSort();
                //更新数据库
                mSelect.setTimeSort(targetPosition);
                app.DBMGet().TaskPosition(mSelect);
                mTarget.setTimeSort(selectPosition);
                app.DBMGet().TaskPosition(mTarget);
                //通知Adapter它的Item发生了移动
                mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {

            }
        });
        helper.attachToRecyclerView(mRecyclerView);
        // 设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }

    //更新所有数据
    private void UpdateListData(String filter)
    {
        mAdapter.DataUpdate(filter);
    }

    //更新顶部标签
    private void UpdateTag()
    {
        String tag = "";
        switch (mFilter)
        {
            case "today":
                tag = this.getString(R.string.today);
                break;
            case "all":
                tag = this.getString(R.string.all);
                break;
            case "todo":
                tag = this.getString(R.string.todo);
                break;
            case "done":
                tag = this.getString(R.string.done);
                break;
        }
        tvTag.setText(tag + "  ▪  " + mAdapter.CountGet());
    }
}
