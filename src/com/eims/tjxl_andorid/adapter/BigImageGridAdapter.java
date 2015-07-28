/**
 * 
 */
package com.eims.tjxl_andorid.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 大图模式
 * @Author zd
 * @date 2015年4月24日 下午2:48:13
 *************************************************************************** 
 */
public class BigImageGridAdapter extends BaseAdapter {

	private static final String TAG = "BigImageGridAdapter";
	private Context context;
	private List<ProductBean> list;
	private ImageLoader mImageLoader;

	public BigImageGridAdapter(Context context, List<ProductBean> list) {
		this.context = context;
		this.list = list;
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		Log.d(TAG," getCount = "+list.size());
		return list == null ? 0 : list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int poistion, View convertview, ViewGroup arg2) {
		if (convertview == null) {
			convertview = View.inflate(context,R.layout.product_item_gridview_layout, null);
		}
		LinearLayout layout = ViewHolder.get(convertview, R.id.ll_product_item);
		ImageView image = ViewHolder.get(convertview, R.id.product_image);
		TextView name = ViewHolder.get(convertview, R.id.product_name);
		TextView price = ViewHolder.get(convertview, R.id.product_price);
		TextView salenumber = ViewHolder.get(convertview, R.id.sale_number);
	//	ImageManager.Load(list.get(poistion).image, image);
		mImageLoader.displayImage(list.get(poistion).getImage(),image);
		name.setText(list.get(poistion).getName());
		Log.d(TAG,"product :name = "+list.get(poistion).getName());
		price.setText("￥ ： "+list.get(poistion).getPrice());
		salenumber.setText("销量 ： " + list.get(poistion).getSaleNumber());
		
		image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString(ProductDeatil.INTENT_KEY, list.get(poistion).id);
				ActivitySwitch.openActivity(ProductDeatil.class, bundle, (Activity) context);
			}
		});
		
		if(list.get(poistion).objectType == 1) {
			convertview.setVisibility(View.VISIBLE);
		} else {
			convertview.setVisibility(View.GONE);
		}
		return convertview;
	}

}
