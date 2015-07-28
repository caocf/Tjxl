package com.eims.tjxl_andorid.ui.shopcart;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.GoodsListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.base.BaseCameraFragment;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean;
import com.eims.tjxl_andorid.entity.Picture;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.Base64;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.imageupload.BitmapUtils;
import com.eims.tjxl_andorid.utils.imageupload.ImageFolder;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.eims.tjxl_andorid.utils.imageupload.ImageUrlBean;
import com.eims.tjxl_andorid.utils.imageupload.MyCustomResponseHandler;
import com.eims.tjxl_andorid.utils.imageupload.SelectPicPopupWindow;
import com.eims.tjxl_andorid.utils.imageupload.Tip;
import com.eims.tjxl_andorid.weght.ImageViewWithDel;
import com.eims.tjxl_andorid.weght.ImageViewWithDel.MyImageViewCallBack;
import com.eims.tjxl_andorid.weght.MyGridView;

@SuppressLint({ "ValidFragment", "NewApi" })
public class RefundFragment extends BaseCameraFragment implements OnClickListener {

	private static final String TAG = "RefundFragment";
	private static final String TAG1 = "RefundFragment";

	private View contentView;
	private TextView tip_no_content;
	private TextView factory_name;
	private ListView mListView;
	private static OrderDetailBean orderDetailBean;
	private static OrderItemBean itemBean;
	private GoodsListAdapter mAdapter;
	private TextView tv_total_price;// 商品总价计算
	private TextView tv_total_refund_price_real;// 最总退款显示文本
	private Button btn_commit;// 提交按钮
	private EditText et_apply_message;// 退款说明填写
	private View view_select_pic;// 上传图片按钮
	private MyGridView myGridView;// 用于显示上传图片

	private static float totalPrice;// 原始总价
	private static float totalRefundPrice;// 退款商品总价格
	private float totalRefundRriceReal;// 实际退款金额，包括退货价钱与运费补偿之类的总和

	private float extraRefundMonny = 0;// 模拟 补偿运费的一个数据
	private Handler mHandler;
	private ImageUpload mImageUpload;

	private boolean isContentViewFound;

	private boolean canCommit = true;

	public static List<RefundGoodBean> refundGoodBeans;// 可以申请退货的商品列表
	public static Map<String, RefundGoodSizeBean> refundGoodsMap;// 用于退货记录，key值表示货品编号，value表示退货数
	public static String factoryName;

	// 申请退款参数
	private String uid;// 会员登录ＩＤ
	private String oid;// 订单ID
	private String is_take;// 是否收到货品
	private String dataArr;// 退货数据字符串
	private String yun;// 运费补偿
	private String total_price;// 总金额
	private String imgs;// 图片证明
	private String desc;// 退货说明
	private Context mContext;

	public RefundFragment(){}
	public RefundFragment(OrderDetailBean bean, OrderItemBean itemBean,
			Handler handler, Context context) {
		orderDetailBean = bean;
		this.itemBean = itemBean;
		if (orderDetailBean != null) {
			oid = bean.paMap.id;
			factoryName = bean.paMap.f_factory_name;
		} else {
			oid = itemBean.id;
			factoryName = itemBean.f_factory_name;
		}
		mHandler = handler;
		mContext = context;
		refundGoodsMap = new HashMap<String, RefundGoodSizeBean>();

		// getTotalPrice();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myGridAdapter = new MyGridAdapter();
		mPicList = new ArrayList<Picture>();// GridView中显示需要上传的图片
		mPathList = new ArrayList<String>();// 上传图片的本地路径
		mUrlList = new ArrayList<String>();// 图片上传后返回图片的网络地址
		mPicList.add(mAddPicture);
		loadRefundApplyInfo();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_refund, null);
		mListView = (ListView) view.findViewById(R.id.good_listview);
		mAdapter = new GoodsListAdapter(refundGoodBeans, inflater, mHandler,getActivity(), false);
		contentView = view.findViewById(R.id.layout_content);
		tip_no_content = (TextView) view.findViewById(R.id.tip_no_content);
		mListView.setAdapter(mAdapter);
		tv_total_price = (TextView) view.findViewById(R.id.total_price);
		tv_total_refund_price_real = (TextView) view.findViewById(R.id.total_price_real);
		btn_commit = (Button) view.findViewById(R.id.commit);
		view_select_pic = view.findViewById(R.id.layout_upload_proof_imgs);
		myGridView = (MyGridView) view.findViewById(R.id.gridview);
		et_apply_message = (EditText) view.findViewById(R.id.et_refund_description);
		factory_name = (TextView) view.findViewById(R.id.factory_name);

		factory_name.setText(factoryName);
		tip_no_content.setVisibility(View.INVISIBLE);
		myGridView.setAdapter(myGridAdapter);
		btn_commit.setOnClickListener(this);
		view_select_pic.setOnClickListener(this);

		tv_total_price.setText("小计金额：" + totalRefundPrice);
		if (totalRefundPrice > 0) {
			tv_total_refund_price_real
					.setText((totalRefundPrice + extraRefundMonny) + "(包括运费补偿"
							+ extraRefundMonny + "元)");
		} else {
			tv_total_refund_price_real.setText("0.0");
		}
		isContentViewFound = true;
		ifShowContentView();
		return view;
	}

	private void ifShowContentView() {
		// 确保在布局实例化结束后执行该方法，以免发生空指针异常
		if (isContentViewFound) {
			if (refundGoodBeans != null && refundGoodBeans.size() > 0) {
				contentView.setVisibility(View.VISIBLE);
				tip_no_content.setVisibility(View.INVISIBLE);
			} else {
				contentView.setVisibility(View.INVISIBLE);
				tip_no_content.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.commit:

			if (canCommit) {
				uploadRefundApply();
			}
			break;
		case R.id.layout_upload_proof_imgs:
			// showpopwindow();
			break;
		}
	}

	public void setTotalPriceText() {
		getTotalRefundPrice();
		tv_total_price.setText("小计金额：" + (totalRefundPrice));
		if (totalRefundPrice > 0) {
			tv_total_refund_price_real
					.setText((totalRefundPrice + extraRefundMonny) + "(包括运费补偿"
							+ extraRefundMonny + "元)");
		} else {
			tv_total_refund_price_real.setText("0.0");
		}
		Log.d(TAG, "totalRefundPrice = " + totalRefundPrice);
	}

	/**
	 * 获取订单总价
	 */
	public static void getTotalPrice() {
		Log.d(TAG, "=====> getTotalPrice");
		if (refundGoodBeans == null || refundGoodBeans.size() == 0)
			return;
		totalPrice = 0;
		for (int i = 0; i < refundGoodBeans.size(); i++) {
			List<RefundGoodSizeBean> sizeList = refundGoodBeans.get(i).goodSizeBeans;
			for (int j = 0; j < sizeList.size(); j++) {
				RefundGoodSizeBean sizeBean = sizeList.get(j);
				refundGoodsMap.put(sizeBean.getUniqueKey(), sizeBean);
				totalPrice += (Float.valueOf(sizeBean.quantity) * Float
						.valueOf(sizeBean.commodity_price));
			}
		}
		Log.d(TAG, " getTotalPrice totalPrice:" + totalPrice);
	}

	/**
	 * 获取退货总价
	 */
	public static void getTotalRefundPrice() {
		Log.d(TAG, "=====> getTotalRefundPrice");
		Set<String> keySet = refundGoodsMap.keySet();
		totalRefundPrice = 0;
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			RefundGoodSizeBean sizeBean = refundGoodsMap.get(iterator.next());
			if (sizeBean.isSelcetd()) {
				totalRefundPrice += (Integer.valueOf(sizeBean.getQuantity()) * Float
						.valueOf(sizeBean.getCommodity_price()));
			}

		}
		Log.d(TAG, "totalRefundPrice:" + totalRefundPrice);
	}

	private void commitRefundApply() {
		if (totalRefundPrice <= 0) {
			Toast.makeText(this.getActivity(), "您没有选择退款商品", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (et_apply_message.getText().toString().isEmpty()) {
			Toast.makeText(this.getActivity(), "请填写退款说明", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		if (mPathList.size() == 0) {
			Toast.makeText(this.getActivity(), "请至少选择一张图片上传",
					Toast.LENGTH_SHORT).show();
			return;
		}
	}

	// 上传退货申请信息
	private void uploadRefundApply() {
		CustomResponseHandler mHandler = new CustomResponseHandler(
				this.getActivity(), true, "正在提交...") {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "commit refund apply result:" + content);
				int type = Integer.valueOf(JSONParseUtils.getString(content,
						"type"));
				Log.d(TAG1,
						"type = " + JSONParseUtils.getString(content, "type")
								+ ", int type = " + type);
				if (type > 0) {
					Toast.makeText(mContext, "提交申请成功", Toast.LENGTH_SHORT)
							.show();
					ActivitySwitch.finishActivity(RefundFragment.this.getActivity());
//					// 清空数据
//					loadRefundApplyInfo();
//					et_apply_message.setText("");
//					mPicList.clear();
//					myGridAdapter.notifyDataSetChanged();

				} else {
					Log.d(TAG1, "提交退款申请失败");
					Toast.makeText(mContext, "提交申请失败", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "onFailure: content:" + content);
			}
		};
		if(!setCommitParas()) return;
		RequestParams params = new RequestParams();// 不可空
		params.put("uid", uid);// 不可空
		params.put("oid", oid);// 不可空
		params.put("is_take", is_take);// 不可空
		params.put("dataArr", dataArr);// 不可空
		params.put("yun", yun);// 不可空
		params.put("total_price", totalRefundPrice+"");// 不可空
		params.put("imgs", imgs);// 不可空
		params.put("desc", desc);// 可空
		HttpClient.uploadRefundApply(mHandler, params);
		Log.d(TAG1, "total_price = "+total_price);
		Log.d(TAG1, "totalRefundPrice = "+totalRefundPrice);
	}

	/**
	 * 设置请求参数
	 */
	private boolean  setCommitParas() {
		uid = AppContext.userInfo.id;
		is_take = "1";//是否已收货
		
		//退货信息
		StringBuffer buffer = new StringBuffer();
		Set<String> keySet = refundGoodsMap.keySet();
		Iterator<String> iterator = keySet.iterator();
		while (iterator.hasNext()) {
			RefundGoodSizeBean sizeBean = refundGoodsMap.get(iterator.next());
			if (sizeBean.isSelcetd()) {
				buffer.append(sizeBean.good_code + "," + sizeBean.quantity
						+ ",");
			}
		}
		dataArr = buffer.toString();
		
		yun = extraRefundMonny + "";//运费补偿金额
		total_price = totalRefundRriceReal + "";//退款金额
		
		//退款说明
		desc = et_apply_message.getText().toString();
		if(desc == null || desc.trim().equals("")){
			showToast("请输入退款说明");
			return false;
		}
				
		//图片路径
		if (mUrlList == null || mUrlList.size() == 0) {
			showToast("请上传凭证");
			return false;
		}
		buffer = new StringBuffer();
		for (int i = 0; i < mUrlList.size(); i++) {
			if (i == mUrlList.size() - 1) {
				buffer.append(mUrlList.get(i));
			} else {
				buffer.append(mUrlList.get(i) + ",");
			}
		}
		imgs = buffer.toString();
		
		return true;
	}

	/**
	 * 下载可退货商品列表信息
	 */
	private void loadRefundApplyInfo() {
		CustomResponseHandler mHandler = new CustomResponseHandler(
				this.getActivity(), true, "正在加载...") {

			@Override
			public void onFinish() {
				super.onFinish();
				ifShowContentView();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "loadRefundApplyInfo result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "可退款货单信息获取失败");
					tip_no_content.setText("数据获取失败");
//					loadRefundApplyInfo();
					return;
				} else {
					refundGoodBeans = JSONParseUtils.refundGoodBeans(content,
							RefundGoodSizeBean.TYPE_REFUND_SELECT, oid);
					mAdapter.setList(refundGoodBeans);
					getTotalPrice();
					setTotalPriceText();
					tip_no_content.setText("该订单中没有可进行退款的商品哦！");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "loadRefundApplyInfo onFailure: content:" + content);
				tip_no_content.setText("数据获取失败");
			}
		};
		uid = AppContext.userInfo.id;
		RequestParams params = new RequestParams();// 不可空
		params.put("uid", uid);// 不可空
		params.put("id", oid);// 不可空
		HttpClient.loadRefundApplyInfo(mHandler, params);

	}
	
	private MyGridAdapter myGridAdapter;
	private ArrayList<Picture> mPicList;// GridView中显示需要上传的图片
	private ArrayList<String> mPathList;// 上传图片的本地路径
	private ArrayList<String> mUrlList;// 图片上传后返回图片的网络地址
	private Picture mAddPicture = new Picture(R.drawable.add_pic, null);

	private void addPicture(Picture picture) {
		Log.d(TAG1, "===>> addPicture, picture path = "+picture.path);
		mPathList.add(picture.getPath());
		int size = mPicList.size();
		mPicList.remove(size - 1);
		mPicList.add(picture);
		if (mPicList.size() < 3) {// 最多加载3张照片
			mPicList.add(mAddPicture);
		}
		myGridAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onUpLoadSuccess(String imageUrl, String imageFile) {
		super.onUpLoadSuccess(imageUrl, imageFile);
		Picture picture = new Picture(0, imageFile);
		Log.d(TAG1, "===>> onUpLoadSuccess, picture path = "+picture.path+", imageUrl = "+imageUrl);
		mUrlList.add(imageUrl);
		addPicture(picture);	
	}

	class MyGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mPicList.size();
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
			ImageViewWithDel view = (ImageViewWithDel) arg1;
			final int position = arg0;
			if (view == null) {
				view = new ImageViewWithDel(RefundFragment.this.getActivity());
			}
			final ImageView bigImageView = view.getBigImageView();
			if (mPicList.get(arg0).getPath() != null) {
				Bitmap bm = BitmapUtils.showimageFull(mPicList.get(arg0)
						.getPath(), 1000, 1000);
				Log.d(TAG1, "bm  = "+bm+", bm size =  "+bm.getByteCount());
				view.setImage(bm, mPicList.get(arg0).getPath());
				view.setVisibility(View.VISIBLE);
				view.setCallback(new MyImageViewCallBack() {

					@Override
					public void imgClick() {

					}

					@Override
					public void del(View v) {
						mPicList.remove(position);
						mPathList.remove(position);
						mUrlList.remove(position);
						myGridAdapter.notifyDataSetChanged();
						if (mPicList.size() < 3
								&& !mPicList.contains(mAddPicture)) {
							mPicList.add(mAddPicture);
							Log.d(TAG, "deleted a origin picture");
						}
					}
				});
			} else {
				view.setImage(mPicList.get(arg0).getId());
				view.setVisibility(View.GONE);
				view.setCallback(new MyImageViewCallBack() {

					@Override
					public void imgClick() {
						showCameraPopwindow(bigImageView,true,true,true);
					}

					@Override
					public void del(View v) {

					}
				});
			}

			return view;
		}

	}

	// 获取
	public List<OrderGoodBean> orderGoodBeans(String content) {
		List<OrderGoodBean> list = new ArrayList<OrderGoodBean>();
		OrderDetailBean detailbean = new OrderDetailBean();
		OrderGoodBean bean = new OrderGoodBean();
		try {
			JSONObject object = new JSONObject(content);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				bean = new OrderGoodBean();
				object = array.getJSONObject(i);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	

}
