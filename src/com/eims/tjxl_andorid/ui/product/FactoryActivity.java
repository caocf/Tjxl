/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BigImageGridAdapter;
import com.eims.tjxl_andorid.adapter.SmalImageAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.FactoryMessageBean;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyGridView;
import com.eims.tjxl_andorid.weght.MyListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: FactoryActivity
 * @Description: 厂家店铺首页
 * @Author zd
 * @date 2015年4月24日 上午10:50:04
 *************************************************************************** 
 */
public class FactoryActivity extends BaseActivity {

	private static final String TAG1 = "FactoryActivity";
	public static final String ID = "id";
	private HeadView head;
	private MyGridView mGridView;
	private BigImageGridAdapter bigImageGridAdapter;
	private SmalImageAdapter smalImageAdapter;
	private List<ProductBean> mProductBeans;
	private MyListView mListView;
	private TextView BigImage, smallImage;
	private Button btn_search;
	private View btn_collect;
	private ImageView iv_is_sc;
	private TextView tv_is_sc;
	private ImageButton btn_menu;
	private LinearLayout ll_image;
	private View layoutSearch;
	private EditText serach_edit;

	// 厂家信息
	private ImageView icon;
	private TextView factoryName;
	private TextView showRoom;
	private TextView address;
	private TextView tv_ms, tv_wl, tv_fw;
	private ImageView iv_ms, iv_wl, iv_fw;
	private TextView tv_more;

	private ImageLoader mImageLoader;
	private FactoryMessageBean mFactoryMessageBean;

	private User user;
	private String uid;
	private TextView btn_online_customer, btn_factory_new_goods, btn_jmcj;// 加盟厂家

	private boolean isCollected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.factory_home_layout);
		mImageLoader = ImageLoader.getInstance();
		user = AppContext.userInfo;
		uid = getIntent().getExtras().getString(ID);
		Log.d(TAG1, "uid = " + uid);
		findviews();
		initheadview();
		initGridView();
		head.setFocusable(true);
		head.setFocusableInTouchMode(true);
		head.setEnabled(true);
		BigImage.performClick();
		BigImage.setOnClickListener(new ImageBtnOnClick());
		smallImage.setOnClickListener(new ImageBtnOnClick());
		btn_collect.setOnClickListener(new ImageBtnOnClick());
		btn_search.setOnClickListener(new ImageBtnOnClick());
		btn_menu.setOnClickListener(new ImageBtnOnClick());
		tv_more.setOnClickListener(new ImageBtnOnClick());
		btn_jmcj.setOnClickListener(new ImageBtnOnClick());
		// layoutSearch.setOnClickListener(new ImageBtnOnClick());
		// btn_online_customer.setOnClickListener(new ImageBtnOnClick());
		btn_factory_new_goods.setOnClickListener(new ImageBtnOnClick());
		loadStoreMessage();
		loadHotSaleMessage();
	}

	private void initGridView() {
		mProductBeans = new ArrayList<ProductBean>();
		if (bigImageGridAdapter == null) {
			bigImageGridAdapter = new BigImageGridAdapter(this, mProductBeans);
		}
		mGridView.setAdapter(bigImageGridAdapter);
		smalImageAdapter = new SmalImageAdapter(this, mProductBeans);
		mListView.setAdapter(smalImageAdapter);
	}

	private void findviews() {
		head = (HeadView) findViewById(R.id.head);
		mGridView = (MyGridView) findViewById(R.id.mgridview);
		mListView = (MyListView) findViewById(R.id.listview_product);
		BigImage = (TextView) findViewById(R.id.big_image);
		btn_jmcj = (TextView) findViewById(R.id.btn_jmcj);
		btn_online_customer = (TextView) findViewById(R.id.btn_online_customer);
		btn_factory_new_goods = (TextView) findViewById(R.id.btn_factory_new_goods);
		smallImage = (TextView) findViewById(R.id.small_image);
		btn_collect = findViewById(R.id.btn_collect);
		btn_search = (Button) findViewById(R.id.btn_search);
		iv_is_sc = (ImageView) findViewById(R.id.iv_is_sc);
		tv_is_sc = (TextView) findViewById(R.id.tv_is_sc);
		ll_image = (LinearLayout) findViewById(R.id.ll_image_type);
		btn_menu = (ImageButton) findViewById(R.id.left_menu_btn);
		layoutSearch = findViewById(R.id.layout_search);
		serach_edit = (EditText) findViewById(R.id.serach_edit);

		icon = (ImageView) findViewById(R.id.logo);
		factoryName = (TextView) findViewById(R.id.factory_name);
		showRoom = (TextView) findViewById(R.id.show_room);
		address = (TextView) findViewById(R.id.address);
		tv_ms = (TextView) findViewById(R.id.tv_ms);
		tv_wl = (TextView) findViewById(R.id.tv_wl);
		tv_fw = (TextView) findViewById(R.id.tv_fw);
		iv_ms = (ImageView) findViewById(R.id.iv_ms);
		iv_wl = (ImageView) findViewById(R.id.iv_wl);
		iv_fw = (ImageView) findViewById(R.id.iv_fw);

		tv_more = (TextView) findViewById(R.id.more);
	}

	private void initheadview() {
		head.setText("厂家店铺首页");
		head.setGobackVisible();
		head.setRightResource();
	}

	private void setFactoryMessage() {
		mImageLoader.displayImage(
				StringUtils.fixImageUrl(mFactoryMessageBean.getHead_img()),
				icon);
		factoryName.setText(mFactoryMessageBean.getFactory_name());
		address.setText("所在地：" + mFactoryMessageBean.getAddress());
		tv_ms.setText("描述：" + mFactoryMessageBean.getMiao());
		tv_wl.setText("物流：" + mFactoryMessageBean.getWuliu());
		tv_fw.setText("服务：" + mFactoryMessageBean.getFuwu());
		if (mFactoryMessageBean.getMiao_a().equals("0")) {
			iv_ms.setImageResource(R.drawable.pfd);
		} else {
			iv_ms.setImageResource(R.drawable.pfg);
		}

		if (mFactoryMessageBean.getWuliu_a().equals("0")) {
			iv_wl.setImageResource(R.drawable.pfd);
		} else {
			iv_wl.setImageResource(R.drawable.pfg);
		}

		if (mFactoryMessageBean.getFuwu_a().equals("0")) {
			iv_fw.setImageResource(R.drawable.pfd);
		} else {
			iv_fw.setImageResource(R.drawable.pfg);
		}
	}

	class ImageBtnOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			Bundle bundle = null;
			switch (v.getId()) {
			case R.id.big_image:
				mGridView.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.GONE);
				ll_image.setBackgroundResource(R.drawable.slt);
				break;
			case R.id.small_image:
				mGridView.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				ll_image.setBackgroundResource(R.drawable.lbs);
				break;
			case R.id.btn_search:// 搜索
				String mSearchText = serach_edit.getText().toString();
				if (TextUtils.isEmpty(mSearchText)) {
					TipToast.makeText(FactoryActivity.this, "请输入搜索关键字", 0)
							.show();
					return;
				}

				bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putString("key_word", mSearchText);
				ActivitySwitch.openActivity(FactorySearchResultActivity.class,
						bundle, FactoryActivity.this);
				break;
			case R.id.btn_collect:// 收藏
				if (AppContext.isLogin) {
					if (isCollected) {
						cancelColletion();
					} else {
						addColletion();
					}
				} else {
					ActivitySwitch.openActivity(LoginActivity.class, null,
							FactoryActivity.this);
				}
				break;
			case R.id.left_menu_btn:
				bundle = new Bundle();
				bundle.putString("uid", uid);
				ActivitySwitch.openActivity(ClassifySearchActivity.class,
						bundle, FactoryActivity.this);
				break;
			case R.id.layout_search:
				bundle = new Bundle();
				bundle.putString("uid", uid);
				ActivitySwitch.openActivity(FactorySearchActivity.class,
						bundle, FactoryActivity.this);
				break;
			case R.id.more:
				bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putInt("mode", 2);
				ActivitySwitch.openActivity(FactoryProductActivity.class,
						bundle, FactoryActivity.this);
				break;
			case R.id.btn_jmcj:// 加盟厂家
				bundle = new Bundle();
				bundle.putString(JionFactoryActivity.UID,
						mFactoryMessageBean.getId());
				ActivitySwitch.openActivity(JionFactoryActivity.class, bundle,
						FactoryActivity.this);
				break;
			case R.id.btn_online_customer:// 在线客户
				break;
			case R.id.btn_factory_new_goods:// 店铺新品
				bundle = new Bundle();
				bundle.putString("uid", uid);
				bundle.putInt("mode", 1);
				ActivitySwitch.openActivity(FactoryProductActivity.class,
						bundle, FactoryActivity.this);
				break;
			default:
				break;
			}
		}
	}

	private void loadStoreMessage() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "factory message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "店铺信息获取失败");
					return;
				} else {
					mFactoryMessageBean = GsonUtils.json2bean(
							JSONParseUtils.getJSONObject(content, "data"),
							FactoryMessageBean.class);
					setFactoryMessage();
					if (AppContext.isLogin)
						isCollected();// 只有登录时才会获取收藏信息
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
		HttpClient.loadFactoryMessage(mHandler, params);
	}

	private void loadHotSaleMessage() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onFinish() {
				super.onFinish();
				bigImageGridAdapter.notifyDataSetChanged();
				smalImageAdapter.notifyDataSetChanged();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "factory hot sale message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "热销信息获取失败");
					return;
				} else {
					List<ProductBean> list = JSONParseUtils
							.parseProductBeans(content);
					mProductBeans.addAll(list);
					mProductBeans.add(new ProductBean(-1));
					bigImageGridAdapter.notifyDataSetChanged();
					smalImageAdapter.notifyDataSetChanged();
					Log.d(TAG1, "list size = " + list.size()
							+ ", mProductBeans size = " + mProductBeans.size());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG, "onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		HttpClient.loadFactoryHatSaleMessage(mHandler, params);
	}

	public void setCollectBtn() {
		if (isCollected) {
			tv_is_sc.setText("已收藏");
			iv_is_sc.setImageResource(R.drawable.y_sc);
		} else {
			tv_is_sc.setText("    收藏");
			iv_is_sc.setImageResource(R.drawable.w_sc);
		}
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
					setCollectBtn();
				}
				// showToast(msg);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "is collected onFailure: content:" + content);
				// showToast("请求失败");
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", AppContext.userInfo.id);
		params.put("ids", mFactoryMessageBean.getId());
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
					setCollectBtn();
				}
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
		params.put("type", "2");//收藏商品type为1，收藏店铺type为1，默认为1
		params.put("ids", mFactoryMessageBean.getId());
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
					setCollectBtn();
				}
				// showToast(msg);
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
		params.put("ids", mFactoryMessageBean.getId());
		HttpClient.CancelCollection(mHandler, params);
	}

}
