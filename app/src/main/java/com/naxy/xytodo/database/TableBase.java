package com.naxy.xytodo.database;


class TableBase
{
    //表名
    static String TableName()
    {
        return "";
    }

    //项目
    static String[] TableColumns()
    {
        return new String[]{};
    }

    //创建表语句
    static String TableCreate()
    {
        return "";
    }

    //更新语句
    static String TableUpgrade()
    {
        return "DROP TABLE IF EXISTS " + TableName();
    }
}
