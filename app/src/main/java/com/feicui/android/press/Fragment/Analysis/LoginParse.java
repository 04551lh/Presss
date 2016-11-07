package com.feicui.android.press.Fragment.Analysis;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/28.
 *
 */
public class LoginParse {
    private Context context;
    private String temp;
    public LoginParse(Context context){
        this.context = context;
    }
    public void parseLogin(String json, String name){
        try {
            JSONObject jsonObject = new JSONObject(json);
            String message = jsonObject.getString("message");
            if(message.equals("OK")){
                JSONObject object =  jsonObject.getJSONObject("data");
                temp = object.getString("explain");
                String token = object.getString("token");
                SharedPreferences preferences = context.getSharedPreferences("login", context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("login", true);
                editor.putString("name", name);
                editor.putString("token", token );
                editor.commit();
            }
            else{
                temp = message;
            }
            Toast.makeText(context, temp, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
