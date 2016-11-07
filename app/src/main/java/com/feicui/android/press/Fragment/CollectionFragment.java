package com.feicui.android.press.Fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.feicui.android.press.Activity.WebViewActivity;
import com.feicui.android.press.Adapter.XListAdapter;
import com.feicui.android.press.Base.MyBaseFragment;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.NewsListEntry;
import com.feicui.android.press.SQLite.Helper.MyHelper;
import com.feicui.android.press.lib3.XMLListView.XListView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 */
public class CollectionFragment extends MyBaseFragment implements WebViewActivity.Updata, AdapterView.OnItemClickListener {
    private XListAdapter xAdapter;
    private ArrayList<NewsListEntry> list;
    private XListView xlv;
    private MyHelper myHelper;
    @Override
    public int setContent() {
        return R.layout.fragment_main;
    }

    @Override
    public void initView() {
        view.findViewById(R.id.ll_mainfragment_title).setVisibility(View.GONE);
        myHelper = new MyHelper(getActivity());
        xlv = (XListView) view.findViewById(R.id.xlv_data);
        xAdapter = new XListAdapter(getActivity(), xlv);
        list = myHelper.queryCollectDB();
    }

    @Override
    public void setListener() {
        xlv.setPullLoadEnable(true);
        xlv.setPullRefreshEnable(true);
        xlv.setOnItemClickListener(this);
        xlv.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        xAdapter.setmList(list);
        xlv.setAdapter(xAdapter);
        WebViewActivity.setUpdata(this);
    }


    @Override
    public XListAdapter updataAdapter() {
        return xAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        NewsListEntry listEntry = xAdapter.getmList().get(position - 1);
        intent.putExtra("listEntry", listEntry);
        startActivity(intent);
    }
}
