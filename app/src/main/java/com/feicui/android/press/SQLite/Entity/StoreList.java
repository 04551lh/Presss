package com.feicui.android.press.SQLite.Entity;

import android.provider.BaseColumns;

/**
 * Created by Administrator on 2016/10/28.
 */
public class StoreList implements BaseColumns{
    //表名
    public static final String TABLE_NAME = "store_list";
    //列名
    public static final String COLUMNS_SUMMARY = "summary";
    public static final String COLUMNS_ICON = "icon";
    public static final String COLUMNS_STAMP = "stamp";
    public static final String COLUMNS_TITLE = "title";
    public static final String COLUMNS_NID = "nid";
    public static final String COLUMNS_LINK = "link";
    public static final String COLUMNS_TYPE = "type";
    //创建表
    public static final String SQL_CREATE_TABLE = " create table " + TABLE_NAME + " ("+
            _ID + " integer primary key,"+ COLUMNS_SUMMARY + " text,"+
            COLUMNS_ICON + " text," + COLUMNS_STAMP + " text," +
            COLUMNS_TITLE + " text," + COLUMNS_NID + " text," +
            COLUMNS_LINK + " text," + COLUMNS_TYPE + " text"+" )";
    //删除表
    public static final String SQL_DELETE_TABLE =
            " DROP TABLE IF EXISTS " + TABLE_NAME;
}
