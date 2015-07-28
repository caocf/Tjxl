package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.ExchangeDetailBean;
import com.eims.tjxl_andorid.entity.LogicticsCampanyBean;
import com.eims.tjxl_andorid.entity.RefundDetailBean;
import com.eims.tjxl_andorid.ui.shopcart.RefundDetailActivity.SpinnerAdapter;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ExchangeProductDetailActivity extends BaseActivity implements
		OnClickListener {
	private static String TAG1 = "ExchangeProductDetailActivity";
	// status为1时（审核通过），reason，id,uid不能为空
	// status为2时（买家发货），logistics_id，logistics_code，id,uid不能为空
	// status为3时（卖家确认收货），id,uid不能为空
	// status为5时（拒绝），id,uid，reason不能为空
	// status为6时（买家取消申请），id,uid不能为空
	// status为9时(驳回 申请失败)，id,uid，reject_desc不能为空
	// status为10时（修改金额），id,uid，total_price不能为空
	public static final String KEY_RETURN_ID = "return_id";// 这个ID是退货之产生的一个退货id，申请退货之后生成的，并不是订单ID
	public static final String KEY_PAGE_TYPE = "page_type";// 页面类型，1：展示详情，2，发货
	public static final int PAGE_TYPE_DETAIL = 1;
	public static final int PAGE_TYPE_LOGISTICS = 2;

	private HeadView headview;
	private View content_view;
	private TextView tip_no_content;// 内容为空提示
	private TextView refund_code;// 退款编号
	private ImageView good_icon;
	private TextView good_name;
	/*
	 * 物流信息
	 */
	private View layout_logistics_name, layout_logistics_id,
			layout_logistics_time;
	private TextView tv_logistics_company, tv_logistics_code;// 物流公司，物流编号
	private EditText et_logistics_code;// 物流编号
	private Spinner et_logistics_company;// 物流公司名称和id
	private TextView tv_logistics_time_title, tv_logistics_time_value;// 物流公司，物流编号

	/*
	 * 四个点击按钮
	 */
	private TextView btn_contact_seller, btn_refund_bill, btn_send_goods,
			btn_right_defense;

	/*
	 * 状态信息
	 */
	private TextView refund_status, time_title_0, time_title_1, time_title_3,
			time_title_8,
			refund_apply_time,// 退款状态，申请退款时间
			// 卖家同意退款时间，卖家收货时间，卖家退款时间
			refund_seller_agree_time, seller_receive_time, seller_refund_time,
			refund_sum, refund_explain;// 退款金额，退款描述
	private TextView tv_refund_finish_time;

	private boolean isShowContentView;
	private int pageType = PAGE_TYPE_DETAIL;
	private String uid = "1";// 用户登录id
	private String replaceId;// 退款订单id

	private ImageLoader mImageLoader;
	private LayoutInflater mInflater;
	private List<LogicticsCampanyBean> logicticsCampanies;
	private SpinnerAdapter mSpinnerAdapter;
	private String logicticsCompanyId;
	private String logicticsCompanyName;
	private String logicticsCode;

	// 售后状态(0申请售后 1卖家同意售后 2买家发货 3卖家确认收货 4卖家退款[不要] 5卖家拒绝 6退款关闭 7申请超时[不要] 8完成时间);
	private ExchangeDetailBean exchangeDetailBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exchange_detail);
		Log.d(TAG1, "===> oncreate");
		findViews();
		initData();
		loadData();
		loadLogicticsInfo();
	}

	public void findViews() {
		Log.d(TAG1, "===> findViews");
		headview = (HeadView) findViewById(R.id.headview);
		tip_no_content = (TextView) findViewById(R.id.tip_no_content);
		content_view = findViewById(R.id.layout_content);

		good_icon = (ImageView) findViewById(R.id.good_icon);
		good_name = (TextView) findViewById(R.id.good_name);
		refund_code = (TextView) findViewById(R.id.tv_refund_code);

		// 物流信息
		layout_logistics_name = findViewById(R.id.layout_logistics_company);
		layout_logistics_id = findViewById(R.id.layout_logistics_id);
		layout_logistics_time = findViewById(R.id.layout_logistics_time);
		tv_logistics_company = (TextView) findViewById(R.id.tv_logistics_company);
		tv_logistics_code = (TextView) findViewById(R.id.tv_logistics_code);
		et_logistics_company = (Spinner) findViewById(R.id.et_logistics_company);
		et_logistics_code = (EditText) findViewById(R.id.et_logistics_code);
		tv_logistics_time_title = (TextView) findViewById(R.id.tv_send_time_title);
		tv_logistics_time_value = (TextView) findViewById(R.id.tv_send_time);

		// 四个按钮
		btn_contact_seller = (TextView) findViewById(R.id.tv_contact_seller);
		btn_refund_bill = (TextView) findViewById(R.id.tv_refund_bill);
		btn_send_goods = (TextView) findViewById(R.id.tv_confrim_send_good);
		btn_right_defense = (TextView) findViewById(R.id.tv_right_defense);

		// 状态信息
		refund_status = (TextView) findViewById(R.id.tv_refund_state);
		refund_apply_time = (TextView) findViewById(R.id.tv_apply_refund_time);
		refund_seller_agree_time = (TextView) findViewById(R.id.tv_agree_refund_time);
		seller_receive_time = (TextView) findViewById(R.id.tv_comfirm_receive_good_time);
		seller_refund_time = (TextView) findViewById(R.id.tv_seller_refund_time);
		time_title_1 = (TextView) findViewById(R.id.tv_time_1);
		time_title_3 = (TextView) findViewById(R.id.tv_time_3);
		time_title_8 = (TextView) findViewById(R.id.tv_time_8);
		refund_sum = (TextView) findViewById(R.id.tv_refund_money);
		refund_explain = (TextView) findViewById(R.id.tv_refund_explain);
		tv_refund_finish_time = (TextView) findViewById(R.id.tv_refund_finish_time);

		btn_refund_bill.setOnClickListener(this);
		btn_contact_seller.setOnClickListener(this);
		btn_right_defense.setOnClickListener(this);
		btn_send_goods.setOnClickListener(this);
		headview.setText("订单详情");
	}

	private void initData() {
		Log.d(TAG1, "===>initData");
		replaceId = getIntent().getExtras().getString(KEY_RETURN_ID);
		pageType = getIntent().getExtras().getInt(KEY_PAGE_TYPE);
		mImageLoader = ImageLoader.getInstance();
		mInflater = LayoutInflater.from(this);
		logicticsCampanies = new ArrayList<LogicticsCampanyBean>();
		logicticsCampanies.add(new LogicticsCampanyBean("","", "请选择"));
		mSpinnerAdapter = new SpinnerAdapter();
	}

	private void setView() {
		ifShowContentView();
		if (exchangeDetailBean == null) {
			isShowContentView = false;
			ifShowContentView();
			return;
		}

		// 不用区分状态的属性
		mImageLoader.displayImage(
				URLs.IMAGE_URL + exchangeDetailBean.commodity_img_m,
				good_icon);
		good_name.setText(exchangeDetailBean.getCommodity_name());
		refund_code.setText("退款编号：" + exchangeDetailBean.getReplace_code());
		refund_status.setText(exchangeDetailBean.getStatu_name());
		refund_apply_time.setText(exchangeDetailBean.getRepace_time0());

		// 卖家同意换货时间
		if (exchangeDetailBean.getRepace_time1() != null
				&& !exchangeDetailBean.getRepace_time1().trim().equals("")) {
			time_title_1.setVisibility(View.VISIBLE);
			refund_seller_agree_time.setVisibility(View.VISIBLE);
			refund_seller_agree_time
					.setText(exchangeDetailBean.getRepace_time1());
		}
		// 卖家收货时间
		if (exchangeDetailBean.getRepace_time4() != null
				&& !exchangeDetailBean.getRepace_time4().trim().equals("")) {
			time_title_3.setVisibility(View.VISIBLE);
			seller_receive_time.setVisibility(View.VISIBLE);
			seller_receive_time.setText(exchangeDetailBean.getRepace_time4());
		}
		// 卖家发货时间
		if (exchangeDetailBean.getRepace_time5() != null
				&& !exchangeDetailBean.getRepace_time5().trim().equals("")) {
			time_title_8.setVisibility(View.VISIBLE);
			seller_refund_time.setVisibility(View.VISIBLE);
			seller_refund_time.setText(exchangeDetailBean.getRepace_time5());
		}

		refund_sum.setText(exchangeDetailBean.getAdjust_price());// 卖家需退金额
		refund_explain.setText(exchangeDetailBean.getDescription());// 退款说明
		tv_refund_finish_time.setText("退款完成时间：未知"
				+ exchangeDetailBean.getRepace_time6());// 退款申请结束时间
		if (pageType == PAGE_TYPE_DETAIL) {// 展示退款详情的物流信息
			layout_logistics_time.setVisibility(View.VISIBLE);
			layout_logistics_id.setVisibility(View.VISIBLE);
			tv_logistics_code.setVisibility(View.VISIBLE);
			tv_logistics_company.setVisibility(View.VISIBLE);
			tv_logistics_time_title.setVisibility(View.VISIBLE);
			tv_logistics_time_value.setVisibility(View.VISIBLE);
			tv_logistics_code.setText(exchangeDetailBean.getDelivery_logistics_number());
			tv_logistics_company.setText(exchangeDetailBean.getDelivery_logistics_name());
			tv_logistics_time_value.setText(exchangeDetailBean.getRepace_time6());// 并不是这个时间，该时间仅用于测试
			if (exchangeDetailBean.getDelivery_logistics_number() == null
					|| exchangeDetailBean.getDelivery_logistics_number().trim().equals("")) {
				tv_logistics_time_value.setText("您还没有发货");
			}
		} else if (pageType == PAGE_TYPE_LOGISTICS) {// 提供物流信息填写页面
			layout_logistics_time.setVisibility(View.VISIBLE);
			layout_logistics_name.setVisibility(View.VISIBLE);
			layout_logistics_id.setVisibility(View.VISIBLE);
			et_logistics_code.setVisibility(View.VISIBLE);
			et_logistics_company.setVisibility(View.VISIBLE);
			btn_send_goods.setVisibility(View.VISIBLE);
			et_logistics_company.setAdapter(mSpinnerAdapter);
			et_logistics_company
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							if (arg2 > 0) {
								logicticsCompanyId = logicticsCampanies.get(arg2).logistics_en;// 物流ID
								logicticsCompanyName = logicticsCampanies.get(arg2).logistics_name;
							} else {
								logicticsCompanyId = "";
								logicticsCompanyName = "";
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							logicticsCompanyId = "";
							logicticsCompanyName = "";
						}
					});
		}

	}

	@Override
	public void onClick(View arg0) {
		Bundle bundle = null;
		switch (arg0.getId()) {
		case R.id.tv_refund_bill:
			bundle = new Bundle();
			bundle.putString(KEY_RETURN_ID, replaceId);
			ActivitySwitch.openActivity(ExchangeProductBillActivity.class, bundle,
					this);
			break;
		case R.id.tv_confrim_send_good:
			commitLogicticsInfo();
			break;
		default:
			break;
		}
	}

	private void ifShowContentView() {
		if (isShowContentView) {
			content_view.setVisibility(View.VISIBLE);
			tip_no_content.setVisibility(View.INVISIBLE);
		} else {
			content_view.setVisibility(View.INVISIBLE);
			tip_no_content.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 提交物流信息
	 */
	private void commitLogicticsInfo(){
		
		if(logicticsCompanyId == null || logicticsCompanyId.trim().equals("")){//判断物流公司Id
			showToast("请选择物流公司");
			return;
		}
		logicticsCode = et_logistics_code.getText().toString();
		if(logicticsCode == null || logicticsCode.trim().equals("")){//判断物流单号
			showToast("请填写物流单号");
			return;
		}
		uploadLogicticsInfo();
	}

	/**
	 * 上传物流信息 status为2时（买家发货），logistics_id，logistics_code，id,uid不能为空
	 */
	private void uploadLogicticsInfo() {
		Log.d(TAG1, "===>loadData");
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在加载...") {
			@Override
			public void onFinish() {
				super.onFinish();
				Log.d(TAG1, "===>uploadLogicticsInfo finish");
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				Log.d(TAG1, "uploadLogicticsInfo onSuccess ,result :" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					showToast("发货信息提交失败");
				} else {
					showToast("发货信息提交成功");
					et_logistics_code.setVisibility(View.GONE);
					layout_logistics_name.setVisibility(View.GONE);
					tv_logistics_code.setVisibility(View.VISIBLE);
					tv_logistics_company.setVisibility(View.VISIBLE);
					tv_logistics_code.setText(logicticsCode);
					tv_logistics_company.setText(logicticsCompanyName);
					btn_send_goods.setText("已发货");
					btn_send_goods.setBackgroundResource(R.drawable.selector_corner_5_btn_gray);
					btn_send_goods.setFocusable(false);
					btn_send_goods.setClickable(false);
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("id", replaceId);
		params.put("uid", uid);
		params.put("logistics_id", logicticsCompanyId);
		params.put("logistics_code", et_logistics_code.getText().toString());
//		params.put("logistics_id", "13");
//		params.put("logistics_code", "hhhhhhh");
		HttpClient.uploadLogicticsInfo(mHandler, params);
	}

	/**
	 * 获取物流信息
	 */
	private void loadLogicticsInfo() {
		Log.d(TAG1, "===>loadData");
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在加载...") {
			@Override
			public void onFinish() {
				super.onFinish();
				Log.d(TAG1, "===>uploadLogicticsInfo finish");
				setView();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				Log.d(TAG1, "loadLogicticsInfo onSuccess result :" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					showToast("物流信息获取失败");
				} else {
					logicticsCampanies.addAll(JSONParseUtils
							.paserLogicticsInfo(content));
					mSpinnerAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "loadLogicticsInfo  error,msg :" + content);
			}
		};
		RequestParams params = new RequestParams();
		HttpClient.loadLogicticsInfo(mHandler, params);
	}

	/**
	 * 
	 * 获取换货详情信息
	 */
	private void loadData() {
		Log.d(TAG1, "===>loadData");
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在加载...") {
			@Override
			public void onFinish() {
				super.onFinish();
				Log.d(TAG1, "===>finish");
				setView();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				Log.d(TAG1, "load exchange detail info onSuccess,result :" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					tip_no_content.setText("数据获取失败");
					isShowContentView = false;
				} else {
					isShowContentView = true;
					tip_no_content.setText("当前没有数据");
					exchangeDetailBean = GsonUtils.json2bean(
							JSONParseUtils.getJSONObject(content, "data"),
							ExchangeDetailBean.class);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "load exchange detail info onFailure 2 ,result :" + error);
				tip_no_content.setText("网络请求失败");
				isShowContentView = false;
			}
		};
		if (AppContext.userInfo != null) {
			uid = AppContext.userInfo.id;
		}
		RequestParams params = new RequestParams();
		Log.d(TAG1, "uid = "+uid+", replaceId = "+replaceId);
		params.put("uid", uid);
		params.put("replaceId", replaceId);// 测试退货ＩＤ
		HttpClient.loadExchangeDetailInfo(mHandler, params);
	}

	class SpinnerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return logicticsCampanies == null ? 0 : logicticsCampanies.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			if (view == null) {
				view = mInflater.inflate(R.layout.layout_spinner_item, null);
			}
			TextView textView = (TextView) view.findViewById(R.id.textview);
			textView.setText(logicticsCampanies.get(arg0).logistics_name);
			return view;
		}

	}
}

