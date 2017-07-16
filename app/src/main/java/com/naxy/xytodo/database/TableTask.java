package com.naxy.xytodo.database;

import com.naxy.xytodo.model.ModelTask;

//任务表
class TableTask extends TableBase {
    //表名
    static String TableName() {
        return "task";
    }
    //项目
    static String[] TableColumns() {
        return new String[]{
                ModelTask.COL_ID,
                ModelTask.COL_CONTENT,
                ModelTask.COL_NOTE,
                ModelTask.COL_COLOR,
                ModelTask.COL_TIME_CREATE,
                ModelTask.COL_TIME_TARGET,
                ModelTask.COL_TIME_DONE,
                ModelTask.COL_TIME_SORT,
                ModelTask.COL_STATUS
        };
    }
    //创建表语句
    static String TableCreate() {
        return "CREATE TABLE " + TableName() + " (" +
                ModelTask.COL_ID + " INTEGER PRIMARY KEY, " +
                ModelTask.COL_CONTENT + " TEXT, " +
                ModelTask.COL_NOTE + " TEXT, " +
                ModelTask.COL_COLOR + " TEXT, " +
                ModelTask.COL_TIME_CREATE + " INTEGER, " +
                ModelTask.COL_TIME_TARGET + " INTEGER, " +
                ModelTask.COL_TIME_DONE + " INTEGER, " +
                ModelTask.COL_TIME_SORT + " INTEGER, " +
                ModelTask.COL_STATUS + " INTEGER " +
                ");";
    }
    //更新语句
    static String TableUpgrade() {
        return "DROP TABLE IF EXISTS " + TableName();
    }
}
