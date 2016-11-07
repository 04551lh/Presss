package com.feicui.android.press.Fragment.Analysis;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.SQLite.Entry.CommitEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/1.
 */
public class CommitParse {
    //请求队列
    private RequestQueue mQueue;
    //存储数据集合
    private ArrayList<CommitEntry> commitList;
    //显示评论实体
    private CommitEntry commitEntry;

    public CommitParse(Context context){
        mQueue = Volley.newRequestQueue(context);
    }

    public void getAnaly(String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(url, listener, errorListener);
        mQueue.add(request);
    }

    /**
     * @description 解析显示评论
     * @param json
     * @return
     * @throws JSONException
     */
    public ArrayList<CommitEntry> parseCommitList(String json) throws JSONException {
        commitList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        if(jsonObject.getString("message").equals("OK")&&jsonObject.getInt("status") == 0){
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    commitEntry = new CommitEntry(
                            object.getString("uid"),
                            object.getString("portrait"),
                            object.getString("stamp"),
                            object.getString("content"),
                            object.getInt("cid")
                    );
                    commitList.add(commitEntry);
                }
            }
        return commitList;
    }
}
