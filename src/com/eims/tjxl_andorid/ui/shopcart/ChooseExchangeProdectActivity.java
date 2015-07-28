package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import loopj.android.http.RequestParams;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 选择换货商品
 * 
 * @author lzh
 * 
 */
public class ChooseExchangeProdectActivity extends BaseActivity implements OnClickListener{

	private static final String TAG1 = "ChooseExchangeProdectActivity";
	private HeadView headView;
	private TextView factory_name;
	private ListView mListView;
	private TextView btn_ok;
	private List<RefundGoodBean> refundGoodBeans;
	private ExchangeGoodsListAdapter mAdapter;

	private LayoutInflater mInflater;

	private String seller_id;
	private String factoryName;

	private Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_exchange_product);
		initData();
		findView();
		loadExchangeProduct();
	}

	private void findView() {
		headView = (HeadView) findViewById(R.id.headview);
		mListView = (ListView) findViewById(R.id.good_listview);
		factory_name = (TextView) findViewById(R.id.factory_name);
		btn_ok = (TextView) findViewById(R.id.ok);
		
		headView.setText("选择商品");
		btn_ok.setOnClickListener(this);
		factory_name.setText(factoryName);
		
		mListView.setAdapter(mAdapter);
	}

	private void initData() {
		seller_id = getIntent().getStringExtra("seller_id");
		factoryName = getIntent().getStringExtra("factoryName");
		mInflater = LayoutInflater.from(this);
		refundGoodBeans = new ArrayList<RefundGoodBean>();
		mAdapter = new ExchangeGoodsListAdapter(refundGoodBeans, mInflater,
				mHandler, this);
	}
	

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ok:
			getSelectedGoodsList();
//			mAdapter.notifyDataSetChanged();
//			mListView.setSelection(0);
//			this.finish();
			ActivitySwitch.finishActivity(this);
			break;

		default:
			break;
		}
	}

	private void loadExchangeProduct() {
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "loadExchangeProduct result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "换货商品信息获取失败");
					return;
				} else {
					List<RefundGoodBean> list = JSONParseUtils.refundGoodBeans(
							content, RefundGoodSizeBean.TYPE_EXCHANGE_RECEIVE, "");
					refundGoodBeans.addAll(list);
					filterSelectedGoods();
					mAdapter.notifyDataSetChanged();
					Log.d(TAG1, "换货商品信息获取成功，refundGoodBeans size = "
							+ refundGoodBeans.size());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "loadExchangeProduct onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();// 不可空
		Log.d(TAG1, "uid = " + AppContext.userInfo.id);
		
		params.put("sellerId", seller_id);// 不可空
		HttpClient.loadExchangeProducts(mHandler, params);
	}
	
	/**
	 * 标记处之前已选过的货品
	 */
	private void filterSelectedGoods(){
		Map<String, RefundGoodSizeBean> map = ExchangeProductFragment.exchangeReceiveGoodsMap;
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		for (int i = refundGoodBeans.size()-1; i >=0; i--) {
			RefundGoodBean goodBean = refundGoodBeans.get(i);
			for (int j = 0; j < goodBean.goodSizeBeans.size(); j++) {
				RefundGoodSizeBean sizeBean = goodBean.goodSizeBeans.get(j);
				sizeBean.setTotal_stock(sizeBean.getQuantity());
				sizeBean.setQuantity("0");
				if(keySet.contains(sizeBean.getUniqueKey())){
					sizeBean.setSelcetd(true);
					sizeBean.setQuantity(map.get(sizeBean.getUniqueKey()).getQuantity());
				}
			}
		}
	}
	/**
	 * 通过selectedGoodSizeMap获取选择的商品
	 */
	public List<RefundGoodBean> selectedGoodBeans;
	public void getSelectedGoodsList(){
		selectedGoodBeans = new ArrayList<RefundGoodBean>();
		 ExchangeProductFragment.exchangeReceiveGoodsMap.clear();
		/**
		 * 直接通过refundGoodBeans获取商品列表
		 * 这里一定要List后面还是遍历，否则删除一个元素的时候其他元素会向前移动，则后面有些元素取不到
		 */
		for (int i = refundGoodBeans.size()-1; i >=0; i--) {
			RefundGoodBean goodBean = refundGoodBeans.get(i);
			boolean goodBeanSelected = false;
			for (int j = goodBean.goodSizeBeans.size()-1; j >= 0; j--) {
				RefundGoodSizeBean sizeBean = goodBean.goodSizeBeans.get(j);
				if(sizeBean.isSelcetd){
					goodBeanSelected = true;
					ExchangeProductFragment.exchangeReceiveGoodsMap.put(sizeBean.getUniqueKey(), sizeBean);
				}else{
					goodBean.goodSizeBeans.remove(sizeBean);
				}
			}
			if(!goodBeanSelected){
				refundGoodBeans.remove(goodBean);
			}
		}
		ExchangeProductFragment.exchangeReceiveGoodBeans.clear();
		ExchangeProductFragment.exchangeReceiveGoodBeans.addAll(refundGoodBeans);
	}

	class ExchangeGoodsListAdapter extends BaseAdapter {

		private List<RefundGoodBean> goodBeans;
		private LayoutInflater mInflater;
		private ImageLoader mImageLoader;
		private Handler mHandler;
		private Context mContext;

		public ExchangeGoodsListAdapter(List<RefundGoodBean> beans,LayoutInflater inflater, Handler handler, Context context) {
			this.goodBeans = beans;
			this.mInflater = inflater;
			this.mHandler = handler;
			this.mImageLoader = ImageLoader.getInstance();
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return goodBeans == null ? 0 : goodBeans.size();
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
			RefundGoodBean goodBean = goodBeans.get(arg0);
			View view = arg1;
			ViewHolder holder = null;

			if (view == null) {
				holder = new ViewHolder();
				view = mInflater.inflate(R.layout.layout_seclet_order_good,null);
				holder.goodIcon = (ImageView) view.findViewById(R.id.product_icon);
				holder.goodName = (TextView) view.findViewById(R.id.product_name);
				holder.goodListView = (ListView) view.findViewById(R.id.good_size_listview);
				holder.delete = (ImageView) view.findViewById(R.id.iv_delete_good);
				view.setTag(holder);
			}
			holder = (ViewHolder) view.getTag();
			mImageLoader.displayImage(goodBean.commodity_img_m, holder.goodIcon);
			holder.goodName.setText(goodBean.commodity_name);
			holder.goodListView.setAdapter(new ExchangeGoodsSizeListAdapter(goodBean.goodSizeBeans, mInflater, mHandler, mContext));
			holder.delete.setVisibility(View.INVISIBLE);
			return view;
		}
	}

	class ViewHolder {
		ImageView goodIcon;
		TextView goodName;
		ListView goodListView;
		ImageView delete;
	}

	class ExchangeGoodsSizeListAdapter extends BaseAdapter {

		private static final String TAG = "GoodsSizeListAdapter";
		private List<RefundGoodSizeBean> goodSizeBeans;
		private LayoutInflater mInflater;
		private Handler mHandler;
		private Context mContext;

		public ExchangeGoodsSizeListAdapter(
				List<RefundGoodSizeBean> goodSizeBeans,
				LayoutInflater inflater, Handler handler, Context context) {
			this.goodSizeBeans = goodSizeBeans;
			this.mInflater = inflater;
			this.mHandler = handler;
			this.mContext = context;
		}

		@Override
		public int getCount() {
			return goodSizeBeans == null ? 0 : goodSizeBeans.size();
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
			final RefundGoodSizeBean sizeBean = goodSizeBeans.get(arg0);
			final int position = arg0;
//			if (sizeBean.quantity_ori <= 0
//					&& !sizeBean.total_stock.trim().equals("")) {
//				sizeBean.quantity_ori = Integer.valueOf(sizeBean.total_stock);// 记录改尺码最原始的商品数（此处为商品库存）
//			}
//			sizeBean.quantity = "0";// 选择换货商品数量初始为0
//			Log.d(TAG, "sizeBean quantity_ori = " + sizeBean.quantity_ori);
			View view = arg1;
			ViewHolder holder = null;

//			if (view == null) {
				holder = new ViewHolder();
				view = mInflater.inflate(
						R.layout.layout_exchange_good_size, null);
				holder.checkBox = (CheckBox) view.findViewById(R.id.checkbox);
				holder.tv_color = (TextView) view.findViewById(R.id.good_color);
				holder.tv_size = (TextView) view.findViewById(R.id.good_size);
				holder.tv_price = (TextView) view.findViewById(R.id.good_price);
				holder.tv_selected_num = (TextView) view
						.findViewById(R.id.selected_num_value);
				view.setTag(holder);
//			}
			holder = (ViewHolder) view.getTag();
			String values[] = sizeBean.spec_name_value.split("，");// 中文输入法的逗号
			holder.tv_color.setText(values[0]);
			if(values.length>1){
				holder.tv_size.setText(values[1]);
			}
			holder.tv_price.setText("￥" + sizeBean.commodity_price);
			holder.tv_selected_num.setText(sizeBean.total_stock+" 件");

			holder.checkBox.setChecked(sizeBean.isSelcetd());
			holder.checkBox.setTag(sizeBean);
			holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
					Log.d(TAG, "isChecied = "+arg1+",sizeBean.isSelcetd = "+sizeBean.isSelcetd);
					sizeBean.setSelcetd(arg1);
				}
			});
			Log.d(TAG, "return view :" + view);
			return view;
		}

		class ViewHolder {
			CheckBox checkBox;
			TextView tv_color;
			TextView tv_size;
			TextView tv_price;
			TextView tv_selected_num;

		}

	}

}
