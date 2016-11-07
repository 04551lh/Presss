package com.feicui.android.press.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.android.press.Base.MyBaseActivity;
import com.feicui.android.press.Fragment.CollectionFragment;
import com.feicui.android.press.Fragment.LoginFragment;
import com.feicui.android.press.Fragment.Main;
import com.feicui.android.press.R;
import com.feicui.android.press.lib3.SlidingMenu.SlidingMenu;

public class MainActivity extends MyBaseActivity implements View.OnClickListener {
    //标题
    private TextView tv_title;
    //资源图标
    private ImageView im_left;
    //登录图标
    private ImageView im_right;
    //侧滑菜单
    private SlidingMenu slidingMenu;
    //左面布局
    private LinearLayout layout[];
    //左面布局背景点击颜色
    private static final int SLIDINGMENU_BACK = 0x33c85555;
    //登录头像
    private ImageView im_login;
    //立即登录
    private TextView tv_login;
    //版本更新
    private TextView tv_up;
    //其他方式
    private LinearLayout ll_qwsw;
    //fragment的切换
    private Fragment fragment;
    //fragment的管理
    private FragmentManager fm;
    //Main
    public Main main;
    //是否登录
    public boolean isLogin;
    //第一次点击的时间
    private long l_firstClickTime;
    //是否点击一次
    boolean is_exit = false;
    //第二次点击的时间
    private long l_secondClickTime;
    private int gid = 1;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    @Override
    public int setContent() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        if(main == null){
            main = new Main();
        }
        addFragment(main);
        im_left = (ImageView) findViewById(R.id.im_left);
        im_right = (ImageView) findViewById(R.id.im_right);
        tv_title = (TextView) findViewById(R.id.tv_title);
        initSlidingMenu();
    }

    /**
     * @description 判断是否用户登录
     */
    private void isLogin(){
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        isLogin = preferences.getBoolean("login", false);
        if(isLogin){
            tv_login.setText(preferences.getString("name", ""));
            ll_qwsw.setVisibility(View.GONE);
        }
    }

    @Override
    public void setListener() {
        im_left.setOnClickListener(this);
        im_right.setOnClickListener(this);
        im_login.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_login:
            case R.id.tv_login:
                if(isLogin){
                    Intent intent = new Intent(this, UserHomeActivity.class);
                    startActivity(intent);
                }
                else{
                    addFragment(new LoginFragment());
                    tv_title.setText(R.string.login_in);
                }
                slidingMenuShowing();
                break;
            case R.id.im_left:
                if(slidingMenu != null && slidingMenu.isMenuShowing()){
                    slidingMenu.showContent();
                }
                else{
                    slidingMenu.showMenu();
                }
                break;
            case R.id.im_right:
                if(slidingMenu != null && slidingMenu.isSecondaryMenuShowing()){
                    slidingMenu.showContent();
                }
                else{
                    slidingMenu.showSecondaryMenu();
                }
                break;
        }
    }
    /**
     * @description 侧滑菜单
     */
    private void initSlidingMenu() {
        //获取实例
        slidingMenu = new SlidingMenu(this);
        //设置滑动方向
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置是否可以滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //划出单位
        slidingMenu.setBehindOffsetRes(R.dimen.slidingMenu);
        //当前显示Activity上滑动
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //左布局
        slidingMenu.setMenu(R.layout.slidingmenu_left);
        //右布局
        slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
        layout = new LinearLayout[5];
        layout[0] = (LinearLayout) slidingMenu.findViewById(R.id.ll_sliding_news);
        layout[1] = (LinearLayout) slidingMenu.findViewById(R.id.ll_sliding_favorite);
        layout[2] = (LinearLayout) slidingMenu.findViewById(R.id.ll_sliding_local);
        layout[3] = (LinearLayout) slidingMenu.findViewById(R.id.ll_sliding_comment);
        layout[4] = (LinearLayout) slidingMenu.findViewById(R.id.ll_sliding_phone);
        layout[0].setBackgroundColor(SLIDINGMENU_BACK);
        for (int i = 0; i < layout.length; i++) {
            layout[i].setOnClickListener(onClickListener);
        }
        im_login = (ImageView) slidingMenu.findViewById(R.id.im_login);
        tv_login = (TextView) slidingMenu.findViewById(R.id.tv_login);
        tv_up = (TextView) slidingMenu.findViewById(R.id.tv_up);
        ll_qwsw = (LinearLayout) slidingMenu.findViewById(R.id.ll_qwsw);
        isLogin();
    }

    /**
     * 是否开启SlidingMenu，如果开启，即关闭
     */
    private void slidingMenuShowing(){
        if(slidingMenu != null && slidingMenu.isMenuShowing()){
            slidingMenu.showContent();
        }
    }

    public void setMyTitle(int title){
        tv_title.setText(getResources().getString(title));
    }
    /**
     * 修改登录后Login布局
     * @param user
     */
    public void changeRightMenu(String user){
        ll_qwsw.setVisibility(View.GONE);
        tv_login.setText(user);
    }
    /**
     * @description 切换fragment
     * @param fragment
     */
    public void addFragment(Fragment fragment){
        this.fragment = fragment;
        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fl_contain, fragment).commit();
    }
    /**
     * @description 设置左面布局监听响应
     */
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < layout.length; i++) {
                layout[i].setBackgroundColor(0);
            }
            switch (v.getId()) {
                case R.id.ll_sliding_news:
                    layout[0].setBackgroundColor(SLIDINGMENU_BACK);
                    slidingMenuShowing();
                    if(main == null){
                        main = new Main();
                    }
                    addFragment(main);
                    tv_title.setText(R.string.home_title);
                    break;
                case R.id.ll_sliding_favorite:
                    layout[1].setBackgroundColor(SLIDINGMENU_BACK);
                    slidingMenuShowing();
                    addFragment(new CollectionFragment());
                    tv_title.setText("收藏");
                    showToast("favorite");
                    break;
                case R.id.ll_sliding_local:
                    layout[2].setBackgroundColor(SLIDINGMENU_BACK);
                    slidingMenuShowing();
                    tv_title.setText("资讯");
                    showToast("local");
                    break;
                case R.id.ll_sliding_comment:
                    layout[3].setBackgroundColor(SLIDINGMENU_BACK);
                    slidingMenuShowing();
                    tv_title.setText("资讯");
                    showToast("comment");
                    break;
                case R.id.ll_sliding_phone:
                    layout[4].setBackgroundColor(SLIDINGMENU_BACK);
                    slidingMenuShowing();
                    tv_title.setText("资讯");
                    showToast("phone");
                    break;
            }
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(slidingMenu != null && slidingMenu.isMenuShowing()){
                slidingMenu.showContent();
                return true;
            }
            if (fragment instanceof  Main ){
                doubleColickExit(keyCode, event);
            }
            else {
                if(main == null){
                    main = new Main();
                }
                tv_title.setText(R.string.home_title);
                addFragment(new Main());
                for (int i = 0; i < layout.length; i++) {
                    layout[i].setBackgroundColor(0);
                }
                layout[0].setBackgroundColor(SLIDINGMENU_BACK);
                return true;
            }
        }
        return false;
    }

    /**
     * 双击退出
     * @param keyCode
     * @param event
     */
    private void doubleColickExit(int keyCode,KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK && is_exit == false){
            is_exit = true;
            l_firstClickTime = System.currentTimeMillis();
            Toast.makeText(this, "再次点击退出",Toast.LENGTH_SHORT).show();
        }
        else if(keyCode == KeyEvent.KEYCODE_BACK && is_exit == true){
            l_secondClickTime = System.currentTimeMillis();
            if(l_secondClickTime - l_firstClickTime <2000){
                finish();
            }
            else {
                is_exit = false;
                doubleColickExit(keyCode, event);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences preferences = getSharedPreferences("login",MODE_PRIVATE);
        isLogin = preferences.getBoolean("login", false);
        if(isLogin){
            tv_login.setText(preferences.getString("name", ""));
            ll_qwsw.setVisibility(View.GONE);
        }
        else{
            tv_login.setText(getResources().getString(R.string.to_login));
            ll_qwsw.setVisibility(View.VISIBLE);
        }
    new Thread(new Runnable() {
        @Override
        public void run() {
        }
    });
    }
}