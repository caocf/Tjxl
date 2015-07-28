package com.eims.tjxl_andorid.adapter;

import java.util.ArrayList;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.base.ImageDataLoader;
import com.eims.tjxl_andorid.entity.BannerBean.BannerItem;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.utils.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 首页轮播图适配器
 * @Author zd
 * @date 2015年4月16日 下午4:27:28
 *************************************************************************** 
 */
public class BannerAdapter extends BaseAdapter {

	private ArrayList<BannerItem> list;
	private Activity mContext;

	public BannerAdapter(Activity mContext) {
		list = new ArrayList<BannerItem>();
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return list==null?0:list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<BannerItem> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.image_items, parent, false);
		}
		ImageView bananaView = (ImageView) convertView.findViewById(R.id.banner_iv);
		BannerItem bannerItem = list.get(position);
		if(bannerItem!=null){
			ImageManager.Load(list.get(position).img, bananaView);
			bananaView.setTag(position);
			bananaView.setOnClickListener(new ImageOnclick(position,bannerItem));			
		}	
		return convertView;
	}

	class ImageOnclick implements View.OnClickListener {
		private int position;
        private BannerItem bean;
		ImageOnclick(int position, BannerItem bannerItem) {
			this.position = position;
			this.bean=bannerItem;
		}

		@Override
		public void onClick(View v) {
			LogUtil.d("banner", "dian ji.....................");
			ImageDataLoader.gotoWitchOne(mContext, bean);
		}
	}
}
