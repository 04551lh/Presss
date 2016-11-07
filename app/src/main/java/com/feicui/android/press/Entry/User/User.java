package com.feicui.android.press.Entry.User;

import java.io.File;

/**
 * Created by Administrator on 2016/10/28.
 * @descreiption 用户中心
 */
public class User {
    //版本
    public static final String VERSION = "ver=1";
    //手机标识符
    public static final String IMEI = "&imei=";
    //用户令牌
    public static final String TOKEN = "&token=";
    //头像
    public static final String PORTRAIT = "&portrait=";
    //用户名
    public static final String UID = "&uid=";
    //密码
    public static final String PWD = "&pwd=";
    //登录设备(0为手机客户端，1为PC网页端)
    public static final String DEVICE = "&device=0";
    //邮箱
    public static final String EMAIL = "&email=";
    //包名
    public static final String PKG = "&pkg=";

    /**
     * 用户中心
     * @param imei
     * @param token
     */
    public static String userHome(int imei, String token) {
        return VERSION + IMEI + imei + TOKEN + token;
    }

    /**
     * @description 头像上传
     * @param token
     * @param portrait
     */
    public static String userImage(String token, File portrait) {
        return TOKEN + token + PORTRAIT + portrait;
    }

    /**
     * @description 登录
     * @param uid
     * @param pwd
     */
    public static String userLogin(String uid, String pwd) {
        return VERSION + UID + uid + PWD + pwd + DEVICE;
    }

    /**
     * @decription 注册
     * @param uid
     * @param email
     * @param pwd
     */
    public static String userRegister(String uid, String email, String pwd) {
        return VERSION + UID + uid + EMAIL + email + PWD + pwd;
    }

    /**
     * @description 找回密码
     * @param email
     */
    public static String getPwd(String email) {
        return VERSION + EMAIL + email;
    }

    /**
     * @decription 版本升级
     * @param imei
     * @param pkg
     */
    public static  String userUpdata(int imei, String pkg) {
       return  IMEI + imei + PKG + pkg + VERSION;
    }

}
