/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.content.Intent;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.CommintOrderBean;
import com.eims.tjxl_andorid.entity.CommintOrderBean.Good;
import com.eims.tjxl_andorid.entity.CommintOrderBean.GoodStandard;
import com.eims.tjxl_andorid.entity.CommintOrderBean.ShopBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.entity.UserAdressBean.AdressBean;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.ui.receipt.ReceiptFragment;
import com.eims.tjxl_andorid.ui.shopcart.OrderActivity.FreightBean.FreightItemBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.DashedLineView;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 填写订单
 * @Author zd
 * @date 2015年6月28日  上午11:29:21
 *************************************************************************** 
 */
public class OrderActivity  extends BaseActivity{
	
	private static final String TAG = "OrderActivity";
	private HeadView headView;
	private MyListView mListView;
	private CommintOrderBean bean;
	private CommintOrderAdapter  mAdapter;
	DecimalFormat df = new DecimalFormat("0.00");
	private TextView totalNum,totalPrice,totalyunfeiprice,AllTotalPrice;
	private TextView editor_adress;
	private TextView mu_name,mu_phone,mu_adress;
	private CheckBox cb_fapiao;
    private Button btn_commint;
	private User user;
	private String adress_id;
	private String city_id;
	private AdressBean serializable;
	String mtotalPrice;
	private boolean isInvoices=false;//是否勾选发票
	private String invoiceId;
	private LinearLayout ll_invoice;
	private String codes;
	private FreightBean freightBean;
	public static String FROM_INTENT="from";//0 代表从购物车  1代表从立即订购
	private String from;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_layout);
		findviews();
		setActionBar();
		initdata();
		setLienster();
	}
	private void  findviews(){
		headView=(HeadView) findViewById(R.id.head);
		mListView=(MyListView) findViewById(R.id.commint_order_listview);
		totalNum=(TextView) findViewById(R.id.order_good_allnum);
		totalPrice=(TextView) findViewById(R.id.order_good_allprice);
		totalyunfeiprice=(TextView) findViewById(R.id.order_good_allyunfei);
		AllTotalPrice=(TextView) findViewById(R.id.order_good_totalprice);
		editor_adress=(TextView) findViewById(R.id.editor_adress);
		mu_name=(TextView) findViewById(R.id.adress_username);
		mu_phone=(TextView) findViewById(R.id.adress_user_phone);
		mu_adress=(TextView) findViewById(R.id.adress_detail);
		cb_fapiao=(CheckBox) findViewById(R.id.cb_fapiao);
		ll_invoice=(LinearLayout) findViewById(R.id.ll_invoice);
		btn_commint=(Button) findViewById(R.id.commint_order);
	}
	
	
	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("填写订单");
		headView.setGobackVisible();
		headView.setRightGone();
	}
	private void initdata(){
		
	    user = AppContext.getLocalUserInfo(mContext);
		Bundle bundle = getIntent().getExtras();
		bean = (CommintOrderBean) bundle.getSerializable("commintorder");
		codes = bundle.getString("codes");
		from = bundle.getString(FROM_INTENT);
		LogUtil.d(TAG,bean.data.size()+"");

		int totalnum=0;
		float toprice=0.0f;
		float mtoyunfei=0;
		if(bean!=null){
	        for(ShopBean temp : bean.data){
	        	for(Good good : temp.commodityList){
	        		for(GoodStandard  standard : good.goodList){
	        			totalnum+=Integer.parseInt(standard.quantity);
	        			toprice+=Integer.parseInt(standard.quantity)*Float.parseFloat((standard.sprice));
	        			
	        		}
	        	}
	        	if(!StringUtils.isEmpty(temp.yunfei)){
	        		mtoyunfei+=Float.parseFloat(temp.yunfei);
	        	}else{
	        		mtoyunfei+=0;
	        	}
	        
	        }
	        totalNum.setText("数量总计："+totalnum+"件");
	        totalyunfeiprice.setText("运费总计：￥"+df.format(mtoyunfei)+"元");
	        totalPrice.setText("货款总计："+df.format(toprice)+"元");
//	        mtotalPrice="￥"+toprice;
	
	        if(!StringUtils.isEmpty(bean.paMap.df_address_id)){
	        	mu_name.setText(bean.paMap.consignee_name);
	        	mu_phone.setText(bean.paMap.consignee_phone);
	        	mu_adress.setText(bean.paMap.address_show);
	        	adress_id=bean.paMap.df_address_id;
	        	city_id=bean.paMap.city_id;	
	        }
	        QueryFreightListByUsers(city_id);
			if(mAdapter==null){
				mAdapter=new CommintOrderAdapter();
			}
			mListView.setAdapter(mAdapter);
	       
		}
		
	}
	
	private void  setLienster(){
		editor_adress.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(OrderActivity.this, AdressListActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		ll_invoice.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				ActivitySwitch.openActivityForResult(InvoiceActivity.class, null, OrderActivity.this, 2);
			}
		});
		cb_fapiao.setOnCheckedChangeListener(new OnCheckedChangeListener() {	
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
			        isInvoices=true;
				}else{
					isInvoices=false;
				}
			}
		});
		btn_commint.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			if(checkinfo()){
				commintOrder();
			}
			}
		});
	}
	private boolean checkinfo(){
		if(TextUtils.isEmpty(mu_adress.getText().toString())){
			TipToast.makeText(mContext, "请选择收货信息", 0).show();
			return false;
		}else if(TextUtils.isEmpty(mu_name.getText().toString())){
			TipToast.makeText(mContext, "请选择收货信息", 0).show();
			return false;
		}else if(TextUtils.isEmpty(mu_phone.getText().toString())){
			TipToast.makeText(mContext, "请选择收货信息", 0).show();
			return false;
		}else if(isInvoices){
			if(StringUtils.isEmpty(invoiceId)){
				TipToast.makeText(mContext, "请填写发票相关信息", 0).show();
				return false;
			}
		}
		return true;
	}
	//提交订单
	private void  commintOrder(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(this, true, "正在提交.."){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if(statusCode==200){
		           LogUtil.d(TAG, content);
		           try {
		        	   Intent intent=new Intent();
		        	   intent.setAction(ReceiptFragment.REFRESH_SHOPPINGCART_ACTION);
		        	   sendBroadcast(intent);
					JSONObject obj=new JSONObject(content);
					String type=obj.getString("type");
					if("1".equals(type)){
						//跳转支付界面
						Bundle  bundle=new Bundle();
						bundle.putString("mtotalPrice", freightBean.tt_money);
						bundle.putString("order_no", obj.getString("data"));
						bundle.putSerializable("adress_bean", serializable);
						ActivitySwitch.openActivity(PayMentActivity.class, bundle, OrderActivity.this);
						OrderActivity.this.finish();
					}else{
						TipToast.makeText(mContext, obj.getString("msg"), 0).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				}
			}
		};
         List<ShopBean> data = bean.data;
         StringBuilder sb=new StringBuilder();
         for(ShopBean mBean : data){
        	 sb.append(mBean.remarks+"@");
         }
		RequestParams params=new RequestParams();
		params.put("uid",user.id);
		params.put("goods_codes",bean.paMap.goods_codes);
		params.put("goods_code_qty",bean.paMap.goods_code_qty);
		params.put("delivery_remark",sb.toString());//备注
		params.put("invoices_id",invoiceId);//发票
		
		params.put("address_id",adress_id);
		params.put("city_id",city_id);
		params.put("coupon_id","");//优惠券
		params.put("payment_method","");//
		HttpClient.Commint_order(mHandler, params);
	}
	
	class CommintOrderAdapter extends BaseAdapter{
        private int num=500;
		private int selectionStart=0;
        private int selectionEnd=0;
        private CharSequence temp;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return bean.data==null ? 0 :bean.data.size();
		}

		
		@Override
		public Object getItem(int arg0) {
		
			return bean.data.get(arg0);
		}

	
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}


		@Override
		public View getView(int arg0, View convertview, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertview==null){
				convertview=View.inflate(mContext, R.layout.commint_order_item_layout, null);
			}
			TextView orderShoeName=(TextView) convertview.findViewById(R.id.order_shoe_name);
			TextView totalPrice=(TextView) convertview.findViewById(R.id.order_total_price);
			TextView totalyunfeiPrice=(TextView) convertview.findViewById(R.id.order_yunfei_price);
			final EditText remarks=(EditText) convertview.findViewById(R.id.remarks);
			final TextView mtextNumber =(TextView) convertview.findViewById(R.id.text_number);
			final ShopBean shopBean = bean.data.get(arg0);
			orderShoeName.setText(shopBean.f_factory_name);
			totalyunfeiPrice.setText("￥ "+shopBean.yunfei+"元");
			float sprice=0.0f;
			for(Good info :shopBean.commodityList){
				for(GoodStandard temp :info.goodList){
					sprice+=Integer.parseInt(temp.quantity)*Float.parseFloat(temp.sprice);
				}
			}
			totalPrice.setText("￥"+df.format(sprice)+"元");
		
			remarks.addTextChangedListener(new TextWatcher() {		
				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
					shopBean.remarks=s.toString();
					temp=s;
				}
				
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
						int arg3) {
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
				//	int number=num-arg0.length();
					mtextNumber.setText(""+arg0.length());
					selectionStart=remarks.getSelectionStart();
					selectionEnd=remarks.getSelectionEnd();
					if(temp.length()>num){
		            	  arg0.delete(selectionStart - 1, selectionEnd);
		                  int tempSelection = selectionStart;
		                  remarks.setText(arg0);
		                  remarks.setSelection(tempSelection);//设置光标在最后
		}
				}
			});
			LinearLayout  ll_content=(LinearLayout) convertview.findViewById(R.id.ll_order_goodcontent);
			addLayout(shopBean, ll_content);
			return convertview;
		}
		
	}
	
	private void addLayout(ShopBean bean,LinearLayout ll_content){
		ll_content.removeAllViews();
		for(final Good temp : bean.commodityList){
			 View  goodview=View.inflate(mContext, R.layout.commint_order_gooditem, null);
			 ImageView  goodImage=(ImageView) goodview.findViewById(R.id.order_goodimage);
			 TextView goodname=(TextView) goodview.findViewById(R.id.order_goodname);
			 ImageManager.Load(temp.main_img_m, goodImage);
			 goodname.setText(temp.commodity_name);
			 LinearLayout ll_goodsize=(LinearLayout) goodview.findViewById(R.id.order_good_size);
			 RelativeLayout root=(RelativeLayout) goodview.findViewById(R.id.root);
			 root.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Bundle bundle=new Bundle();
					bundle.putString(ProductDeatil.INTENT_KEY, temp.commodity_id);
					ActivitySwitch.openActivity(ProductDeatil.class, bundle, OrderActivity.this);
					//OrderActivity.this.finish();
				}
			});
			 for(GoodStandard  item : temp.goodList){
				 View  goodSizeView=View.inflate(mContext, R.layout.commint_order_goodsize_item, null);
				 TextView colorAndSize=(TextView) goodSizeView.findViewById(R.id.good_color_size);
				 TextView onePrice=(TextView) goodSizeView.findViewById(R.id.one_good_price);
				 TextView totalPrice=(TextView) goodSizeView.findViewById(R.id.total_item_price);
				 TextView num=(TextView) goodSizeView.findViewById(R.id.size_num);		 
				 colorAndSize.setText(item.spec_name_value);
				 onePrice.setText(item.sprice);
				 float tp=Integer.parseInt(item.quantity)*Float.parseFloat(item.sprice);
				 totalPrice.setText("￥"+df.format(tp));
				 num.setText("x"+item.quantity);
				 ll_goodsize.addView(goodSizeView); 
			 }
			 
			 ll_content.addView(goodview);
		}
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 1:
			Bundle extras = data.getExtras();
			serializable = (AdressBean) extras.getSerializable("adress");
			LogUtil.d(TAG, serializable.address_show);
			if(serializable!=null){
				mu_name.setText(serializable.consignee_name);
				mu_phone.setText(serializable.consignee_phone);
				mu_adress.setText(serializable.address_show+serializable.address);
				adress_id = serializable.id;
				city_id=serializable.city_id;
				QueryFreightListByUsers(city_id);
				LogUtil.d(TAG, "id--"+adress_id);
			}
			break;
		case RESULT_OK:
			LogUtil.d(TAG, "接收发票id-----------》》》》》》》》》》》");
			invoiceId = data.getStringExtra("invoiceId");
			LogUtil.d(TAG, invoiceId);
			break;
		default:
			break;
		}
		
	}
	private void  QueryFreightListByUsers(String cid){
		CustomResponseHandler  mHandler=new CustomResponseHandler(this, false, ""){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);			
	              LogUtil.d(TAG, content);
	              freightBean = GsonUtils.json2bean(content, FreightBean.class);
	              float totalyf=0;
	              for(ShopBean sBean : bean.data){
	            	  for(FreightItemBean bean:freightBean.data){
		            	   if(sBean.seller_id.equals(bean.seller_id)){
		            		   sBean.yunfei=bean.freight;
		            		   totalyf+=Float.parseFloat(bean.freight);
		            	   }
		               }
	              }
	              mAdapter.notifyDataSetChanged();
	              AllTotalPrice.setText("￥"+freightBean.tt_money+"元");
	              totalyunfeiprice.setText("运费总计：￥"+df.format(totalyf)+"元");
			}
		};

		RequestParams params=new RequestParams();
		params.put("uid",user.id);
		params.put("codes",codes);//货品编号
		if("0".equals(from)){
			params.put("ty","0");//购买类型
		}else{
			params.put("ty","1");//立即购买
		}
	
		if(!StringUtils.isEmpty(cid)){
			params.put("city_id",cid);//城市id
		}else{
	    	params.put("city_id","-1");//城市id
		}
	
		HttpClient.iQueryFreightListByUsers(mHandler, params);
	}
	
	class  FreightBean{
		public List<FreightItemBean> data;
		public String tt_money;//总价格
		public class FreightItemBean{
			public String freight;//运费
			public String seller_id;//卖家id
		
		}
	}

}
