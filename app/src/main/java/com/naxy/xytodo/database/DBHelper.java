package com.naxy.xytodo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//拓展帮助类
class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "com_naxy_xytodo.db";
    private static final int DATABASE_VERSION = 1;

    DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableTask.TableCreate());
        db.execSQL(TableTaskSub.TableCreate());
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeVersion = oldVersion;
        // 版本升级处理
        if (1 == upgradeVersion) {
        }
        // 如果超出预想处理范围，直接重制数据库
        if (upgradeVersion != newVersion) {
            // Drop tables
            db.execSQL(TableTask.TableUpgrade());
            db.execSQL(TableTaskSub.TableUpgrade());
            // Create tables
            onCreate(db);
        }
    }

}
