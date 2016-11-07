package com.feicui.android.press.Fragment;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feicui.android.press.Activity.MainActivity;
import com.feicui.android.press.Activity.WebViewActivity;
import com.feicui.android.press.Adapter.HListAdapter;
import com.feicui.android.press.Adapter.XListAdapter;
import com.feicui.android.press.Base.MyBaseFragment;
import com.feicui.android.press.Entry.News;
import com.feicui.android.press.Entry.Press.Press;
import com.feicui.android.press.Fragment.Analysis.ListParse;
import com.feicui.android.press.Fragment.Analysis.TitleParse;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.NewsListEntry;
import com.feicui.android.press.SQLite.Entry.NewsSortEntry;
import com.feicui.android.press.SQLite.Entry.Type;
import com.feicui.android.press.SQLite.Helper.MyHelper;
import com.feicui.android.press.lib3.View.HorizontalListView;
import com.feicui.android.press.lib3.XMLListView.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/28.
 *
 */
public class Main extends MyBaseFragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {
    //横向ListView
    public HorizontalListView hlv;
    //横向ListView适配器
    public HListAdapter hAdapter;
    //存放标题数据列表
    private ArrayList<NewsSortEntry> sortList;
    private ArrayList<Type> oldsortList;
    //标题实体
    NewsSortEntry sortEntry;
    //纵向ListView
    private XListView xlv;
    //纵向ListView的适配器
    private XListAdapter xAdapter;
    //内容数据列表
    private ArrayList<NewsListEntry> newsList;
    //内容数据实体
    private NewsListEntry listEntry;
    //HListView解析类
    private TitleParse titleParse;
    //XListView解析类
    private ListParse listParse;
    //数据库帮助类
    private MyHelper myHelper;
    //新闻标题的位置
    private int subid = 1;
    //下拉设置
    private static final int up_dir = 1;
    //方向为1时，新闻ID
    private static final int nid = 1;
    //下拉设置
    private static final int load_dir = 2;
    //选择新闻类型
    private ImageView im_extre;
    private MainActivity mainActivity;
    private int gid;

    @Override
    public int setContent() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        hlv = (HorizontalListView) view.findViewById(R.id.hlv_data);
        xlv = (XListView) view.findViewById(R.id.xlv_data);
        xAdapter = new XListAdapter(getActivity(),xlv);
        im_extre = (ImageView) view.findViewById(R.id.im_extr);
        hAdapter = new HListAdapter(getActivity());
        sortList = new ArrayList<>();
        newsList = new ArrayList<>();
        titleParse = new TitleParse(getActivity());
        listParse = new ListParse(getActivity());
        myHelper = new MyHelper(getActivity());
        mainActivity = (MainActivity) getActivity();
        gid = mainActivity.getGid();
        Log.i("yzg", gid+"");
        loadNewsType();
    }

    @Override
    public void setListener() {
        hlv.setOnItemClickListener(this);
        im_extre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.addFragment(new SeletionFragment());
                mainActivity.setMyTitle(R.string.type);
            }
        });
        xAdapter.setmList(newsList);
        xlv.setAdapter(xAdapter);
        xlv.setPullLoadEnable(true);
        xlv.setPullRefreshEnable(true);
        xlv.setXListViewListener(this);
        xlv.setOnItemClickListener(xListener);
        xlv.setRefreshTime(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date()));
    }

    /**
     * @description 第一次加载新闻标题
     */
    private void loadNewsType(){
        ArrayList<NewsSortEntry> arrayList = myHelper.queryNewsSortDB(gid);
        if(arrayList.size() <= 0 && isNetworkAvailable(getActivity())){
            String url = News.HTTP + News.NEWS_SORT + Press.getSort(1111111111);
            titleParse.getAnalysy(url, hlistener, errorListener);
        }
        else{
            sortList = arrayList;
            hAdapter.setmList(sortList);
            hlv.setAdapter(hAdapter);
        }
        if(hAdapter.getmList()!=null && hAdapter.getmList().size() >0){
            subid = hAdapter.getmList().get(0).getSubid();
        }
        loadNewsRefresh();
    }

    Response.Listener<String> hlistener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                oldsortList = titleParse.parseNewsSort(s);
                for (int i = 0; i < oldsortList.size(); i++) {
                    if(oldsortList.get(i).getGid() == gid){
                        for (int j = 0; j < oldsortList.get(i).getList().size(); j++) {
                            sortEntry = new NewsSortEntry(oldsortList.get(i).getList().get(i).getSubgroup(),
                                    oldsortList.get(i).getList().get(i).getSubid());
                            sortList.add(sortEntry);
                        }
                        hAdapter.setmList(sortList);
                        xlv.setAdapter(hAdapter);
                        myHelper.insertNewsSortDB(sortList, gid);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    Response.ErrorListener errorListener =  new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hAdapter.setMposition(position);
        if(subid != position+1){
            subid = hAdapter.getmList().get(position).getSubid();
            xAdapter.getmList().clear();
            loadNewsRefresh();
            hAdapter.notifyDataSetChanged();
        }
    }

    Response.Listener<String> xuplistener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                newsList = listParse.parseNewsList(s);
                myHelper.insertNewsListDB(newsList);
                xAdapter.setmList(newsList);
                xlv.setAdapter(xAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Response.Listener<String> xloadlistener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            try {
                newsList = listParse.parseNewsList(s);
                myHelper.insertNewsListDB(newsList);
                xAdapter.insertNewsList(newsList);
                xAdapter.update();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onRefresh() {
        loadNewsRefresh();
        xlv.stopRefresh();
        xlv.stopLoadMore();
    }

    @Override
    public void onLoadMore() {
        loadNewsMore();
        xlv.stopRefresh();
        xlv.stopLoadMore();
    }

    /**
     * @description 刷新新闻数据
     */
    private void loadNewsRefresh(){
        Log.i("isNet", isNetworkAvailable(getActivity())+"");
        if(!isNetworkAvailable(getActivity())){
            Log.i("yzg", "SQLite");
            newsList = myHelper.queryNewsListDB(0, 19, subid);
            if(newsList != null && newsList.size() >= 0){
                xAdapter.setmList(newsList);
                xAdapter.notifyDataSetChanged();
            }
        }
        else {
            String url = News.HTTP + News.NEWS_LIST + Press.VERSION + Press.SUBID + subid +
                    Press.DIR + up_dir + Press.NID + nid + Press.STAMP + Press.CNT;
            listParse.getAnalysy(url, xuplistener, errorListener);
        }
    }

    /**
     * @description 下拉更新
     */
    private void loadNewsMore(){
        newsList = xAdapter.getmList();
        if(isNetworkAvailable(getActivity())){
            String url =  News.HTTP + News.NEWS_LIST + Press.VERSION + Press.SUBID + subid +
                    Press.DIR + load_dir + Press.NID + newsList.get(newsList.size()  - 1).getNid() + Press.STAMP + Press.CNT;
            listParse.getAnalysy(url, xloadlistener, errorListener);
        }
        else{
            ArrayList<NewsListEntry> list = myHelper.queryNewsListDB(newsList.size(), newsList.size() + 20, subid);
            if(list != null && list.size() >= 0){
                xAdapter.insertNewsList(list);
                xAdapter.updata();
            }
        }
    }

    private AdapterView.OnItemClickListener xListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            listEntry = xAdapter.getmList().get(position - 1);
            intent.putExtra("listEntry", listEntry);
            startActivity(intent);
        }
    };
}
