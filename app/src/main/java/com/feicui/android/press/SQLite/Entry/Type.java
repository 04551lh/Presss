package com.feicui.android.press.SQLite.Entry;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/3.
 */
public class Type {
    private ArrayList<NewsSortEntry> list;
    private int gid;
    private String group;

    public Type(ArrayList<NewsSortEntry> list, int gid, String group) {
        this.list = list;
        this.gid = gid;
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public ArrayList<NewsSortEntry> getList() {
        return list;
    }

    public void setList(ArrayList<NewsSortEntry> list) {
        this.list = list;
    }
}
