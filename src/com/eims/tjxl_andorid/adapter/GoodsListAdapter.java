package com.eims.tjxl_andorid.adapter;

import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodBean;
import com.eims.tjxl_andorid.entity.GoodDetail;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.ui.shopcart.ExchangeProductFragment;
import com.eims.tjxl_andorid.ui.shopcart.RefundFragment;
import com.eims.tjxl_andorid.ui.user.MyCollectionsActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GoodsListAdapter extends BaseAdapter{

	private static  final String TAG = "GoodsListAdapter";
	private List<RefundGoodBean> goodBeans;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	private Handler mHandler;
	private Context mContext;
	private boolean isShowDeleteIcon = false;
	
	public GoodsListAdapter(List<RefundGoodBean> beans,LayoutInflater inflater,Handler handler,Context context,boolean isShow){
		this.goodBeans = beans;
		this.mInflater = inflater;
		this.mHandler = handler;
		this.mImageLoader = ImageLoader.getInstance();
		this.mContext = context;
		this.isShowDeleteIcon = isShow;
	}
	
	public void setList(List<RefundGoodBean>  list){
		goodBeans = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return goodBeans == null ? 0 : goodBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		final RefundGoodBean goodBean = goodBeans.get(arg0);
		View view = arg1;
		ViewHolder holder = null;
		
		if(view == null){
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.layout_seclet_order_good, null);
			holder.layoutGoodInfo = view.findViewById(R.id.layout_good_info);
			holder.goodIcon  = (ImageView) view.findViewById(R.id.product_icon);
			holder.goodName = (TextView) view.findViewById(R.id.product_name);
			holder.goodListView = (ListView) view.findViewById(R.id.good_size_listview);
			holder.deleteImage = (ImageView) view.findViewById(R.id.iv_delete_good);
			view.setTag(holder);
		}
		holder = (ViewHolder) view.getTag();
		holder.layoutGoodInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString(ProductDeatil.INTENT_KEY, goodBean.commodity_id);
				ActivitySwitch.openActivity(ProductDeatil.class, bundle,(Activity)mContext);
			}
		});
		mImageLoader.displayImage(URLs.IMAGE_URL+goodBean.commodity_img_m, holder.goodIcon);
		Log.d(TAG, "Good's icon url = "+goodBean.commodity_img_m);
		holder.goodName.setText(goodBean.commodity_name);
		holder.goodListView.setAdapter(new GoodsSizeListAdapter(goodBean.goodSizeBeans, mInflater,mHandler,mContext));
		
		if(isShowDeleteIcon){
			holder.deleteImage.setVisibility(View.VISIBLE);
			holder.deleteImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					for(int i=0; i<goodBean.goodSizeBeans.size(); i++){
						RefundGoodSizeBean sizeBean = goodBean.goodSizeBeans.get(i);
						sizeBean.setSelcetd(false);
						sendMessage(sizeBean);
					}
					goodBeans.remove(goodBean);
					GoodsListAdapter.this.notifyDataSetChanged();
				}
			});
		}else {
			holder.deleteImage.setVisibility(View.INVISIBLE);
		}
		
		return view;
	}
	
	/**
	 * 此处的ViewHolder并没有作用，因为在ScrollView中listView会一次性加载完
	 * 所有数据，也就不会存在ListView item重用的情况，这样Holder也就起不到缓存
	 * view id的作用了
	 * @author lzh
	 *
	 */
	class ViewHolder{
		View layoutGoodInfo;
		ImageView goodIcon;
		TextView goodName;
		ListView goodListView;
		ImageView deleteImage;
	}
	
	private void sendMessage(RefundGoodSizeBean sizeBean){
		switch (sizeBean.type) {
		case RefundGoodSizeBean.TYPE_REFUND_SELECT:
			RefundFragment.refundGoodsMap.put(sizeBean.getUniqueKey(), sizeBean);
			break;
		case RefundGoodSizeBean.TYPE_EXCHANGE_RECEIVE:
			ExchangeProductFragment.exchangeReceiveGoodsMap.put(sizeBean.getUniqueKey(), sizeBean);
			break;
		case RefundGoodSizeBean.TYPE_EXCHANGE_SEND:
			ExchangeProductFragment.exChangeSendGoodsMap.put(sizeBean.getUniqueKey(), sizeBean);
			break;
		default:
			break;
		}
		mHandler.sendEmptyMessage(sizeBean.getType());
	}

	
}
