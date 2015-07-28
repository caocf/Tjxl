/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;
import org.json.JSONObject;

import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.alipay.PayUtils;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.dialog.DeleteCollectionDialog;
import com.eims.tjxl_andorid.dialog.UserSHDialog;
import com.eims.tjxl_andorid.dialog.UserSHDialog.OnSHClickListener;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.pay.wx.PayUtil;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.utils.imageupload.TipsToast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 订单列表  条目封装
 * @Author zd
 * @date 2015年7月1日  上午10:12:55
 *************************************************************************** 
 */
public class OrderItemView extends  LinearLayout{

    private Context mContext;
    private TextView order_item_shoename;
    private TextView order_Status;
    private ImageView order_item_shoeImages;
    private TextView order_no;
    private TextView order_money;
    private TextView order_addtime;
    private TextView order_item_numer;
    private Button btn_cancel_order,btn_gopay;
    private Button btn_showlwuliu,btn_OrderOk;
    private Button btn_shouhou,btn_Ordercomment;
    private OrderItemBean data;
    private String uid;
    private String username;
    private int position;//该View在ListView中的位置
    private UserSHDialog mShDialog;
	public OrderItemView(Context context) {
		super(context);
		this.mContext=context;
		LayoutInflater.from(context).inflate(R.layout.order_item_view, this, true);
		findView();
		setListener();
	}

	public OrderItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		LayoutInflater.from(context).inflate(R.layout.order_item_view, this, true);
		findView();
		setListener();
	}
	
	
	
	private void findView() {
		order_item_shoename=(TextView)findViewById(R.id.order_item_shoename);
		order_Status=(TextView) findViewById(R.id.order_item_status);
		order_item_shoeImages=(ImageView) findViewById(R.id.order_shoeImages);
		order_no=(TextView) findViewById(R.id.order_item_no);
		order_money=(TextView) findViewById(R.id.order_item_money);
		order_addtime=(TextView) findViewById(R.id.order_item_addtime);
		order_item_numer=(TextView) findViewById(R.id.order_item_numner);
		btn_cancel_order=(Button) findViewById(R.id.order_cancel);
		btn_gopay=(Button) findViewById(R.id.order_gopay);
		btn_showlwuliu=(Button) findViewById(R.id.order_showwuliu);
		btn_OrderOk=(Button) findViewById(R.id.order_shouhuo);
		btn_shouhou=(Button) findViewById(R.id.order_shouhou);//售后
		btn_Ordercomment=(Button) findViewById(R.id.order_gocomment);
	}
	public void setUid(String uid){
		this.uid=uid;
	}
	
	public void setUsername(String username){
		this.username=username;
	}
	public void setData(OrderItemBean data){
		this.data = data;
		refreshItemView();
	}
	
	public void refreshItemView() {
	        order_item_shoename.setText(data.f_factory_name);
			order_Status.setText(data.status_name);
			ImageManager.Load(data.commodity_img_m, order_item_shoeImages);
			order_no.setText(data.order_code);
			order_money.setText(data.total_price);
			order_addtime.setText(data.addtime);
			order_item_numer.setText(data.quantity+"双");
		    updataButtom();
		
	}

	private void updataButtom() {
		//(1待 付款，2待发货，4确认收货，5交易完成，6交易关闭) 
		btn_cancel_order.setVisibility(View.GONE);
		btn_gopay.setVisibility(View.GONE);
		btn_showlwuliu.setVisibility(View.GONE);
		btn_OrderOk.setVisibility(View.GONE);
		btn_shouhou.setVisibility(View.GONE);
		btn_Ordercomment.setVisibility(View.GONE);
		if("1".equals(data.status)){//代付款  
			btn_cancel_order.setVisibility(View.VISIBLE);
			btn_gopay.setVisibility(View.VISIBLE);
		}else if("2".equals(data.status)){
			btn_cancel_order.setVisibility(View.GONE);
			btn_gopay.setVisibility(View.GONE);
			btn_showlwuliu.setVisibility(View.GONE);
			btn_OrderOk.setVisibility(View.GONE);
			btn_shouhou.setVisibility(View.GONE);
			btn_Ordercomment.setVisibility(View.GONE);
		}else if("4".equals(data.status)){//确认收货
			btn_showlwuliu.setVisibility(View.VISIBLE);
			btn_OrderOk.setVisibility(View.VISIBLE);
		}else if("5".equals(data.status)){
			btn_shouhou.setVisibility(View.VISIBLE);
			btn_Ordercomment.setVisibility(View.VISIBLE);
			if(data.comments_status.equals("1")){
				btn_Ordercomment.setText("已评价");
				btn_Ordercomment.setTextColor(mContext.getResources().getColor(R.color.white));
				btn_Ordercomment.setBackgroundResource(R.drawable.red_btn_click);
			}else if(data.comments_status.equals("0")){
				btn_Ordercomment.setText("去评价");
				btn_Ordercomment.setTextColor(mContext.getResources().getColor(R.color.white));
				btn_Ordercomment.setBackgroundResource(R.drawable.red_btn_click);
			}
		}else if("6".equals(data.status)){
			btn_cancel_order.setVisibility(View.GONE);
			btn_gopay.setVisibility(View.GONE);
			btn_showlwuliu.setVisibility(View.GONE);
			btn_OrderOk.setVisibility(View.GONE);
			btn_shouhou.setVisibility(View.GONE);
			btn_Ordercomment.setVisibility(View.GONE);
		}
	}
	
	private  void  setListener(){
		btn_cancel_order.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				CancelOrder();
				final DeleteCollectionDialog dialog = new DeleteCollectionDialog(mContext);
				dialog.setMsg("您确定要取消该订单吗？");
				dialog.setOnclickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						if(arg0.getId() == R.id.button_cancel){
							dialog.dismiss();
						}else if(arg0.getId() == R.id.button_ok){
							CancelOrder();
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
		});
		btn_gopay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				goPay();
				goPayWX();
			}
		});
		
		btn_showlwuliu.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle=new Bundle();
				bundle.putString("logistics_no", data.logistics_no);
				bundle.putString("logistics_en", data.logistics_en);
				ActivitySwitch.openActivity(OrderWuliuQueryActivity.class, bundle, (Activity)mContext);
			}
		});
		btn_OrderOk.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mShDialog=new UserSHDialog(mContext, R.style.load_dialog,username);
			    mShDialog.setClickListener(new OnSHClickListener() {			
					@Override
					public void onCliclkSHListener(String content) {
						try {
							JSONObject jsonObject=new JSONObject(content);
							String type = jsonObject.getString("type");
							if("-1".equals(type)){
								TipToast.makeText(mContext, "密码输入错误", 0).show();
							}else{
								OrderOk();
								mShDialog.dismiss();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				mShDialog.show(); 
			}
			
		});
		btn_Ordercomment.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				if(data.comments_status.equals("0")){
					Bundle bundle = new Bundle();
					bundle.putString("order_id", data.id);
					bundle.putInt("position", position);
					ActivitySwitch.openActivity(OrderAssessmentActivity.class, bundle, (Activity)mContext);
				}
			}
		});
		
		btn_shouhou.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putInt(ApplyAfterSaleServiceActivity.KEY_EXTRA_FROM, ApplyAfterSaleServiceActivity.KEY_EXTRA_FROM_LIST);
				bundle.putSerializable(ApplyAfterSaleServiceActivity.KEY_EXTRA_LIST_ITEM, data);
				ActivitySwitch.openActivity(ApplyAfterSaleServiceActivity.class, bundle, (Activity)mContext);
			}
		});
	}
	
    /**取消订单
     * @param  **/
    private  void  CancelOrder(){
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, false, ""){
			@Override
    		public void onSuccess(int statusCode, String content) {
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, content);
    			LogUtil.d(TAG, content);
//                data.status="6";
//                data.status_name="交易关闭";
                refreshItemView();
    			//发送广播刷新待付款订单列表
    			Intent intent = new Intent(OrderListActivity.ACTION_REFRESH_LIST);
    			intent.putExtra("from", "1");
    			intent.putExtra("position", position);
    			mContext.sendBroadcast(intent);
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("uid", uid);
    	params.put("id", data.id);
    	HttpClient.Order_Cancel(handler, params);
    }
    
    //微信支付
    private  void goPayWX(){
    	PayUtil payUtil = new PayUtil(mContext);
    	payUtil.pay();
    }
    //去付款
    private void  goPay(){
    	/*
    	 * 支付宝支付
    	 */
//    	PayUtils pay=new PayUtils((Activity)mContext,"2");
//    	pay.pay();
    	
    	/*
    	 * 微信支付
    	 */
//    	PayUtil payUtil = new PayUtil(mContext, data.order_code, "订单 body", data.total_price);
//    	payUtil.Pay();
    }
	
    /**确认收货**/
    private  void  OrderOk(){
    
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, false, ""){
			@Override
    		public void onSuccess(int statusCode, String content) {
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, content);
    			LogUtil.d(TAG, content);
    			data.status="5";
    		    data.status_name="交易完成";
//                  refreshItemView();
    		    Intent intent = new Intent(OrderListActivity.ACTION_REFRESH_LIST);
    		    intent.putExtra("position", position);
    		    mContext.sendBroadcast(intent);
    		//	data
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("uid", uid);
    	params.put("id", data.id);
    	HttpClient.Order_Result_Okl(handler, params);
    }

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
