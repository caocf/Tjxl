package com.eims.tjxl_andorid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.MessageBean;

import java.util.ArrayList;

/**
 * 消息列表 Created by kuangyong on 2015/6/25.
 */
public class MessageListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<MessageBean> data;// 展厅集合

	public MessageListAdapter(Context context,
							  ArrayList<MessageBean> data) {
		this.context = context;
		this.data = data;
	}

	public void setData(ArrayList<MessageBean> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		MyHolder holder=null;
		if (convertview == null) {
			holder=new MyHolder();
			convertview = View.inflate(context, R.layout.layout_item_message, null);
			holder.tv_message_name= (TextView) convertview.findViewById(R.id.tv_message_name);
			holder.tv_message_time= (TextView) convertview.findViewById(R.id.tv_message_time);
			convertview.setTag(holder);
		}else{
			holder=(MyHolder)convertview.getTag();
		}
		String status=data.get(position).getRead_status();
		if("1".equals(status)){//是否已读(0否，1是)
			setTextColor(holder,context.getResources().getColor(R.color.gray));
		}else{
			setTextColor(holder,context.getResources().getColor(R.color.black));
		}
		holder.tv_message_name.setText(data.get(position).getSend_title());
		holder.tv_message_time.setText(data.get(position).getSend_time());
		return convertview;
	}

	private void setTextColor(MyHolder holder,int color){
		holder.tv_message_name.setTextColor(color);
		holder.tv_message_time.setTextColor(color);
	}

	static class MyHolder{
		TextView tv_message_name;
		TextView tv_message_time;
	}

}
