package com.eims.tjxl_andorid.weght;

import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.WqPopAdapter;
import com.eims.tjxl_andorid.adapter.WqPopAdapter.onItemClickListener;
import com.eims.tjxl_andorid.entity.AppyforWqBean.WqType;




import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

public class WqPopupWindow extends PopupWindow implements OnItemClickListener {

	private List<WqType> mItems;
	private WqPopupWindow mWindow;
	private onItemClickListener mListener;

	public WqPopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WqPopupWindow(Context context, int width, List<WqType> items) {
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View contentView = inflater.inflate(R.layout.activity_pop, null);
		// 设置PopupWindow的View
		this.setContentView(contentView);
		// 设置PopupWindow弹出窗体的宽
		this.setWidth(width);
		// 设置PopupWindow弹出窗体的高
		this.setHeight(android.view.WindowManager.LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// 刷新状态
		this.update();
		// 实例化一个ColorDrawable颜色为半透明
	    this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
       
		this.mItems = items;
		ListView listView = (ListView) contentView.findViewById(R.id.lv_list);
		mWindow = this;
		WqPopAdapter adapter = new WqPopAdapter(mWindow,context, mItems);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		WqPopupWindow.this.dismiss();

	}

	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
	}

	public void close() {
		this.dismiss();
	}

	public int position() {

		return 0;
	}

	public void setOnItemClickListener(onItemClickListener listener) {
		this.mListener = listener;
	}

	public onItemClickListener getListener() {
		// 可以通过this的实例来获取设置好的listener
		return mListener;
	}
}
