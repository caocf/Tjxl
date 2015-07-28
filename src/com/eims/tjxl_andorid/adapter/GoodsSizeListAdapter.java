package com.eims.tjxl_andorid.adapter;

import java.net.Socket;
import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.R.layout;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodSizeBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.ui.shopcart.ExchangeProductFragment;
import com.eims.tjxl_andorid.ui.shopcart.RefundFragment;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsSizeListAdapter extends BaseAdapter{

	private static final String TAG = "GoodsSizeListAdapter";
	private List<RefundGoodSizeBean> goodSizeBeans;
	private LayoutInflater mInflater;
	private Handler mHandler;
	private Context mContext;
	public GoodsSizeListAdapter(List<RefundGoodSizeBean> goodSizeBeans,LayoutInflater inflater,Handler handler,Context context){
		this.goodSizeBeans = goodSizeBeans;
		this.mInflater = inflater;
		this.mHandler = handler;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		return goodSizeBeans == null ? 0 : goodSizeBeans.size();
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
		final RefundGoodSizeBean sizeBean = goodSizeBeans.get(arg0);
		Log.d("zhiheng","sizebean = "+sizeBean);
		final int position = arg0;
		if(sizeBean.quantity_ori <= 0 && !sizeBean.quantity.trim().equals("")){
			sizeBean.quantity_ori = Integer.valueOf(sizeBean.quantity);//记录改尺码最原始的商品数
		}
		if(sizeBean.getType() == RefundGoodSizeBean.TYPE_EXCHANGE_RECEIVE && !sizeBean.quantity.trim().equals("")){
			sizeBean.quantity_ori = Integer.valueOf(sizeBean.total_stock);//换回产品的最大值应该是商家的库存量
		}
		Log.d(TAG, "sizeBean quantity_ori before= "+sizeBean.quantity_ori);
		View view = arg1;
		ViewHolder holder = null;
		
//		if(view == null){//此处判断没有意义，见ViewHolder类注释
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.layout_select_order_good_size, null);
			holder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
			holder.tv_color = (TextView) view.findViewById(R.id.good_color);
			holder.tv_size = (TextView) view.findViewById(R.id.good_size);
			holder.tv_price = (TextView) view.findViewById(R.id.good_price);
			holder.tv_selected_num = (TextView) view.findViewById(R.id.selected_num_value);
			holder.iv_add = (TextView) view.findViewById(R.id.add);
			holder.iv_jian = (TextView) view.findViewById(R.id.jian);
			holder.layout_num_select = view.findViewById(R.id.layout_select_num);
			view.setTag(holder);
//		}
		holder = (ViewHolder) view.getTag();
		Log.d("zhiheng","view = "+view+",  holder = "+holder);
		String values[] = sizeBean.spec_name_value.split("，");//中文输入法的逗号
		holder.tv_color.setText(values[0]);
		if(values.length > 1) holder.tv_size.setText(values[1]);
		holder.tv_price.setText("￥"+sizeBean.commodity_price);
		holder.tv_selected_num.setText(sizeBean.quantity);
		
		if(sizeBean.getType() == RefundGoodSizeBean.TYPE_REFUND_BILL){
			holder.layout_num_select.setBackgroundResource(0);
			holder.checkBox.setVisibility(View.GONE);
		}else {
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.layout_num_select.setBackgroundResource(R.drawable.jxk);
			holder.tv_selected_num.setText(sizeBean.getQuantity());
			holder.iv_add.setTag(R.id.first_tag,Integer.valueOf(sizeBean.quantity_ori));
			holder.iv_add.setTag(R.id.second_tag,holder.tv_selected_num);
			holder.iv_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					int tag = (Integer) arg0.getTag(R.id.first_tag);
					TextView tv_select_num = (TextView) arg0.getTag(R.id.second_tag);
					int num = Integer.valueOf(tv_select_num.getText().toString());
					if(num < tag){
						num++;
					}
					sizeBean.setQuantity(num+"");
					tv_select_num.setText(num+"");
					sendMessage(sizeBean);
					GoodsSizeListAdapter.this.notifyDataSetChanged();
				}
			});
			holder.iv_jian.setTag(R.id.first_tag,Integer.valueOf(sizeBean.quantity_ori));
			holder.iv_jian.setTag(R.id.second_tag,holder.tv_selected_num);
			holder.iv_jian.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					int tag = (Integer) arg0.getTag(R.id.first_tag);
					TextView tv_select_num = (TextView) arg0.getTag(R.id.second_tag);
					int num = Integer.valueOf(tv_select_num.getText().toString());
					if(num > 0){
						num--;
					}
					Log.d(TAG, "jian btn clicked ,tag = "+tag);
					tv_select_num.setText(num+"");
					sizeBean.quantity = num+"";
					sendMessage(sizeBean);
					GoodsSizeListAdapter.this.notifyDataSetChanged();
				}
			});
//			if(sizeBean.getType() == RefundGoodSizeBean.TYPE_EXCHANGE_RECEIVE){
			Log.d("zhiheng","isSelected before = "+sizeBean.isSelcetd);
			holder.checkBox.setChecked(sizeBean.isSelcetd);
//			}
			holder.checkBox.setTag(sizeBean);
			holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					sizeBean.setSelcetd(arg1);
					Log.d("zhiheng", "isChecied = "+arg1+",sizeBean.isSelcetd = "+sizeBean.isSelcetd);
					sendMessage(sizeBean);
				}
			});
		}
		Log.d(TAG, "return view :"+view);
		return view;
	}
	
	class ViewHolder{
		CheckBox checkBox;
		TextView tv_color;
		TextView tv_size;
		TextView tv_price;
		TextView tv_selected_num;
		TextView iv_add;
		TextView iv_jian;
		View layout_num_select;
		
		
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
