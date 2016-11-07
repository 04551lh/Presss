package com.feicui.android.press.Fragment;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.feicui.android.press.Activity.MainActivity;
import com.feicui.android.press.Base.MyBaseFragment;
import com.feicui.android.press.Entry.News;
import com.feicui.android.press.Entry.User.User;
import com.feicui.android.press.Fragment.Analysis.LoginParse;
import com.feicui.android.press.R;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/28.
 * @description 登录界面
 */
public class LoginFragment extends MyBaseFragment implements View.OnClickListener, TextWatcher {
    //注册
    private Button bt_lregister;
    //找回密码
    private Button bt_getpwd;
    //登录
    private Button bt_llogin;
    //输入用户名
    private EditText et_lname;
    private String name;
    //输入密码
    private EditText et_lpwd;
    //MainActivity
    private MainActivity mainActivity;
    //Http
    String http = News.HTTP + News.USER_LOGIN;
    @Override
    public int setContent() {
        return R.layout.fragment_login;
    }

    @Override
    public void initView() {
        et_lname = (EditText) view.findViewById(R.id.et_lname);
        et_lpwd = (EditText) view.findViewById(R.id.et_lpwd);
        bt_llogin = (Button) view.findViewById(R.id.bt_llogin);
        bt_lregister = (Button) view.findViewById(R.id.bt_lregister);
        bt_getpwd = (Button) view.findViewById(R.id.bt_getpwd);
        mainActivity = ((MainActivity)getActivity());
    }

    @Override
    public void setListener() {
        et_lpwd.addTextChangedListener(this);
        bt_lregister.setOnClickListener(this);
        bt_llogin.setOnClickListener(this);
        bt_getpwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_llogin:
                name = this.et_lname.getText().toString();
                String pwd = this.et_lpwd.getText().toString();
                if(isNetworkAvailable(getActivity())){
                    new LoginFragment.MyAsyncTask().execute(http, name, pwd);
                    Log.i("YZG",name);
                    et_lname.setText("");
                    et_lpwd.setText("");
                }
                else{
                    mainActivity.showToast("没有网络");
                }
                break;
            case R.id.bt_lregister:
                mainActivity.setMyTitle(R.string.register_in);
                mainActivity.addFragment(new Register());
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String password = s.toString();
        String username = et_lname.getText().toString();
        if(password.length() > 0 && username.length() > 0){
            bt_llogin.setBackgroundResource(R.drawable.button_backgroud);
            bt_llogin.setEnabled(true);
        }
        else{
            bt_llogin.setBackgroundResource(R.drawable.normalbutton_normal);
            bt_llogin.setEnabled(false);
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            //存储数据
            StringBuffer str = null;
            try {
                //Http的首部
                URL url = new URL(params[0]);
                //用户输入信息
                String login = User.userLogin(params[1], params[2]);
                //建立网络连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //上传方式
                connection.setRequestMethod("POST");
                //允许读入
                connection.setDoOutput(true);
                //设置格式
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length",  login.length() + "");
                //建立读入流
                OutputStream out = connection.getOutputStream();
                //读入数据
                out.write(login.getBytes());
                //关闭
                out.close();
                //获取请求码
                int code = connection.getResponseCode();
                if(code == 200) {
                    //建立写出流
                    BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                    //缓冲区
                    byte[] b = new byte[1024];
                    //计数器
                    int count = 0;
                    str = new StringBuffer();
                    //读取数据
                    while ((count = in.read(b)) != -1) {
                        str.append(new String(b, 0, count));
                    }
                    //关闭流
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return str.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            //解析数据
            LoginParse loginParse = new LoginParse(getActivity());
            Log.i("YZG", name);
            loginParse.parseLogin(s, name);
            mainActivity.changeRightMenu(name);
            mainActivity.isLogin = true;
            if(mainActivity.main == null){
                mainActivity.main = new Main();
            }
            mainActivity.addFragment(mainActivity.main);
            mainActivity.setMyTitle(R.string.home_title);
        }
    }
}
