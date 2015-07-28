package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alipay.sdk.util.JsonUtils;
import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.GoodsListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 退款详情子页面 --退款清单页面
 * 
 * @author lzh
 * 
 */
public class RefundDetailBillActivity extends BaseActivity {

	private static final String TAG1 = "RefundDetailBillActivity";
	private HeadView headview;
	private TextView tip_no_content;
	private View layout_content;

	private TextView factory_name;
	private TextView total_price;
	private GridView gridView;
	private ListView mListView;
	private String[] picUrls;
	private ImageLoader mImageLoader;
	private LayoutInflater mInflater;
	
	private boolean isShowContentView;
	public static List<RefundGoodBean> refundGoodBeans;// 退货清单中的商品列表
	private GoodsListAdapter mAdapter;
	private PicAdapter mPicAdapter;
	
	//请求参数
	private String uid;
	private String return_id;//申请退款订单ID
	
	private String factoryName;
	private String totalPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_refund_detail_bill);
		findViews();
		initData();
		loadData();
	}

	private void findViews() {
		headview = (HeadView) findViewById(R.id.headview);
		factory_name = (TextView) findViewById(R.id.factory_name);
		total_price = (TextView) findViewById(R.id.total_price);
		gridView = (GridView) findViewById(R.id.gridview);
		layout_content = findViewById(R.id.layout_content);
		tip_no_content = (TextView) findViewById(R.id.tip_no_content);
		mListView = (ListView) findViewById(R.id.good_listview);

		headview.setText("退款清单");
	}

	private void initData() {
		return_id = getIntent().getExtras().getString(RefundDetailActivity.KEY_RETURN_ID);
		mInflater = LayoutInflater.from(this);
		mImageLoader = ImageLoader.getInstance();
		
		refundGoodBeans = new ArrayList<RefundGoodBean>();
		
		mPicAdapter = new PicAdapter();
		gridView.setAdapter(mPicAdapter);
	}
	
	private void setView(){
		mPicAdapter.notifyDataSetChanged();
		factory_name.setText(factoryName);
		mAdapter = new GoodsListAdapter(refundGoodBeans, mInflater, null, this,false);
		mListView.setAdapter(mAdapter);
		total_price.setText("小计金额："+totalPrice);
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
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				Log.d(TAG1, "load refund bill info,result :"+content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					tip_no_content.setText("数据获取失败");
					isShowContentView = false;
				} else {
					isShowContentView = true;
					tip_no_content.setText("当前没有数据");
					refundGoodBeans = JSONParseUtils.refundGoodBeans(content,RefundGoodSizeBean.TYPE_REFUND_BILL,return_id);
					String returnMap = JSONParseUtils.getJSONObject(content, "returnMap");
					picUrls = JSONParseUtils.getString(returnMap, "img_path_list").split(",");
					factoryName = JSONParseUtils.getString(returnMap, "f_factory_name");
					totalPrice = JSONParseUtils.getString(returnMap, "total_price");
					Log.d(TAG1, "picUrls length = "+picUrls.length);
					setView();
				}
			}

			@Override
			public void onFailure(Throwable error) {
				super.onFailure(error);
				Log.d(TAG1, "load refund bill,result :"+error);
				tip_no_content.setText("网络请求失败");
				isShowContentView = false; 
			}
		};
		if (AppContext.userInfo != null) {
			uid = AppContext.userInfo.id;
		}
		Log.d(TAG1, "uid = "+uid+",  return_id = "+return_id);
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("return_id", return_id);
		HttpClient.loadRefundBillInfo(mHandler, params);
	}

	class PicAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return picUrls == null ? 0 : picUrls.length;
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
			if(view == null){
				view = mInflater.inflate(R.layout.layout_picture_item, null);
			}
			
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			mImageLoader.displayImage(URLs.IMAGE_URL+picUrls[arg0], imageView);
			return view;
		}
	}
}
