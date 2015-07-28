package com.eims.tjxl_andorid.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.ExhibitionBean;
import com.eims.tjxl_andorid.utils.ViewHolder;

import java.util.ArrayList;

/**
 * 展厅列表适配器 Created by kuangyong on 2015/6/25.
 */
public class ExhibitionTextListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ExhibitionBean> data;// 展厅集合

	public ExhibitionTextListAdapter(Context context,
			ArrayList<ExhibitionBean> data) {
		this.context = context;
		this.data = data;
	}

	public void setData(ArrayList<ExhibitionBean> data) {
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
		if (convertview == null) {
			convertview = View.inflate(context, R.layout.category_item, null);
		}
		TextView type = ViewHolder.get(convertview, R.id.cate_type);
		type.setText(data.get(position).getExhibition());
		return convertview;
	}

}
