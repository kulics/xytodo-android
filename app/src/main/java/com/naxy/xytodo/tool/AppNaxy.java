package com.naxy.xytodo.tool;

import android.app.Application;

import com.naxy.xytodo.database.DBManager;

//应用入口
public class AppNaxy extends Application
{

    private DBManager mDBM;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mDBM = new DBManager(this);
    }

    public DBManager DBMGet()
    {
        return mDBM;
    }

}
