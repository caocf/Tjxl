/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.entity.GoodDetail;
import com.eims.tjxl_andorid.ui.product.BuyRecordsFragment.BuyGoodBean.BuyInfo;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 购买记录
 * @Author zd
 *************************************************************************** 
 */
public class BuyRecordsFragment extends android.support.v4.app.Fragment {

	public static BuyRecordsFragment buyRecordsFragment;
	private MyListView listview;
    private BuyGoodBean buyGoodBean;
    private BuyAdapter  mAdapter;
    private TextView message;
    
    private List<BuyInfo> infos=new ArrayList<BuyRecordsFragment.BuyGoodBean.BuyInfo>();
//	public static BuyRecordsFragment getInstance() {
//		if (buyRecordsFragment == null) {
//			buyRecordsFragment = new BuyRecordsFragment();
//		}
//		return buyRecordsFragment;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.buy_good_layout, null);
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	   findview();
		loadtData();
	}
	
	private void findview(){
		listview=(MyListView) getActivity().findViewById(R.id.listview_buy);
		message=(TextView) getActivity().findViewById(R.id.msg);
		if(mAdapter==null){
			mAdapter=new BuyAdapter();
		}
		listview.setAdapter(mAdapter);
	}

	private void  loadtData(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), false, ""){
			@Override
		    public void onSuccess(int statusCode, String content) {
		    	// TODO Auto-generated method stub
		    	super.onSuccess(statusCode, content);
		    	LogUtil.d(TAG, content);
		    	if(content.contains("data")){
		    		buyGoodBean = GsonUtils.json2bean(content, BuyGoodBean.class);
		    		infos.clear();
		            infos.addAll(buyGoodBean.data);
		    		//listview.setSelection(buyGoodBean.data.size());
		    //		LogUtil.d(TAG, buyGoodBean.data.size()+"");
		    		message.setVisibility(View.GONE);
		    	}else{
		    		message.setVisibility(View.VISIBLE);
		    	}
		    	mAdapter.notifyDataSetChanged();
		    }	
		};
		RequestParams params=new RequestParams();
		params.put("id", ProductDeatil.goodId);
		params.put("curPage", "1");
		params.put("pageSize", "15");
		
		HttpClient.QueryGoodBuyRecords(mHandler, params);
	}
	
	public  class  BuyGoodBean{
		public List<BuyInfo> data;	
		public String type;
		public String msg;
		public class BuyInfo {
			public String addtime;
			public String buyer_name;
			public String commodity_img_m;
			public String commodity_name;
			public String commodity_price;
			public String difference_price;
			public String quantity;
			public String spec_name_value;
			
		}
		
	}
	
	public class  BuyAdapter extends  BaseAdapter{
		@Override
		public int getCount() {

			return infos==null ? 0 : infos.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return infos.get(arg0);
		}

	
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int postioion, View convertview, ViewGroup arg2) {
			
			ViewHolder  holder;
			if(convertview==null){
				holder=new ViewHolder();
				convertview=View.inflate(getActivity(), R.layout.buy_good_records_layout,null);
				holder.name=(TextView) convertview.findViewById(R.id.buy_goodname);
				holder.price=(TextView) convertview.findViewById(R.id.buy_price);
				holder.saleNum=(TextView) convertview.findViewById(R.id.buy_sale_num);
				holder.buyDate=(TextView) convertview.findViewById(R.id.buy_date);
				convertview.setTag(holder);
			}else{
				holder=(ViewHolder) convertview.getTag();
			}
			
			BuyInfo buyInfo = infos.get(postioion);
			if(buyInfo!=null){
				 holder.name.setText(buyInfo.commodity_name);
			     holder.price.setText("￥"+buyInfo.commodity_price);
			     holder.saleNum.setText(buyInfo.quantity+"双");
			     holder.buyDate.setText(buyInfo.addtime+"");
			}
			    
			    		 			
		
			return convertview;
		}
		
	}
	
	class ViewHolder{
		TextView  name;
		TextView price;
		TextView saleNum;
		TextView buyDate;
	}
	
	

}
