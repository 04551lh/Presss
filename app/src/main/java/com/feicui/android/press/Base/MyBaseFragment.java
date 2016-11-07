package com.feicui.android.press.Base;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.feicui.android.press.R;

/**
 * Created by Administrator on 2016/10/28.
 */
public abstract class MyBaseFragment extends Fragment {
    public View view;
    private  Dialog dialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setContent(), null);
        initView();
        setListener();
        return view;
    }
    /**
     * 设置当前布局文件
     */
    public abstract int setContent();

    /**
     * 加载控件
     */
    public abstract void initView();

    /**
     * 设置监听
     */
    public abstract void setListener();

    /**
     * 显示提示
     * @param message 提示内容
     */
    public void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 网络连接
     * @param content 上下文
     * @return 是否连接网络
     */
    public  boolean isNetworkAvailable(Context content){
        ConnectivityManager connectivityManager = (ConnectivityManager) content.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null){
            return false;
        }
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info == null || !info.isAvailable()){
            return false;
        }
        return true;
    }

    /**
     * 加载数据时候显示的Dialog
     * @param message 加载时候显示的数据
     * @param cancelable 可不可以用返回键取消
     */
    public void loading(String message, boolean cancelable){
        //加载布局
        View view = getActivity().getLayoutInflater().inflate(R.layout.loading_dialog, null);
        //绑定控件
        LinearLayout ll_load = (LinearLayout) view.findViewById(R.id.ll_load);
        ImageView im_load = (ImageView) view.findViewById(R.id.im_load);
        TextView tv_load = (TextView) view.findViewById(R.id.tv_load);
        //加载动画
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.loading_anim);
        im_load.startAnimation(animation);
        //判断信息是否需要更新
        if(message != null){
            tv_load.setText(message);
        }
        //设置Dialog
        dialog =  new Dialog(getActivity(), R.style.loading_dialog);
        dialog.setCancelable(cancelable);
        dialog.setContentView(ll_load);
        dialog.show();
    }

    /**
     * 关闭Dialog
     */
    public void cancelDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }
}
