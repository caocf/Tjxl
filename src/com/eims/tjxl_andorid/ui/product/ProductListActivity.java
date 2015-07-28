package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.R.integer;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.MyListAdapter.onItemClickListener;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.FilterItemBean;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.home.SearchActivity;
import com.eims.tjxl_andorid.ui.product.ProductFilterFragment.OnFilterFinishListener;
import com.eims.tjxl_andorid.ui.product.ShoesListFragment.OnNotifyLoadDatasListener;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyPopupWindow;

public class ProductListActivity extends BaseActivity {

	private static final String TAG1 = "ProductList";

	private Context mContext;

	private View view_back;
	private ImageButton right_menu_btn;
	private Button btn_sale, btn_multiple, btn_filter;
	private LinearLayout ll_price;
	private TextView mTvPrice;
	private TextView serach_edit;
	private ImageView mIcon;
	private View lineMultiple, lineSale, linePrice, lineFilter;
	private FragmentManager fragmentManager;
	private ShoesListFragment shoesListFragment;
	private ProductFilterFragment productFilterFragment;
	private ArrayList<String> items;
	private MyPopupWindow popupWindow;
	private List<ProductBean> mDataMultiple;
	private List<ProductBean> mDataSale;
	private List<ProductBean> mDataPriceInc;
	private List<ProductBean> mDataPriceDec;
	private List<FilterItemBean> mFilterItemBeans;

	// 参数
	private int curPage = 1;
	private int curPageNew = 1;
	private int curPageSale = 1;
	private int curPagePriceDec = 1;
	private int curPagePriceInc = 1;
	private String searchKey;
	private int pageSize = 20;
	private String pps;// 属性拼接字符串
	private int mode = -1;// -1：综合，1：销量，2价格
	private int priceOrd = -1;// 排序 -1：降序 ；1升序
	private String pri;// 价格下限
	private String pri2;// 价格上限
	private String bid;// 品牌
	private boolean isChangeOrd = false;
	private boolean isLoadFirst = true;
	private String title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		init();
		findviews();
		initDatas();

		btn_multiple.performClick();

		view_back.setOnClickListener(new BtnOnClick());
		btn_multiple.setOnClickListener(new BtnOnClick());
		btn_sale.setOnClickListener(new BtnOnClick());
		ll_price.setOnClickListener(new BtnOnClick());
		btn_filter.setOnClickListener(new BtnOnClick());
		right_menu_btn.setOnClickListener(new BtnOnClick());

		searchProduct();
	}

	private void init() {
		mContext = this;
		searchKey = getIntent().getStringExtra("key_word");
		mDataMultiple = new ArrayList<ProductBean>();
		mDataPriceInc = new ArrayList<ProductBean>();
		mDataPriceDec = new ArrayList<ProductBean>();
		mDataSale = new ArrayList<ProductBean>();
		mFilterItemBeans = new ArrayList<FilterItemBean>();
	}

	private void findviews() {
		view_back = findViewById(R.id.view_back);
		right_menu_btn = (ImageButton) findViewById(R.id.right_menu_btn);
		btn_multiple = (Button) findViewById(R.id.bottom_multiple);
		btn_sale = (Button) findViewById(R.id.bottom_sale);
		btn_filter = (Button) findViewById(R.id.bottom_filter);
		ll_price = (LinearLayout) findViewById(R.id.ll_price);
		mTvPrice = (TextView) findViewById(R.id.bottom_price);
		serach_edit = (TextView) findViewById(R.id.serach_edit);
		mIcon = (ImageView) findViewById(R.id.icon);
		lineMultiple = findViewById(R.id.line_multiple);
		lineSale = findViewById(R.id.line_sale);
		linePrice = findViewById(R.id.line_price);

		lineFilter = findViewById(R.id.line_filter);

		serach_edit.setText(searchKey);

	}

	private void initProperties() {
		Log.d(TAG1, "===>>initProperties");
		mDataMultiple.clear();
		mDataPriceDec.clear();
		mDataPriceInc.clear();
		mDataSale.clear();

		curPage = 1;
		curPageNew = 1;
		curPageSale = 1;
		curPagePriceDec = 1;
		curPagePriceInc = 1;
		priceOrd = -1;
		isLoadFirst = true;
		isChangeOrd = false;
		btn_multiple.performClick();
	}

	private void initDatas() {
		fragmentManager = getSupportFragmentManager();
		shoesListFragment = new ShoesListFragment();
		productFilterFragment = new ProductFilterFragment();
		fragmentManager.beginTransaction().add(R.id.container, productFilterFragment).commit();
		fragmentManager.beginTransaction().add(R.id.container, shoesListFragment).commit();
		shoesListFragment
				.setOnNotifyLoadDatasListener(new OnNotifyLoadDatasListener() {

					@Override
					public void onNotify() {
						searchProduct();
					}
				});

		productFilterFragment
				.setOnFilterFinishListener(new OnFilterFinishListener() {

					@Override
					public void filterFinish(String pps, String pri,
							String pri2, String bid) {
						showShoesListFragment();
						setPps(pps);
						setPri(pri);
						setPri2(pri2);
						setBid(bid);
						initProperties();
						// searchProduct();
					}
				});
		items = new ArrayList<String>();
	}

	private void searchProduct() {
		getCurPage();
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onFinish() {
				super.onFinish();
				if(shoesListFragment.isCreatedView()) shoesListFragment.setOnFooterRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "searchProduct result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "商品搜索失败");
					return;
				} else {
					List<ProductBean> lists = JSONParseUtils
							.parseProductBeans(content);
					Log.d(TAG1, "onSuccess lists size = " + lists.size()
							+ ", mode = " + mode);

					setCurPageX();
					switch (mode) {
					case -1:
						mDataMultiple.addAll(lists);
						shoesListFragment.reflesh(mDataMultiple, mode);
						break;
					case 1:
						mDataSale.addAll(lists);
						shoesListFragment.reflesh(mDataSale, mode);
						break;
					case 2:
						if (priceOrd == -1) {
							mDataPriceInc.addAll(lists);
							shoesListFragment.reflesh(mDataPriceInc, mode);
						} else if (priceOrd == 1) {
							mDataPriceDec.addAll(lists);
							shoesListFragment.reflesh(mDataPriceDec, mode);
						}
						break;
					default:
						break;
					}
				
					if (lists.size() == 0) showNoMoreToast(lists);
					
					if (mFilterItemBeans.size() == 0) {
						mFilterItemBeans = JSONParseUtils
								.ParseFilterItems(content);
						productFilterFragment.setData(mFilterItemBeans);
					}
					
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG, "onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		String type = null;
		Log.d(TAG1, "searchKey = " + searchKey + ",curPage = " + curPage
				+ ",mode = " + mode);
		params.put("serachKey", searchKey);
		params.put("curPage", curPage + "");
		params.put("pageSize", pageSize + "");
		params.put("pps", pps);
		params.put("fel", mode + "");
		params.put("ord", priceOrd + "");
		params.put("pri", pri);
		params.put("pri2", pri2);
		params.put("bid", bid);
		HttpClient.LoadSearchProduct(mHandler, params);
	}
	
	/**
	 * 是否弹出Toast，提示“已没有更多商品了哦”
	 * @param lists
	 */
	private void showNoMoreToast(List<ProductBean> lists) {
		boolean isShow = false;
		switch (mode) {
		case -1:
			if(mDataMultiple.size() > 0) isShow = true;
			break;
		case 1:
			if(mDataSale.size() > 0) isShow = true;
			break;
		case 2:
			if (priceOrd == -1) {
				if(mDataPriceInc.size() > 0) isShow = true;
			} else if (priceOrd == 1) {
				if(mDataPriceDec.size() > 0) isShow = true;
			}
			break;
		default:
			break;
		}
		
		if (isShow) showToast("已没有更多商品了哦！");
	}

	private void setPopupwindow() {

		right_menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				popupWindow = new MyPopupWindow(ProductListActivity.this,
						right_menu_btn.getWidth() * 3, HeadView.items);
				// popupWindow.setAnimationStyle(R.style.PopupAnimation);
				// popupWindow.setAnimationStyle(Style.CONFIRM);
				popupWindow.showAsDropDown(right_menu_btn, 0, 0);
				popupWindow.update();
				popupWindow.setOnItemClickListener(new onItemClickListener() {
					@Override
					public void click(int position, View view) {
						AppManager.getAppManager().finishToHome();
						if (position != 4) {
							MainActivity.radioGroup.getChildAt(position)
									.performClick();
							MainActivity.mainPager.setCurrentItem(position);
						} else {
							ActivitySwitch.openActivity(SearchActivity.class,
									null, (Activity) ProductListActivity.this);
						}

					}
				});
			}
		});
	}

	class BtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.view_back:
				ActivitySwitch.finishActivity(ProductListActivity.this);
				break;
			case R.id.right_menu_btn:
				setPopupwindow();
				break;
			case R.id.bottom_multiple:
				isChangeOrd = false;
				mode = -1;
				btn_multiple.setTextColor(getResources().getColor(
						R.color.sheng_red));
				btn_sale.setTextColor(getResources().getColor(
						R.color.button_text));
				mTvPrice.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_filter.setTextColor(getResources().getColor(
						R.color.button_text));
				lineMultiple.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				lineSale.setBackgroundColor(getResources().getColor(
						R.color.white));
				linePrice.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineFilter.setBackgroundColor(getResources().getColor(
						R.color.white));
				showShoesListFragment();
				if (mDataMultiple.size() <= 0) {
					searchProduct();
				} else {
					shoesListFragment.reflesh(mDataMultiple, mode);
				}
				break;
			case R.id.bottom_sale:
				isChangeOrd = false;
				mode = 1;
				btn_multiple.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_sale.setTextColor(getResources()
						.getColor(R.color.sheng_red));
				mTvPrice.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_filter.setTextColor(getResources().getColor(
						R.color.button_text));
				lineMultiple.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineSale.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				linePrice.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineFilter.setBackgroundColor(getResources().getColor(
						R.color.white));
				showShoesListFragment();
				if (mDataSale.size() <= 0) {
					searchProduct();
				} else {
					shoesListFragment.reflesh(mDataSale, mode);
				}
				
				break;
			case R.id.ll_price:
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
					mIcon.setImageResource(R.drawable.jt_s);
					
					if (mDataPriceInc.size() <= 0) {
						searchProduct();
					} else {
						shoesListFragment.reflesh(mDataPriceInc, mode);
					}
				} else {
					mIcon.setImageResource(R.drawable.jt);
					
					if (mDataPriceDec.size() <= 0) {
						searchProduct();
					} else {
						shoesListFragment.reflesh(mDataPriceDec, mode);
					}
				}

				btn_multiple.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_sale.setTextColor(getResources().getColor(
						R.color.button_text));
				mTvPrice.setTextColor(getResources()
						.getColor(R.color.sheng_red));
				btn_filter.setTextColor(getResources().getColor(
						R.color.button_text));
				lineMultiple.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineSale.setBackgroundColor(getResources().getColor(
						R.color.white));
				linePrice.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				lineFilter.setBackgroundColor(getResources().getColor(
						R.color.white));
				showShoesListFragment();

				break;
			case R.id.bottom_filter:
				isChangeOrd = false;
				btn_multiple.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_sale.setTextColor(getResources().getColor(
						R.color.button_text));
				mTvPrice.setTextColor(getResources().getColor(
						R.color.button_text));
				btn_filter.setTextColor(getResources().getColor(
						R.color.sheng_red));
				lineMultiple.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineSale.setBackgroundColor(getResources().getColor(
						R.color.white));
				linePrice.setBackgroundColor(getResources().getColor(
						R.color.white));
				lineFilter.setBackgroundColor(getResources().getColor(
						R.color.sheng_red));
				showFilterFragment();
				productFilterFragment.refresh();
				break;
			default:
				break;
			}
		}
	}

	public void getCurPage() {
		switch (mode) {
		case -1:
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
		case -1:
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

	public void showShoesListFragment() {
		fragmentManager.beginTransaction().show(shoesListFragment).commit();
		fragmentManager.beginTransaction().hide(productFilterFragment).commit();
	}
	
	public void showFilterFragment() {
		fragmentManager.beginTransaction().hide(shoesListFragment).commit();
		fragmentManager.beginTransaction().show(productFilterFragment).commit();
	}

	public String getPps() {
		return pps;
	}

	public void setPps(String pps) {
		this.pps = pps;
	}

	public String getPri() {
		return pri;
	}

	public void setPri(String pri) {
		this.pri = pri;
	}

	public String getPri2() {
		return pri2;
	}

	public void setPri2(String pri2) {
		this.pri2 = pri2;
	}

	public String getBid() {
		return bid;
	}

	public void setBid(String bid) {
		this.bid = bid;
	}

}
