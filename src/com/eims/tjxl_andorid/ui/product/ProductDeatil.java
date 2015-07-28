/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import java.io.Serializable;
import java.net.URL;
import java.security.spec.MGF1ParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import loopj.android.http.RequestParams;
import uk.co.senab.photoview.ImagePagerActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.Constans;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.db.dao.ProductDao;
import com.eims.tjxl_andorid.db.dbmodel.Product;
import com.eims.tjxl_andorid.entity.CommintOrderBean;
import com.eims.tjxl_andorid.entity.GoodDetail;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.entity.GoodDetail.GoodSize;
import com.eims.tjxl_andorid.entity.ShopCartBean.Good;
import com.eims.tjxl_andorid.entity.ShopCartBean.GoodStandard;
import com.eims.tjxl_andorid.entity.ShopCartBean.ShopBean;
import com.eims.tjxl_andorid.ui.product.WuliuSelectorActivity.WuLiuP;
import com.eims.tjxl_andorid.ui.shopcart.OrderActivity;
import com.eims.tjxl_andorid.ui.shopcart.ShoppingCarDialogActivity;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.DensityUtil;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.NetworkUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyViewPager;
import com.google.gson.Gson;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 商品详情界面
 * @Author zd
 * @date 2015年4月16日 下午3:55:54
 *************************************************************************** 
 */
public class ProductDeatil extends BaseActivity implements OnClickListener {

	
	
	private HeadView headView;
	private ViewPager mPager;
	private LinearLayout ll_pointview;
	private ArrayList<String> imagePath;
	private ArrayList<ImageView> ViewList;
	private ArrayList<ImageView> pointsList;
	private ProductDetailAdapter detailAdapter;
	private ImageView btn_share;
	private ImageView btn_shouchang;

	private final int AD_WIDTH = 480;
	private final int AD_HEIGHT = 150;
	private View view1, view2, view3, view4;
	private Button btn_attrbuite, btn_comment, btn_buy_records, btn_recommend;
	// private MyViewPager infoPager;
	private ItemOnclickLister itemOnclickLister;
	private static final int PRODUCT_ATTRBUITE = 0;
	private static final int PRODUCT_COMMENT = 1;
	private static final int PRODUCT_BUY_RECORDS = 2;
	private static final int PRODUCT_RECOMMEND = 3;
	public static final String INTENT_KEY = "goodId";
	public static final String INTENT_KEY_FROM = "intent_from";
	public static final String TAG1 = "ProudctDeatil";
	public static final int FROM_OTHERS = 0;//从别的页面跳转过来
	public static final int FROM_PRODUCT_HUHUAN = 1;//从货品互换页面跳转过来
	
	private ProductAttrubitsFragment attrubitsFragment;
	private ProductCommentFragment commentFragment;
	private BuyRecordsFragment buyRecordsFragment;//成交记录
	private RecommendedFragment recommendedFragment;//我的购买记录
	private Fragment currentFragment;
	private Button btnNowBuy, btn_joinShopCart;// 立即购买,加入进货单
	private RelativeLayout mFactoryLayout;
	private ImageButton imgBtn_right;
	public static String goodId;
	public static GoodDetail detail;
	private TextView mTGoodName, mPrice, minBatch, mTotalSale;
	private RelativeLayout rl_selector_good;// 选购商品
	private RelativeLayout chooseAdress;
	private TextView mShoeAdress, mLoginUserAdress;
	private ImageView userIcon;
	private TextView yunfei;
	private TextView shoeName;
	public  User user;
	public static List<GoodSize> selectorGoodList = new ArrayList<GoodDetail.GoodSize>();
	private ShoppingCarDialogActivity shoppingCarDialog;
	private LinearLayout ll_tuwendetail;
	// 图文详情---所有图片地址
	public String allstr = "";
	private boolean isCollected = false;
    private ScrollView mScrollView;
	private LinearLayout ll_butt;
	private String tuwenxianqing;
	private int from;//用于表示从什么页面跳转过来
	private String codess;//货品编号
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_detail_layout);
		shoppingCarDialog = new ShoppingCarDialogActivity(mContext,R.style.load_dialog);
		findviews();
		setActionBar();
		initViewPager();
		initdata();
		initUI();
	}
   private void  initUI(){
	   mScrollView.setVisibility(View.INVISIBLE);
	   ll_butt.setVisibility(View.INVISIBLE);
	   if(from == FROM_PRODUCT_HUHUAN){
		   btn_comment.setVisibility(View.GONE);
		   view2.setVisibility(View.GONE);
	   }
   }
	
	private void initdata() {
		Bundle bundle = getIntent().getExtras();
	    goodId = bundle.getString(INTENT_KEY);
	    from = bundle.getInt(INTENT_KEY_FROM,FROM_OTHERS);
	//	goodId="11";
		if (goodId == null) {
			return;
		}
		LogUtil.d(TAG1, "商品id：" + goodId);
		CustomResponseHandler handler = new CustomResponseHandler(mContext,
				true, "正在加载....") {
			@Override
			public void onSuccess(int statusCode, String content) {
				Log.d(TAG1, "load good detail ,result = "+content);
				super.onSuccess(statusCode, content);
				if (statusCode == 200) {
					resovleJson(content);
					if(AppContext.isLogin) isCollected();
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("id", goodId);
		HttpClient.QueryGoodDetail(handler, params);
	}

	private void setActionBar() {
		headView.setText("商品详情");
		headView.setGobackVisible();
		headView.setRightResource();
	}

	private void setListener() {
		itemOnclickLister = new ItemOnclickLister();
		btn_share.setOnClickListener(this);
		btn_shouchang.setOnClickListener(this);
		btn_attrbuite.setOnClickListener(itemOnclickLister);
		btn_comment.setOnClickListener(itemOnclickLister);
		btn_buy_records.setOnClickListener(itemOnclickLister);
		btn_recommend.setOnClickListener(itemOnclickLister);
		// headView.setRightListener(this);
		btnNowBuy.setOnClickListener(this);
		mFactoryLayout.setOnClickListener(this);
		rl_selector_good.setOnClickListener(this);
		chooseAdress.setOnClickListener(this);
		btn_joinShopCart.setOnClickListener(this);
		ll_tuwendetail.setOnClickListener(this);
	}

	/**
	 * 初始化ViewPager的，动态设置Viewpgaer的高
	 */
	private void initViewPager() {
		WindowManager manager = getWindowManager();
		Point point = AppContext.getPICSize(manager);
		int PIC_WID = point.x;// 屏幕的宽
		int bitmapWidth = AD_WIDTH;
		int bitmapHeight =DensityUtil.dip2px(mContext, AD_HEIGHT);
		int bitmapHeight2=DensityUtil.px2dip(mContext, AD_HEIGHT);
		LogUtil.d("zd","bitmapHeight   =="+ bitmapHeight+   "    "+ bitmapHeight2);
		int wedgitWidth = PIC_WID;
		// 根据容器的宽度去缩放图片的高度
		int scallHeight = AppContext.getScallZoomHeight(manager, bitmapHeight,
				bitmapWidth, wedgitWidth);
		mPager.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, scallHeight));
	}

	private void findviews() {
		// TODO Auto-generated method stub
		headView = (HeadView) findViewById(R.id.head);
		mPager = (ViewPager) findViewById(R.id.vPager);
		ll_pointview = (LinearLayout) findViewById(R.id.ll_poiont);
		btn_share = (ImageView) findViewById(R.id.btn_share);
		btn_shouchang = (ImageView) findViewById(R.id.btn_shouchang);
		btn_attrbuite = (Button) findViewById(R.id.product_attribute);
		btn_comment = (Button) findViewById(R.id.product_comment_btn);
		btn_buy_records = (Button) findViewById(R.id.buy_records_btn);
		btn_recommend = (Button) findViewById(R.id.recommended_btn);
		btnNowBuy = (Button) findViewById(R.id.btn_nowbuy);
		mFactoryLayout = (RelativeLayout) findViewById(R.id.rl_factory);
		imgBtn_right = (ImageButton) headView.findViewById(R.id.imgbtn_right);
		view1 = findViewById(R.id.view01);
		view2 = findViewById(R.id.view02);
		view3 = findViewById(R.id.view03);
		view4 = findViewById(R.id.view04);
		mTGoodName = (TextView) findViewById(R.id.goodname);
		mPrice = (TextView) findViewById(R.id.price);
		minBatch = (TextView) findViewById(R.id.min_batch);
		mTotalSale = (TextView) findViewById(R.id.total_sale);
		rl_selector_good = (RelativeLayout) findViewById(R.id.seletor_good);
		chooseAdress = (RelativeLayout) findViewById(R.id.choose_adress);
		mShoeAdress = (TextView) findViewById(R.id.shoe_adress);
		mLoginUserAdress = (TextView) findViewById(R.id.login_useradress);
		yunfei = (TextView) findViewById(R.id.yunfei);
		btn_joinShopCart = (Button) findViewById(R.id.btn_join_shopcart);
		shoeName=(TextView) findViewById(R.id.shoe_name);
		ll_tuwendetail=(LinearLayout) findViewById(R.id.ll_tuwendetail);
		mScrollView=(ScrollView) findViewById(R.id.detail_borderscrollview);
		ll_butt = (LinearLayout) findViewById(R.id.ll_bottom);
		userIcon=(ImageView) findViewById(R.id.icon);
		headView.setText("商品详情");
	}

	/** 统一显示UI **/
	private void showUI() {
		showTopPager();
		btn_attrbuite.performClick();
		btn_attrbuite.setTextColor(getResources().getColor(R.color.sheng_red));// 红色
		showButtomItem();
		setListener();
		updateUI();
	}

	/**
	 * 
	 * 显示顶部ViewPager.
	 * 
	 */
	private void showTopPager() {
		ViewList = new ArrayList<ImageView>();
		pointsList = new ArrayList<ImageView>();
		imagePath = new ArrayList<String>();
		if (detail != null) {
			for (String path : detail.imgArr) {
				LogUtil.d(TAG1, path);
				imagePath.add(path);
			}
		}
		if (imagePath == null && imagePath.size() < 1) {
			return;
		}
		for (int i = 0; i < imagePath.size(); i++) {
			ImageView img = new ImageView(getApplication());
			LogUtil.d("zd", "imageurl   :   "+imagePath.get(i));
			ImageManager.Load(imagePath.get(i), img);
			img.setLayoutParams(new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT));
			img.setScaleType(ScaleType.FIT_XY);
			ViewList.add(img);
		}
		ll_pointview.removeAllViews();
		for (int i = 0; i < ViewList.size(); i++) {
			ImageView point = new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
					LinearLayout.LayoutParams.MATCH_PARENT, 1);
			// if(i!=0){
			// params.setMargins(2, 0, 0, 0);
			// }
			point.setLayoutParams(params);
			point.setBackgroundResource(R.drawable.image_point_selector);
			pointsList.add(point);
			ll_pointview.addView(point);

		}
		if (pointsList != null && pointsList.size() > 0) {
			pointsList.get(0).setSelected(true);
		}
		if (detailAdapter == null) {
			detailAdapter = new ProductDetailAdapter();
			mPager.setAdapter(detailAdapter);
		} else {
			detailAdapter.notifyDataSetChanged();
		}
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				ImageView pointView = (ImageView) pointsList.get(position);
				for (ImageView view : pointsList) {
					view.setSelected(false);
				}
				pointView.setSelected(true);
				// currentADPosition = position;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	class ProductDetailAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ViewList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
			object = null;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			// TODO Auto-generated method stub
			((ViewPager) container).addView(ViewList.get(position));
			ImageView imageview = ViewList.get(position);
			imageview.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					imageBrower(position, imagePath);
				}
			});
			return ViewList.get(position);
		}
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(getBaseContext(), ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		    Intent  intent=null;
		    Bundle	bundle=null;
		switch (v.getId()) {
		case R.id.btn_share:
			ActivitySwitch.openActivity(ShareActivity.class,null,ProductDeatil.this);
			break;
		case R.id.btn_shouchang:
			if (AppContext.isLogin) {
				if (isCollected) {
					cancelColletion();
				} else {
					addColletion();
				}
			} else {
				ActivitySwitch.openActivity(LoginActivity.class,null,this);
			}
			break;
		case R.id.seletor_good:
			 intent = new Intent(ProductDeatil.this,
					SelectProductActivity.class);
			 bundle = new Bundle();
			bundle.putSerializable("goodDetail", detail);
			intent.putExtras(bundle);
			startActivityForResult(intent, 1);
			break;

		case R.id.btn_join_shopcart:
           goshopping("0");
			break;

		case R.id.btn_nowbuy:
				goshopping("1");
			break;
		case R.id.rl_factory:
			Log.d(TAG1, "detail.pdMap.user_id = "+detail.pdMap.user_id);
			// 进入店铺首页
			 bundle=new Bundle();
			bundle.putString(FactoryActivity.ID, detail.pdMap.user_id);
			ActivitySwitch.openActivity(FactoryActivity.class, bundle,
					ProductDeatil.this);
			break;
		case R.id.choose_adress:
			 intent = new Intent(ProductDeatil.this,
					WuliuSelectorActivity.class);
			intent.putExtra("province_city", detail.userMap.province_name
					+ "  " + detail.userMap.city_name);
			intent.putExtra("uid", detail.userMap.id);
			startActivityForResult(intent, 0);
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			break;
		case R.id.ll_tuwendetail:
			  if(tuwenxianqing=="" || tuwenxianqing==null){
				  TipToast.makeText(mContext, "此商品没有图文详情", 0).show();
				  return;
			  }
			   intent = new Intent(ProductDeatil.this,GraphicDetailsActivity.class);	
				intent.putExtra(GraphicDetailsActivity.IMAGES_TAG,tuwenxianqing);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			break;
		default:
			break;
		}
	}
	/**  0   加入购物车   1 提交订单**/
	private void  goshopping(String type){
		/*
		 * 计算选购的商品个数
		 */
		int quantity = 0;
		for (int i = 0; i < selectorGoodList.size(); i++) {
			GoodSize size = selectorGoodList.get(i);
			quantity += size.quantity;
		}
		LogUtil.d("zd", "selector good number ="+quantity);
		LogUtil.d("zd", "selectorGoodList.size ="+selectorGoodList.size());
		if (AppContext.isLogin) {
			user = AppContext.getLocalUserInfo(mContext);
			LogUtil.d(TAG1, user.username);
             if("2".equals(user.check_status)){ 
		    	  TipToast.makeText(mContext, "亲，你的身份未审核通过呢", 0).show();
		    	  return;
		      }else if("3".equals(user.check_status)){
		    	  TipToast.makeText(mContext, "亲，你的资料还未完善,快去完善吧", 0).show();
		    	  return;
		      }
			if (selectorGoodList.size() > 0) {
				if(quantity<Integer.parseInt(detail.pdMap.min_batch)){
					TipToast.makeText(mContext, "不满足起批量", 0).show();
					return;
				}
				LogUtil.d(TAG1, "选择商品规格数：" + selectorGoodList.size());
				addShopCart(selectorGoodList,type);
			} else {
				TipToast.makeText(mContext, "请选择商品规格", 0).show();
			}
		} else {
			// 跳转到登录界面
			Bundle bundle2 = new Bundle();
			bundle2.putInt(LoginActivity.LOGIN_TYPE_KEY,
					LoginActivity.LOGIN_TYPE_VALUE_DETAIL);
			ActivitySwitch.openActivity(LoginActivity.class, bundle2,
					ProductDeatil.this);
		}
	}
	

	class ItemOnclickLister implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int tag = Integer.parseInt((String) v.getTag());
			if (currentFragment != null)
				getSupportFragmentManager().beginTransaction()
						.hide(currentFragment).commit();
			switch (tag) {
			case PRODUCT_ATTRBUITE:
				// 设置字体颜色
				// 设置当前viewpager的item
				btn_attrbuite.setTextColor(getResources().getColor(
						R.color.sheng_red));// 红色
				btn_comment.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_buy_records.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_recommend.setTextColor(getResources().getColor(
						R.color.button_text));
				view1.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				view2.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view3.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view4.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				showFragment(attrubitsFragment);

				break;
			case PRODUCT_COMMENT:
				btn_attrbuite.setTextColor(getResources().getColor(
						R.color.button_text));// 红色
				btn_comment.setTextColor(getResources().getColor(
						R.color.sheng_red));
				btn_buy_records.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_recommend.setTextColor(getResources().getColor(
						R.color.button_text));
				view1.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view2.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				view3.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view4.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				showFragment(commentFragment);
				break;
			case PRODUCT_BUY_RECORDS:
				btn_attrbuite.setTextColor(getResources().getColor(
						R.color.button_text));// 红色
				btn_comment.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_buy_records.setTextColor(getResources().getColor(
						R.color.red));
				btn_recommend.setTextColor(getResources().getColor(
						R.color.button_text));
				view1.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view2.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view3.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				view4.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				showFragment(buyRecordsFragment);
				break;
			case PRODUCT_RECOMMEND:
				btn_attrbuite.setTextColor(getResources().getColor(
						R.color.button_text));// 红色
				btn_comment.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_buy_records.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_recommend.setTextColor(getResources().getColor(
						R.color.sheng_red));
				view1.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view2.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view3.setBackgroundColor(getResources().getColor(
						R.color.order_background));
				view4.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				showFragment(recommendedFragment);
				break;
			default:
				break;
			}

		}

	}

	/**
	 * 
	 * 显示底部（商品属性、商品评论、购买记录、同厂推荐）
	 * 
	 */
	private void showButtomItem() {

	//	attrubitsFragment = ProductAttrubitsFragment.getInstance();
		 attrubitsFragment=new ProductAttrubitsFragment();
		//commentFragment = ProductCommentFragment.getInstance();
		commentFragment=new ProductCommentFragment();
		//buyRecordsFragment = BuyRecordsFragment.getInstance();
		buyRecordsFragment=new BuyRecordsFragment();
		//recommendedFragment = RecommendedFragment.getInstance();
		recommendedFragment=new RecommendedFragment();
		currentFragment = attrubitsFragment;

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.setCustomAnimations(R.anim.activity_new,
				R.anim.activity_out);
		transaction.add(R.id.content_frame, attrubitsFragment)
				.show(attrubitsFragment).commit();
	}

	/**
	 * 
	 * Fragment切换
	 * 
	 * @author zd
	 * @version 1.0, 2015年4月17日 下午2:54:19
	 */
	private void showFragment(Fragment fragment) {
		currentFragment = fragment;
		
		//mScrollView.fullScroll(ScrollView.FOCUS_DOWN);//滚动到底部
		if (!fragment.isAdded())

			getSupportFragmentManager().beginTransaction()
					.add(R.id.content_frame, fragment).commit();
		getSupportFragmentManager().beginTransaction().setCustomAnimations(
				R.anim.activity_new, R.anim.activity_out);
		
		getSupportFragmentManager().beginTransaction().show(fragment).commit();
		mScrollView.scrollTo(0,mScrollView.getMeasuredHeight());
	}

	private void resovleJson(String content) {
		try {
			JSONObject obj = new JSONObject(content);
			String type = obj.getString("type");
			if ("1".equals(type)) {
				if (content.contains("data")) {
					String data = obj.getString("data");
					detail = GsonUtils.json2bean(data, GoodDetail.class);
					    LogUtil.d(TAG1, detail.pdMap.commodity_name);
					    mScrollView.setVisibility(View.VISIBLE);
					    ll_butt.setVisibility(View.VISIBLE);
					    addProductToLocal();
					    showUI();
					    tuwenxianqing = detail.pdMap.content;
				}else{
					mScrollView.setVisibility(View.INVISIBLE);
					ll_butt.setVisibility(View.INVISIBLE);
					TipToast.makeText(mContext, obj.getString("msg"), 0).show();
				}
			}else{
				mScrollView.setVisibility(View.INVISIBLE);
				ll_butt.setVisibility(View.INVISIBLE);
				TipToast.makeText(mContext, obj.getString("msg"), 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把商品数据保存到本地
	 */
	private void addProductToLocal(){
		if(null!=detail){
			ProductDao proDao=new ProductDao();
			Product pro=new Product();
			if(null!=detail.pdMap){
				Product product=ProductDao.findOneById(detail.pdMap.id, mContext);
				if(null!=product){
					ProductDao.delProduct(detail.pdMap.id, mContext);
				}
			}
			if(null!=detail.imgArr&&detail.imgArr.size()!=0){
				pro.img=detail.imgArr.get(0);
			}
//			pro.img="asdfasf";
			if(null!=detail.pdMap){
				pro.id=detail.pdMap.id;
				pro.name=detail.pdMap.commodity_name;
				pro.price=detail.pdMap.sprice;
				pro.sales_num=detail.pdMap.total_sell;
			}
//			pro.id=detail.pdMap.id;
//			pro.name=detail.pdMap.commodity_name;
//			pro.price=detail.pdMap.sprice;
//			pro.sales_num=detail.pdMap.total_sell;
			proDao.addProduct(pro, mContext);
		}
	}

	private void updateUI() {
		if (detail != null) {
			mTGoodName.setText(detail.pdMap.commodity_name);
			mPrice.setText("价格：" + detail.pdMap.sprice);
			minBatch.setText(detail.pdMap.min_batch + "双起批");
			mTotalSale.setText("已售：" + detail.pdMap.total_sell);
			mShoeAdress.setText(detail.userMap.province_name + ""
					+ detail.userMap.city_name + "送至");
			yunfei.setText("运费：0.00");
			shoeName.setText(detail.userMap.f_factory_name);
			ImageManager.Load(URLs.IMAGE_URL+"/xlw/"+detail.userMap.head_img, userIcon);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 0:
			if (data != null) {
				Bundle bunde = data.getExtras();
				String pName = bunde.getString("pname");
				String cName = bunde.getString("cname");
				String cTNname = bunde.getString("countryname");
				String feiyong = bunde.getString("feiyong");
				mLoginUserAdress.setText(pName + cName + cTNname);
				yunfei.setText("运费：￥" + feiyong);
			}
			break;
		case 1:
			if (data != null) {
				LogUtil.d("zd", "goback ---  onbackground  ");
				List<GoodSize> selectorGoods = (List<GoodSize>) data
						.getSerializableExtra("goodselector");
				// selectorGoodList.clear();
				LogUtil.d("zd", "selectorGoods.size =="+selectorGoods.size());
				selectorGoodList.addAll(selectorGoods);
				LogUtil.d("zd", "选购商品数量：" + selectorGoodList.size());
			}
		default:
			break;
		}
	}

	/**
	 * 加入购物车
	 * @param buytype 
	 * 
	 * @param selectorGoodLists
	 **/
	private void addShopCart(List<GoodSize> selectorGoodListss, final String buytype) {
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (statusCode == 200) {
					LogUtil.d(TAG, content);
					try {
						JSONObject obj = new JSONObject(content);
						String type = obj.getString("type");
						if ("1".equals(type)) {
							if("0".equals(buytype)){
								selectorGoodList.clear();
								shoppingCarDialog.show();
							}else if("1".equals(buytype)){
								 NowBuyCommint(selectorGoodList);
							}
							
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		String codes = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < selectorGoodListss.size(); i++) {
			GoodSize goodSize = selectorGoodListss.get(i);
			sb.append(goodSize.goods_code + "," + goodSize.quantity + ",颜色:"
					+ goodSize.goods_color + "，" + goodSize.spec_name + ":"
					+ goodSize.spec_value + ",");
		}
		if (sb.length() > 0) {
			codes = sb.substring(0, sb.length() - 1);
		}
		RequestParams params = new RequestParams();
		params.put("uid", user.id);
		params.put("codes", codes);
		params.put("cid", detail.pdMap.id);
		params.put("is_sample", "0");// 是否样品
		params.put("sellid", detail.userMap.id);// 卖家id
		HttpClient.addShopCart(mHandler, params);
	}

	/**
	 * 查询该商品是否有被添加收藏
	 * 
	 * @param selectorGoodLists
	 **/
	private void isCollected() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "is collected  message result:" + content);
				String type = JSONParseUtils.getString(content, "type");
				String msg = JSONParseUtils.getString(content, "msg");
				if (Integer.valueOf(type) < 0) {
					isCollected = true;
					btn_shouchang.setImageResource(R.drawable.yscd);
				}
//				showToast(msg);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "is collected onFailure: content:" + content);
//				showToast("请求失败");
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", AppContext.userInfo.id);
		params.put("ids", goodId);
		HttpClient.IQUERY_IS_COLLECTED(mHandler, params);
	}
	
	/**
	 * 查询该商品是否有被添加收藏
	 * 
	 * @param selectorGoodLists
	 **/
	private void addColletion() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "add collection  message result:" + content);
				String type = JSONParseUtils.getString(content, "type");
				String msg = JSONParseUtils.getString(content, "msg");
				if (Integer.valueOf(type) > 0) {
					isCollected = true;
					btn_shouchang.setImageResource(R.drawable.yscd);
				}
//				showToast(msg);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "add collection onFailure: content:" + content);
				showToast("请求失败");
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", AppContext.userInfo.id);
		params.put("type", "1");//收藏商品type为1，收藏店铺type为1，默认为1
		params.put("ids", goodId);
		Log.d(TAG1, "add collection good id = "+goodId);
		HttpClient.AddCollection(mHandler, params);
	}
	
	/**
	 * 取消收藏
	 * 
	 * @param selectorGoodLists
	 **/
	private void cancelColletion() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "cancel collection message result:" + content);
				String type = JSONParseUtils.getString(content, "type");
				String msg = JSONParseUtils.getString(content, "msg");
				if (Integer.valueOf(type) > 0) {
					isCollected = false;
					btn_shouchang.setImageResource(R.drawable.wscd);
				}
//				showToast(msg);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, " cancel collection onFailure: content:" + content);
				showToast("请求失败");
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", AppContext.userInfo.id);
		params.put("ids", goodId);
		HttpClient.CancelCollection(mHandler, params);
	}
	
	/**
	 * 立即购买
	 * @param selectorGoodList2 
	 * @return
	 */
	private void  NowBuyCommint(List<GoodSize> selectorGoodList2){

		CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, false, ""){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, "立即购买："+content);
		        try {
					JSONObject obj=new JSONObject(content);
					if("1".equals(obj.getString("type"))){
						CommintOrderBean orderBean = GsonUtils.json2bean(content, CommintOrderBean.class);
						Bundle  bundle=new Bundle();
						bundle.putSerializable("commintorder", orderBean);
						bundle.putString("codes", codess);
						bundle.putString(OrderActivity.FROM_INTENT, "1");
						ActivitySwitch.openActivity(OrderActivity.class, bundle,ProductDeatil.this);
					}else{
						TipToast.makeText(ProductDeatil.this,obj.getString("msg") , 0).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		StringBuilder sb=new StringBuilder();
		String codes = null;

		for (int i = 0; i < selectorGoodList2.size(); i++) {
			GoodSize goodSize = selectorGoodList2.get(i);
			sb.append(goodSize.goods_code + "_" + goodSize.quantity + ",");
		}
		if (sb.length() > 0) {
			codes = sb.substring(0, sb.length() - 1);
		}
		codess=codes;
		RequestParams params=new RequestParams();
		params.put("uid",user.id);
		params.put("codes",codes);
		params.put("ty","1");//购买类型 0 购物车结算，1立即购买
		HttpClient.Commint_ShopCart(mHandler, params);
	}

}
