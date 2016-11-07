package com.feicui.android.press.Fragment;

import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.Activity.MainActivity;
import com.feicui.android.press.Base.MyBaseFragment;
import com.feicui.android.press.Entry.News;
import com.feicui.android.press.Entry.Press.Press;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.NewsSortEntry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/3.
 *
 */
public class SeletionFragment extends MyBaseFragment implements View.OnClickListener {
    private ArrayList<Integer> subid;
    private ArrayList<String> note;
    private RequestQueue queue;
    private TextView tv_first, tv_second, tv_three, tv_four;
    private MainActivity mainActivity;
    private ArrayList<ArrayList> mType,type;
    private ArrayList<NewsSortEntry> sortList;
    private NewsSortEntry sortEntry;
    private Main main;
    @Override
    public int setContent() {
        return R.layout.fragment_selection;
    }

    @Override
    public void initView() {
       tv_first = (TextView) view.findViewById(R.id.ll_first);
        tv_second = (TextView) view.findViewById(R.id.ll_second);
        tv_three = (TextView) view.findViewById(R.id.ll_three);
        tv_four = (TextView) view.findViewById(R.id.ll_four);
        subid = new ArrayList<>();
        mType = new ArrayList();
        mainActivity = (MainActivity) getActivity();
        queue = Volley.newRequestQueue(getActivity());
        getAnaly();
    }

    @Override
    public void setListener() {
        tv_first.setOnClickListener(this);
        tv_second.setOnClickListener(this);
        tv_three.setOnClickListener(this);
        tv_four.setOnClickListener(this);

    }

    private void getAnaly(){
        queue = Volley.newRequestQueue(getActivity());
        note = new ArrayList<>();
        type = new ArrayList();
        sortList = new ArrayList<>();
        String url = News.HTTP + News.NEWS_SORT + Press.getSort(1111111111);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    if(jsonObject.getString("message").equals("OK")&&jsonObject.getInt("status") == 0){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            note.add(object.getString("group"));
                            JSONArray array = object.getJSONArray("subgrp");
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject jObject = array.getJSONObject(j);
                                int id = jObject.getInt("subid");
                                sortEntry = new NewsSortEntry(jObject.getString("subgroup"), jObject.getInt("subid"));
                                sortList.add(sortEntry);
                                subid.add(id);
                            }
                            type.add(sortList);
                            }
                        if(note != null && note.size() > 0){
                            tv_first.setText(note.get(0));
                            tv_second.setText(note.get(1));
                            tv_three.setText(note.get(2));
                            tv_four.setText(note.get(3));
                        }
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_first:
                mainActivity.setMyTitle(R.string.title_news);
                mainActivity.setGid(1);
                mainActivity.addFragment(new Main());
                break;
            case R.id.ll_second:
                mainActivity.setMyTitle(R.string.caijing);
                mainActivity.setGid(2);
                mainActivity.addFragment(new Main());
                break;
            case R.id.ll_three:
                mainActivity.setMyTitle(R.string.keji);
                mainActivity.setGid(3);
                mainActivity.addFragment(new Main());
                break;
            case R.id.ll_four:
                mainActivity.setMyTitle(R.string.tiyu);
                mainActivity.setGid(4);
                mainActivity.addFragment(new Main());
                break;
        }
    }
}
