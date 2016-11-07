package com.feicui.android.press.SQLite.Entity;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/10/28.
 */
public class NewsSort implements BaseColumns {
    //存放数据库的路径
    public static final String DB_PATH = "data/data/com.feicui.android.press/databases";
    //表名
    public static final String TABLE_NAME = "news_sort";
    //列名
    public static final String COLUMNS_SUBGROUP = "subgroup";
    public static final String COLUMNS_SUBID = "subid";
    public static final String COLUMNS_GID = "gid";
    //创建表
    public static final String SQL_CREATE_TABLE = " create table " + TABLE_NAME +
            " ("+ _ID + " integer primary key,"+ COLUMNS_SUBGROUP + " text,"+
            COLUMNS_SUBID + " text," + COLUMNS_GID + " text"
            +" )";
    //删除表
    public static final String SQL_DELETE_TABLE = " DROP TABLE IF EXISTS " + TABLE_NAME;
}
