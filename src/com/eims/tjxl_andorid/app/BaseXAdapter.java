package com.eims.tjxl_andorid.app;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BaseXAdapter<T> extends BaseAdapter {

	public Context mContext;
	public ArrayList<T> mListData;

	public BaseXAdapter(Context context) {
		this.mContext = context;
		mListData = new ArrayList<T>();
	}

	public void cleanSelect() {

	}

	@Override
	public int getCount() {
		return mListData.size();
	}

	@Override
	public Object getItem(int position) {
		return mListData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return -1;
	}

	public int getId(int position) {
		return -1;
	}

	public ArrayList<T> getList() {
		return mListData;
	}

	public void deleteItem() {
		mListData.clear();
		notifyDataSetChanged();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addItem(Collection<? extends T> t) {
		if (t == null) {
			return;
		}
		if (t instanceof ArrayList) {
			mListData.addAll(t);
		} else {
			mListData.add((T) t);
		}
		notifyDataSetChanged();
	}
	


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
