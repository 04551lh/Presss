package com.feicui.android.press.SQLite.Entry;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 */
public class NewsSortEntry {
    private String subgroup;
    private int subid;
    private int gid;
    public NewsSortEntry(){}

    public NewsSortEntry(String subgroup, int subid) {
        this.subid = subid;
        this.subgroup = subgroup;
    }

    public NewsSortEntry(String subgroup, int subid, int gid) {
        this.subgroup = subgroup;
        this.subid = subid;
        this.gid = gid;
    }

    public String getSubgroup() {
        return subgroup;
    }

    public void setSubgroup(String subgroup) {
        this.subgroup = subgroup;
    }

    public int getSubid() {
        return subid;
    }

    public void setSubid(int subid) {
        this.subid = subid;
    }

    public int getGroup() {
        return gid;
    }

    public void setGroup(int gid) {
        this.gid = gid;
    }
}
