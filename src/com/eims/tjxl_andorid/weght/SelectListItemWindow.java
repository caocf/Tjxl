package com.eims.tjxl_andorid.weght;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;

import java.util.ArrayList;

public class SelectListItemWindow extends PopupWindow {

	private ListView list;
	private View mMenuView = null;

	public SelectListItemWindow(Context context,int width,int height,ArrayList<String> data,AdapterView.OnItemClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.pop_list, null);
		list = (ListView) mMenuView.findViewById(R.id.list);
		list.setOnItemClickListener(itemsOnClick);
		ListAdapter adaper=new ListAdapter(data,context);
		list.setAdapter(adaper);

		//LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width, height);
		//list.setLayoutParams(parms);


		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(width);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(height);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.popAni);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable();
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
		this.update();
	}


	class ListAdapter extends BaseAdapter{

		private ArrayList<String> data;
		private Context context;
		public ListAdapter(ArrayList<String> data,Context context){
			this.data=data;
			this.context=context;
		}

		@Override
		public int getCount() {
			return (null==data||data.size()==0)?0:data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyHolder myHolder=null;
			if(null==convertView){
				convertView=LayoutInflater.from(context).inflate(R.layout.item_list,null);
				myHolder=new MyHolder();
				myHolder.tv_item=(TextView)convertView.findViewById(R.id.tv_item);
				convertView.setTag(myHolder);
			}else{
				myHolder=(MyHolder)convertView.getTag();
			}
			myHolder.tv_item.setText(data.get(position));
			return convertView;
		}
	}

	static class MyHolder{
		TextView tv_item;
	}

}
