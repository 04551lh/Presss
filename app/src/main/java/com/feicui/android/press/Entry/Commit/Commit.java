package com.feicui.android.press.Entry.Commit;

/**
 * Created by Administrator on 2016/10/28.
 */
public class Commit {
    //版本
    public static final String VERSION = "&ver=1";
    //新闻ID
    public static final String NID = "&nid=";
    //用户令牌
    public static final String TOKEN =  "&token=";
    //手机标识符
    public static final String IMEI =  "&imei=111111111111111";
    //评论内容
    public static final String CTX = "&ctx=";
    //新闻类型
    public static final String TYPE = "&type=1";
    //拉动方向
    public static final String DIR =  "&dir=";
    //新闻时间
    public static final String STAMP = "&stamp=20140321";
    //评论ID
    public static final String CID =  "&cid=";
    //数目
    public static final String CNT =  "&cnt=20";

    /**
     * @description 发布评论
     * @param nid
     * @param token
     * @param ctx
     * @return
     */
    public static String getCommit(int nid, String token, String ctx){
        return VERSION + NID + nid + TOKEN + token + IMEI + CTX + ctx;
    }

    /**
     * @description 显示评论
     * @param nid
     * @param cid
     * @param dir
     * @return
     */
    public static String getList(int nid, int cid, int dir){
        return VERSION + NID +nid + TYPE + STAMP + CID + cid +DIR +dir +CNT;
    }

    /**
     * @description 评论数量
     * @param nid
     * @return
     */
    public static String getNum(int nid){
        return VERSION + NID +nid;
    }
}
