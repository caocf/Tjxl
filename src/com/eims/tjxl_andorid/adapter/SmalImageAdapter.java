/**
 * 
 */
package com.eims.tjxl_andorid.adapter;

import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.ViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 小图模式（ListView）
 * @Author zd
 * @date 2015年4月24日 下午2:48:13
 *************************************************************************** 
 */
public class SmalImageAdapter extends BaseAdapter {

	private static final String TAG = "SmalImageAdapter";
	private Context context;
	private List<ProductBean> list;
	private ImageLoader mImageLoader;

	public SmalImageAdapter(Context context, List<ProductBean> list) {
		this.context = context;
		this.list = list;
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		return list.get(arg0);
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
	public View getView(int poistion, View convertview, ViewGroup arg2) {
	 
		if (convertview == null) {
			convertview = View.inflate(context,R.layout.product_item_listview_layout, null);
		}
		Log.d(TAG,"position = "+poistion+", list size = "+list.size());
		if(list.get(poistion).objectType == 1){
			convertview.setVisibility(View.VISIBLE);
			ImageView image = ViewHolder.get(convertview, R.id.product_image);
			TextView name = ViewHolder.get(convertview, R.id.product_name);
			TextView price = ViewHolder.get(convertview, R.id.product_price);
			TextView salenumber = ViewHolder.get(convertview, R.id.sale_number);
			//	ImageManager.Load(list.get(poistion).image, image);
			mImageLoader.displayImage(list.get(poistion).image, image);
			name.setText(list.get(poistion).name);
			price.setText("￥ ： "+list.get(poistion).price);
			salenumber.setText("销量:" + list.get(poistion).getSaleNumber());
		} else {
			convertview.setVisibility(View.INVISIBLE);
		}
		return convertview;
	}

}
