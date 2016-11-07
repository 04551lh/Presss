package com.feicui.android.press.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.feicui.android.press.Base.MyBaseActivity;
import com.feicui.android.press.R;


/**
 * Created by Administrator on 2016/10/22.
 * @descreiption 引导界面
 */
public class GuideActivity extends MyBaseActivity implements ViewPager.OnPageChangeListener {
    //声明控件
    private ViewPager vp_show;
    private View view[] = new View[4];
    private ImageView im_first, im_second, im_three, im_four;
    //定义ViewPager标题
    String title[] = {"昨天", "今天", "明天", "未来"};
    @Override
    public int setContent() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView(){
       SharedPreferences preferences = getSharedPreferences("data", MODE_PRIVATE);
        final boolean isFirstOpen = preferences.getBoolean("firstOpen", false);
        if(isFirstOpen){
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
            finish();
        }
        else{
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstOpen", true);
            editor.commit();
        }

        vp_show = (ViewPager) findViewById(R.id.vp_show);
        view[0] = getLayoutInflater().inflate(R.layout.view_first, null);
        view[1] = getLayoutInflater().inflate(R.layout.view_second, null);
        view[2] = getLayoutInflater().inflate(R.layout.view_three, null);
        view[3] = getLayoutInflater().inflate(R.layout.view_four, null);
        view[3].findViewById(R.id.bt_index).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        im_first = (ImageView) findViewById(R.id.view_first);
        im_second = (ImageView) findViewById(R.id.view_second);
        im_three = (ImageView) findViewById(R.id.view_three);
        im_four = (ImageView) findViewById(R.id.view_four);
        vp_show.setAdapter(new MyViewPageAdapter());
    }

    @Override
    public void setListener() {
        vp_show.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        switch (position){
            case 0:
                im_first.setImageResource(R.drawable.user_true);
                im_second.setImageResource(R.drawable.usr_false);
                im_three.setImageResource(R.drawable.usr_false);
                im_four.setImageResource(R.drawable.usr_false);
                break;
            case 1:
                im_first.setImageResource(R.drawable.usr_false);
                im_second.setImageResource(R.drawable.user_true);
                im_three.setImageResource(R.drawable.usr_false);
                im_four.setImageResource(R.drawable.usr_false);
                break;
            case 2:
                im_first.setImageResource(R.drawable.usr_false);
                im_second.setImageResource(R.drawable.usr_false);
                im_three.setImageResource(R.drawable.user_true);
                im_four.setImageResource(R.drawable.usr_false);
                break;
            case 3:
                im_first.setImageResource(R.drawable.usr_false);
                im_second.setImageResource(R.drawable.usr_false);
                im_three.setImageResource(R.drawable.usr_false);
                im_four.setImageResource(R.drawable.user_true);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyViewPageAdapter extends PagerAdapter {

        /**
         * @description 定义View长度
         * @return View长度
         */
        @Override
        public int getCount() {
            return view.length;
        }

        /**
         * @descreiption 添加View布局
         * @param container 父容器
         * @param position 位置
         * @return 添加的View
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = view[position];
            container.addView(v);
            return v;
        }

        /**
         * @descreiption 添加标题
         * @param position 标题下标
         * @return 标题
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        /**
         * @description 删除View
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(view[position]);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
