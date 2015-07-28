package com.eims.tjxl_andorid.ui.shopcart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.GoodsListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.entity.OrderDetailBean;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 退货申请填写页面
 * 
 * @author lzh
 * 
 */
@SuppressLint("ValidFragment")
public class ExchangeProductFragment extends Fragment implements
		OnClickListener {

	private static final String TAG1 = "ExchangeProductFragment";
	private View contentView;
	private TextView tip_no_content;
	private TextView factory_name;
	private TextView btn_choose_send_goods, btn_choose_receive_goods;
	private ListView mSendListView;// 退回商品列表
	private ListView mReceiveListView;// 换回商品列表
	private TextView tv_total_send_price;// 退回商品总价
	private TextView tv_total_receive_price;// 换回商品总价
	private Button btn_commit;// 提交按钮
	private EditText et_apply_message;// 换货说明填写

	private static OrderDetailBean orderDetailBean;
	private static OrderItemBean orderItemBean;
	private GoodsListAdapter mSendAdapter;
	private GoodsListAdapter mReceiveAdapter;
	private Handler mHandler;
	private LayoutInflater mInflater;
	private ImageUpload mImageUpload;
	private boolean isContentViewFound;
	private Context mContext;

	private OrderDetailBean detailBean;
	public static List<RefundGoodBean> exChangeSendGoodBeans;// 选择退回的的商品列表
	public static List<RefundGoodBean> exchangeReceiveGoodBeans;// 选择换回的商品列表
	public static Map<String, RefundGoodSizeBean> exChangeSendGoodsMap;// 用于记录退回货品，key值表示货品编号，value表示退货数
	public static Map<String, RefundGoodSizeBean> exchangeReceiveGoodsMap;// 用于记录换回，key值表示货品编号，value表示退货数

	// 请求可换货商品信息参数
	private String uid;// 登录用户ＩＤ
	private String oids;// 订单ID集合，订单详情ID集合(注：要申请换货的订单ID)
	private String dataArr;// 换回商品字符串，换回商品字符串，格式如下[货号编号,货品数量,货品规格]
	private String dataArrOut;// 换出商品字符串，换回商品字符串，格式如下[货号编号,货品数量,货品规格]
	private String cid;// 城市Id
	private String desc;// 换货说明

	private String seller_id;

	private String factoryName;
	private float totalSendPrice;
	private float totalReceivePrice;

	public ExchangeProductFragment() {
		super();
	}

	public ExchangeProductFragment(OrderDetailBean detailBean,
			OrderItemBean itemBean, Handler handler, Context context) {
		initData(detailBean, itemBean, handler, context);
	}

	private void initData(OrderDetailBean detailBean, OrderItemBean itemBean,
			Handler handler, Context context) {
		this.orderDetailBean = detailBean;
		this.orderItemBean = itemBean;
		this.mHandler = handler;
		this.mContext = context;
		exchangeReceiveGoodBeans = new ArrayList<RefundGoodBean>();
		exChangeSendGoodBeans = new ArrayList<RefundGoodBean>();
		exchangeReceiveGoodsMap = new TreeMap<String, RefundGoodSizeBean>();
		exChangeSendGoodsMap = new TreeMap<String, RefundGoodSizeBean>();
		if (orderDetailBean != null) {
//			oids = orderDetailBean.paMap.id;
			seller_id = orderDetailBean.paMap.seller_id;
		} else {
//			oids = orderItemBean.id;
			seller_id = orderItemBean.seller_id;
		}
		mInflater = LayoutInflater.from(mContext);
		mSendAdapter = new GoodsListAdapter(exChangeSendGoodBeans, mInflater,
				mHandler, mContext, true);
		mReceiveAdapter = new GoodsListAdapter(exchangeReceiveGoodBeans,
				mInflater, mHandler, mContext, true);
		loadCanSendGoods();
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG1, "===>> onResume");
		mSendAdapter.notifyDataSetChanged();
		mReceiveAdapter.notifyDataSetChanged();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_exchange_product, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		contentView = view.findViewById(R.id.layout_content);
		tip_no_content = (TextView) view.findViewById(R.id.tip_no_content);
		factory_name = (TextView) view.findViewById(R.id.factory_name);
		mReceiveListView = (ListView) view.findViewById(R.id.listview_receive);
		mSendListView = (ListView) view.findViewById(R.id.listview_send);
		tv_total_receive_price = (TextView) view.findViewById(R.id.tv_total_price_receive);
		tv_total_send_price = (TextView) view.findViewById(R.id.tv_total_price_send);
		btn_commit = (Button) view.findViewById(R.id.commit);
		btn_choose_receive_goods = (TextView) view.findViewById(R.id.btn_choose_receive_goods);
		btn_choose_send_goods = (TextView) view.findViewById(R.id.btn_choose_send_goods);
		et_apply_message = (EditText) view.findViewById(R.id.et_exchange_description);
		isContentViewFound = true;

		btn_choose_receive_goods.setOnClickListener(this);
		btn_choose_send_goods.setOnClickListener(this);
		btn_commit.setOnClickListener(this);
		mReceiveListView.setAdapter(mReceiveAdapter);
		mSendListView.setAdapter(mSendAdapter);
		et_apply_message.setHint("请填写换货说明");

		setView();
	}

	private void setView() {
		ifShowContentView();
		mSendAdapter.notifyDataSetChanged();
		mReceiveAdapter.notifyDataSetChanged();
		factory_name.setText(factoryName);
	}

	@Override
	public void onClick(View arg0) {
		Bundle bundle;
		switch (arg0.getId()) {
		case R.id.btn_choose_receive_goods:
			bundle = new Bundle();
			bundle.putString("seller_id", seller_id);
			bundle.putString("factoryName", factoryName);
			ActivitySwitch.openActivity(ChooseExchangeProdectActivity.class,
					bundle, this.getActivity());
			break;
		case R.id.btn_choose_send_goods:
			break;
		case R.id.commit:
			commitApply();
			break;

		default:
			break;
		}
	}

	private void commitApply() {
		if (totalSendPrice <= 0) {
			showToast("请选择退回商品");
			return;
		}

		if (totalReceivePrice <= 0) {
			showToast("请选择换回商品");
			return;
		}

		if (totalReceivePrice < totalSendPrice) {
			showToast("换回商品价格不得小于退回商品价格");
			return;
		}

		desc = et_apply_message.getText().toString();
		if (desc == null || desc.trim().equals("")) {
			showToast("请填写换货说明");
			return;
		}
		uploadExchangeApply();
	}

	private void loadCanSendGoods() {
		
		CustomResponseHandler mHandler = new CustomResponseHandler(
				this.getActivity(), true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				int type = Integer.valueOf(JSONParseUtils.getString(content,"type"));
				Log.d(TAG1, "loadCanSendGoods result:" + content + ",type = "+ type);
				if (!JSONParseUtils.isErrorJSONResult(content)) {

					List<RefundGoodBean> list = JSONParseUtils.refundGoodBeans(
							content, RefundGoodSizeBean.TYPE_EXCHANGE_SEND, "");
					if (cid == null) {
						cid = JSONParseUtils.getString(JSONParseUtils.getJSONObject(content, "dtMap"),"address_cid");
					}
					factoryName = JSONParseUtils.getString(JSONParseUtils.getJSONObject(content, "dtMap"),"f_factory_name");
					tip_no_content.setText("该订单中没有可进行换货的商品哦！");
					addSendGoods(list);
					setView();
				} else {
					Log.d(TAG1, "可退款货单信息获取失败");
					tip_no_content.setText("数据获取异常");
					return;
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "loadCanSendGoods onFailure: content:" + content);
				tip_no_content.setText("服务器异常");
				showToast("服务器异常");
			}
		};
		uid = AppContext.userInfo.id;
		RequestParams params = new RequestParams();// 不可空
		params.put("uid", uid);// 不可空
		params.put("seller_id", seller_id);// 不可空
		HttpClient.loadExchangeInfo(mHandler, params);
	}

	private void uploadExchangeApply() {
		CustomResponseHandler mHandler = new CustomResponseHandler(
				this.getActivity(), true, "正在提交...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "uploadExchangeApply result:" + content);
				int type = Integer.valueOf(JSONParseUtils.getString(content,
						"type"));
				if (type > 0) {
					showToast("换货申请提交成功");
//					init();
					ActivitySwitch.finishActivity(ExchangeProductFragment.this.getActivity());
				} else {
					Log.d(TAG1, "提交换货申请失败");
					tip_no_content.setText("提交换货申请失败");
					showToast("换货申请提交失败，type = " + type);
					return;
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "uploadExchangeApply onFailure: content:" + content);
				tip_no_content.setText("服务器异常");
				showToast("服务器异常");
			}
		};
		setCommitApplyParams();
		RequestParams params = new RequestParams();// 不可空
		params.put("uid", uid);// 不可空
		params.put("oids", oids);// 不可空
		params.put("dataArr", dataArr);// 不可空
		params.put("dataArrOut", dataArrOut);// 不可空
		params.put("cid", cid);// 不可空
		params.put("desc", desc);// 可空
		HttpClient.uploadExchangeApply(mHandler, params);
	}
	
	/**
	 * 换货申请提交成功后还原界面，并重新下载换货数据
	 */
	private void init(){
		et_apply_message.setText("");
		et_apply_message.setHint("请填写换货说明");
		
		exchangeReceiveGoodBeans.clear();
		exchangeReceiveGoodsMap.clear();
		exChangeSendGoodBeans.clear();
		exChangeSendGoodsMap.clear();
		mSendAdapter.notifyDataSetChanged();
		mReceiveAdapter.notifyDataSetChanged();
		
		setReceiveTotalPriceText();
		setSendTotalPriceText();
		
		loadCanSendGoods();
	}

	/**
	 * 设置参数
	 */

	private void setCommitApplyParams() {

		uid = AppContext.userInfo.id;
		Log.d(TAG1, "uid = " + uid);
		Log.d(TAG1, "oids = " + oids);
		Set<String> sendGoodIds = new TreeSet<String>();
		Set<String> keySet = exChangeSendGoodsMap.keySet();
		Iterator<String> iteratorSend = keySet.iterator();
		RefundGoodSizeBean sizeBean = null;
		StringBuffer buffer = new StringBuffer();
		while (iteratorSend.hasNext()) {
			sizeBean = exChangeSendGoodsMap.get(iteratorSend.next());
			if (sizeBean.isSelcetd()) {
				buffer.append(sizeBean.good_code + "," + sizeBean.getQuantity()+ ",");
				if (!sendGoodIds.contains(sizeBean.order_id)) {
					sendGoodIds.add(sizeBean.order_id);// 获取退回商品所属的订单id
				}
			}
		}
		dataArrOut = buffer.toString().substring(0, buffer.length() - 1);
		keySet = exchangeReceiveGoodsMap.keySet();
		Iterator<String> iteratorReceive = keySet.iterator();
		buffer = new StringBuffer();
		while (iteratorReceive.hasNext()) {
			sizeBean = exchangeReceiveGoodsMap.get(iteratorReceive.next());
			if (sizeBean.isSelcetd()) {
				buffer.append(sizeBean.good_code + "," + sizeBean.getQuantity()
						+ "," + sizeBean.getSpec_name_value() + ",");
			}

		}
		dataArr = buffer.toString().substring(0, buffer.length() - 1);
		Log.d(TAG1, "dataArrOut = " + dataArrOut);
		Log.d(TAG1, "dataArr    = " + dataArr);
		Log.d(TAG1, "cid = " + cid);

		Iterator<String> iterator = sendGoodIds.iterator();
		
		buffer = new StringBuffer();
		while (iterator.hasNext()) {
			buffer.append(iterator.next() + ",");
		}
		oids = buffer.toString().substring(0, buffer.length() - 1);
		Log.d(TAG1, "sendGoodIds size = "+sendGoodIds.size()+", oids = " + oids);
	}

	/**
	 * 添加退回的商品
	 * 
	 * @param list
	 *            :要添加的退回商品列表
	 */
	private void addSendGoods(List<RefundGoodBean> list) {
		exChangeSendGoodBeans.clear();
		exChangeSendGoodBeans.addAll(list);
		for (int i = exChangeSendGoodBeans.size() - 1; i >= 0; i--) {
			RefundGoodBean goodBean = exChangeSendGoodBeans.get(i);
			boolean isRemoveGood = true;// 是否删除不包含货品的商品对象
			for (int j = goodBean.goodSizeBeans.size() - 1; j >= 0; j--) {
				RefundGoodSizeBean sizeBean = goodBean.goodSizeBeans.get(j);
				Log.d(TAG1, "sizeBean.quanlity = " + sizeBean.quantity);
				if (sizeBean.quantity.trim().equals("0")) {
					goodBean.goodSizeBeans.remove(sizeBean);
					continue;
				}
				isRemoveGood = false;
				if (!exChangeSendGoodsMap.containsKey(sizeBean.getUniqueKey())) {
					exChangeSendGoodsMap.put(sizeBean.getUniqueKey(), sizeBean);
				}
			}
			if (isRemoveGood) {
				exChangeSendGoodBeans.remove(goodBean);
			}
		}
		mSendAdapter.setList(exChangeSendGoodBeans);
		
		
	}

	/**
	 * 添加换回的商品
	 * 
	 * @param list
	 *            :要添加的huan回商品列表
	 */
	private void addReceiveGoods(List<RefundGoodBean> list) {
		Log.d(TAG1, "addReceiveGoods,list size = " + list.size());
		exchangeReceiveGoodBeans.addAll(list);
		for (int i = 0; i < list.size(); i++) {
			RefundGoodBean goodBean = list.get(i);
			for (int j = 0; j < goodBean.goodSizeBeans.size(); j++) {
				Log.d(TAG1, "goodBean.goodSizeBeans size = "
						+ goodBean.goodSizeBeans.size());
				RefundGoodSizeBean sizeBean = goodBean.goodSizeBeans.get(j);
				if (!exchangeReceiveGoodsMap.containsKey(sizeBean
						.getUniqueKey())) {
					exchangeReceiveGoodsMap.put(sizeBean.getUniqueKey(),
							sizeBean);
				}
			}
		}
		mReceiveAdapter.setList(exchangeReceiveGoodBeans);
	}

	/**
	 * 获取退回商品总价
	 */
	public void setSendTotalPriceText() {
		Log.d(TAG1, "=====> setReceiveTotalPriceText");
		Set<String> keySet = exChangeSendGoodsMap.keySet();
		totalSendPrice = 0;
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			RefundGoodSizeBean sizeBean = exChangeSendGoodsMap.get(iterator
					.next());
			if (sizeBean.isSelcetd()) {
				totalSendPrice += (Integer.valueOf(sizeBean.getQuantity()) * Float
						.valueOf(sizeBean.getCommodity_price()));
			}

		}
		BigDecimal b = new BigDecimal(totalSendPrice);  
		totalSendPrice =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); 
		tv_total_send_price.setText("小计金额：" + (totalSendPrice));
		Log.d(TAG1, "totalSendPrice:" + totalSendPrice);
	}

	/**
	 * 获取换回商品总价
	 */
	public void setReceiveTotalPriceText() {
		Log.d(TAG1, "=====> setReceiveTotalPriceText");
		Set<String> keySet = exchangeReceiveGoodsMap.keySet();
		totalReceivePrice = 0;
		Iterator<String> iterator = keySet.iterator();
		Log.d(TAG1, "setReceiveTotalPriceText keySet size =" + keySet.size());
		while (iterator.hasNext()) {
			RefundGoodSizeBean sizeBean = exchangeReceiveGoodsMap.get(iterator
					.next());
			if (sizeBean.isSelcetd()) {
				totalReceivePrice += (Integer.valueOf(sizeBean.getQuantity()) * Float
						.valueOf(sizeBean.getCommodity_price()));
			}

		}
		BigDecimal b = new BigDecimal(totalReceivePrice);  
		totalReceivePrice =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		tv_total_receive_price.setText("小计金额：" + (totalReceivePrice));
		Log.d(TAG1, "totalReceivePrice:" + totalReceivePrice);
	}

	private void ifShowContentView() {
		// 确保在布局实例化结束后执行该方法，以免发生空指针异常
		if (isContentViewFound) {
			if (exChangeSendGoodBeans.size() > 0) {
				contentView.setVisibility(View.VISIBLE);
				tip_no_content.setVisibility(View.INVISIBLE);
			} else {
				contentView.setVisibility(View.INVISIBLE);
				tip_no_content.setVisibility(View.VISIBLE);
			}
		}
	}

	public void showToast(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
