package com.feicui.android.press.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.feicui.android.press.Base.MyBaseAdapter;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.CommitEntry;
import com.feicui.android.press.lib3.XMLListView.XListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ComXListAdapter extends MyBaseAdapter<CommitEntry> {
    private XListView xlv;
    //存储器
    private LruCache<String,Bitmap> lruCache;
    //三方架构
    RequestQueue request;
    public ComXListAdapter(Context context, XListView lv) {
        super(context);
        xlv = lv;
        lruCache = new LruCache<>(1024*1024);
        request = Volley.newRequestQueue(context);
    }

    private Bitmap getImages(String url){
        String iconName = url.substring(url.lastIndexOf("/") + 1);
        Bitmap bitmap = lruCache.get(iconName);
        if(bitmap != null){
            return bitmap;
        }
        bitmap = getLocalImage(iconName);
        if(bitmap != null){
            return bitmap;
        }
        getHttpConnect(url, iconName);
        return bitmap;
    }

    /**
     * @description 获取本地图片
     * @param iconName
     * @return
     */
    private Bitmap getLocalImage(String iconName){
        Bitmap bitmap = null;
        File file = null;
        File[] files = mContent.getCacheDir().listFiles();
        for (File f: files) {
            if(f.getName().equals(iconName)){
                file = f;
                break;
            }
        }
        if(file == null){
            return null;
        }
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        lruCache.put(iconName,bitmap);
        return bitmap;
    }

    private void getHttpConnect(final String url, final String iconName){
        ImageRequest imageRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                ImageView im = (ImageView) xlv.findViewWithTag(url);
                if(im != null){
                    im.setImageBitmap(bitmap);
                    File file = mContent.getCacheDir();
                    if(file.exists()){
                        file.mkdir();
                    }
                    File mFile = new File(file, iconName);
                    OutputStream out = null;
                    try {
                        out = new FileOutputStream(mFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    lruCache.put(iconName, bitmap);
                }

            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.add(imageRequest);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        ComXListAdapter.HolderView holderView = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.xlv_commit, null);
            holderView = new ComXListAdapter.HolderView(convertView);
            convertView.setTag(holderView);
        }
        else{
            holderView = (HolderView) convertView.getTag();
        }
        holderView.tv_name.setText(mList.get(position).getUid());
        holderView.tv_time.setText(mList.get(position).getTime());
        holderView.tv_context.setText(mList.get(position).getContext());
        if(mList.get(position).getPortrait() != null && mList.get(position).getPortrait().contains("/")){
            Bitmap b = getImages(mList.get(position).getPortrait());
            if(b != null){
                holderView.im_portrain.setImageBitmap(b);
            }
        }
        holderView.im_portrain.setTag(mList.get(position).getPortrait());
        return convertView;
    }

    class HolderView{
        public ImageView im_portrain;
        public TextView tv_name, tv_time, tv_context;
        public HolderView(View view){
            im_portrain = (ImageView) view.findViewById(R.id.im_commit_portrain);
            tv_context = (TextView) view.findViewById(R.id.tv_commit_context);
            tv_time = (TextView) view.findViewById(R.id.tv_commit_time );
            tv_name = (TextView) view.findViewById(R.id.tv_commit_name);
        }
    }
}
