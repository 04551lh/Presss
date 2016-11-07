package com.feicui.android.press.Fragment.Analysis;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.SQLite.Entry.NewsListEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/10/28.
 * @description 解析列表
 */
public class ListParse {
    private RequestQueue queue;
    private ArrayList<NewsListEntry> list;
    private NewsListEntry listEntry ;
    private JSONObject jsonObject;
    public ListParse(Context context){
        queue = Volley.newRequestQueue(context);
    }
    public void getAnalysy(String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(url, listener, errorListener);
        queue.add(request);
    }

    public ArrayList<NewsListEntry> parseNewsList(String json) throws JSONException {
        list = new ArrayList<>();
        jsonObject = new JSONObject(json);
        if(jsonObject.getString("message").equals("OK")&&jsonObject.getInt("status") == 0){
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                listEntry = new NewsListEntry(
                        object.getString("summary"),
                        object.getString("icon"),
                        object.getString("stamp"),
                        object.getString("title"),
                        object.getInt("nid"),
                        object.getString("link"),
                        1);
                list.add(listEntry);
            }
        }
        return list;
    }
}
