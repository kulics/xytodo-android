package com.naxy.xytodo.database;

import com.naxy.xytodo.model.ModelTaskSub;

//子任务表
class TableTaskSub extends TableBase {
    //表名
    static String TableName() {
        return "task_sub";
    }
    //项目
    static String[] TableColumns() {
        return new String[]{
                ModelTaskSub.COL_ID,
                ModelTaskSub.COL_ID_TASK,
                ModelTaskSub.COL_CONTENT,
                ModelTaskSub.COL_STATUS
        };
    }
    //创建表语句
    static String TableCreate() {
        return "CREATE TABLE " + TableName() + " (" +
                ModelTaskSub.COL_ID + " INTEGER PRIMARY KEY, " +
                ModelTaskSub.COL_ID_TASK + " INTEGER, " +
                ModelTaskSub.COL_CONTENT + " TEXT, " +
                ModelTaskSub.COL_STATUS + " INTEGER " +
                ");";
    }
    //更新语句
    static String TableUpgrade() {
        return "DROP TABLE IF EXISTS " + TableName();
    }
}
