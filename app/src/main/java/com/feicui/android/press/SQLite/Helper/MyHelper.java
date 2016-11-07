package com.feicui.android.press.SQLite.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.feicui.android.press.Entry.News;
import com.feicui.android.press.SQLite.Entity.NewsList;
import com.feicui.android.press.SQLite.Entity.NewsSort;
import com.feicui.android.press.SQLite.Entity.StoreList;
import com.feicui.android.press.SQLite.Entry.NewsListEntry;
import com.feicui.android.press.SQLite.Entry.NewsSortEntry;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 */
public class MyHelper extends SQLiteOpenHelper {
    //数据库版本
    public static final int VERSION = 1;
    //数据库名称
    public  static final String DB_NAME = "newsClient.db";

    private SQLiteDatabase db;
    private ContentValues values;
    private ArrayList<NewsSortEntry> title;
    private ArrayList<NewsListEntry> text;
    private NewsSortEntry newsSort;
    private NewsListEntry newsList;

    public MyHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NewsSort.SQL_CREATE_TABLE);
        db.execSQL(NewsList.SQL_CREATE_TABLE);
        db.execSQL(StoreList.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(NewsSort.SQL_DELETE_TABLE);
        db.execSQL(NewsList.SQL_DELETE_TABLE);
        db.execSQL(StoreList.SQL_DELETE_TABLE);
        onCreate(db);
    }

    /**
     * @description 插入标题数据库
     * @param list
     */
    public void insertNewsSortDB(ArrayList<NewsSortEntry> list, int gid){
        db = getWritableDatabase();
        for (int position = 0; position < list.size(); position++) {
            values = new ContentValues();
            values.put(NewsSort.COLUMNS_SUBGROUP, list.get(position).getSubgroup());
            values.put(NewsSort.COLUMNS_SUBID, list.get(position).getSubid());
            values.put(NewsSort.COLUMNS_GID, gid);
            db.insert(NewsSort.TABLE_NAME, null, values);
        }
    }
    /**
     * @description 插入新闻列表数据库
     * @param list
     */
    public void insertNewsListDB(ArrayList<NewsListEntry> list){
        db = getWritableDatabase();
        for (int position = 0; position <list.size() ; position++) {
          Cursor mCursor =  db.rawQuery(" select * from " + NewsList.TABLE_NAME +" where nid = ?" ,new String[]{list.get(position).getNid()+""});
            if(mCursor.getCount() > 0){
                continue;
            }
            values = new ContentValues();
            values.put(NewsList.COLUMNS_SUMMARY, list.get(position).getSummary());
            values.put(NewsList.COLUMNS_ICON, list.get(position).getIcon());
            values.put(NewsList.COLUMNS_STAMP, list.get(position).getStamp());
            values.put(NewsList.COLUMNS_TITLE, list.get(position).getTitle());
            values.put(NewsList.COLUMNS_NID, list.get(position).getNid());
            values.put(NewsList.COLUMNS_LINK, list.get(position).getLink());
            values.put(NewsList.COLUMNS_TYPE, list.get(position).getType());
            db.insert(NewsList.TABLE_NAME, null, values);
        }
    }

    /**
     * @description 添加收藏
     * @param listEntry
     */
    public void insertColloctDB(NewsListEntry listEntry){
        db = getWritableDatabase();
        values = new ContentValues();
        values.put(StoreList.COLUMNS_SUMMARY, listEntry.getSummary());
        values.put(StoreList.COLUMNS_ICON, listEntry.getIcon());
        values.put(StoreList.COLUMNS_STAMP, listEntry.getStamp());
        values.put(StoreList.COLUMNS_TITLE, listEntry.getTitle());
        values.put(StoreList.COLUMNS_NID, listEntry.getNid());
        values.put(StoreList.COLUMNS_LINK, listEntry.getLink());
        values.put(StoreList.COLUMNS_TYPE, listEntry.getType());
        db.insert(StoreList.TABLE_NAME, null, values);
        db.close();
    }

    /**
     * @description 删除新闻
     * @param listEntry
     */
    public void deleteCollectDB(NewsListEntry listEntry){
        db = getWritableDatabase();
        db.delete(StoreList.TABLE_NAME,
                " nid = ?",
                new String[]{listEntry.getNid()+""});
    }

    /**
     * @description 查询收藏数据
     * @return
     */
    public ArrayList<NewsListEntry> queryCollectDB(){
        text = new ArrayList<>();
        db = getReadableDatabase();
        Cursor cursor = db.query(StoreList.TABLE_NAME,
                new String[]{"*"},
                null,
                null,
                null,
                null,
                null,
                null
        );
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String summary = cursor.getString(cursor.getColumnIndexOrThrow(StoreList.COLUMNS_SUMMARY));
                String icon = cursor.getString(cursor.getColumnIndexOrThrow(StoreList.COLUMNS_ICON));
                String stamp = cursor.getString(cursor.getColumnIndexOrThrow(StoreList.COLUMNS_STAMP));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(StoreList.COLUMNS_TITLE));
                int nid = cursor.getInt(cursor.getColumnIndexOrThrow(StoreList.COLUMNS_NID));
                String link = cursor.getString(cursor.getColumnIndexOrThrow(StoreList.COLUMNS_LINK));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(StoreList.COLUMNS_TYPE));
                newsList = new NewsListEntry(summary, icon, stamp, title, nid,link, type);
                text.add(newsList);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return text;
    }

    /**
     * @description 更新收藏数据
     * @param listEntry
     */
    public void upCollectData(NewsListEntry listEntry){
        db = getWritableDatabase();
        String str = listEntry.getNid()+"";
        ContentValues values = new ContentValues();
        values.put(NewsList.COLUMNS_TYPE, listEntry.getType());
        db.update(NewsList.TABLE_NAME,
                values,
                " nid = ? ",
                new String[]{str}
        );
        db.close();
    }
    /**
     * @descreiption 查询标题数据库
     * @return
     */
    public ArrayList<NewsSortEntry> queryNewsSortDB(int gid){
        db = getReadableDatabase();
        title = new ArrayList<>();
        Cursor cursor = db.rawQuery(" select * from " + NewsSort.TABLE_NAME + " where gid =  ?" , new String[]{gid + ""});
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String subgroup = cursor.getString(cursor.getColumnIndexOrThrow(NewsSort.COLUMNS_SUBGROUP));
                int subid = cursor.getInt(cursor.getColumnIndexOrThrow(NewsSort.COLUMNS_SUBID));
                newsSort = new NewsSortEntry(subgroup,subid);
                title.add(newsSort);
            }while (cursor.moveToNext());
        }
       if(db != null){
           db.close();
       }
        if(cursor != null){
            cursor.close();
        }
        return title;
    }

    public ArrayList<NewsListEntry> queryNewsListDB(int start, int end, int subId){
        text = new ArrayList<>();
        db = getWritableDatabase();
        String sql = " select * from "+ NewsList.TABLE_NAME + " order by _id limit " + end + " offset " + start;
        Cursor cursor = db.rawQuery(sql, null);
//        String _ID = " _ID";
//        String limit = end + " offset " + start;
//        Cursor cursor = db.query(NewsList.TABLE_NAME,
//                new String[]{"*"},
//                null,
//                null,
//                null,
//                null,
//                _ID,
//                limit);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String summary = cursor.getString(cursor.getColumnIndexOrThrow(NewsList.COLUMNS_SUMMARY));
                String icon = cursor.getString(cursor.getColumnIndexOrThrow(NewsList.COLUMNS_ICON));
                String stamp = cursor.getString(cursor.getColumnIndexOrThrow(NewsList.COLUMNS_STAMP));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(NewsList.COLUMNS_TITLE));
                int nid = cursor.getInt(cursor.getColumnIndexOrThrow(NewsList.COLUMNS_NID));
                String link = cursor.getString(cursor.getColumnIndexOrThrow(NewsList.COLUMNS_LINK));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow(NewsList.COLUMNS_TYPE));
                newsList = new NewsListEntry(summary, icon, stamp, title, nid,link, type);
                text.add(newsList);
            }while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return text;
    }

    public int queryNewsListDB(int nid){
        int type = 1;
        newsList = new NewsListEntry();
        db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" select * from " + NewsList.TABLE_NAME +" where nid = ?" ,new String[]{nid+""});
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            type = cursor.getInt(cursor.getColumnIndex(NewsList.COLUMNS_TYPE));
        }
        cursor.close();
        db.close();
        return type;
    }

}
