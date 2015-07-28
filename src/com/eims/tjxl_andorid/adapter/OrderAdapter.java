/**
 * 
 */
package com.eims.tjxl_andorid.adapter;

import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseXAdapter;
import com.eims.tjxl_andorid.entity.OrderBean;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.imageupload.TipsToast;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月29日  下午4:37:01
 *************************************************************************** 
 */
public class OrderAdapter extends BaseXAdapter<OrderBean.OrderItemBean> implements OnClickListener{

	private Context mContext;
	public Callback mCallback;
	private OrderItemBean temp;
	/**
	 * @param context
	 */
	public OrderAdapter(Context context,Callback callback) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext=context;
		this.mCallback=callback;
	}
	
	/**
	      * 自定义接口，用于回调按钮点击事件到Activity
	      * @author zd
	      */
	    public interface Callback {
	         public void click(View v,OrderItemBean obj);
	     }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 ViewHolder holder=null;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=View.inflate(mContext, R.layout.order_list_item_layout, null);
			holder.order_item_shoename=(TextView) convertView.findViewById(R.id.order_item_shoename);
			holder.order_Status=(TextView) convertView.findViewById(R.id.order_item_status);
			holder.order_item_shoeImages=(ImageView) convertView.findViewById(R.id.order_shoeImages);
			holder.order_no=(TextView) convertView.findViewById(R.id.order_item_no);
			holder.order_money=(TextView) convertView.findViewById(R.id.order_item_money);
			holder.order_addtime=(TextView) convertView.findViewById(R.id.order_item_addtime);
			holder.order_item_numer=(TextView) convertView.findViewById(R.id.order_item_numner);
			holder.btn_left=(Button) convertView.findViewById(R.id.order_left);
			holder.btn_right=(Button) convertView.findViewById(R.id.order_right);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		OrderItemBean bean = mListData.get(position);
	    holder.order_item_shoename.setText(bean.f_factory_name);
		holder.order_Status.setText(bean.status_name);
		ImageManager.Load(bean.commodity_img_m, holder.order_item_shoeImages);
		holder.order_no.setText(bean.order_code);
		holder.order_money.setText(bean.total_price);
		holder.order_addtime.setText(bean.addtime);
		holder.order_item_numer.setText(bean.quantity+"双");
		//5交易完成，6交易关闭
		if("1".equals(bean.status)){
			holder.btn_left.setText("取消订单");
			holder.btn_right.setText("去付款");
			holder.btn_left.setTag("1");
			holder.btn_right.setTag("10");
		}else if("2".equals(bean.status)){
			holder.btn_left.setText("查看物流");
			holder.btn_right.setText("确认收货");
			holder.btn_left.setTag("2");
			holder.btn_right.setTag("20");
		}else if("4".equals(bean.status)){
			holder.btn_left.setText("申请售后");
			holder.btn_right.setText("去评价");
			holder.btn_left.setTag("4");
			holder.btn_right.setTag("40");
		}else if("5".equals(bean.status)){
			holder.btn_left.setVisibility(View.GONE);
			holder.btn_right.setVisibility(View.GONE);
		}else if("6".equals(bean.status)){
			holder.btn_left.setVisibility(View.GONE);
			holder.btn_right.setVisibility(View.GONE);
		}
		
//         holder.btn_left.setOnClickListener(new BtnLeftClick(holder));
//		 holder.btn_right.setOnClickListener(new BtnRightClick(holder));
		 temp=bean;
		 holder.btn_left.setOnClickListener(this);
		 holder.btn_right.setOnClickListener(this);
		return convertView;
	}
	
// class BtnLeftClick implements OnClickListener{
//		ViewHolder holder;
//        public BtnLeftClick(ViewHolder holder){
//        	this.holder=holder;
//        }
//		@Override
//		public void onClick(View arg0) {
//			String key=(String)holder.btn_left.getTag();
//			if("1".equals(key)){
//				TipsToast.makeText(mContext, "取消订单", 0).show();
//				//sCancelOrder(holder.btn_left);
//			}else if("2".equals(key)){
//				TipsToast.makeText(mContext, "查看物流", 0).show();
//			}else if("4".equals(key)){
//				TipsToast.makeText(mContext, "申请售后", 0).show();
//			}
//		}	
//	}
// 
// class BtnRightClick implements OnClickListener{
//		ViewHolder holder;
//     public BtnRightClick(ViewHolder holder){
//     	this.holder=holder;
//     }
//		@Override
//		public void onClick(View arg0) {
//			String key=(String) holder.btn_right.getTag();
//			if("1".equals(key)){
//				  TipsToast.makeText(mContext, "去付款", 0).show();
//			// goPay();
//			}else if("2".equals(key)){
//				 TipsToast.makeText(mContext, "确认收货", 0).show();
//				//OrderOk();
//			}else if("4".equals(key)){
//				TipsToast.makeText(mContext, "去评价", 0).show();
//			}
//		}	
//	}
	
  class ViewHolder{
	  TextView order_item_shoename;
	  TextView order_Status;
	  ImageView order_item_shoeImages;
	  TextView order_no;
	  TextView order_money;
	  TextView order_addtime;
	  TextView order_item_numer;
	  Button btn_left,btn_right;
  }

   //响应按钮点击事件,调用子定义接口，并传入View
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mCallback.click(v,temp);
	}
  
//  /**取消订单
// * @param btn_left **/
//  private  void  CancelOrder(final Button btn_left){
//
//  	CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在加载"){
//			@Override
//  		public void onSuccess(int statusCode, String content) {
//  			// TODO Auto-generated method stub
//  			super.onSuccess(statusCode, content);
//  			LogUtil.d(TAG, content);
//  			//隐藏 按钮
//  			String tag = (String) btn_left.getTag();
//  			if("1".equals(tag)){
//  				btn_left.setVisibility(View.GONE);
//  			}
//  	    	ll_btn.setVisibility(View.GONE);
//  		}
//  	};
//  	RequestParams params=new RequestParams();
//  	params.put("uid", user.id);
//  	params.put("id", orderId);
//  	HttpClient.Order_Cancel(handler, params);
//  }

}
