package com.feicui.android.press.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.Adapter.UserXListAdapter;
import com.feicui.android.press.Base.MyBaseActivity;
import com.feicui.android.press.Entry.News;
import com.feicui.android.press.Entry.User.User;
import com.feicui.android.press.Fragment.Analysis.UserHomeParse;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.CommitEntry;
import com.feicui.android.press.SQLite.Entry.UserHomeEntry;
import com.feicui.android.press.lib3.Image.MultiPosttRequest;
import com.feicui.android.press.lib3.XMLListView.XListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016/11/1.
 *
 */
public class UserHomeActivity extends MyBaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    //头像
    private ImageView im_portrait;
    //名字
    private TextView tv_nid;
    //地点
    private TextView tv_address;
    //时间
    private TextView tv_time;
    //积分
    private TextView tv_integration;
    //评论数量
    private TextView tv_comnum;
    //返回
    private ImageView im_back;
    //退出登录
    private Button bt_logout;
    //PopupWindow
    private PopupWindow pp;
    //XListView
    private XListView xlv;
    private UserXListAdapter xAdapter;
    //解析帮助类
    private UserHomeParse homeParse;
    //用户中心实体
    private UserHomeEntry homeEntry;

    private Button bt_photo, bt_camera, bt_out;

    private ArrayList<UserHomeEntry> list;
    //相机请求码
    private static final int CAMERA_REQUEST = 201;
    //相册请求码
    private static final int ALBUM_REQUEST = 301;
    //剪切请求码
    private static final int CROP_REQUEST = 401;

    //token
    private String token;

    Intent intent;

    @Override
    public int setContent() {
        return R.layout.user;
    }

    @Override
    public void initView() {
        im_portrait = (ImageView) findViewById(R.id.im_user_portrait);
        tv_nid = (TextView) findViewById(R.id.tv_user_nid);
        tv_address = (TextView) findViewById(R.id.tv_user_address);
        tv_time = (TextView) findViewById(R.id.tv_user_time);
        tv_integration = (TextView) findViewById(R.id.tv_user_integration);
        tv_comnum = (TextView) findViewById(R.id.tv_user_comnum);
        im_back = (ImageView) findViewById(R.id.im_home_back);
        bt_logout = (Button) findViewById(R.id.bt_logout);
        xlv = (XListView) findViewById(R.id.xlv_home);
        xlv.setRefreshTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        xAdapter = new UserXListAdapter(this);
        homeParse = new UserHomeParse(this);
        initPP();
        initPerson();
    }

    @Override
    public void setListener() {
        im_portrait.setOnClickListener(this);
        bt_logout.setOnClickListener(this);
        im_back.setOnClickListener(this);
        bt_out.setOnClickListener(this);
        bt_photo.setOnClickListener(this);
        bt_camera.setOnClickListener(this);
        xlv.setPullRefreshEnable(true);
        xlv.setXListViewListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_user_portrait:
                showToast("camera");
                if (pp != null && pp.isShowing()) {
                    pp.dismiss();
                } else {
                    pp.showAtLocation(findViewById(R.id.ll_home), Gravity.BOTTOM, 10, 10);
                    pp.setOutsideTouchable(true);
                }
                break;
            case R.id.bt_camera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
                break;
            case R.id.bt_photo:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, ALBUM_REQUEST );
                break;
            case R.id.bt_out:
                pp.dismiss();
                break;
            case R.id.im_home_back:
                finish();
                break;
            case R.id.bt_logout:
                SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putBoolean("login", false);
                editor.commit();
                finish();
                break;
        }
    }

    private void initPP() {
        pp = new PopupWindow();
        View view = getLayoutInflater().inflate(R.layout.home_pp_windows, null);
        pp.setContentView(view);
        pp.setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pp.setFocusable(true);
        pp.setBackgroundDrawable(new BitmapDrawable());
        bt_camera = (Button) view.findViewById(R.id.bt_camera);
        bt_photo = (Button) view.findViewById(R.id.bt_photo);
        bt_out = (Button) view.findViewById(R.id.bt_out);
    }


    @Override
    public void onRefresh() {
        initPerson();
        xlv.stopRefresh();
    }

    @Override
    public void onLoadMore() {

    }


    private void initPerson() {
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);
        token = preferences.getString("token", "");
        String url = News.HTTP + News.USER_HOME + User.userHome(1111111111, token);
        homeParse.getAnalysy(url, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Log.i("yzg", s);
            list = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("message").equals("OK") && jsonObject.getInt("status") == 0) {
                    JSONObject object = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = object.getJSONArray("loginlog");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject obj = jsonArray.getJSONObject(j);
                        homeEntry = new UserHomeEntry(obj.getString("time"), obj.getString("address"), obj.getInt("device"));
                        list.add(homeEntry);
                    }
                    tv_comnum.setText(object.getInt("comnum")+"");
                    tv_nid.setText( object.getString("uid"));
                    tv_integration.setText(object.getInt("integration")+"");
                    tv_time.setText(list.get(list.size() - 1).getTime());
                    tv_address.setText(list.get(list.size() - 1).getAddress());
                    homeParse.getAnalysyImage(object.getString("portrait"), new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            Log.i("yzg", bitmap + "");
                            im_portrait.setImageBitmap(bitmap);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            xAdapter.setmList(list);
            xlv.setAdapter(xAdapter);
        }
    };

    private void crop(Uri url){
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(url, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST){
            if(data != null){
                crop(data.getData());
            }
        } else if(requestCode == ALBUM_REQUEST){
            if(data != null){
                crop(data.getData());
            }
        }else if(requestCode == CROP_REQUEST){
            if(data != null){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                im_portrait.setImageBitmap(bitmap);
                saveImage(bitmap);
                upLoadImage();
            }
        }
    }

    private void saveImage(Bitmap bitmap){
        String icon = homeEntry.getName() + ".jpg";
        File file = new File(getCacheDir(), "images");
        if(file.exists()){
            file.mkdirs();
        }
        try {
            OutputStream out = new FileOutputStream(new File(file, icon));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void upLoadImage(){
        File file = new File(getCacheDir() + File.separator + "image" + File.separator + homeEntry.getName() + ".jpg");
        String url = News.HTTP + News.USER_IMAGE + User.TOKEN + token;
        RequestQueue queue = Volley.newRequestQueue(this);
        MultiPosttRequest request = new MultiPosttRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.buildMultipartEntity("portrait", file);
        queue.add(request);
    }
}
