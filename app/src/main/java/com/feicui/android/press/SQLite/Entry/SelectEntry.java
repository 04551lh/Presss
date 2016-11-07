package com.feicui.android.press.SQLite.Entry;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/3.
 *
 */
public class SelectEntry {
    private String title;
    private String icon;
    private String summary;
    private String time;
    private String note;

    public String getTitle() {
        return title;
    }
    public SelectEntry() {
    }
    public SelectEntry(String title, String icon, String summary, String time, String note) {
        this.title = title;
        this.icon = icon;
        this.summary = summary;
        this.time = time;
        this.note = note;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
