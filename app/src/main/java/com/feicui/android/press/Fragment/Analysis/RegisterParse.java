package com.feicui.android.press.Fragment.Analysis;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/10/28.
 */
public class RegisterParse {
    private Context context;
    public RegisterParse(Context context){
        this.context = context;
    }
    public void registerParse(String json, String name){
        try {
            JSONObject jsonObject =  new JSONObject(json);
            String message = jsonObject.getString("message");
            String temp = "";
            if(message.equals("OK")){
                JSONObject object = jsonObject.getJSONObject("data");
                int result = object.getInt("result");
                switch (result){
                    case 0:
                        String token = object.getString("token");
                        temp = object.getString("explain");

                        SharedPreferences preferences = context.getSharedPreferences("register", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("register", true);
                        editor.putString("name", name);
                        editor.putString("token", token);
                        editor.commit();
                        break;
                    case -1:
                        temp =  "服务器不允许注册（用户数量已满）";
                        break;
                    case -2:
                        temp = "用户名重复";
                        break;
                    case -3:
                        temp =  "邮箱重复";
                        break;
                }

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
