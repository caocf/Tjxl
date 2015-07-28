/**
 * 
 */
package com.eims.tjxl_andorid.weght;

import java.util.ArrayList;
import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.PopwinListAdapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 选购商品界面 需要使用的popwindow
 * @Author zd
 * @date 2015年4月20日 下午5:34:49
 *************************************************************************** 
 */
public class SelectGoodsPopWindow extends PopupWindow {

	private List<String> items;

	public SelectGoodsPopWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View mMenuView = inflater.inflate(
				R.layout.select_prodcut_popwindow_layout, null);
		items = new ArrayList<String>();
		// btn_delete.setOnClickListener(itemsOnClick);
		// btn_jubao.setOnClickListener(itemsOnClick);
		// btn_take_photo.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.ll_pop).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
		// this.mItems = items;
		items.add("黑色  42");
		items.add("黑色  42");
		items.add("黑色   42");
		items.add("黑色   42");
		items.add("黑色  42");
		ListView listView = (ListView) mMenuView
				.findViewById(R.id.procurement_listview);
		PopwinListAdapter adapter = new PopwinListAdapter(context, items);
		listView.setAdapter(adapter);
	}

}
