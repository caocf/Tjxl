package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.GoodsListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExchangeProductBillActivity extends BaseActivity {
	private static final String TAG1 = "ExchangeProductBillActivity";
	private HeadView headview;
	private TextView tip_no_content;
	private View layout_content;

	private TextView factory_name;
	private TextView total_price_send;
	private TextView total_price_receive;
	private ListView listView_send;
	private ListView listView_receive;
	private LayoutInflater mInflater;

	private boolean isShowContentView;
	public static List<RefundGoodBean> sendGoodBeans;// 退回的商品清单
	public static List<RefundGoodBean> receiveGoodBeans;// 换来的商品清单
	private GoodsListAdapter mSendAdapter;
	private GoodsListAdapter mReceiveAdapter;

	// 请求参数
	private String uid;
	private String replaceId;// 申请退款订单ID

	private String factoryName;
	private String totalPriceSend;
	private String totalPriceReceive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_exchage_bill);
		findViews();
		initData();
		loadData();
	}

	private void findViews() {
		headview = (HeadView) findViewById(R.id.headview);
		factory_name = (TextView) findViewById(R.id.factory_name);
		total_price_send = (TextView) findViewById(R.id.total_price_send);
		total_price_receive = (TextView) findViewById(R.id.total_price_receive);
		layout_content = findViewById(R.id.layout_content);
		tip_no_content = (TextView) findViewById(R.id.tip_no_content);
		listView_receive = (ListView) findViewById(R.id.good_listview_receive);
		listView_send = (ListView) findViewById(R.id.good_listview_send);

		headview.setText("退货清单");
	}

	private void initData() {
		replaceId = getIntent().getExtras().getString(
				RefundDetailActivity.KEY_RETURN_ID);
		mInflater = LayoutInflater.from(this);

		sendGoodBeans = new ArrayList<RefundGoodBean>();
		receiveGoodBeans = new ArrayList<RefundGoodBean>();

	}

	private void setView() {
		factory_name.setText(factoryName);
		mSendAdapter = new GoodsListAdapter(sendGoodBeans, mInflater, null,
				this, false);
		mReceiveAdapter = new GoodsListAdapter(receiveGoodBeans, mInflater,
				null, this, false);
		listView_receive.setAdapter(mReceiveAdapter);
		listView_send.setAdapter(mSendAdapter);
		total_price_send.setText("小计金额：" + totalPriceSend);
		total_price_receive.setText("小计金额：" + totalPriceReceive);
	}

	/**
	 * 
	 * 获取退款清单信息
	 */
	private void loadData() {
		Log.d(TAG1, "===>loadData");
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在加载...") {
			@Override
			public void onFinish() {
				super.onFinish();
				Log.d(TAG1, "load exchange bill onFinish");
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				Log.d(TAG1, "load exchange bill info onSuccess,result :" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					tip_no_content.setText("数据获取失败");
					isShowContentView = false;
				} else {
					isShowContentView = true;
					tip_no_content.setText("当前没有数据");
					sendGoodBeans = JSONParseUtils.refundGoodBeans(content,
							RefundGoodSizeBean.TYPE_REFUND_BILL, replaceId,"comListOut");
					receiveGoodBeans = JSONParseUtils.refundGoodBeans(content,
							RefundGoodSizeBean.TYPE_REFUND_BILL, replaceId,"comList");
					String data = JSONParseUtils.getJSONObject(content,
							"data");
					factoryName = JSONParseUtils.getString(data,
							"f_factory_name");
					totalPriceSend = JSONParseUtils.getString(data,
							"total_price_out");
					totalPriceReceive = JSONParseUtils.getString(data,
							"total_price");
					setView();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "load exchange bill onFailure,result :" + error);
				tip_no_content.setText("网络请求失败");
				isShowContentView = false;
			}
		};
		if (AppContext.userInfo != null) {
			uid = AppContext.userInfo.id;
		}
		Log.d(TAG1, "uid = " + uid + ",  replaceId = " + replaceId);
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("replaceId", replaceId);
		HttpClient.loadExchangeBillInfo(mHandler, params);
	}
}
