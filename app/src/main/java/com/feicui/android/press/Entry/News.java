package com.feicui.android.press.Entry;

/**
 * Created by Administrator on 2016/10/28.
 */
public class News {
    //向服务器请求首部
    public static final String HTTP = "http://118.244.212.82:9092/newsClient/";
    //新闻分类
    public static final String NEWS_SORT = "news_sort?";
   //新闻列表
    public static final String NEWS_LIST = "news_list?";
    //新闻内容
    public static final String NEWS_IMAGE = "news_image?";
    //发布评论
    public static final String CMT_COMMIT = "cmt_commit?";
    //显示评论
    public static final String CMT_LIST = "cmt_list?";
    //评论数量
    public static final String CMT_NUM = "cmt_num?";
    //用户中心
    public static final String USER_HOME = "user_home?";
    //头像上传
    public static final String USER_IMAGE = "user_image?";
    //登录
    public static final String USER_LOGIN = "user_login?";
    //注册
    public static final String USER_REGISTER = "user_register?";
    //找回密码
    public static final String USER_FORGETPASS = "user_forgetpass?";
    //版本升级
    public static final String UPDATA = "updata?";
}
