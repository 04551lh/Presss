package com.feicui.android.press.Fragment.Analysis;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.SQLite.Entry.UserHomeEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/1.
 *
 */
public class UserHomeParse {
    private RequestQueue queue;
    public UserHomeParse(Context context){
        queue = Volley.newRequestQueue(context);
    }
    public void getAnalysy(String url, Response.Listener<String> listener, Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(url, listener, errorListener);
        queue.add(request);
    }

    public void getAnalysyImage(String url, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener){
        ImageRequest imageRequest = new ImageRequest(url, listener, 0, 0, Bitmap.Config.RGB_565, errorListener);
        queue.add(imageRequest);
    }
}
