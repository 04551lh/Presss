package com.feicui.android.press.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
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
import com.feicui.android.press.SQLite.Entry.NewsListEntry;
import com.feicui.android.press.lib3.XMLListView.XListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/10/28.
 *
 */
public class XListAdapter extends MyBaseAdapter<NewsListEntry> {
    //容器
    private XListView xlv;
    //存储器
    private LruCache<String,Bitmap> lruCache;
    //三方架构
    RequestQueue request;
    //
    public XListAdapter(Context context, XListView lv) {
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
        if(bitmap != null){
            lruCache.put(iconName,bitmap);
        }
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
        XListAdapter.XHolderView holderView = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.xlist_item, null);
            holderView = new XListAdapter.XHolderView(convertView);
            convertView.setTag(holderView);
        }
        else{
            holderView = (XListAdapter.XHolderView) convertView.getTag();
        }

        holderView.tv_xtitle.setText(mList.get(position).getTitle());
        holderView.tv_xsummary.setText(mList.get(position).getSummary());
        holderView.tv_xhour.setText(mList.get(position).getStamp());
        if(mList.get(position).getIcon()!=null){
            Bitmap b = getImages(mList.get(position).getIcon());
            if(b != null){
                holderView.im_xlv.setImageBitmap(b);
            }
        }
        holderView.im_xlv.setTag(mList.get(position).getIcon());
        return convertView;
    }
    class XHolderView {
        ImageView im_xlv;
        TextView tv_xtitle, tv_xsummary, tv_xhour;

        public XHolderView(View view) {
            im_xlv = (ImageView) view.findViewById(R.id.im_xml);
            tv_xtitle = (TextView) view.findViewById(R.id.tv_xtitle);
            tv_xsummary = (TextView) view.findViewById(R.id.tv_xsummary);
            tv_xhour = (TextView) view.findViewById(R.id.tv_xhour);
        }
    }
}
