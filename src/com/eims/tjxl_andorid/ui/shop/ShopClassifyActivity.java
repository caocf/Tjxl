package com.eims.tjxl_andorid.ui.shop;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.ui.product.FactorySearchActivity;
import com.eims.tjxl_andorid.ui.product.ShoesListFragment;
import com.eims.tjxl_andorid.ui.product.ShoesListFragment.OnNotifyLoadDatasListener;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;

public class ShopClassifyActivity extends BaseActivity {
	private static final String TAG1 = "ShopClassifyActivity";
	private TextView btn_back;
	private TextView tv_title;
	private ImageView right_imageview;

	private List<ProductBean> mProductBeansNew;
	private List<ProductBean> mProductBeansSale;
	private List<ProductBean> mProductBeansPriceDec;
	private List<ProductBean> mProductBeansPriceInc;

	private Button btn_newGood, btn_sale;
	private LinearLayout ll_price;
	private TextView mTvPrice;
	private ImageView mIcon;
	private View lineNewGood, lineSale, linePrice;

	private FragmentManager fragmentManager;
	private ShoesListFragment shoesListFragment;
	private String kword;
	private String cgid;
	private int curPage = 1;
	private int curPageNew = 1;
	private int curPageSale = 1;
	private int curPagePriceDec = 1;
	private int curPagePriceInc = 1;
	private boolean isLoadFirst = true;
	private String title;

	private int mode;
	private int priceOrd = -1;
	private boolean isChangeOrd = false;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_classify);
		init();
		findviews();
		loadDatas();
		btn_newGood.performClick();
		btn_newGood.setOnClickListener(new BtnOnClick());
		btn_sale.setOnClickListener(new BtnOnClick());
		ll_price.setOnClickListener(new BtnOnClick());
		btn_back.setOnClickListener(new BtnOnClick());
		right_imageview.setOnClickListener(new BtnOnClick());
	}

	private void init() {
		Intent intent = getIntent();
		if (intent != null) {
			uid = intent.getExtras().getString("uid");
			cgid = intent.getExtras().getString("cgid");
			title = intent.getExtras().getString("title");
		}
		fragmentManager = getSupportFragmentManager();
		shoesListFragment = new ShoesListFragment();
		fragmentManager.beginTransaction()
				.replace(R.id.container, shoesListFragment).commit();

		mProductBeansNew = new ArrayList<ProductBean>();
		mProductBeansSale = new ArrayList<ProductBean>();
		mProductBeansPriceDec = new ArrayList<ProductBean>();
		mProductBeansPriceInc = new ArrayList<ProductBean>();

		shoesListFragment
				.setOnNotifyLoadDatasListener(new OnNotifyLoadDatasListener() {

					@Override
					public void onNotify() {
						loadDatas();
					}
				});
	}

	private void findviews() {

		btn_back = (TextView) findViewById(R.id.left_back);
		tv_title = (TextView) findViewById(R.id.mid_title);
		right_imageview = (ImageView) findViewById(R.id.right_imageview);
		btn_newGood = (Button) findViewById(R.id.bottom_newGood);
		btn_sale = (Button) findViewById(R.id.bottom_sale);
		ll_price = (LinearLayout) findViewById(R.id.ll_price);
		mTvPrice = (TextView) findViewById(R.id.bottom_price);
		mIcon = (ImageView) findViewById(R.id.icon);
		lineNewGood = findViewById(R.id.line_newgood);
		lineSale = findViewById(R.id.line_sale);
		linePrice = findViewById(R.id.line_price);

		tv_title.setText(title);
	}

	@Override
	protected void onStart() {
		super.onStart();
		shoesListFragment.reflesh(mProductBeansNew, 0);
	}

	private void loadDatas() {
		getCurPage();
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onFinish() {
				super.onFinish();
				shoesListFragment.setOnFooterRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "factory message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "店铺分类商品信息获取失败");
					return;
				} else {
					List<ProductBean> list = JSONParseUtils
							.parseProductBeans(content);
					if (list.size() == 0) {
//						Toast.makeText(getApplicationContext(), "已没有更多数据",
//								Toast.LENGTH_SHORT).show();
						return;
					}
					isLoadFirst = false;
					setCurPageX();
					refreshList(list);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "onFailure: content:" + content);
			}
		};

		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("cgid", cgid);
		params.put("curPage", curPage + "");
		HttpClient.loadFactoryProductMessage(mHandler, params);
		// for (int i = 0; i < 6; i++) {
		// ProductBean bean = new ProductBean();
		// bean.image =
		// "http://d3.java.shovesoft.com/doov/upload/image/admin/2014/20140418/201404181339355874.jpg";
		// bean.name = "New Balance/NB/新百伦 运动户外爬山";
		// bean.price = "195.0";
		// bean.min_batch = "8751";
		// mProductBeansNew.add(bean);
		// }
	}

	public void getCurPage() {
		switch (mode) {
		case 0:
			curPage = curPageNew++;
			break;
		case 1:
			curPage = curPageSale++;
			break;
		case 2:
			if (priceOrd == -1) {
				curPage = curPagePriceDec++;
			} else {
				curPage = curPagePriceInc++;
			}
			break;
		default:
			break;
		}

	}

	public void setCurPageX() {
		switch (mode) {
		case 0:
			curPageNew++;
			break;
		case 1:
			curPageSale++;
			break;
		case 2:
			if (priceOrd == -1) {
				curPagePriceDec++;
			} else {
				curPagePriceInc++;
			}
			break;
		default:
			break;
		}
	}

	private void refreshList(List<ProductBean> list) {
		if (mode == 0) {
			mProductBeansNew.clear();
			mProductBeansNew.addAll(list);
			shoesListFragment.reflesh(mProductBeansNew, 0);
		} else if (mode == 1) {
			mProductBeansSale.clear();
			mProductBeansSale.addAll(list);
			shoesListFragment.reflesh(mProductBeansSale, 0);
		} else if (mode == 2) {
			if (priceOrd == -1) {

				mProductBeansPriceDec.clear();
				mProductBeansPriceDec.addAll(list);
				shoesListFragment.reflesh(mProductBeansPriceDec, 0);
			} else {
				mProductBeansPriceInc.clear();
				mProductBeansPriceInc.addAll(list);
				shoesListFragment.reflesh(mProductBeansPriceInc, 0);
			}
		}
	}

	class BtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.left_back:
				AppManager.getAppManager().finishActivity(
						ShopClassifyActivity.this);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				break;
			case R.id.right_imageview:
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				ActivitySwitch.openActivity(FactorySearchActivity.class, bundle, ShopClassifyActivity.this);
				break;
			case R.id.bottom_newGood:
				btn_newGood.setTextColor(getResources().getColor(
						R.color.sheng_red));
				btn_sale.setTextColor(getResources().getColor(
						R.color.button_text));
				mTvPrice.setTextColor(getResources().getColor(
						R.color.button_text));
				lineNewGood.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				lineSale.setBackgroundColor(getResources().getColor(
						R.color.white));
				linePrice.setBackgroundColor(getResources().getColor(
						R.color.white));
				mode = 0;
				isChangeOrd = false;
				if (mProductBeansNew.size() == 0) {
					loadDatas();
				}
				break;
			case R.id.bottom_sale:
				btn_newGood.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_sale.setTextColor(getResources()
						.getColor(R.color.sheng_red));
				mTvPrice.setTextColor(getResources().getColor(
						R.color.button_text));
				lineNewGood.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineSale.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				linePrice.setBackgroundColor(getResources().getColor(
						R.color.white));
				mode = 1;
				isChangeOrd = false;
				if (mProductBeansSale.size() == 0) {
					loadDatas();
				}
				break;
			case R.id.ll_price:
				btn_newGood.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_sale.setTextColor(getResources().getColor(
						R.color.button_text));
				mTvPrice.setTextColor(getResources()
						.getColor(R.color.sheng_red));
				lineNewGood.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineSale.setBackgroundColor(getResources().getColor(
						R.color.white));
				linePrice.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				mode = 2;
				if (isChangeOrd) {
					if (priceOrd == -1) {
						priceOrd = 1;
					} else {
						priceOrd = -1;
					}
				} else {
					isChangeOrd = true;
				}

				if (priceOrd == -1) {
					mIcon.setImageResource(R.drawable.jt);
					shoesListFragment.reflesh(mProductBeansPriceInc, mode);
				} else {
					mIcon.setImageResource(R.drawable.jt_s);
					shoesListFragment.reflesh(mProductBeansPriceDec, mode);
				}
				if (mProductBeansPriceDec.size() == 0
						|| mProductBeansPriceInc.size() == 0) {
					loadDatas();
				}
				break;
			default:
				break;
			}

		}

	}
}
