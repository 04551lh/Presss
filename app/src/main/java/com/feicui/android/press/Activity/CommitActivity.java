package com.feicui.android.press.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.feicui.android.press.Adapter.ComXListAdapter;
import com.feicui.android.press.Base.MyBaseActivity;
import com.feicui.android.press.Entry.Commit.Commit;
import com.feicui.android.press.Entry.News;
import com.feicui.android.press.Fragment.Analysis.CommitParse;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.CommitEntry;
import com.feicui.android.press.SQLite.Entry.NewsListEntry;
import com.feicui.android.press.lib3.XMLListView.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/1.
 *
 */
public class CommitActivity extends MyBaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    //内容显示列表
    private XListView xlv;
    //发布内容
    private EditText tv_commit;
    //发送
    private ImageView im_send;
    //适配器
    private ComXListAdapter xAdapter;
    //接收的ListEntry
    private NewsListEntry listEntry;
    //自定义帮助类
    private CommitParse commitParse;
    //显示评论集合
    private ArrayList<CommitEntry> commitList;
    //nid
    private int nid;
    //返回
    private ImageView im_back;
    //是否登录
    public boolean isLogin;
    //用户令牌
    private String token;
    private String userName;
    //
    private int cid;
    private String ctx;
    @Override
    public int setContent() {
        return R.layout.activity_commit;
    }

    @Override
    public void initView() {
        listEntry = (NewsListEntry) getIntent().getSerializableExtra("listEntry");
        xlv = (XListView) findViewById(R.id.xlv_commit);
        xlv.setRefreshTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
        tv_commit = (EditText) findViewById(R.id.tv_commit);
        im_send = (ImageView) findViewById(R.id.im_send);
        im_back = (ImageView) findViewById(R.id.im_commit_back);
        xAdapter = new ComXListAdapter(this, xlv);
        commitParse = new CommitParse(this);
        nid = listEntry.getNid();
        initCommint(nid, 1, 1);
    }

    @Override
    public void setListener() {
        im_send.setOnClickListener(this);
        im_back.setOnClickListener(this);
        xlv.setPullRefreshEnable(true);
        xlv.setPullLoadEnable(true);
        xlv.setXListViewListener(this);
        xlv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_send:
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                isLogin = preferences.getBoolean("login", false);
                if(isLogin){
                    token = preferences.getString("token", "");
                    userName = preferences.getString("name", "");
                    ctx = tv_commit.getText().toString();
                    String http = News.HTTP + News.CMT_COMMIT;
                    CommitActivity.MyAsyncTask myAsyncTask = new MyAsyncTask();
                    myAsyncTask.execute(http, nid+"", token, ctx);
                    tv_commit.setText("");
                }
                else{
                    showToast("请登录");
                }
                break;
            case R.id.im_commit_back:
                finish();
                break;
        }
    }

    /**
     * @description 加载评论内容
     * @param nid
     * @param dir
     * @param cid
     */
    private void initCommint(int nid, int cid, int dir){
        String http = News.HTTP + News.CMT_LIST + Commit.getList(nid, cid, dir);
        commitParse.getAnaly(http, listenerList, errorListener);
    }

    Response.Listener<String> listenerList = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                commitList = commitParse.parseCommitList(s);
                xAdapter.setmList(commitList);
                xlv.setAdapter(xAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    private void CommintMore(int nid, int cid, int dir){
        String http = News.HTTP + News.CMT_LIST + Commit.getList(nid, cid, dir);
        commitParse.getAnaly(http, listenerMore, errorListener);
        Log.i("yzg",http);
    }

    Response.Listener<String> listenerMore = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                commitList = commitParse.parseCommitList(s);
                xAdapter.insertNewsList(commitList);
                xlv.setAdapter(xAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onRefresh() {
        initCommint(nid, 1, 1);
        xlv.stopRefresh();
        xlv.stopLoadMore();
    }

    @Override
    public void onLoadMore() {
        ArrayList<CommitEntry> list = xAdapter.getmList();
        cid = list.get(list.size() -1).getCid();
        CommintMore(nid, 2, cid);
        xlv.stopLoadMore();
        xlv.stopRefresh();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * @description 发布评论
     */
    class MyAsyncTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            //存储数据
            StringBuffer str = null;
            try {
                //Http的首部
                URL url = new URL(params[0]);
                //用户输入信息
                String commit =  Commit.getCommit(Integer.parseInt(params[1]), params[2], params[3]);
                Log.i("yzg", url + commit);
                //建立网络连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //上传方式
                connection.setRequestMethod("POST");
                //允许读入
                connection.setDoOutput(true);
                //设置格式
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length",  commit.length() + "");
                //建立读入流
                OutputStream out = connection.getOutputStream();
                //读入数据
                out.write(commit.getBytes());
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
            String temp = "";
            try {
                JSONObject jsonObject = new JSONObject(s);
                String message = jsonObject.getString("message");
                if(message.equals("OK")){
                    if(jsonObject.getInt("status") == 0){
                        JSONObject object = jsonObject.getJSONObject("data");
                        switch (object.getInt("result")){
                            case 0 :
                                temp = object.getString("explain");
                                CommitEntry commitEntry = new CommitEntry();
                                commitEntry.setCid(0);
                                commitEntry.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                commitEntry.setContext(ctx);
                                commitEntry.setUid(userName);
                                xAdapter.insertNewsData(commitEntry);
                                xAdapter.updata();
                                xlv.setSelection(0);
                                break;
                            case -1:
                                temp = "非法关键字";
                                break;
                            case -2:
                                temp = "禁止评论（政治敏感新闻）";
                                break;
                            case -3:
                                temp = "禁止评论（用户被禁言）";
                                break;
                        }
                    }
                }else {
                    temp = message;
                }
                showToast(temp);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
