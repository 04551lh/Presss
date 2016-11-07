package com.feicui.android.press.Fragment.Analysis;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.SQLite.Entry.NewsSortEntry;
import com.feicui.android.press.SQLite.Entry.Type;
import com.feicui.android.press.SQLite.Helper.MyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 * @description 解析标题
 */
public class TitleParse {
    private RequestQueue queue;
    private ArrayList<NewsSortEntry> list;
    private ArrayList<Type> typeList;
    private Type type;
    private NewsSortEntry sortEntry;
    private  JSONObject jsonObject;
    private MyHelper myHelper;
    public TitleParse(Context context){
       queue = Volley.newRequestQueue(context);
       myHelper = new MyHelper(context);
    }
    public void getAnalysy(String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(url, listener, errorListener);
        queue.add(request);
    }

    public ArrayList<Type> parseNewsSort(String json) throws JSONException {
        list = new ArrayList<>();
        typeList = new ArrayList<>();
        jsonObject = new JSONObject(json);
        if(jsonObject.getString("message").equals("OK")&&jsonObject.getInt("status") == 0){
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                JSONArray array = object.getJSONArray("subgrp");
                for (int j = 0; j < array.length(); j++) {
                    JSONObject jObject = array.getJSONObject(j);
                    sortEntry = new NewsSortEntry(jObject.getString("subgroup"), jObject.getInt("subid"));
                    list.add(sortEntry);
                }
                int gid = object.getInt("gid");
                type = new Type(list, gid, object.getString("group"));
                typeList.add(type);
                myHelper.insertNewsSortDB(list, gid);
                list.clear();
            }
        }
        return typeList;
    }

}
