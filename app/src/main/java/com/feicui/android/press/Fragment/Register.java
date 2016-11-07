package com.feicui.android.press.Fragment;


import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.feicui.android.press.Activity.MainActivity;
import com.feicui.android.press.Base.MyBaseFragment;
import com.feicui.android.press.Entry.News;
import com.feicui.android.press.Entry.User.User;
import com.feicui.android.press.Fragment.Analysis.RegisterParse;
import com.feicui.android.press.R;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/28.
 *
 */
public class Register extends MyBaseFragment implements View.OnClickListener, TextWatcher {
    //用户名
    private EditText et_name;
    private String name;
    //密码
    private EditText et_pwd, et_repwd;
    private String pwd, repwd;
    //邮箱
    private EditText et_email;
    private String email;
    //注册
    private Button bt_register;
    //重新注册
    private Button bt_inregister;
    //注册的Http
    private String http = News.HTTP + "/" + News.USER_REGISTER;
    //MainActivity
    private MainActivity mainActivity;
    @Override
    public int setContent() {
        return R.layout.fragment_register;
    }

    @Override
    public void initView() {
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_pwd = (EditText) view.findViewById(R.id.et_pwd);
        et_repwd = (EditText) view.findViewById(R.id.et_repwd);
        et_email = (EditText) view.findViewById(R.id.et_email);
        bt_register = (Button) view.findViewById(R.id.bt_register);
        bt_inregister = (Button) view.findViewById(R.id.bt_inregister);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void setListener() {
        et_email.addTextChangedListener(this);
        bt_register.setOnClickListener(this);
        bt_inregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_register:
                if(name.matches("[a-zA-Z0-9_]{6,24}")&&pwd.matches("[a-zA-Z0-9_]{6,24}")){
                    if((pwd.equals(repwd))){
                        new MyAsyncTask().execute(http, name, email, pwd);
                        et_name.setText("");
                        et_pwd.setText("");
                        et_repwd.setText("");
                        et_email.setText("");
                    }
                    else{
                        Toast.makeText(getActivity(), "两次密码不相同！", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(),"用户名和密码只能是数字，大小写字母，下划线的6至24位！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_inregister:
                et_name.setText("");
                et_pwd.setText("");
                et_repwd.setText("");
                et_email.setText("");
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
        email  = s.toString();
        name = et_name.getText().toString();
        pwd = et_pwd.getText().toString();
        repwd = et_repwd.getText().toString();
        if(email.length() > 0 && name.length() > 0 && pwd.length() > 0 && repwd.length() > 0){
            bt_register.setBackgroundResource(R.drawable.button_backgroud);
            bt_register.setEnabled(true);
        }
        else{
            bt_register.setBackgroundResource(R.drawable.normalbutton_normal);
            bt_register.setEnabled(false);
        }
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //存储数据
            StringBuffer str = null;
            try {
                //Http的首部
                URL url = new URL(http);
                //用户输入信息
                String register = User.userRegister(params[1], params[2], params[3]);
                //建立网络连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //上传方式
                connection.setRequestMethod("POST");
                //允许读入
                connection.setDoOutput(true);
                //设置格式
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length",  register.length() + "");
                //建立读入流
                OutputStream out = connection.getOutputStream();
                //读入数据
                out.write(register.getBytes());
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
            RegisterParse registerParse = new RegisterParse(mainActivity);
            registerParse.registerParse(s, name);
        }
    }

}
