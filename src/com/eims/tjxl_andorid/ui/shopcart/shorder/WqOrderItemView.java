/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart.shorder;

import loopj.android.http.RequestParams;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.entity.ReplaceOrderBean.ReplaceItemBean;
import com.eims.tjxl_andorid.entity.WqOrderBean.WqOrderItemBean;
import com.eims.tjxl_andorid.ui.shopcart.WqOrderDeatilActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 维权订单 列表
 * @Author zd
 * @date 2015年7月6日  上午10:57:33
 *************************************************************************** 
 */

	public class WqOrderItemView extends  LinearLayout{

	    private static final String TAG = "WqOrderItemView";
		private Context mContext;
	    private TextView order_wq_shoename;//卖家名称
	    private TextView order_wqStatus;//维权订单状态
	    private  TextView order_no;//维权订单编号
	    private   TextView order_addtime;//申请时间
	    private   TextView order_wqType;//维权订单类型
	    private   TextView order_type;//维权类型
	    private Button ddwqxq;//订单维权详情
	    private Button hhwqxq;//换货维权详情
	    private Button thwqxq;//退货维权详情
	    private Button plwqxq;//评论维权详情
		private Bundle bundle;
	    private WqOrderItemBean data;
	    private String uid;

	    
		public WqOrderItemView(Context context) {
			super(context);
			this.mContext=context;
			LayoutInflater.from(context).inflate(R.layout.wq_item_view, this, true);
			findView();
			setListener();
		}

		public WqOrderItemView(Context context, AttributeSet attrs) {
			super(context, attrs);
			this.mContext=context;
			LayoutInflater.from(context).inflate(R.layout.wq_item_view, this, true);
			findView();
			setListener();
		}
		
		
		
		private void findView() {
			order_wq_shoename=(TextView)findViewById(R.id.wq_shoe_username);
			order_wqStatus=(TextView) findViewById(R.id.order_wq_status);
			order_no=(TextView) findViewById(R.id.order_wq_no);
			order_addtime=(TextView) findViewById(R.id.order_wq_addtime);
			order_wqType=(TextView) findViewById(R.id.order_wq_type);
			order_type=(TextView) findViewById(R.id.ordertype);

			ddwqxq=(Button) findViewById(R.id.wqxq);
			hhwqxq=(Button) findViewById(R.id.hhwqxq);
			thwqxq=(Button) findViewById(R.id.thwqxq);
			plwqxq=(Button) findViewById(R.id.plwqxq);
		
		}
		public void setUid(String uid){
			this.uid=uid;
		}
		
		public void setData(WqOrderItemBean data){
			this.data = data;
			refreshItemView();
		}
		
		public void refreshItemView() {
		    	order_wq_shoename.setText(data.s_username);
		    	order_wqStatus.setText(data.order_type_name);
				order_no.setText(data.uygur_power_code);
				order_addtime.setText(data.addtime);
				order_wqType.setText(data.up_status_name);
				order_type.setText(data.type_name);
			    updataButtom();
			
		}

		private void updataButtom() {
			// TODO Auto-generated method stub
//			from_order_type=0 （订单维权详情）
//					from_order_type=1 （换货维权详情）
//					from_order_type=2 （退货维权详情）
//					from_order_type=3 （评论维权详情）
			ddwqxq.setVisibility(View.GONE);
			hhwqxq.setVisibility(View.GONE);
			thwqxq.setVisibility(View.GONE);
			plwqxq.setVisibility(View.GONE);
	
		    String  status=data.from_order_type;
	        if("0".equals(status)){
	        	ddwqxq.setVisibility(View.VISIBLE);
	        }else if("1".equals(status)){
	        	hhwqxq.setVisibility(View.VISIBLE);
	        }else if("2".equals(status)){
	        	thwqxq.setVisibility(View.VISIBLE);
	        }else if("3".equals(status)){
	        	plwqxq.setVisibility(View.VISIBLE);
	        }
	  
		    
		}
		
		private  void  setListener(){
			BtnClickListener clickListener=new BtnClickListener();
			ddwqxq.setOnClickListener(clickListener);	
			hhwqxq.setOnClickListener(clickListener);
			thwqxq.setOnClickListener(clickListener);
			plwqxq.setOnClickListener(clickListener);
		}
		
		class BtnClickListener implements OnClickListener{
		

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.wqxq:
					 ToWqActivity(WqOrderDeatilActivity.DDWQ);
					break;
				case R.id.hhwqxq:
					 ToWqActivity(WqOrderDeatilActivity.HHWQ);
					break;
				case R.id.thwqxq:
					 ToWqActivity(WqOrderDeatilActivity.THWQ);
					break;
				case R.id.plwqxq:
			        ToWqActivity(WqOrderDeatilActivity.PLWQ);
					break;
				default:
					break;
				}
			}
			
		}
		private  void  ToWqActivity(String type){
			bundle = new Bundle();
			bundle.putString("wqid", data.id);
			bundle.putString("thorderid", data.from_order_id);//订单来源id
			bundle.putString(WqOrderDeatilActivity.FROM_TYPE,type);
			ActivitySwitch.openActivity(WqOrderDeatilActivity.class, bundle, (Activity)mContext);
		}
		

	    
	}



