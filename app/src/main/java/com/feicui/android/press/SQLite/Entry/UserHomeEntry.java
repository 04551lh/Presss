package com.feicui.android.press.SQLite.Entry;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/1.
 */
public class UserHomeEntry {
    private String name;
    private String portrait;
    private int integration;
    private int comnum;
    private String time;
    private String address;
    ArrayList<UserHomeEntry> list;
    private int device;

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public UserHomeEntry(){};
    public UserHomeEntry(String name, String portrait, int integration, int comnum, ArrayList<UserHomeEntry> list) {
        this.name = name;
        this.portrait = portrait;
        this.integration = integration;
        this.comnum = comnum;
        this.list = list;
    }

    public UserHomeEntry(String time, String address, int device) {
        this.time = time;
        this.address = address;
        this.device = device;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public int getComnum() {
        return comnum;
    }

    public void setComnum(int comnum) {
        this.comnum = comnum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
