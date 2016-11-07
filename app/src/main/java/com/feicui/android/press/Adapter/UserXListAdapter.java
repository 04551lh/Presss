package com.feicui.android.press.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feicui.android.press.Base.MyBaseAdapter;
import com.feicui.android.press.R;
import com.feicui.android.press.SQLite.Entry.UserHomeEntry;

/**
 * Created by Administrator on 2016/11/2.
 *
 */
public class UserXListAdapter extends MyBaseAdapter<UserHomeEntry> {
    public UserXListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getMyView(int position, View convertView, ViewGroup parent) {
        UserXListAdapter.XHolderView holderView = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.xlv_user, null);
            holderView = new UserXListAdapter.XHolderView(convertView);
            convertView.setTag(holderView);
        } else {
            holderView = (UserXListAdapter.XHolderView) convertView.getTag();
        }

        holderView.tv_time.setText(mList.get(position).getTime());
        holderView.tv_address.setText(mList.get(position).getAddress());
        holderView.tv_device.setText(mList.get(position).getDevice()+"");
        return convertView;
    }

    class XHolderView {
        TextView tv_time, tv_address, tv_device;

        public XHolderView(View view) {
            tv_time = (TextView) view.findViewById(R.id.tv_home_time);
            tv_address = (TextView) view.findViewById(R.id.tv_home_address);
            tv_device = (TextView) view.findViewById(R.id.tv_home_device);
        }
    }
}
