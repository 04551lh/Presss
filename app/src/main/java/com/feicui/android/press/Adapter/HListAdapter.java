package com.feicui.android.press.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feicui.android.press.Base.MyBaseAdapter;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.NewsSortEntry;

/**
 * Created by Administrator on 2016/10/28.
 */
public class HListAdapter extends MyBaseAdapter<NewsSortEntry> {
    private int mposition;

    public int getMposition() {
        return mposition;
    }

    public void setMposition(int mposition) {
        this.mposition = mposition;
    }

    public HListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        HListAdapter.HolderView holderView = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.horizon_item, null);
            holderView = new HListAdapter.HolderView();
            holderView. tv_horizon_item_data = (TextView)convertView.findViewById(R.id.tv_horizon_item_data);
            convertView.setTag(holderView);
        }
        else {
            holderView = (HListAdapter.HolderView) convertView.getTag();
        }
        holderView. tv_horizon_item_data.setText(mList.get(position).getSubgroup());
        if(mposition == position){
            holderView. tv_horizon_item_data.setTextColor(Color.RED);
        }
        return convertView;
    }
    public class HolderView{
        public TextView tv_horizon_item_data;
    }
}
