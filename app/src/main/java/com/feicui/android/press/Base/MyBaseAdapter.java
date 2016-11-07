package com.feicui.android.press.Base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/10/18.
 * @description Adapter基类
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    //上下文
    protected Context mContent;
    //数据列表
    protected ArrayList<T> mList = new ArrayList<T>();
    //布局加载器
    protected  LayoutInflater inflater;

    /**
     * 设置数据列表
     * @return 数据列表
     */
    public ArrayList<T> getmList() {
        return mList;
    }

    /**
     * 设置数据
     * @param mList 数据列表
     */
    public void setmList(ArrayList<T> mList) {
        this.mList = mList;
    }

    public MyBaseAdapter(Context context) {
        mContent = context;
        inflater = LayoutInflater.from(mContent);
    }

    /**
     * 添加单行数据
     * @param t 数据类型
     */
    public void insertNewsData(T t){
        mList.add(0,t);
    }

    /**
     * 添加多行数据
     * @param list 数据列表
     */
    public void insertNewsList(ArrayList<T> list){
        mList.addAll(list);
    }

    /**
     * 刷新适配器
     */
    public void updata(){
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        if (mList == null && mList.size() == 0) {
            return null;
        }
        if (position > mList.size()) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(){
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return getMyView(position,convertView,parent);
    }

    /**
     * 此方法用于适配器继承
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public abstract View getMyView(int position, View convertView, ViewGroup parent);
}