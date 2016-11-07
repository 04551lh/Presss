package com.feicui.android.press.SQLite.Entry;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CommitEntry {
    //评论者名字
    private String uid;
    //用户头像连接
    private String portrait;
    //评论时间
    private String time;
    //评论内容
    private String context;
    //评论编号
    private int cid;

    public CommitEntry(){}
    public CommitEntry(String uid, String portrait, String time, String context, int cid) {
        this.uid = uid;
        this.portrait = portrait;
        this.time = time;
        this.context = context;
        this.cid = cid;
    }

    public String getUid() {
        return uid;

    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
