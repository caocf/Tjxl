package com.eims.tjxl_andorid.ui.product;

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

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BigImageGridAdapter;
import com.eims.tjxl_andorid.adapter.SmalImageAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.FilterItemBean;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.ui.home.SearchActivity;
import com.eims.tjxl_andorid.ui.product.ShoesListFragment.OnNotifyLoadDatasListener;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.weght.MyGridView;
import com.eims.tjxl_andorid.weght.MyListView;

public class FactoryProductActivity extends BaseActivity {
	private static final String TAG1 = "ProductHotSaleActivity";
	private TextView btn_back;
	private TextView tv_title;
	private ImageView right_imageview;

	private List<ProductBean> mProductBeans;
	private Button btn_newGood, btn_sale;
	private LinearLayout ll_price;
	private TextView mTvPrice;
	private ImageView mIcon;
	private ImageView mIvgoback;
	private View lineNewGood, lineSale, linePrice;

	private FragmentManager fragmentManager;
	private ShoesListFragment shoesListFragment;

	private List<ProductBean> mDataMultiple;
	private List<ProductBean> mDataSale;
	private List<ProductBean> mDataPriceInc;
	private List<ProductBean> mDataPriceDec;
	private List<FilterItemBean> mFilterItemBeans;

	private int curPage = 1;
	private int curPageNew = 1;
	private int curPageSale = 1;
	private int curPagePriceDec = 1;
	private int curPagePriceInc = 1;
	private String searchKey;
	private int pageSize = 20;
	private int mode = -1;// 1：新品，2：销量，3：价格
	private int priceOrd = -1;// 排序 -1：降序 ；1升序
	private boolean isChangeOrd = false;
	private boolean isLoadFirst = true;
	private String title;

	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_classify);
		init();
		findviews();
		// loadDatas();
		btn_newGood.setOnClickListener(new BtnOnClick());
		btn_sale.setOnClickListener(new BtnOnClick());
		ll_price.setOnClickListener(new BtnOnClick());
		mIvgoback.setOnClickListener(new BtnOnClick());
		btn_back.setOnClickListener(new BtnOnClick());
		right_imageview.setOnClickListener(new BtnOnClick());

		if (mode == 1) {
			btn_newGood.performClick();
		} else if (mode == 2) {
			btn_sale.performClick();
		}
		tv_title.setText("全部商品");
	}

	private void init() {
		uid = getIntent().getExtras().getString("uid");
		mode = getIntent().getExtras().getInt("mode");
		fragmentManager = getSupportFragmentManager();
		shoesListFragment = new ShoesListFragment();
		fragmentManager.beginTransaction().add(R.id.container, shoesListFragment).commit();
		shoesListFragment.setOnNotifyLoadDatasListener(new OnNotifyLoadDatasListener() {

			@Override
			public void onNotify() {
				shoesListFragment.setOnFooterRefreshComplete();
				loadDatas();
			}
		});
		mDataMultiple = new ArrayList<ProductBean>();
		mDataPriceInc = new ArrayList<ProductBean>();
		mDataPriceDec = new ArrayList<ProductBean>();
		mDataSale = new ArrayList<ProductBean>();
		mProductBeans = new ArrayList<ProductBean>();
	}

	private void findviews() {

		mIvgoback = (ImageView) findViewById(R.id.iv_headview_goback);
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
	}

	@Override
	protected void onStart() {
		super.onStart();
//		shoesListFragment.reflesh(mProductBeans, 0);
	}

	private void loadDatas() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "factory message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "店铺热销信息获取失败");
					return;
				} else {
					List<ProductBean> lists = JSONParseUtils
							.parseProductBeans(content);
					if (lists.size() > 0) {
						setCurPageX();
						switch (mode) {
						case 1:
							mDataMultiple.addAll(lists);
							break;
						case 2:
							mDataSale.addAll(lists);
							break;
						case 3:
							if (priceOrd == -1) {
								mDataPriceDec.addAll(lists);
							} else if (priceOrd == 1) {
								mDataPriceInc.addAll(lists);
							}
							break;
						default:
							break;
						}

						shoesListFragment.reflesh(JSONParseUtils.parseProductBeans(content), mode);
					} else {

					}
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
		params.put("curPage", curPage + "");
		params.put("pageSize", pageSize + "");
		params.put("fel", mode + "");
		params.put("ord", priceOrd + "");
		HttpClient.loadFactoryProductMessage(mHandler, params);
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
		case 1:
			curPageNew++;
			break;
		case 2:
			curPageSale++;
			break;
		case 3:
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

	class BtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.iv_headview_goback:
			case R.id.left_back:
				AppManager.getAppManager().finishActivity(
						FactoryProductActivity.this);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				break;
			case R.id.right_imageview:
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				ActivitySwitch.openActivity(FactorySearchActivity.class,bundle, FactoryProductActivity.this);
				break;
			case R.id.bottom_newGood:
				mode = 1;
				isChangeOrd = false;
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
				if (mDataMultiple.size() <= 0) {
					loadDatas();
				} else {
					shoesListFragment.reflesh(mDataMultiple, mode);
				}
				break;
			case R.id.bottom_sale:
				mode = 2;
				isChangeOrd = false;
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
				if (mDataSale.size() <= 0) {
					loadDatas();
				} else {
					shoesListFragment.reflesh(mDataSale, mode);
				}
				break;
			case R.id.ll_price:
				Log.d(TAG1, "ll_price clicked ...");
				mode = 3;
				if (isChangeOrd) {
					if (priceOrd == -1) {
						priceOrd = 1;
					} else {
						priceOrd = -1;
					}
				} else {
					isChangeOrd = true;
				}

				Log.d(TAG1, "ll_price clicked ,priceOrd = " + priceOrd);
				if (priceOrd == -1) {
					mIcon.setImageResource(R.drawable.jt_s);
					if (mDataPriceDec.size() <= 0) {
						loadDatas();
					} else {
						shoesListFragment.reflesh(mDataPriceInc, mode);
					}
				} else {
					mIcon.setImageResource(R.drawable.jt);
					if (mDataPriceInc.size() <= 0) {
						loadDatas();
					} else {
						shoesListFragment.reflesh(mDataPriceDec, mode);
					}
				}
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
				break;
			default:
				break;
			}

		}

	}
}
