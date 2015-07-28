/**
 * 
 */
package com.eims.tjxl_andorid.ui.receipt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
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
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.entity.CommintOrderBean;
import com.eims.tjxl_andorid.entity.ShopCartBean;
import com.eims.tjxl_andorid.entity.GoodDetail.GoodSize;
import com.eims.tjxl_andorid.entity.ShopCartBean.Good;
import com.eims.tjxl_andorid.entity.ShopCartBean.GoodStandard;
import com.eims.tjxl_andorid.entity.ShopCartBean.ShopBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.personcenter.PersonalCenterFragment;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.ui.product.WuliuSelectorActivity.WuLiuP;
import com.eims.tjxl_andorid.ui.shopcart.ApplyforWqActivity;
import com.eims.tjxl_andorid.ui.shopcart.OrderActivity;
import com.eims.tjxl_andorid.ui.shopcart.ToLoginDialogActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.NetworkUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 进货单
 * @Author zd
 *************************************************************************** 
 */
public class ReceiptFragment extends BaseLazyFragment {
	private static final String TAG = "ReceiptFragment";
	public static ReceiptFragment personalCenterFragment;
	private TextView  mShopNumber,btn_del;
	private ToLoginDialogActivity loginDialog;
	private User user;
	private ShopCartBean cartBean;
	DecimalFormat df = new DecimalFormat("0.00");
	private CheckBox cb_shopCartAll;
	private ListView mListView;
	private ShoppingCarListAdapter mAdapter;
    private RelativeLayout ll_buttom;
    private boolean isShowButtom=false;
    private Button commint_shoppingCar;//结算按钮
    private TextView totalNum,totalPrice;
    private LinearLayout ll_shopcarnull;
    private Button btn_goHome;
    private boolean check_bl=false;
	int countss=0;
	public static String  REFRESH_SHOPPINGCART_ACTION="refresh_ShoppingCart.action";
	private RefreshShopRevicer mRefreshShopRevicer;
	private String codess;
	private boolean ishaveGood=false;//购物车中是否有货品
	public static ReceiptFragment getInstance() {
		if (personalCenterFragment == null) {
			personalCenterFragment = new ReceiptFragment();
		}
		return personalCenterFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.shop_car_layout, null);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		findviews();
		loginDialog=new ToLoginDialogActivity(ct, R.style.load_dialog);
		setListener();
		initButtom();
		mRefreshShopRevicer=new RefreshShopRevicer();
		IntentFilter filter=new IntentFilter();
		filter.addAction(REFRESH_SHOPPINGCART_ACTION);
		getActivity().registerReceiver(mRefreshShopRevicer, filter);
	}
	
	private void  findviews(){
		mShopNumber=(TextView) getActivity().findViewById(R.id.all_goodnum);
		btn_del=(TextView) getActivity().findViewById(R.id.btn_del);
		cb_shopCartAll=(CheckBox) getActivity().findViewById(R.id.cb_shopcarall);
		mListView=(ListView) getActivity().findViewById(R.id.shopping_car_listview);
		ll_buttom=(RelativeLayout) getActivity().findViewById(R.id.ll_bcart);
		commint_shoppingCar=(Button) getActivity().findViewById(R.id.commint_shoping_car);
		totalNum=(TextView) getActivity().findViewById(R.id.shopcar_total_number);
		totalPrice=(TextView) getActivity().findViewById(R.id.shopping_totalPrice);
		ll_shopcarnull=(LinearLayout) getActivity().findViewById(R.id.shopping_car_null);
		btn_goHome=(Button) getActivity().findViewById(R.id.btn_gohome);
	}
	
	@Override
	protected void lazyload() {
		if(!AppContext.isLogin){
			if(loginDialog==null){
				loginDialog=new ToLoginDialogActivity(ct, R.style.load_dialog);
			}
			loginDialog.show();
		}else{
			user = AppContext.getLocalUserInfo(ct);
			loadCart(true);		
		}
		
	}
	
	private void setListener(){

         
		btn_goHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				MainActivity.radioGroup.getChildAt(0).performClick();
		    	MainActivity.mainPager.setCurrentItem(0);
			}
		});
		btn_del.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				delGood();
			}
		});
		
		//判断是否选择了商品 ，判断选择的商品中的货品数量是否大于等于起批量
		commint_shoppingCar.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				if("1".equals(user.check_status)){
					   commintShopCar();
			      }else if("2".equals(user.check_status)){ 
			    	  TipToast.makeText(getActivity(), "亲，你的身份未审核通过呢", 0).show();
			      }else if("3".equals(user.check_status)){
			    	  TipToast.makeText(getActivity(), "亲，你的资料还未完善,快去完善吧", 0).show();
			      }
				
			}
		});
		cb_shopCartAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {		
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isCheck) {
	
				LogUtil.d(TAG, "isselect all :"+check_bl+"        "+isCheck);
				if(!check_bl){
				     //全选 全不选
					   for(ShopBean bean:cartBean.data){
						   bean.isSelect=isCheck;
						     for(Good good: bean.commodityList){
				            	 good.isSelect=isCheck;
				            	 for(GoodStandard temp : good.goodList){
				            		 temp.isSelect=isCheck;
				            	 }
				            }
					   }	
					   mAdapter.notifyDataSetChanged(); 
					   updateButtom();
				}else{
					LogUtil.d(TAG, isCheck+"");
					  if(isCheck){
						  for(ShopBean bean:cartBean.data){
							   bean.isSelect=isCheck;
							     for(Good good: bean.commodityList){
					            	 good.isSelect=isCheck;
					            	 for(GoodStandard temp : good.goodList){
					            		 temp.isSelect=isCheck;
					            	 }
					            }
						   }	
					  }
					
					   mAdapter.notifyDataSetChanged(); 
					   updateButtom();
					   check_bl = false;
				}	 
				      
			}
		});
	}
  
	private void loadCart(boolean isloading){
		
		boolean available = NetworkUtils.getInstance().isAvailable();
		if(!available){
			TipToast.makeText(ct, "亲，你的网络有问题哟", 0).show();
			return;
		}
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), isloading, "正在加载..."){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, ""+content);
				ResolveJson(content);
			}
	
		};
		RequestParams params=new RequestParams();
		params.put("uid",AppContext.userInfo.id);
		HttpClient.QueryShopCart(mHandler, params);
	}
	
	private void  ResolveJson(String content){
		try {
			JSONObject obj=new JSONObject(content);
			String type = obj.getString("type");
			String data=obj.getString("data");
		    if("1".equals(type)){
		    	if(data.length()>6){
		    		ll_shopcarnull.setVisibility(View.GONE);
		            mListView.setVisibility(View.VISIBLE);
		    		cartBean = GsonUtils.json2bean(content, ShopCartBean.class);
		    //		LogUtil.d(TAG, cartBean.data.get(0).f_factory_name);
		    	//	mShopNumber.setText("全部货品 （"+cartBean.data.size()+"）");
		    		if(mAdapter==null){
						mAdapter=new ShoppingCarListAdapter();
					}
		    		mListView.setAdapter(mAdapter);
		    		isShowButtom=true;
		    		ShowButtom();
		    		initButtom();
		    		ishaveGood=true;
		    	}else{
		    	//	mShopNumber.setText("全部货品 （0）");
		    	   mListView.setVisibility(View.GONE);
		    		isShowButtom=false;
		    		ShowButtom();
		    	//	TipToast.makeText(ct, "亲，快去逛逛吧，进货单空的呢", 0).show();
		    		ll_shopcarnull.setVisibility(View.VISIBLE);
		    		mAdapter.notifyDataSetChanged();
		    		ishaveGood=false;
		    	}
		     }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void  ShowButtom(){
	if(isShowButtom){
		ll_buttom.setVisibility(View.VISIBLE);
	}else{
		ll_buttom.setVisibility(View.GONE);
   	}
	}

	
	/** 减号 点击事件 **/
	class JianIvOnClick implements OnClickListener {
		private TextView etid_num;
		private int goodnum = 0;// editText中的初始值
	//	private String code;
		private TextView price;
	//	private String sprice;
	//	private String minBatch;
		private GoodStandard bean;
		private Good mGood;
		public JianIvOnClick(TextView etid_num, int goodnum,TextView price,GoodStandard bean, Good good) {
			this.etid_num = etid_num;
			this.goodnum = goodnum;
        //    this.code=code;
            this.price=price;
       //     this.sprice=sprice;
       //     this.minBatch=minBatch;
            this.bean=bean;
            this.mGood=good;
		}

		@Override
		public void onClick(View arg0) {
			if (TextUtils.isEmpty(etid_num.getText().toString())){
				etid_num.setText("0");
			}
			else{
				goodnum = Integer.parseInt(etid_num.getText().toString());
			}
			if (goodnum > 0){	
				goodnum--;
			    etid_num.setText(goodnum + "");
			    bean.quantity=String.valueOf(goodnum);
			  
			    LogUtil.d(TAG, "剩余商品count："+(mGood.totalQuantity-1));
			    if((mGood.totalQuantity-1)<Integer.parseInt(mGood.min_batch)){
			    	if((mGood.totalQuantity-1)<=0){
			    		 mGood.desc="1";
			    	}else{
			    		 mGood.desc="0";
			    	} 	
			    }else{
			    	mGood.desc="1";
			    }
			    mAdapter.notifyDataSetChanged();	
			    modfiyShopCart(bean.good_code, String.valueOf(goodnum),price,bean.sprice);
				if(bean.isSelect){
					updateButtom();
				}
			}
		}
	}

	/** 加号 点击事件 **/
	class addIvOnClick implements OnClickListener {
		private TextView etid_num;
		private int goodnum = 0;// editText中的初始值
		private String code;
		private TextView price;
		private String sprice;
		private GoodStandard bean;
		private Good mGood;
		public addIvOnClick(TextView etid_num, int goodnum,String code,TextView price,String sprice,GoodStandard bean, Good good) {
			this.etid_num = etid_num;
			this.goodnum = goodnum;
			this.code=code;
			this.price=price;
			this.sprice=sprice;
			this.bean=bean;
			this.mGood=good;
		}

		@Override
		public void onClick(View arg0) {
			if (TextUtils.isEmpty(etid_num.getText().toString()))
				etid_num.setText("0");
			else
				goodnum = Integer.parseInt(etid_num.getText().toString());
				goodnum++;
				etid_num.setText(goodnum +"");
				bean.quantity=String.valueOf(goodnum);
		
			    LogUtil.d(TAG, "剩余商品     count："+(mGood.totalQuantity+1));
			    if((mGood.totalQuantity+1)>=Integer.parseInt(mGood.min_batch)){
			    	 mGood.desc="1";
			    }else{
			    	if((mGood.totalQuantity+1)>1){
			    		 mGood.desc="0";
			    	}else{
			    		 mGood.desc="1";
			    	}
			    	
			    }
				mAdapter.notifyDataSetChanged();
				modfiyShopCart(code, String.valueOf(goodnum),price,sprice);
				if(bean.isSelect){
					updateButtom();
				}
			
		}
	}

	/***
	 * @param sprice 
	 * @param code: 当前规格货号
	 * @param qty:  当前规格数量
	 */
	private void  modfiyShopCart(String code,final String qty,final TextView price, final String sprice){
		boolean available = NetworkUtils.getInstance().isAvailable();
		if(!available){
			TipToast.makeText(ct, "亲，你的网络有问题哟", 0).show();
			return;
		}
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), false, ""){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, "购物车修改："+content);
				//修改成功，更新当前商品规格价格
				try {
					JSONObject obj=new JSONObject(content);
					if("1".equals(obj.getString("type"))){
						 float goodPrice=(Integer.parseInt(qty))*Float.parseFloat(sprice);
						 price.setText("￥"+df.format(goodPrice));
						 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
		};
		RequestParams params=new RequestParams();
		params.put("uid",user.id);
		params.put("code",code);
		params.put("qty",qty);
		HttpClient.Modfiy_ShopCart(mHandler, params);
	}

	private void  addGoodLayout(final ShopBean shopBean,LinearLayout ll_goodLayout,boolean isremove, final CheckBox cbShoeAll){
		ll_goodLayout.removeAllViews();
		for(final Good good:shopBean.commodityList){//循环商品
			View gooditem=View.inflate(ct, R.layout.shopcart_good_item, null);
			RelativeLayout root=(RelativeLayout) gooditem.findViewById(R.id.root);
			ImageView GoodImage=(ImageView) gooditem.findViewById(R.id.good_imgage);
			TextView goodName=(TextView) gooditem.findViewById(R.id.good_name);
			TextView minBatch=(TextView) gooditem.findViewById(R.id.min_batch);
	        TextView msg=(TextView) gooditem.findViewById(R.id.message);
			ImageManager.Load(good.main_img_m, GoodImage);
			goodName.setText(good.commodity_name);
			minBatch.setText(good.min_batch+"双起批");
			root.setOnClickListener(new OnClickListener() {		
				@Override
				public void onClick(View arg0) {
					Bundle bundle=new Bundle();
					bundle.putString(ProductDeatil.INTENT_KEY, good.commodity_id);
				   ActivitySwitch.openActivity(ProductDeatil.class, bundle, getActivity());
				}
			});
			if("0".equals(good.desc)){
				msg.setVisibility(View.VISIBLE);
				msg.setText("不满足起批量");
			}else{
				msg.setVisibility(View.GONE);
			}	
			//商品规格填充
			 LinearLayout ll_goodSize=(LinearLayout) gooditem.findViewById(R.id.ll_gooditem_size);
			if(isremove){
    			ll_goodSize.removeAllViews();
			}
			int countnum=0;
		
				for(final GoodStandard temp:good.goodList){
					View goodSizeitem=View.inflate(ct, R.layout.shop_cart_size_layout, null);
					CheckBox cbSize=(CheckBox) goodSizeitem.findViewById(R.id.cb_goodsize);
					TextView color=(TextView) goodSizeitem.findViewById(R.id.shop_good_color);
					TextView size=(TextView) goodSizeitem.findViewById(R.id.shop_good_size);
					final TextView num=(TextView) goodSizeitem.findViewById(R.id.shop_num);
					TextView price=(TextView) goodSizeitem.findViewById(R.id.shop_good_price);
					TextView jian=(TextView) goodSizeitem.findViewById(R.id.jian);
					TextView jia=(TextView) goodSizeitem.findViewById(R.id.add);
					String[] split = temp.spec_name_value.split("，");
					color.setText(split[0]);
					size.setText(split[1]);
					num.setText(temp.quantity);
					LogUtil.d(TAG, "good item:"+temp.isSelect);
					cbSize.setChecked(temp.isSelect);
				  
					float goodPrice=(Integer.parseInt(temp.quantity))*Float.parseFloat(temp.sprice);
					price.setText("￥"+df.format(goodPrice));
					good.totalQuantity=0;
				     final String tempNum = num.getText().toString();
				     jia.setOnClickListener(new addIvOnClick(num,Integer.parseInt(temp.quantity),temp.good_code,price,temp.sprice,temp,good));
				     jian.setOnClickListener(new JianIvOnClick(num, Integer.parseInt(temp.quantity),price,temp,good));
				 //       LogUtil.d(TAG, "iten:"+temp.isSelect);
						LogUtil.d(TAG, "quantity:"+temp.quantity);
						if(temp.isSelect){
							countnum+=Integer.parseInt(temp.quantity);
							good.totalQuantity=countnum;	
						}else{
							countnum+=0;
							good.totalQuantity=countnum;
						}
						     
                      LogUtil.d(TAG, "total quantity:"+good.totalQuantity);
          	    	if(good.totalQuantity<Integer.parseInt(good.min_batch)){
			    		if(good.totalQuantity==0){
			    			good.desc="1";
			    		}else{
			    			good.desc="0";
			    			msg.setVisibility(View.VISIBLE);
			    		}	
		    		}else{
		    			good.desc="1";
		    			msg.setVisibility(View.GONE);
		    		}
				       cbSize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						    	temp.isSelect=arg1;		   
						    	if(arg1){//当所有商品都被选中时  店铺也应该选中							    	
						    		if(!temp.isCount){
						    			good.totalQuantity+=Integer.parseInt(tempNum);
						    			temp.isCount=true;
						    		}
						    		shopBean.shopTempCount++;
						    	}else{
						    		if(temp.isCount){
							    		good.totalQuantity-=Integer.parseInt(tempNum);
							    		temp.isCount=false;
						    		}	 
						    		shopBean.shopTempCount--;
						    		LogUtil.d(TAG, "good.totalQuantity----"+good.totalQuantity);

						    	}
						        
						       	LogUtil.d(TAG, "is checked = "+arg1+",tempCount:"+shopBean.shopTempCount);
						    	LogUtil.d(TAG, "is init shopCount:"+shopBean.shopCount);
						    	if(good.totalQuantity<Integer.parseInt(good.min_batch)){
						    		if(good.totalQuantity==0){
						    			good.desc="1";
						    		}else{
						    			good.desc="0";
						    		}	
					    		}else{
					    			good.desc="1";
					    		}
					    	   	if(shopBean.shopTempCount!=0&&shopBean.shopCount==shopBean.shopTempCount){
					    	   		shopBean.isSelect=true;
					    	   		mAdapter.notifyDataSetChanged();
					    	   	}else{
					    	   		shopBean.isSelect=false;
					    	   		mAdapter.notifyDataSetChanged();
					    	   	}
						    	//更新商品价格 即数量
						    	updateButtom();
						}
					});
					ll_goodSize.addView(goodSizeitem);
				}
				
				ll_goodLayout.addView(gooditem);	
		
			
		}
	}

	/**更新底部数据**/
	private void  updateButtom(){
		int total=0;
		float sprice=0.0f;
	    int count=0;
		//去循环所有店铺下的 商品规格数量
		for(ShopBean shopBean:cartBean.data){  
			if(shopBean.isSelect){
				count++;
			}
				 for(Good good : shopBean.commodityList){
					 for(GoodStandard temp : good.goodList){
						LogUtil.d(TAG, "updateButtom--"+temp.isSelect);
						 if(temp.isSelect){
							  total+=Integer.parseInt(temp.quantity);
							  sprice+=Integer.parseInt(temp.quantity)*Float.parseFloat((temp.sprice));
						 }
						
					 }
				 }
	
		}
		if(cb_shopCartAll.isChecked()){
			check_bl=true;
			cb_shopCartAll.setChecked(false);
		}
		LogUtil.d(TAG, "选中店铺count : "+count);
		LogUtil.d(TAG, "店铺cartBean.data  : "+cartBean.data.size());
		if(count!=0&&count==cartBean.data.size()){		
			cb_shopCartAll.setChecked(true);
		}
		totalNum.setText("数量总计:  "+total);
		totalPrice.setText("￥"+df.format(sprice));
		
	}
	
	private void  initButtom(){
		cb_shopCartAll.setChecked(false);
    	totalNum.setText("数量总计：0");
    	totalPrice.setText("￥0.00元");
	}
	
	/**
	 * 提交购物车
	 * @return
	 */
	private void  commintShopCar(){
		boolean available = NetworkUtils.getInstance().isAvailable();
		if(!available){
			TipToast.makeText(ct, "亲，你的网络有问题哟", 0).show();
			return;
		}
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), true, "正在结算中..."){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, "购物车提交："+content);
		        try {
					JSONObject obj=new JSONObject(content);
					if("1".equals(obj.getString("type"))){
						CommintOrderBean orderBean = GsonUtils.json2bean(content, CommintOrderBean.class);
						Bundle  bundle=new Bundle();
						bundle.putSerializable("commintorder", orderBean);
						bundle.putString("codes", codess);
						bundle.putString(OrderActivity.FROM_INTENT, "0");
						ActivitySwitch.openActivity(OrderActivity.class, bundle, getActivity());
					}else{
						TipToast.makeText(ct,obj.getString("msg") , 0).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		StringBuilder sb=new StringBuilder();
		String codes = null;
		int count=0;
		int batchCount=0;
		for(ShopBean shopBean:cartBean.data){
				 for(Good good : shopBean.commodityList){
						 for(GoodStandard temp : good.goodList){
							 if(temp.isSelect){
								 sb.append(temp.good_code);
								 sb.append(",");
						    	 count++;
						    	 if(good.totalQuantity<Integer.parseInt(good.min_batch)){
									 good.desc="0";//0 代表不满足起批量  1代表满足
									 mAdapter.notifyDataSetChanged();
									 batchCount++;
								 }	
							 }			
						 }
			
						 LogUtil.d(TAG, "select count:"+count+"     good  total quality:"+good.totalQuantity);
				 }
		}
		if(sb.length()>0){
			codes=sb.substring(0, sb.length()-1);
		}
		codess=codes;
		RequestParams params=new RequestParams();
		params.put("uid",user.id);
		params.put("codes",codes);
		params.put("ty","0");//购买类型 0 购物车结算，1立即购买
		if(count==0){
			TipToast.makeText(getActivity(), "亲，你还没选择一件宝贝呢", 0).show();
			return;
	    }
		if(batchCount!=0){
			TipToast.makeText(getActivity(), "购买数量不能低于起批量", 0).show();
			return;
		}
		HttpClient.Commint_ShopCart(mHandler, params);
	}


	class ShoppingCarListAdapter extends BaseAdapter{
        private boolean isReomve=false;
		@Override
		public int getCount() {
			return cartBean.data==null ? 0:cartBean.data.size();
		}

	
		@Override
		public Object getItem(int arg0) {
		
			return cartBean.data.get(arg0);
		}

	
		@Override
		public long getItemId(int arg0) {
		
			return arg0;
		}

	
		@Override
		public View getView(int postion, View covertview, ViewGroup arg2) {
			ViewHolder holder;
		 
	        if(covertview==null){
	        	holder=new ViewHolder();
	        	covertview=View.inflate(ct, R.layout.receipt_item_layout, null);
	            holder.ll_goodItem=(LinearLayout) covertview.findViewById(R.id.ll_gooditem);
	            holder.shoeName=(TextView) covertview.findViewById(R.id.shoe_name);
	            holder.cbShoeAll=(CheckBox) covertview.findViewById(R.id.cb_shoe_all);
	            covertview.setTag(holder);
	        }else{
	        	holder=(ViewHolder) covertview.getTag();
	          }
	        final ShopBean shopBean = cartBean.data.get(postion);
	         holder.shoeName.setText(shopBean.f_factory_name);
	         holder.cbShoeAll.setChecked(shopBean.isSelect);
	         shopBean.shopCount=0;
	         for(Good good: shopBean.commodityList){
                    shopBean.shopCount+=good.goodList.size();
            }
	  
	         holder.cbShoeAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean ischeck) {
				
					LogUtil.d(TAG, "cbShoeAll---"+ischeck);
					shopBean.isSelect=ischeck;
					if(ischeck){
						shopBean.shopTempCount= shopBean.shopCount;
						for(Good good: shopBean.commodityList){
			            	 good.isSelect=ischeck;   	
			            	 for(GoodStandard temp : good.goodList){
			            		 temp.isSelect=ischeck;
			            	 }
			            }
					
					
					}else{
						if( shopBean.shopTempCount== shopBean.shopCount){//相等 说明点击的是店铺的checkbox  不想等说明点击的是货品的checkbox
							shopBean.shopTempCount= 0;
							for(Good good: shopBean.commodityList){
				            	 good.isSelect=ischeck;   	
				            	 for(GoodStandard temp : good.goodList){
				            		 temp.isSelect=ischeck;
				            	 }
				            }
						}	
					}
		            isReomve=true;
		            notifyDataSetChanged();
		        	updateButtom();

				}
			});
	         addGoodLayout(shopBean, holder.ll_goodItem,isReomve,holder.cbShoeAll);
			return covertview;
		}
		
	}
	
	class ViewHolder{
		 LinearLayout ll_goodItem;
		 TextView shoeName;
		 CheckBox cbShoeAll;
	}
	/**删除商品**/
	private void delGood(){
		if(!ishaveGood){
		  return;
		}
		StringBuilder sb=new  StringBuilder();
		String codes=null;
		int delcount=0;
		for(ShopBean sBean : cartBean.data){
			for(Good mGood : sBean.commodityList){
				for(GoodStandard mSize : mGood.goodList){
					if(mSize.isSelect){
					   sb.append(mSize.good_code+",");	
					   delcount++;
					}
				}
			}
		}
		if(sb.length()>0){
			codes=sb.substring(0, sb.length()-1);
		}
		if(delcount==0){
			TipToast.makeText(getActivity(), "请选择你要删除的商品", 0).show();
			return;
		}
		final String[] code=codes.split(",");
		CustomResponseHandler mHandler=new CustomResponseHandler(getActivity(), true, "正在删除..."){
		    @Override
		    public void onSuccess(int statusCode, String content) {
		    	// TODO Auto-generated method stub
		    	super.onSuccess(statusCode, content);
		    	LogUtil.d(TAG, content);
                loadCart(false);
		    }	
		};
		RequestParams params=new RequestParams();
		params.put("uid", user.id);
		params.put("code", codes);
		LogUtil.d(TAG,"codes:   "+codes);
		HttpClient.iDelShopCar(mHandler, params);
	}
	
	class RefreshShopRevicer extends BroadcastReceiver{
		@Override
		public void onReceive(Context arg0, Intent intent) {
	         String action = intent.getAction();
	         if(REFRESH_SHOPPINGCART_ACTION.equals(action)){
	        	 LogUtil.d(TAG, "刷新购物车列表....");
	        	 loadCart(false);
	         }
		}
		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(mRefreshShopRevicer);
	}
	
}
