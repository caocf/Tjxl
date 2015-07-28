/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;

import org.json.JSONObject;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.MyListAdapter.onItemClickListener;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.api.ListResponseHandler;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseListActivity;
import com.eims.tjxl_andorid.constans.Constans;
import com.eims.tjxl_andorid.entity.OrderBean;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.entity.ReplaceOrderBean;
import com.eims.tjxl_andorid.entity.ReplaceOrderBean.ReplaceItemBean;
import com.eims.tjxl_andorid.entity.ReturnGoodBean;
import com.eims.tjxl_andorid.entity.ReturnGoodBean.ReturnItemBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.entity.WqOrderBean;
import com.eims.tjxl_andorid.entity.WqOrderBean.WqOrderItemBean;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.ui.shopcart.shorder.ReplaceOrderAdapter;
import com.eims.tjxl_andorid.ui.shopcart.shorder.ReturnGoodAdapter;
import com.eims.tjxl_andorid.ui.shopcart.shorder.WqOrderAdapter;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyPopupWindow;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月30日  上午9:24:34
 *************************************************************************** 
 */
public class OrderListActivity extends BaseListActivity {
	
	protected static final String TAG = "OrderListActivity";
	protected static final String TAG1 = "OrderListActivity";
    private  HeadView head;
	private User user;
	private MyPopupWindow popupWindow;
	private List<String> mItems;
    private RelativeLayout rl_ordertype;
    private TextView typeText;
    private String orderstatus="-1"; //  -1 全部订单   (1待 付款，2待发货，4确认收货，5交易完成，6交易关闭) 
    private String return_status="-1";// 退货状态  可空，默认-1
    private OrderItemBean bean;
    private ReturnItemBean mReturnItemBean;
    private LinearLayout ll_customer;//售后商品
    private TextView orderTextType;
    //列表显示类型  0显示已买商品  1售后商品
    private static int Order_ChangesType=0;
    private int Customer_OrderType=10;//默认为10  （退货订单）  20 换货订单  30维权订单
    private RefreshOrderReciver  retrunOrderReciver;
	private String from;
	
	public static final String 	ACTION_REFRESH_LIST = "action.com.eims.refreshOrderList";
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_list_layout);
		findviews();
		user=AppContext.getLocalUserInfo(OrderListActivity.this);
		LogUtil.d(TAG, user.id);
		initActionBar();  
		initadapter();
		initview();
		initdata(Order_ChangesType);
		initUI();
		registerRefreshReceiver();
		
		retrunOrderReciver=new RefreshOrderReciver();
		IntentFilter filter=new IntentFilter(ApplyforWqActivity.RETURN_ORDERCODE_ACTION);
		filter.addAction(ApplyforWqActivity.RETURN_ORDERCODE_ACTION);
		filter.addAction(ApplyforWqActivity.REPLACE_ORDER_ACTION);
		filter.addAction(WqOrderDeatilActivity.WQ_REFRESH);
		registerReceiver(retrunOrderReciver, filter);
	};
	
	private  void  initUI(){
		if(getIntent().getExtras()!=null){
			Bundle bundle = getIntent().getExtras();
			from = bundle.getString("from");
			loadData();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		loadData();
	}
	
	private void loadData(){
//		-1全部订单   (1待 付款，2待发货，4确认收货，5交易完成，6交易关闭) 
				if("1".equals(from)){//代付款
					orderstatus="1";
					Order_ChangesType=0;
					typeText.setText(mItems.get(1));
					initadapter();
					loadData(1);
				}else if("2".equals(from)){//待发货
					orderstatus="2";
					Order_ChangesType=0;
					typeText.setText(mItems.get(2));
					loadData(1);
				}else if("3".equals(from)){//代收货
					orderstatus="4";
					Order_ChangesType=0;
					typeText.setText(mItems.get(3));
				    loadData(1);
				}else if("4".equals(from)){//待评价
					orderstatus="5";
					Order_ChangesType=0;
					typeText.setText(mItems.get(4));
					loadData(1);
				}else if("5".equals(from)){//售后
					Order_ChangesType=1;
					initdata(Order_ChangesType);
					initadapter();
					typeText.setText(mItems.get(0));
					loadData(1);
				}else if("-1".equals(from)){//全部订单
					loadData(1);
				}
	}
	
	private  void  findviews(){
		head = (HeadView) findViewById(R.id.head);
		rl_ordertype=(RelativeLayout) findViewById(R.id.ll_ordertype);
		typeText=(TextView) findViewById(R.id.order_type);
		ll_customer=(LinearLayout) findViewById(R.id.Customer_service);
		orderTextType=(TextView) findViewById(R.id.order_type_changs);
	}
	private void initview(){
        initXListView();
        setListener();
    	mXListView.setDivider(null);
        mXListView.setDividerHeight(0);
	}
	
	 private void  initadapter(){
         if(0==Order_ChangesType){
        	 mAdapter=new OrderListAdapter(OrderListActivity.this,user.id,user.username);
         }else if(1==Order_ChangesType){
        	 initCustomerAdapter();
         }
         
     }
     
     private void initActionBar() { 
    	 head.setText("已买商品");
 		 head.setRightResource();
 	}
     
     private  void  initdata(int type){
    	 	mItems = new ArrayList<String>();
    	 	mItems.clear();
    	 	if(0==type){
    	 		mItems.add("全部订单");
        		mItems.add("待付款订单");
        		mItems.add("待发货订单");
        		mItems.add("待收货订单");
        		mItems.add("待评价订单");
        		orderTextType.setText("售后商品");
    	 	}else if(1==type){
        		mItems.add("退货中订单");
    	 		mItems.add("换货中订单");
        		mItems.add("维权中订单");
        		orderTextType.setText("已买商品");
    	 	}
    		typeText.setText(mItems.get(0));
     }
     
    private void setListener(){
    	rl_ordertype.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				showOrderTypePop();
			}
		});

	//	LogUtil.d(TAG, "Order_ChangesType---"+Order_ChangesType);
		mXListView.setOnItemClickListener(new OnItemClickListener() {
			private Bundle bundle;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Bundle bundle = new Bundle();
				String orderId = null;
				if(Order_ChangesType==0){
					bean = (OrderItemBean) arg0.getAdapter().getItem(arg2);
					LogUtil.d(TAG, "点击Item："+bean.id);
					bundle = new Bundle();
					bundle.putString("order_id", bean.id);
					orderId =  bean.id;
//					if(bean.status.equals("1") || bean.status.equals("4") || bean.status.equals("5")){//取消订单，确定收货收货,评价订单
					OrderItemView orderItemView = (OrderItemView) arg1.findViewById(R.id.orderElectricItemView);
					int position = orderItemView.getPosition();														//之后删除该位置的数据，刷新列表,由于所有种类共用一个ListView,所以传递position时不用区分状态
					bundle.putInt("position", orderItemView.getPosition());//当statue为5时，传入的arg2有问题，点击第一个item时,arg2却等于1
					Log.d("zhiheng","Listview clicked order id = "+orderId+", position = "+arg2);
					bundle.putString("order_id", orderId);
					ActivitySwitch.openActivity(OrderDetailActivity.class, bundle, OrderListActivity.this);
				}
				
			}
		});
    	
    	
    	
    	ll_customer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ChangesUI();
			}
		});

    }
    
    private void  ChangesUI(){
		if(Order_ChangesType==0){
			//点击售后商品, 显示售后商品列表，即数据
			 head.setText("售后商品");
			 Order_ChangesType=1;
		}else if(1==Order_ChangesType){
			//点击已买商品显示 全部订单列表（已买商品）
			 head.setText("已买商品");
			 Order_ChangesType=0;			
		}
		Customer_OrderType=10;
		initdata(Order_ChangesType);
		initadapter();
		loadData(1);
	
    }
    
    private void  showOrderTypePop(){
    	popupWindow = new MyPopupWindow(this, rl_ordertype.getWidth(),
				mItems);
		popupWindow.showAsDropDown(rl_ordertype, 0, 0);
		popupWindow.setOnItemClickListener(new onItemClickListener() {
			@Override
			public void click(int position, View view) {
				typeText.setText(mItems.get(position));
				if(0==Order_ChangesType){
				if("全部订单".equals(mItems.get(position))){
					orderstatus="-1";
					loadData(1);
				}else  if("待付款订单".equals(mItems.get(position))){
					orderstatus="1";
					loadData(1);
				}else  if("待发货订单".equals(mItems.get(position))){
					orderstatus="2";
					loadData(1);
				}else  if("待收货订单".equals(mItems.get(position))){
					orderstatus="4";
					loadData(1);
				}else  if("待评价订单".equals(mItems.get(position))){
					orderstatus="5";
					loadData(1);
				}
			}else if(1==Order_ChangesType){
				
				if("退货中订单".equals(mItems.get(position))){
					Customer_OrderType=10;
					loadData(1);
				}else if("换货中订单".equals(mItems.get(position))){
					Customer_OrderType=20;
					loadData(1);
				}else if("维权中订单".equals(mItems.get(position))){
					Customer_OrderType=30;
					loadData(1);
				}
		     	initCustomerAdapter();
			}
		}
	});
    }

     @Override
     protected void loadData(int loadType) {
     	
     	if (loadType == 1) {     
 		 	mPageindex = 1;	
 			mAdapter.deleteItem();
 		} else {
 			mPageindex += 1;
 		}
     	if(Order_ChangesType==0){
     		loadOrderAll(loadType);
     	}else if(Order_ChangesType==1){
            if(Customer_OrderType==10){
            	loadCustomer(loadType);
            }else  if(Customer_OrderType==20){//加载换货订单
            	loadReplaceOrder(loadType);
            }else if(Customer_OrderType==30){
            	//加载维权订单
            	loadActivistOrder(loadType);
            }
     	
     	}
     
     	
     }
     
     private void initCustomerAdapter(){
    	 if(Customer_OrderType==10){
    		 mAdapter=new ReturnGoodAdapter(OrderListActivity.this, user.id);
    	 }else if(Customer_OrderType==20){
    		 mAdapter=new ReplaceOrderAdapter(OrderListActivity.this, user.id);
    	 }else if(Customer_OrderType==30){
    		 mAdapter=new WqOrderAdapter(OrderListActivity.this, user.id);
    	 }
     }
     
    //付款成功回调,更新订单状态
    public void PayMentSuccess(){
    	LogUtil.d(TAG, "支付宝付款成功，回调方法");
        loadData(1);   
    }
    
    private void loadOrderAll(int loadType){
    	LogUtil.d(TAG, "loadOrderAll------------->>>>>>>>>>"+orderstatus);
    	ListResponseHandler mHandler=new ListResponseHandler(mXListLayout, loadType){
     		@Override
     		public void onRefreshData(String content, int loadType) {
     			// TODO Auto-generated method stub
     			super.onRefreshData(content, loadType);
     			try {
						JSONObject object=new JSONObject(content);
						String type = object.getString("type");
						Log.d("zhiheng", "loadOrderAll onRefreshData result is :"+content);
						Log.d("zhiheng", "mAdapter get count =  "+mAdapter.getCount()+", get list size = "+mAdapter.getList().size());
						if("1".equals(type)&&content.contains("data")){		
							LogUtil.d(TAG, content);
						    	OrderBean orderBean=GsonUtils.json2bean(content, OrderBean.class);
								mXListView.setAdapter(mAdapter);
								mAdapter.addItem(orderBean.data);	
								mXListView.setSelection(mAdapter.mListData.size()-orderBean.data.size());
								if(orderBean.data.size()<10){
									mXListView.setPullLoadEnable(false);
								}else{
									mXListView.setPullLoadEnable(true);
								}
								
						}else{
							mAdapter.deleteItem();
						}
					  
					} catch (Exception e) {
						e.printStackTrace();
						Log.d("zhiheng", "loadOrderAll onRefreshData Exception e  =  "+e);
					}
     		}
 
     		@Override
     		public void onFailure(Throwable error, String content) {
     			// TODO Auto-generated method stub
     			super.onFailure(error, content);
     			TipToast.makeText(mContext, "数据加载失败", 0).show();
     		}
     	};
     	RequestParams params=new RequestParams();
     	params.put("uid", user.id);
     	params.put("status",orderstatus);
     	params.put("curPage", String.valueOf(mPageindex));
     	params.put("pageSize",String.valueOf(Constans.PAGESIZE));
     	HttpClient.query_order(mHandler, params);
    }
    
    /*** 
     * @author zd
     * @version   
     *   加载售后退货订单
     */
    private void  loadCustomer(int type){
     	ListResponseHandler mHandler=new ListResponseHandler(mXListLayout, type){
     		@Override
     		public void onRefreshData(String content, int loadType) {
     			// TODO Auto-generated method stub
     			super.onRefreshData(content, loadType);
     			LogUtil.d(TAG1, "售后订单："+content);
     			ReturnGoodBean returnGoodBean = GsonUtils.json2bean(content, ReturnGoodBean.class);
     		//	LogUtil.d(TAG, "售后订单数量："+returnGoodBean.data.size());
				mXListView.setAdapter(mAdapter);
			    mAdapter.addItem(returnGoodBean.data);
			    mXListView.setSelection(mAdapter.mListData.size()-returnGoodBean.data.size());
			    if(returnGoodBean.data.size()<10){
			    	mXListView.setPullLoadEnable(false);
			    }else{
			    	mXListView.setPullLoadEnable(true);
			    }
     		}
 
     		@Override
     		public void onFailure(Throwable error, String content) {
     			// TODO Auto-generated method stub
     			super.onFailure(error, content);
     			TipToast.makeText(mContext, "数据加载失败", 0).show();
     		}
     	};
     	RequestParams params=new RequestParams();
     	params.put("uid", user.id);
     	params.put("userType", user.member_type);
     	params.put("return_statu",return_status);//退货状态  可空，默认-1
     	params.put("curPage", String.valueOf(mPageindex));
     	params.put("pageSize",String.valueOf(Constans.PAGESIZE));
     	HttpClient.QueryReturn(mHandler, params);
    }

    
    /*** 
     * @author zd
     * @version   
     *   加载售后换货订单
     */
    private void  loadReplaceOrder(int type){
     	ListResponseHandler mHandler=new ListResponseHandler(mXListLayout, type){
     		@Override
     		public void onRefreshData(String content, int loadType) {
     			// TODO Auto-generated method stub
     			super.onRefreshData(content, loadType);
     			LogUtil.d(TAG1, "售后换货订单："+content);
			   ReplaceOrderBean replaceOrderBean=GsonUtils.json2bean(content, ReplaceOrderBean.class);
			   mXListView.setAdapter(mAdapter);
			   mAdapter.addItem(replaceOrderBean.data);
			    mXListView.setSelection(mAdapter.mListData.size()-replaceOrderBean.data.size());
				if(replaceOrderBean.data.size()<10){
					mXListView.setPullLoadEnable(false);
				}else{
					mXListView.setPullLoadEnable(true);
				}
     		}
 
     		@Override
     		public void onFailure(Throwable error, String content) {
     			// TODO Auto-generated method stub
     			super.onFailure(error, content);
     			TipToast.makeText(mContext, "数据加载失败", 0).show();
     		}
     	};
     	RequestParams params=new RequestParams();
     	params.put("uid", user.id);
     	params.put("curPage", String.valueOf(mPageindex));
     	params.put("pageSize",String.valueOf(Constans.PAGESIZE));
     	HttpClient.QueryReplaceOrder(mHandler, params);
    }
    
    
    /*** 
     * @author zd
     * @version   
     *   加载售后维权订单
     */
    private void  loadActivistOrder(int type){
     	ListResponseHandler mHandler=new ListResponseHandler(mXListLayout, type){
     		@Override
     		public void onRefreshData(String content, int loadType) {
     			// TODO Auto-generated method stub
     			super.onRefreshData(content, loadType);
     			LogUtil.d(TAG1, "售后维权订单："+content);
//			   ReplaceOrderBean replaceOrderBean=GsonUtils.json2bean(content, ReplaceOrderBean.class);

     			WqOrderBean orderBean=GsonUtils.json2bean(content, WqOrderBean.class);
 			   mXListView.setAdapter(mAdapter);
 			   mAdapter.addItem(orderBean.data);
 			   mXListView.setSelection(mAdapter.mListData.size()-orderBean.data.size());
				if(orderBean.data.size()<10){
					mXListView.setPullLoadEnable(false);
				}else{
					mXListView.setPullLoadEnable(true);
				}
     		}
 
     		@Override
     		public void onFailure(Throwable error, String content) {
     			// TODO Auto-generated method stub
     			super.onFailure(error, content);
     			TipToast.makeText(mContext, "数据加载失败", 0).show();
     		}
     	};
     	RequestParams params=new RequestParams();
     	params.put("uid", user.id);
     	params.put("curPage", String.valueOf(mPageindex));
     	params.put("pageSize",String.valueOf(Constans.PAGESIZE));
     	HttpClient.QueryWqOrderList(mHandler, params);
    }
    
  /**刷新退货列表广播接收者**/
  public   class RefreshOrderReciver extends BroadcastReceiver{
    	@Override
    	public void onReceive(Context arg0, Intent intent) {
    		String action=intent.getAction();
    		if(ApplyforWqActivity.RETURN_ORDERCODE_ACTION.equals(action)){
    			LogUtil.d(TAG, "刷新退货列表");
    			Order_ChangesType=1;
    			Customer_OrderType=10;
    			loadData(1);
    		}else if(ApplyforWqActivity.REPLACE_ORDER_ACTION.equals(action)){
    			LogUtil.d(TAG, "刷新换货货维权列表");
    			Order_ChangesType=1;
    			Customer_OrderType=20;
    			loadData(1);
    		}else if(WqOrderDeatilActivity.WQ_REFRESH.equals(action)){
    			LogUtil.d(TAG, "刷新维权列表");
    			Order_ChangesType=1;
    			Customer_OrderType=30;
    			loadData(1);
    		}
    	}
    	
    }
    

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	Order_ChangesType=0;
    	Customer_OrderType=10;
    	mAdapter.deleteItem();
    	mAdapter=null;
        unregisterReceiver(retrunOrderReciver);
        unregisterReceiver(refreshListBroadcastReceiver);
    }
    
    /**
     * 注册刷新订单列表的Receiver
     */
    private void registerRefreshReceiver(){
    	IntentFilter filter =new IntentFilter();
    	filter.addAction(ACTION_REFRESH_LIST);
    	registerReceiver(refreshListBroadcastReceiver, filter);
    }
   
    BroadcastReceiver refreshListBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Log.d(TAG, "refreshListBroadcastReceiver onReceiver:deleteItem");
			int position = arg1.getIntExtra("position", -1);
			if(position >= 0){
				mAdapter.getList().remove(position);
			}
			mAdapter.notifyDataSetChanged();
//			from = arg1.getStringExtra("from");
//			mAdapter.deleteItem();//如果是重新加载数据的话这一句代码一定要执行，且在loadData()之前
//			loadData();
			Log.d(TAG, "refreshListBroadcastReceiver onReceiver: position = "+position);
		}
	};

}
