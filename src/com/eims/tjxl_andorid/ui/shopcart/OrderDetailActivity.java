package com.eims.tjxl_andorid.ui.shopcart;

import loopj.android.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony.Sms.Conversations;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;

import android.widget.ScrollView;

import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.alipay.PayUtils;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.dialog.DeleteCollectionDialog;
import com.eims.tjxl_andorid.entity.OrderBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodSizeBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.pay.wx.PayUtil;
import com.eims.tjxl_andorid.ui.product.FactoryActivity;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.imageupload.TipsToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月30日  下午4:16:33
 *************************************************************************** 
 */
public class OrderDetailActivity extends BaseActivity implements OnClickListener{
	private static final String TAG = "OrderDetailActivity";
	private HeadView head;
	private User user;
	private String orderId;
	private Bundle bundle;
	private OrderDetailBean detailBean;
	private TextView order_status;//订单状态
	private TextView order_username_phone;//收货人姓名和电话
	private TextView order_useradress;//收货人地址
	private TextView factory_name;//供应商
	private Button btn_lianxifactory;//联系卖家
	private Button btn_gofactory;//进入店铺
	private Button btn_left;//底部左边按钮
	private Button btn_right;//底部右边按钮
	private Button btn_contact_seller,btn_to_store;
	private TextView order_no;//单号
	private TextView order_addtime;//下单时间
	private TextView user_remarks;//买家留言
	private TextView totoal_money;//应付额
	private MyListView  myListView;
	private GoodAdapter mAdapter;
	private LinearLayout ll_btn;
	private int position;//用于记录该订单详情在订单列表里面的位置,用于发广播更新列表
    private TextView orderYf;//订单运费
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_detail_layout);
		findviews();
		initActionBar();
		user=AppContext.getLocalUserInfo(OrderDetailActivity.this);
		//test
		if(user == null){
			user = new User();
			user.id = "1";
		}
		bundle = getIntent().getExtras();
		orderId=bundle.getString("order_id");
		position = bundle.getInt("position",-1);//跳转时可不传该参数
		Log.d("zhiheng", "onCreate orderId = "+orderId+", position = "+position);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		loaddata();//再次获取数据，以便评价获或其他操作结束后再次刷新数据
	}
	
	private void findviews(){
		head=(HeadView) findViewById(R.id.head);
		order_status=(TextView) findViewById(R.id.order_status);
		order_username_phone=(TextView) findViewById(R.id.order_username_phone);
		order_useradress=(TextView) findViewById(R.id.order_adress);
		factory_name=(TextView) findViewById(R.id.factory_name);
		order_no=(TextView) findViewById(R.id.order_no);
		order_addtime=(TextView) findViewById(R.id.order_addtime);
		user_remarks=(TextView) findViewById(R.id.remarks);
		totoal_money=(TextView) findViewById(R.id.total_om);
		myListView=(MyListView) findViewById(R.id.order_detail_listview);
		btn_left=(Button) findViewById(R.id.btn_left);
		btn_right=(Button) findViewById(R.id.btn_right);
		ll_btn=(LinearLayout) findViewById(R.id.ll_btn);
		btn_contact_seller = (Button) findViewById(R.id.btn_contact_seller);
		btn_to_store = (Button) findViewById(R.id.btn_to_store);
		orderYf=(TextView) findViewById(R.id.order_yunfei);
	}
	
    private void initActionBar() { 
		head.setText("订单详情");
		head.setRightResource();
	}
    
	@Override
	public void onClick(View arg0) {
		Bundle bundle = null;
		switch (arg0.getId()) {
		case R.id.btn_contact_seller:
			break;
		case R.id.btn_to_store:
			bundle = new Bundle();
			bundle.putString(FactoryActivity.ID, detailBean.paMap.seller_id);
			ActivitySwitch.openActivity(FactoryActivity.class, bundle, this);
			
			break;

		default:
			break;
		}
	}
    
    private void  updataButtom(String status){
    	ll_btn.setVisibility(View.VISIBLE);
    	if("1".equals(status)){
    		btn_left.setText("取消订单");
    		btn_right.setText("去付款");
    		btn_left.setTag("1");
    		btn_right.setTag("1");
    	}else if("2".equals(status)){//代发货
    		ll_btn.setVisibility(View.GONE);
    	}else if("4".equals(status)){//确认收货
    		btn_left.setText("查看物流");
		    btn_right.setText("确认收货");
		    btn_left.setTag("4");
    		btn_right.setTag("4");
		}else if("5".equals(status)){//交易完成
			    btn_left.setText("申请售后");
			    //判断订单评价状态
			    if(detailBean.paMap.comments_status.equals("1")){
			    	btn_right.setText("已评价");
			    	btn_right.setTextColor(mContext.getResources().getColor(R.color.black));
			    	btn_right.setBackgroundResource(R.drawable.comm_broder_line);
			    	ll_btn.setVisibility(View.GONE);
				}else if(detailBean.paMap.comments_status.equals("0")){
					btn_right.setText("去评价");
					btn_right.setTextColor(mContext.getResources().getColor(R.color.white));
					btn_right.setBackgroundResource(R.drawable.red_btn_click);
				}
				btn_left.setTag("5");
	    		btn_right.setTag("5");
		}else if("6".equals(status)){
			ll_btn.setVisibility(View.GONE);
		}
    	
    	btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String key=(String) btn_left.getTag();
				Log.d(TAG, "OrderDetail cancel order clicked");
				if("1".equals(key)){
//					CancelOrder();
					Log.d(TAG, "OrderDetail cancel order clicked and show dialog");
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
				}else if("4".equals(key)){
					Bundle bundle=new Bundle();
					bundle.putString("logistics_no", detailBean.paMap.logistics_no);
					bundle.putString("logistics_en", detailBean.paMap.logistics_en);
					ActivitySwitch.openActivity(OrderWuliuQueryActivity.class, bundle, OrderDetailActivity.this);
				}else if("5".equals(key)){
//					TipsToast.makeText(mContext, "申请售后", 0).show();
					Bundle bundle = new Bundle();
					bundle.putInt(ApplyAfterSaleServiceActivity.KEY_EXTRA_FROM, ApplyAfterSaleServiceActivity.KEY_EXTRA_FROM_DETAIL);
					bundle.putSerializable(ApplyAfterSaleServiceActivity.KEY_EXTRA_ORDER,detailBean);
					ActivitySwitch.openActivity(ApplyAfterSaleServiceActivity.class, bundle, OrderDetailActivity.this);
				}
			}
		});
    	
    	btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String key=(String) btn_right.getTag();
				if("1".equals(key)){
					//TipsToast.makeText(mContext, "去付款", 0).show();
				 goPay();
				}else if("4".equals(key)){
					//TipsToast.makeText(mContext, "确认收货", 0).show();
					OrderOk();
				}else if("5".equals(key)){
					if(detailBean.paMap.comments_status.equals("0")){
						Bundle bundle = new Bundle();
						bundle.putString("order_id", detailBean.paMap.id);
						bundle.putInt("position", position);
						Log.d("zhiheng", "OrderDetailActivity statue is 5,position = "+position);
						ActivitySwitch.openActivity(OrderAssessmentActivity.class, bundle, OrderDetailActivity.this);
					}
				}
			}
		});
    	
    	btn_contact_seller.setOnClickListener(this);
    	btn_to_store.setOnClickListener(this);
    	
    }
    /**取消订单**/
    private  void  CancelOrder(){
    	if(!available){
    		TipsToast.makeText(mContext, "亲，你的网络有点问题哦", 0).show();
    		return;
    	}
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在加载"){
			@Override
    		public void onSuccess(int statusCode, String content) {
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, content);
    			LogUtil.d(TAG, content);
    			detailBean.paMap.status="6";
    			detailBean.paMap.status_name="交易关闭";
    			showUI();
    			//发送广播刷新待付款订单列表
    			Intent intent = new Intent(OrderListActivity.ACTION_REFRESH_LIST);
    			intent.putExtra("from", "1");
    			intent.putExtra("position", position);
    			OrderDetailActivity.this.sendBroadcast(intent);
    			Log.d("zhiheng","has send broadcast...");
    			//隐藏 按钮
    	    	//ll_btn.setVisibility(View.GONE);
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("uid", user.id);
    	params.put("id", orderId);
    	HttpClient.Order_Cancel(handler, params);
    }
    /**确认收货**/
    private  void  OrderOk(){
    	if(!available){
    		TipsToast.makeText(mContext, "亲，你的网络有点问题哦", 0).show();
    		return;
    	}
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, false, ""){
			@Override
    		public void onSuccess(int statusCode, String content) {
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, content);
    			LogUtil.d(TAG, content);
    			detailBean.paMap.status="5";
    			detailBean.paMap.status_name="交易完成";
    			showUI();
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("uid", user.id);
    	params.put("id", orderId);
    	HttpClient.Order_Result_Okl(handler, params);
    }
    //去付款
    private void  goPay(){
    	/*
    	 * 支付宝支付
    	 */
//    	PayUtils pay=new PayUtils(OrderDetailActivity.this,"2");
//    	pay.pay();
    	
    	/*
    	 * 微信支付
    	 */
//    	PayUtil payUtil = new PayUtil(this, detailBean.paMap.order_code, "订单 body", detailBean.paMap.total_price);
    	PayUtil payUtil = new PayUtil(this);
    	payUtil.pay();
    }
    //付款成功回调,更新订单状态
    public void PayMentSuccess(){
    	LogUtil.d(TAG, "支付宝付款成功，回调方法");
    //	String key=(String) btn_right.getTag();
    	detailBean.paMap.status="2";//支付成功 ---- 代发货
    	detailBean.paMap.status_name="代发货";
    	showUI();
//    	if("1".equals(key)){
////    		btn_right.setText("确认收货");
////    		btn_left.setText("查看物流");
////    		btn_right.setTag("2");
////    		btn_left.setTag("2");
//    		loaddata();
//    	}
    }
    
    private void  loaddata(){
    	if(!available){
    		TipsToast.makeText(mContext, "亲，你的网络有点问题哦", 0).show();
    		return;
    	}
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在加载"){
			@Override
    		public void onSuccess(int statusCode, String content) {
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, content);
    			LogUtil.d(TAG, content);
    			detailBean = GsonUtils.json2bean(content, OrderDetailBean.class);
    			if(detailBean != null && detailBean.paMap != null ) detailBean.paMap.id = orderId;
    			LogUtil.d(TAG, "运费："+detailBean.paMap.logistics_fare);
    			showUI();
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("uid", user.id);
    	params.put("id", orderId);
    	HttpClient.query_order_info(handler, params);
    }
    
    private void  showUI(){
    	if(detailBean!=null){
    		order_status.setText(detailBean.paMap.status_name);
    		order_username_phone.setText(detailBean.paMap.receipt_user+"      "+detailBean.paMap.receipt_mobile);
    		order_useradress.setText(detailBean.paMap.receipt_address);
    		totoal_money.setText("￥"+detailBean.paMap.total_price+"元");
    		order_no.setText(detailBean.paMap.order_code);
    		order_addtime.setText(detailBean.paMap.addtime);
    		user_remarks.setText(detailBean.paMap.order_remark);
    		factory_name.setText("供应商 :  "+detailBean.paMap.f_factory_name);
    		orderYf.setText("应付款(含运费￥"+detailBean.paMap.logistics_fare+"元):");
    		updataButtom(detailBean.paMap.status);
    		if(mAdapter==null){
    			mAdapter=new GoodAdapter();
    		}
    		myListView.setAdapter(mAdapter);
    	}
    }
    
    class  GoodAdapter extends BaseAdapter{


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return detailBean.data==null ? 0 :detailBean.data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return detailBean.data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

	
		@Override
		public View getView(int arg0, View covertview, ViewGroup arg2) {
	         if(covertview==null){
	        	 covertview=View.inflate(mContext, R.layout.order_detail_listview_item_layout, null);
	         }
	         ImageView image=(ImageView) covertview.findViewById(R.id.good_image);
	         TextView name= (TextView) covertview.findViewById(R.id.good_name);
	         LinearLayout ll_order_goodsize=(LinearLayout) covertview.findViewById(R.id.ll_order_goodsize);
	         final OrderGoodBean orderGoodBean = detailBean.data.get(arg0);
	         ImageManager.Load(orderGoodBean.commodity_img_m, image);
	         name.setText(orderGoodBean.commodity_name);
	         RelativeLayout root=(RelativeLayout) covertview.findViewById(R.id.root);
	         root.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					bundle=new Bundle();
					bundle.putString(ProductDeatil.INTENT_KEY, orderGoodBean.commodity_id);
					ActivitySwitch.openActivity(ProductDeatil.class, bundle, OrderDetailActivity.this);
				}
			});
	         ll_order_goodsize.removeAllViews();
	         addLayout(ll_order_goodsize,orderGoodBean);
			return covertview;
		}
    	
    }
    private void  addLayout(LinearLayout ll_order_goodsize, OrderGoodBean orderGoodBean){
    	for(OrderGoodSizeBean temp : orderGoodBean.goodList){
    		  View view=View.inflate(mContext, R.layout.orderdetail_listview_item_goodsize_layout, null);
    		  TextView sizeColor=(TextView) view.findViewById(R.id.order_good_size_color);
    		  TextView sizePrice=(TextView) view.findViewById(R.id.good_sizeprice);
    		  TextView sizeNum=(TextView) view.findViewById(R.id.good_sizenum);
    		  String[] split = temp.spec_name_value.split("，");
    		  String[] colors=split[0].split(":");
    		  String colorValue=colors[1];
    		  String[] size=split[1].split(":");
    		  String sizeValue=size[1];
    		  sizeColor.setText(colorValue+"  "+ sizeValue);
    		  sizePrice.setText("￥"+temp.commodity_price);
    		  sizeNum.setText("x"+temp.quantity);
    		  ll_order_goodsize.addView(view);
    		  
    	}
    }

}
