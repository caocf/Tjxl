package com.eims.tjxl_andorid.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.ExhibitionBean;
import com.eims.tjxl_andorid.ui.exhibition.ExhibitionDetailActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;

import java.util.ArrayList;

/**
 * 展厅列表适配器 Created by kuangyong on 2015/6/25.
 */
public class ExhibitionListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<ExhibitionBean> data;// 展厅集合

	public ExhibitionListAdapter(Context context, ArrayList<ExhibitionBean> data) {
		this.context = context;
		this.data = data;
	}

	public void setData(ArrayList<ExhibitionBean> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return (null == data || 0 == data.size()) ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.exhibition_list_lv_item, null);
			holder = new MyHolder();
			holder.layoutright = (LinearLayout) convertView
					.findViewById(R.id.layout_right);
			holder.ivbigright = (ImageView) convertView
					.findViewById(R.id.iv_big_right);
			holder.ivright4 = (ImageView) convertView
					.findViewById(R.id.iv_right4);
			holder.ivright3 = (ImageView) convertView
					.findViewById(R.id.iv_right3);
			holder.ivright2 = (ImageView) convertView
					.findViewById(R.id.iv_right2);
			holder.ivright1 = (ImageView) convertView
					.findViewById(R.id.iv_right1);
			holder.layoutleft = (LinearLayout) convertView
					.findViewById(R.id.layout_left);
			holder.ivleft4 = (ImageView) convertView
					.findViewById(R.id.iv_left4);
			holder.ivleft3 = (ImageView) convertView
					.findViewById(R.id.iv_left3);
			holder.ivleft2 = (ImageView) convertView
					.findViewById(R.id.iv_left2);
			holder.ivleft1 = (ImageView) convertView
					.findViewById(R.id.iv_left1);
			holder.ivbigleft = (ImageView) convertView
					.findViewById(R.id.iv_big_left);
			holder.tvexhibitionname = (TextView) convertView
					.findViewById(R.id.tv_exhibition_name);
			convertView.setTag(holder);
		} else {
			holder = (MyHolder) convertView.getTag();
		}
		holder.ivbigright.setOnClickListener(myOnClickListener);
		holder.ivbigright.setTag(data.get(position).getId());
		holder.ivright4.setOnClickListener(myOnClickListener);
		holder.ivright4.setTag(data.get(position).getId());
		holder.ivright3.setOnClickListener(myOnClickListener);
		holder.ivright3.setTag(data.get(position).getId());
		holder.ivright2.setOnClickListener(myOnClickListener);
		holder.ivright2.setTag(data.get(position).getId());
		holder.ivright1.setOnClickListener(myOnClickListener);
		holder.ivright1.setTag(data.get(position).getId());
		holder.ivleft4.setOnClickListener(myOnClickListener);
		holder.ivleft4.setTag(data.get(position).getId());
		holder.ivleft3.setOnClickListener(myOnClickListener);
		holder.ivleft3.setTag(data.get(position).getId());
		holder.ivleft2.setOnClickListener(myOnClickListener);
		holder.ivleft2.setTag(data.get(position).getId());
		holder.ivleft1.setOnClickListener(myOnClickListener);
		holder.ivleft1.setTag(data.get(position).getId());
		holder.ivbigleft.setOnClickListener(myOnClickListener);
		holder.ivbigleft.setTag(data.get(position).getId());
		showImages(holder, data.get(position).getImgList());
		setShowStyle(position, holder);
		holder.tvexhibitionname.setText(data.get(position).getExhibition());
		return convertView;
	}

    /**
     * 展示图片
     * @param holder
     * @param imgs
     */
    private void showImages( MyHolder holder,ArrayList<String> imgs){
        ArrayList<ImageView> views=new ArrayList<ImageView>();
        views.add(holder.ivbigleft);
        views.add(holder.ivleft1);
        views.add(holder.ivleft2);
        views.add(holder.ivleft3);
        views.add(holder.ivleft4);
        views.add(holder.ivbigright);
        views.add(holder.ivright1);
        views.add(holder.ivright2);
        views.add(holder.ivright3);
        views.add(holder.ivright4);
		ArrayList<String> images=new ArrayList<String>();
		images.addAll(imgs);
		images.addAll(imgs);
        for (int i=0;i<imgs.size();i++){
			if(TextUtils.isEmpty(imgs.get(i))){
				views.get(i).setVisibility(View.INVISIBLE);
			}else{
				views.get(i).setVisibility(View.VISIBLE);
				ImageManager.Load(imgs.get(i), views.get(i));
			}
		}
    }

    /**
     * 控制大图在左边还是右边
     * @param position
     * @param holder
     */
	private void setShowStyle(int position, MyHolder holder) {
		if (0 == position % 2) {
			holder.layoutright.setVisibility(View.GONE);
			holder.layoutleft.setVisibility(View.VISIBLE);
		} else {
			holder.layoutright.setVisibility(View.VISIBLE);
			holder.layoutleft.setVisibility(View.GONE);
		}
	}

	private View.OnClickListener myOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String exhibition_id= (String) v.getTag();
			if(!TextUtils.isEmpty(exhibition_id)){
				Bundle bundle=new Bundle();
				bundle.putString(ExhibitionDetailActivity.EXHIBITION_ID,exhibition_id);
				ActivitySwitch.openActivity(ExhibitionDetailActivity.class, bundle,(Activity)context);
			}
		}
	};

	static class MyHolder {
		LinearLayout layoutright;
		ImageView ivbigright;
		ImageView ivright4;
		ImageView ivright3;
		ImageView ivright2;
		ImageView ivright1;
		LinearLayout layoutleft;
		ImageView ivleft4;
		ImageView ivleft3;
		ImageView ivleft2;
		ImageView ivleft1;
		ImageView ivbigleft;
		TextView tvexhibitionname;//展厅名称
	}
}
