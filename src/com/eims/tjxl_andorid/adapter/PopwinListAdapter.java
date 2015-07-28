package com.eims.tjxl_andorid.adapter;

import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.ui.home.SearchActivity;
import com.eims.tjxl_andorid.utils.ViewHolder;
import com.eims.tjxl_andorid.weght.MyPopupWindow;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PopwinListAdapter extends BaseAdapter {
	private List<String> mItems;
	private Context context;

	public PopwinListAdapter(Context context, List<String> items) {
		this.mItems = items;
		this.context = context;
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertview, ViewGroup arg2) {
		// 获取设置好的listener
		if (convertview == null) {
			convertview = View.inflate(context,
					R.layout.procurement_item_layout, null);
		}
		TextView tv = ViewHolder.get(convertview, R.id.color_size);
		tv.setText(mItems.get(position));

		return convertview;
	}

}
