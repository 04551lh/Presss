package com.feicui.android.press.Entry.Press;

/**
 * Created by Administrator on 2016/10/28.
 */
public class Press {
    //手机标识符
    public static final String IMEI = "&imei=";
    //版本
    public static final String VERSION = "ver=1";
    //分类号
    public static final String SUBID = "&subid=";
    //方向
    public static final String DIR = "&dir=";
    //新闻ID
    public static final String NID = "&nid=";
    //新闻时间
    public static final String STAMP = "&stamp=20140321";
    //数目
    public static final String CNT = "&cnt=20";
    //常量
    public static final int M = 1;
    /**
     * @description 新闻分类或者新闻内容
     * @param imei
     */
    public static String getSort(long imei) {
        return VERSION + IMEI + imei;
    }

    /**
     * @description 新闻列表
     * @param dir
     * @param nid
     */
    public static String getList(int dir, int nid) {
      return VERSION + SUBID + DIR + dir + NID + nid + STAMP + "20140321" +CNT + "20";
    }

}
