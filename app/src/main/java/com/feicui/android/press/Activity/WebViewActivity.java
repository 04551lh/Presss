package com.feicui.android.press.Activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.Adapter.XListAdapter;
import com.feicui.android.press.Base.MyBaseActivity;
import com.feicui.android.press.Entry.Commit.Commit;
import com.feicui.android.press.Entry.News;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.NewsListEntry;
import com.feicui.android.press.SQLite.Helper.MyHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/31.
 *
 */
public class WebViewActivity extends MyBaseActivity implements View.OnClickListener {
    //新闻实体类
    private NewsListEntry listEntry,  collectEntry;
    //WebView
    private WebView wb_link;
    //加载进度条
    private ProgressBar pb_progress;
    //PopupWindow
    private PopupWindow pp;
    //选项菜单
    private ImageView im_menu;
    //评论数量
    private TextView tv_cnt;
    //返回
    private ImageView im_back;
    //收藏
    private TextView tv_for;
    //分享
    private TextView tv_share;
    //取消
    private TextView tv_in;
    //布局
    private LinearLayout ll_not;
    //数据库帮助类
    private MyHelper myHelper;
    //更新适配器
    private static Updata updata;
    @Override
    public int setContent() {
        return R.layout.activity_webview;
    }


    @Override
    public void initView() {
        ll_not = (LinearLayout) findViewById(R.id.ll_notNet);
        pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
        wb_link = (WebView) findViewById(R.id.wb_link);
        wb_link.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        im_back = (ImageView) findViewById(R.id.im_back);
        im_menu = (ImageView) findViewById(R.id.im_menu);
        tv_cnt = (TextView) findViewById(R.id.tv_cnt);
        listEntry = (NewsListEntry) getIntent().getSerializableExtra("listEntry");
        wb_link.loadUrl(listEntry.getLink());
        myHelper = new MyHelper(this);
        if(isNetworkAvailable(this)){
            ll_not.setVisibility(View.GONE);
            pb_progress.setVisibility(View.VISIBLE);
            wb_link.setVisibility(View.VISIBLE);
        }
        initPopupWindows();
        getCnt();
    }

    @Override
    public void setListener() {
        im_back.setOnClickListener(this);
        im_menu.setOnClickListener(this);
        wb_link.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                pb_progress.setProgress(newProgress);
                if(newProgress >= 100){
                    pb_progress.setVisibility(View.GONE);
                }
            }
        });
        tv_cnt.setOnClickListener(this);
        tv_for.setOnClickListener(this);
        tv_in.setOnClickListener(this);
        tv_share.setOnClickListener(this);
    }

    /**
     * @description 显示评论数目
     */
    private void getCnt(){
        String url = News.HTTP + News.CMT_NUM + Commit.VERSION + Commit.NID + listEntry.getNid();
        Log.i("yzg",url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("yzg", s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int cnt = jsonObject.getInt("data");
                    if(cnt == 0){
                        tv_cnt.setText("跟帖");
                    }
                    else{
                        tv_cnt.setText("跟帖：" + cnt);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im_back:
                finish();
                break;
            case R.id.im_menu:
                if(pp != null && pp.isShowing() || !isNetworkAvailable(this)){
                    pp.dismiss();
                }
                else{
                    pp.showAtLocation(findViewById(R.id.ll_pp), Gravity.BOTTOM, 10, 10);
                    pp.setOutsideTouchable(true);
                }
                break;
            case R.id.tv_cnt:
                Intent intent = new Intent(this, CommitActivity.class);
                intent.putExtra("listEntry", listEntry);
                startActivity(intent);
                break;
            case R.id.tv_for:
                if(myHelper.queryNewsListDB(listEntry.getNid()) == 1){
                    listEntry.setType(2);
                    tv_for.setText(getResources().getString(R.string.false_collection));
                    myHelper.insertColloctDB(listEntry);
                    myHelper.upCollectData(listEntry);
                   if( updata != null){
                       XListAdapter xAdapter = updata.updataAdapter();
                       xAdapter.getmList().add(listEntry);
                       xAdapter.updata();
                   }
                    showToast("收藏成功");
                    pp.dismiss();
                }
                else{
                    listEntry.setType(1);
                    tv_for.setText(getResources().getString(R.string.true_collection));
                    myHelper.upCollectData(listEntry);
                    myHelper.deleteCollectDB(listEntry);
                    if(updata != null){
                        XListAdapter xAdapter = updata.updataAdapter();
                        for (int i = 0; i < xAdapter.getmList().size(); i++) {
                            if(xAdapter.getmList().get(i).getNid() == listEntry.getNid()){
                                xAdapter.getmList().remove(i);
                                xAdapter.updata();
                                break;
                            }
                        }
                    }
                    showToast("取消成功");
                    pp.dismiss();
                }
                break;
            case R.id.tv_share:
                showToast("分享");
                break;
            case R.id.tv_in:
                pp.dismiss();
                break;
        }
    }

    private void initPopupWindows(){
        pp = new PopupWindow();
        View view = getLayoutInflater().inflate(R.layout.commit_pp_widons, null);
        pp.setContentView(view);
        pp.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pp.setFocusable(true);
        pp.setBackgroundDrawable(new BitmapDrawable());
        tv_for = (TextView) view.findViewById(R.id.tv_for);
            if(myHelper.queryNewsListDB(listEntry.getNid()) != 1){
                tv_for.setText(getResources().getString(R.string.false_collection));
            }
            else {
                tv_for.setText("收藏");
            }
        tv_share = (TextView) view.findViewById(R.id.tv_share);
        tv_in = (TextView) view.findViewById(R.id.tv_in);
    }


    public static void setUpdata(Updata updata){
        WebViewActivity.updata = updata;
    }
    /**
     * @descriprion 刷新收藏的接口
     */
    public interface Updata{
        XListAdapter updataAdapter();
    }
}
