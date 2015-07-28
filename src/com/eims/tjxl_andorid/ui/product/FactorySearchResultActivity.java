/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.content.Intent;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BigImageGridAdapter;
import com.eims.tjxl_andorid.adapter.SmalImageAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.ui.product.FactoryActivity.ImageBtnOnClick;
import com.eims.tjxl_andorid.ui.product.ShoesListFragment.OnNotifyLoadDatasListener;
import com.eims.tjxl_andorid.ui.shop.ShopClassifyActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyGridView;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * 店铺搜索页
 * 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年6月22日 下午2:21:59
 *************************************************************************** 
 */
public class FactorySearchResultActivity extends BaseActivity {

	private static final String TAG1 = "FactorySearchResultActivity";
	public static final String RESULT_TAG_KEY = "tag_key";
	public static final String RESULT_TAG_CLASSIFY = "classify";
	private List<ProductBean> mProductBeansNew;
	private List<ProductBean> mProductBeansSale;
	private List<ProductBean> mProductBeansPriceDec;
	private List<ProductBean> mProductBeansPriceInc;
	private Button btn_newGood, btn_sale;
	private LinearLayout ll_price;
	private TextView mTvPrice;
	private ImageView mIcon;
	private View lineNewGood, lineSale, linePrice;
	private ImageButton imgBtn_goBack;
	private TextView goBackText;
	private ImageButton right_menu_btn;
	private TextView serach_key_word;

	private FragmentManager fragmentManager;
	private ShoesListFragment shoesListFragment;

	private String kword;
	private int mode;
	private int priceOrd = -1;
	private boolean isChangeOrd = false;
	private int curPage = 1;
	private int curPageNew = 1;
	private int curPageSale = 1;
	private int curPagePriceDec = 1;
	private int curPagePriceInc = 1;
	private boolean isLoadFirst = true;
	private String title;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.factory_search_layout);
		init();
		findviews();
		loadDatas();
		btn_newGood.performClick();
		btn_newGood.setOnClickListener(new BtnOnClick());
		btn_sale.setOnClickListener(new BtnOnClick());
		ll_price.setOnClickListener(new BtnOnClick());
		imgBtn_goBack.setOnClickListener(new BtnOnClick());
		goBackText.setOnClickListener(new BtnOnClick());
		right_menu_btn.setOnClickListener(new BtnOnClick());
	}

	private void init() {
		Intent intent = getIntent();
		if (intent != null) {
			uid = intent.getExtras().getString("uid");
			kword = intent.getExtras().getString("key_word");
		}
		fragmentManager = getSupportFragmentManager();
		shoesListFragment = new ShoesListFragment();
		fragmentManager.beginTransaction()
				.replace(R.id.container, shoesListFragment).commit();

		shoesListFragment
				.setOnNotifyLoadDatasListener(new OnNotifyLoadDatasListener() {

					@Override
					public void onNotify() {
						loadDatas();
					}
				});

		mProductBeansNew = new ArrayList<ProductBean>();
		mProductBeansSale = new ArrayList<ProductBean>();
		mProductBeansPriceDec = new ArrayList<ProductBean>();
		mProductBeansPriceInc = new ArrayList<ProductBean>();
	}

	private void findviews() {

		btn_newGood = (Button) findViewById(R.id.bottom_newGood);
		btn_sale = (Button) findViewById(R.id.bottom_sale);
		ll_price = (LinearLayout) findViewById(R.id.ll_price);
		mTvPrice = (TextView) findViewById(R.id.bottom_price);
		mIcon = (ImageView) findViewById(R.id.icon);
		lineNewGood = findViewById(R.id.line_newgood);
		lineSale = findViewById(R.id.line_sale);
		linePrice = findViewById(R.id.line_price);
		imgBtn_goBack = (ImageButton) findViewById(R.id.iv_headview_goback);
		goBackText = (TextView) findViewById(R.id.iv_headview_goback_text);
		right_menu_btn = (ImageButton) findViewById(R.id.right_menu_btn);
		serach_key_word = (TextView) findViewById(R.id.serach_key_word);
		
		serach_key_word.setText(kword);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG1, "mProductBeans size = " + mProductBeansNew.size());
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
				Log.d(TAG1, "factory product message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "店铺商品获取失败");
					return;
				} else {
					List<ProductBean> list = JSONParseUtils
							.parseProductBeans(content);
					if (list.size() > 0) {
						setCurPageX();
						refreshList(list);
					} else {
						Toast.makeText(getApplicationContext(), "已没有更多数据",
								Toast.LENGTH_SHORT).show();
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
		params.put("kword", kword);
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
				mProductBeansPriceInc.clear();
				mProductBeansPriceInc.addAll(list);
				shoesListFragment.reflesh(mProductBeansPriceInc, 0);
			} else {
				mProductBeansPriceDec.clear();
				mProductBeansPriceDec.addAll(list);
				shoesListFragment.reflesh(mProductBeansPriceDec, 0);
			}
		}
	}

	class BtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.bottom_newGood:
				btn_newGood.setTextColor(getResources().getColor(
						R.color.sheng_red));
				btn_sale.setTextColor(getResources().getColor(
						R.color.button_text));
				mTvPrice.setTextColor(getResources().getColor(
						R.color.button_text));
				mIcon.setImageResource(R.drawable.jt);
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
				mIcon.setImageResource(R.drawable.jt);
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
						mIcon.setImageResource(R.drawable.jt);
					} else {
						priceOrd = -1;
						mIcon.setImageResource(R.drawable.jt_s);
					}
				} else {
					isChangeOrd = true;
				}
				if (mProductBeansPriceDec.size() == 0
						|| mProductBeansPriceInc.size() == 0) {
					loadDatas();
				}
				break;
			case R.id.iv_headview_goback:
			case R.id.iv_headview_goback_text:
				AppManager.getAppManager().finishActivity(
						FactorySearchResultActivity.this);
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
				break;
			case R.id.right_menu_btn:
				Bundle bundle = new Bundle();
				bundle.putString("uid", uid);
				ActivitySwitch.openActivity(ClassifySearchActivity.class, bundle,
						FactorySearchResultActivity.this);
				break;
			default:
				break;
			}

		}

	}

}
