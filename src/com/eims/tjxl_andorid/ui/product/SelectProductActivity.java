/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import loopj.android.http.RequestParams;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.GoodDetail;
import com.eims.tjxl_andorid.entity.GoodDetail.GoodColor;
import com.eims.tjxl_andorid.entity.GoodDetail.GoodSize;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.product.pop.ProvincePop.ClickItemListener;
import com.eims.tjxl_andorid.ui.shopcart.ShoppingCarDialogActivity;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.utils.ViewHolder;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyGridView;
import com.eims.tjxl_andorid.weght.SelectGoodsPopWindow;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 商品选购
 * @Author zd
 * @date 2015年4月18日 上午11:25:14
 *************************************************************************** 
 */
public class SelectProductActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG = null;
	private HeadView head;
	//private ArrayList<String> colorList = new ArrayList<String>();// 存放颜色 列表的集合
	private MyGridView mColorGridView;
	private ColorAdapter mcAdapter;
	/** 尺寸，库存，数量 行 **/
//	private TableLayout mTabLayout;
//	private List<GoodColor> list;
	private LinearLayout ll_pop;
	private SelectGoodsPopWindow popWindow;
	private RelativeLayout rl_total, root;
	private GoodDetail bean;
	private TextView GoodName,price;
	private ImageView good_image;
	private LinearLayout  llLayout;
    private String selectColorCode;
    private TextView mTotalNum,mTotalPrice;
    /**用于存放商品 选中规格**/
    private ArrayList<GoodSize>  totalList=new ArrayList<GoodDetail.GoodSize>();
 //   private Map<String,GoodSize> map=new HashMap<String, GoodDetail.GoodSize>();
    float commitPrice = 0f;
    private int totalNum=0;
  //  private GoodQuanListener mGoodQuanListener;
    public static SelectProductActivity instance;
    private Button btn_addShop,btn_showShop;
    private User user;
    private ShoppingCarDialogActivity  shoppingCarDialog;
    private TextView minBatch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_product);
		instance=this;
		shoppingCarDialog=new ShoppingCarDialogActivity(mContext, R.style.load_dialog);
		findviews();
		initheadview();
		initData();
		
	
	}

	private void findviews() {
		head = (HeadView) findViewById(R.id.head);
		 ll_pop=(LinearLayout) findViewById(R.id.ll_pop);
		rl_total = (RelativeLayout) findViewById(R.id.rl_total);
		root = (RelativeLayout) findViewById(R.id.root);
		mColorGridView = (MyGridView) findViewById(R.id.color_gridview);
	//	mTabLayout = (TableLayout) findViewById(R.id.tablelayout);
		GoodName=(TextView) findViewById(R.id.goods_name_selector);
		price=(TextView) findViewById(R.id.price_seltor);
		good_image=(ImageView) findViewById(R.id.product_icon);
		llLayout=(LinearLayout) findViewById(R.id.ll_selector_item);
		mTotalNum=(TextView) findViewById(R.id.total_number);
		mTotalPrice=(TextView) findViewById(R.id.total_price);
		btn_addShop=(Button) findViewById(R.id.btn_addshop);
		btn_showShop=(Button) findViewById(R.id.show_shop);
		minBatch=(TextView) findViewById(R.id.min_batch);
	}

	private void initheadview() {
		head.setText("商品选购");
		head.setRightGone();
		//head.setGobackVisible();
		head.ClickBlack(new ClickListener());
	}
	class ClickListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
           	filterGoodSizes();
			 ProductDeatil.selectorGoodList.clear();
			 Intent intent=new Intent();
			 ArrayList<GoodDetail.GoodSize> goodList = (ArrayList<GoodSize>) bean.goodList;
			 LogUtil.d("zd","goodlist="+goodList.size());
			 intent.putExtra("goodselector", goodList);
			 setResult(1, intent);
			 AppManager.getAppManager().finishActivity();
			((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
		}
		
	}
	
	private void initData(){
		Bundle bundle = getIntent().getExtras();
		bean = (GoodDetail) bundle.getSerializable("goodDetail");
		if(bean==null){
			return;
		}
		LogUtil.d(TAG, bean.colorList.size()+"");
		LogUtil.d(TAG,bean.pdMap.commodity_name);
		GoodName.setText(bean.pdMap.commodity_name);
		minBatch.setText(bean.pdMap.min_batch+"双起批");
		price.setText("￥"+bean.pdMap.sprice);
		List<String> imgArr = bean.imgArr;
		ImageManager.Load(imgArr.get(0), good_image);
		setListener();
		showColorLayout();
		updataTotalPrice(0.00f,0);
	}

	private void setListener() {
	//	ll_pop.setOnClickListener(this);
		rl_total.setOnClickListener(this);
		mColorGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				GoodColor goodColor=(GoodColor) mcAdapter.getItem(arg2);
		//		ImageManager.Load(goodColor.goods_color_img, good_image);
				// 设置选中时的样式
				mcAdapter.setSelectedPosition(arg2);
				showSizeAndNum();
				mcAdapter.notifyDataSetChanged();
			}
		});
		
		btn_addShop.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(AppContext.isLogin){
					   user = AppContext.getLocalUserInfo(mContext);
					   LogUtil.d(TAG, user.username);
					   filterGoodSizes();
				       if(bean.goodList.size()>0){
				    	     LogUtil.d(TAG, "选择商品规格数："+bean.goodList.size());
				    	     addShopCart(bean.goodList);
				       }else{ 
				    	   TipToast.makeText(mContext, "请选择商品规格", 0).show();
				       }
				}else{
					//跳转到登录界面
//					Bundle	bundle2 = new Bundle();
//					bundle2.putInt(LoginActivity.LOGIN_TYPE_KEY, LoginActivity.LOGIN_TYPE_VALUE_DETAIL);
//					ActivitySwitch.openActivity(LoginActivity.class, bundle2,ProudctDeatil.this);
					 TipToast.makeText(mContext, "亲，你还没登录哟", 0).show();
				}
			}
		});
		btn_showShop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppManager.getAppManager().finishToHome();
				MainActivity.radioGroup.getChildAt(2).performClick();
		    	MainActivity.mainPager.setCurrentItem(2);
			}
		});
	}
	  float totalprices=0.0f;
	  int quantity=0;
	/** 显示颜色布局 **/
	private void showColorLayout() {
    //     list=bean.colorList;
		mColorGridView.setNumColumns(3);
		mColorGridView.setColumnWidth(AppContext.getPICSize(getWindowManager()).x / 3);
		mcAdapter = new ColorAdapter();
		//默认为0
		mcAdapter.setSelectedPosition(0);
		mColorGridView.setAdapter(mcAdapter);
		showSizeAndNum();
		
	}

	/** 显示尺码 库存 购买数量布局 **/
	private void showSizeAndNum() {
		//默认情况下是选中红色,所以加载的尺码也应是 红色所对应的尺码
		llLayout.removeAllViews();
		
		for(int k=0;k<bean.goodList.size();k++){
		//	for(final GoodSize goodSize : bean.goodList){
			final GoodSize goodSize = bean.goodList.get(k);
			if(selectColorCode.equals(goodSize.goods_color)){
				//	LogUtil.d("zd", "尺码："+goodSize.spec_value+"-------------"+goodSize.goods_code);	
				View view = View.inflate(SelectProductActivity.this,R.layout.selector_good_number_item, null);
				TextView size = (TextView) view.findViewById(R.id.goods_size);
				TextView goodsnum = (TextView) view.findViewById(R.id.goods_num);
				final EditText edit_num = (EditText) view.findViewById(R.id.num_edit);
				ImageView img_jiannum = (ImageView) view.findViewById(R.id.jian_num);
				ImageView add_num = (ImageView) view.findViewById(R.id.add_num);
				goodsnum.setText(goodSize.goods_stock);
				size.setText(goodSize.spec_value);
				//设置初始值
				edit_num.setText(goodSize.quantity+"");
				img_jiannum.setOnClickListener(new JianIvOnClick(edit_num, 1,goodSize));
				add_num.setOnClickListener(new addIvOnClick(edit_num, 1,goodSize));
				
				edit_num.addTextChangedListener(new TextWatcher() {		
					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						String s=arg0.toString();
						edit_num.setSelection(edit_num.getText().length());
						if(checkGoodsCount(s)){
							 if(Integer.parseInt(s)>Integer.parseInt(goodSize.goods_stock)){
								 edit_num.setText(goodSize.goods_stock);
								}
							 goodSize.quantity=Integer.parseInt(s);		
							 mTotalPrice.setText("总价：￥"+getGoodTotalPrice()+"元");		
							 mTotalNum.setText("采购清单："+getGoodTotalQuantity()+"件"); 
						}
					}
					
					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
							int arg3) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
						
					}
				});
				llLayout.addView(view);
			}	
		}

	}
	/**
	 *计算当前商品下所有货品总价格 
	 **/
	private String  getGoodTotalPrice(){
		 DecimalFormat df = new DecimalFormat("0.00");
		float totalprice=0.0f;
		for(int i=0;i<bean.goodList.size();i++){
			GoodSize temp = bean.goodList.get(i);
			totalprice+=temp.quantity*(Float.parseFloat(temp.sprice));
		}
		return df.format(totalprice)+"";
	}
	/**
	 *计算当前商品下所有选泽货品总数量
	 **/
	private String  getGoodTotalQuantity(){
		 DecimalFormat df = new DecimalFormat("0.00");
		int quantity=0;
		for(int i=0;i<bean.goodList.size();i++){
			GoodSize temp = bean.goodList.get(i);
			quantity+=temp.quantity;
		}
		return quantity+"";
	}
	
//	 * 检测 修改数量的合法性  
//	 */
	private boolean checkGoodsCount(String count) {
		if (TextUtils.isEmpty(count)) {
			TipToast.makeText(mContext, "数量不能为空", 0).show();
			return false;
		} else if (!TextUtils.isDigitsOnly(count)) {
			TipToast.makeText(mContext, "请输入一个数字", 0).show();
			return false;
		}else if(count.length()>=6){
			TipToast.makeText(mContext, "请输入一个合法的商品数量", 0).show();
			return false;
		}
		return true;
	}

	/** 减号 点击事件 **/
	class JianIvOnClick implements OnClickListener {
		private TextView etid_num;
		private int goodnum = 0;// editText中的初始值
        private GoodSize goodSize;
		public JianIvOnClick(TextView etid_num, int goodnum,GoodSize goodSize) {
			this.etid_num = etid_num;
			this.goodnum = goodnum;
			this.goodSize=goodSize;
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
			    goodSize.quantity=goodnum;
			    totalNum--;
			    commitPrice-=Float.parseFloat(goodSize.sprice)*1; 
				 mTotalNum.setText("采购清单："+getGoodTotalQuantity()+"件"); 
			     mTotalPrice.setText("总价：￥"+getGoodTotalPrice()+"元");		
			}
		}
	}

	/** 加号 点击事件 **/
	class addIvOnClick implements OnClickListener {
		private EditText etid_num;
		private int goodnum = 0;// editText中的初始值
		 private GoodSize goodSize;
		public addIvOnClick(EditText etid_num, int goodnum, GoodSize goodSize) {
			this.etid_num = etid_num;
			this.goodnum = goodnum;
			this.goodSize=goodSize;
		}

		@Override
		public void onClick(View arg0) {
			if (TextUtils.isEmpty(etid_num.getText().toString()))
				etid_num.setText("0");
			else
				goodnum = Integer.parseInt(etid_num.getText().toString());
			//    totalNum++;
				goodnum++;
				if(goodnum>Integer.parseInt(goodSize.goods_stock))
					  goodnum=Integer.parseInt(goodSize.goods_stock);
				etid_num.setText(goodnum +"");
			    goodSize.quantity=goodnum;
			    commitPrice+=Float.parseFloat(goodSize.sprice)*1; 
				mTotalNum.setText("采购清单："+getGoodTotalQuantity()+"件"); 
			    mTotalPrice.setText("总价：￥"+getGoodTotalPrice()+"元");		
		}
	}
	
	private void filterGoodSizes(){
		for(int i = bean.goodList.size() - 1; i >= 0;  i--) {
			GoodSize goodSize = bean.goodList.get(i);
			if (goodSize.quantity == 0) {
				bean.goodList.remove(i);
			}else{
				LogUtil.d("zd","goodsize.quantity ="+goodSize.quantity);
			}
		}
		Log.d(TAG," bean.goodList size = "+ bean.goodList.size());
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_total:
			// ll_pop.setVisibility(View.VISIBLE);
//			popWindow = new SelectGoodsPopWindow(this, null);
//			popWindow.showAtLocation(root, Gravity.BOTTOM
//					| Gravity.CENTER_HORIZONTAL, 0, 0); // 设置layout在PopupWindow中显示的位置
			break;
		default:
			break;
		}
	}
	/**初始化总价格和总数量**/
	private void updataTotalPrice(float price,int num){
	  	DecimalFormat df = new DecimalFormat("0.00");
		 mTotalNum.setText("采购清单："+num+"件"); 
		 mTotalPrice.setText("总价：￥"+df.format((price))+"元");		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		filterGoodSizes();
		 ProductDeatil.selectorGoodList.clear();
		 Intent intent=new Intent();
		 ArrayList<GoodDetail.GoodSize> goodList = (ArrayList<GoodSize>) bean.goodList;
		 LogUtil.d("zd","goodlist  ="+goodList.size());
		 intent.putExtra("goodselector", goodList);
		 setResult(1, intent);
		 super.onBackPressed();
	}
	
	class ColorAdapter extends BaseAdapter {

		private int selectedPosition = 0;// 选中的位置

		@Override
		public int getCount() {
			return bean.colorList == null ? 0 : bean.colorList.size();
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
			selectColorCode=bean.colorList.get(position).goods_color;
			LogUtil.d(TAG, "code---"+selectColorCode);
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return bean.colorList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplication(),R.layout.color_item, null);
			}
			TextView colorName = ViewHolder.get(convertView, R.id.text_color_id);
			colorName.setText(bean.colorList.get(position).goods_color);
			final RelativeLayout color_layout_id = ViewHolder.get(convertView,R.id.color_layout_id);
			final ImageView icon = ViewHolder	.get(convertView, R.id.iv_color_id);
			if (position == selectedPosition) {
				//System.out.println("初始化selectColorId----"+bean.colorList.get(position).id);
				color_layout_id.setBackgroundResource(R.drawable.gxy);
				icon.setVisibility(View.GONE);
				//selectColorCode=bean.colorList.get(position).id;	
			} else {
				color_layout_id.setBackgroundResource(R.drawable.color_item_bg);
				icon.setVisibility(View.GONE);
			}
			return convertView;
		}

	}
	
	
    /**加入购物车
    * @param selectorGoodLists **/
	private void  addShopCart(List<GoodSize> selectorGoodListss){	
		filterGoodSizes();
		CustomResponseHandler  mHandler=new CustomResponseHandler(this, true, "正在加载..."){
		
		@Override
		public void onSuccess(int statusCode, String content) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, content);
			if(statusCode==200){
               LogUtil.d(TAG, content);
               try {
					JSONObject obj=new JSONObject(content);
					String type = obj.getString("type");
					if("1".equals(type)){
					  //  selectorGoodList.clear();
						shoppingCarDialog.show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
	String codes = "";
	StringBuilder sb=new StringBuilder();
	for(int i=0;i<selectorGoodListss.size();i++){
		GoodSize goodSize = selectorGoodListss.get(i);
		sb.append(goodSize.goods_code+","+goodSize.quantity+",颜色:"+goodSize.goods_color+"，"+goodSize.spec_name+":"+goodSize.spec_value+",");
	}
	if(sb.length()>0){
		codes= sb.substring(0, sb.length()-1);
	}
	RequestParams params=new RequestParams();
	params.put("uid",user.id);
	params.put("codes",codes);
	params.put("cid",bean.pdMap.id);
	params.put("is_sample","0" );//是否样品
	params.put("sellid",bean.userMap.id);//卖家id
	HttpClient.addShopCart(mHandler, params);
	}
	
	
}
