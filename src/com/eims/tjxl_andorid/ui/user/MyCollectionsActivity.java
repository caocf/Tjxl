package com.eims.tjxl_andorid.ui.user;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.dialog.DeleteCollectionDialog;
import com.eims.tjxl_andorid.entity.FactoryCollectionBean;
import com.eims.tjxl_andorid.entity.ProductCollectionBean;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.product.FactoryActivity;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.PullToRefreshView;
import com.eims.tjxl_andorid.weght.PullToRefreshView.OnFooterRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyCollectionsActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG1 = "MyCollectionsActivity";
	private HeadView headview;
	private TextView tv_fac_col, tv_pro_col;
	private View line_pro, line_fac;
	private ListView listview_pro, listview_fac;
	private PullToRefreshView pullToRefreshViewPro, pullToRefreshViewFac;

	private LayoutInflater mInflater;

	private List<FactoryCollectionBean> mFactoryCollectionBeans;
	private List<ProductCollectionBean> mProductCollectionBeans;
	private FactoryCollectionBean noMoreFactoryBean;
	private ProductCollectionBean noMoreProductBean;
	private FacColAdapter mFacColAdapter;
	private ProCloAdapter mProCloAdapter;

	private ImageLoader mImageLoader;

	private int index;
	private int curPageFac = 1;
	private int curPagePro = 1;

	private int pageSize = 20;
	
	private String uid;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_collection);
		findView();
		init();

		loadFactoryDatas();
		loadProdcutDatas();
	}

	private void init() {
		user = AppContext.userInfo;
		index = getIntent().getIntExtra("index", 1);

		mInflater = LayoutInflater.from(this);
		mProductCollectionBeans = new ArrayList<ProductCollectionBean>();
		mFactoryCollectionBeans = new ArrayList<FactoryCollectionBean>();
		noMoreFactoryBean = new FactoryCollectionBean(1);
		noMoreProductBean = new ProductCollectionBean(1);
		mFacColAdapter = new FacColAdapter();
		mProCloAdapter = new ProCloAdapter();

		mImageLoader = ImageLoader.getInstance();

		listview_fac.setAdapter(mFacColAdapter);
		listview_pro.setAdapter(mProCloAdapter);
		if (index == 1) {
			tv_pro_col.performClick();
		} else {
			tv_fac_col.performClick();
		}
	}

	private void findView() {
		headview = (HeadView) findViewById(R.id.head);
		tv_fac_col = (TextView) findViewById(R.id.tv_fac_col);
		tv_pro_col = (TextView) findViewById(R.id.tv_pro_col);
		line_fac = findViewById(R.id.line_fac);
		line_pro = findViewById(R.id.line_pro);
		listview_fac = (ListView) findViewById(R.id.listview_factory_col);
		listview_pro = (ListView) findViewById(R.id.listview_product_col);
		pullToRefreshViewFac = (PullToRefreshView) findViewById(R.id.pull_to_refresh_listview_fac);
		pullToRefreshViewPro = (PullToRefreshView) findViewById(R.id.pull_to_refresh_listview_pro);

		listview_pro.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 < mProductCollectionBeans.size()-1){//多添加了一个空白对象，所以要做次处理
					ProductCollectionBean bean = mProductCollectionBeans.get(arg2);
					if(bean.getIs_sell().equals("0")){//已下架
						Toast.makeText(MyCollectionsActivity.this, "该商品已下架", Toast.LENGTH_SHORT).show();
					}else if(bean.getIs_sell().equals("1")){//销售中
						Bundle bundle = new Bundle();
						Log.d("zhiheng", "good id = "+mProductCollectionBeans.get(arg2).getCommodity_id());
						bundle.putString(ProductDeatil.INTENT_KEY, mProductCollectionBeans.get(arg2).getCommodity_id());
						ActivitySwitch.openActivity(ProductDeatil.class, bundle,
								MyCollectionsActivity.this);
					}
				}
			}
		});

		pullToRefreshViewFac.setCanPullDown(false);
		pullToRefreshViewFac
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {

					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						loadFactoryDatas();
					}
				});

		pullToRefreshViewPro.setCanPullDown(false);
		pullToRefreshViewPro
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {

					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						loadProdcutDatas();
					}
				});

		tv_fac_col.setOnClickListener(this);
		tv_pro_col.setOnClickListener(this);

		headview.setText("我的收藏");
		headview.setRightResource();
	}

	private void loadFactoryDatas() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onFinish() {
				super.onFinish();
				pullToRefreshViewFac.onFooterRefreshComplete();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "收藏厂家信息 result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "收藏厂家信息获取失败");
					return;
				} else {
					List<FactoryCollectionBean> list = JSONParseUtils
							.parseFactoryCollectionInfo(content);
					mFactoryCollectionBeans.addAll(list);
					if (list.size() < pageSize) {
						if (mFactoryCollectionBeans.contains(noMoreFactoryBean)) {
							mFactoryCollectionBeans.remove(noMoreFactoryBean);
						}
						mFactoryCollectionBeans.add(noMoreFactoryBean);
						pullToRefreshViewFac.setCanPullUp(false);
					}
					curPageFac++;
					mFacColAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", user.id);
		params.put("curPage", curPageFac + "");
		HttpClient.loadFactoryCollectionInfo(mHandler, params);
	}

	private void loadProdcutDatas() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "收藏产品信息 result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "收藏产品信息获取失败");
					return;
				} else {
					List<ProductCollectionBean> list = JSONParseUtils
							.parseProductCollectionInfo(content);
					mProductCollectionBeans.addAll(list);
					if (list.size() < pageSize) {
						if (mProductCollectionBeans.contains(noMoreProductBean)) {
							mProductCollectionBeans.remove(noMoreProductBean);
						}
						mProductCollectionBeans.add(noMoreProductBean);
						pullToRefreshViewPro.setCanPullUp(false);
					}
					curPagePro++;
					mProCloAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", user.id);
		params.put("curPage", curPagePro + "");
		HttpClient.loadProductCollectionInfo(mHandler, params);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_pro_col:
			pullToRefreshViewFac.setVisibility(View.INVISIBLE);
			pullToRefreshViewPro.setVisibility(View.VISIBLE);
			line_pro.setVisibility(View.VISIBLE);
			line_fac.setVisibility(View.INVISIBLE);
			tv_fac_col.setTextColor(getResources().getColor(R.color.black));
			tv_pro_col.setTextColor(getResources().getColor(R.color.sheng_red));
			break;
		case R.id.tv_fac_col:
			pullToRefreshViewFac.setVisibility(View.VISIBLE);
			pullToRefreshViewPro.setVisibility(View.INVISIBLE);
			line_pro.setVisibility(View.INVISIBLE);
			line_fac.setVisibility(View.VISIBLE);
			tv_fac_col.setTextColor(getResources().getColor(R.color.sheng_red));
			tv_pro_col.setTextColor(getResources().getColor(R.color.black));
			break;

		default:
			break;
		}
	}

	class ProCloAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			Log.d(TAG1, "get Product count = " + mProductCollectionBeans.size());
			return mProductCollectionBeans.size();
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
			ProViewHolder holder = null;
			if (view == null) {
				holder = new ProViewHolder();
				view = mInflater.inflate(R.layout.layout_pro_col_item, null);
				holder.no_more = (TextView) view.findViewById(R.id.tv_no_more);
				holder.layout = view.findViewById(R.id.layout);
				holder.icon = (ImageView) view.findViewById(R.id.icon);
				holder.name = (TextView) view.findViewById(R.id.tv_name);
				holder.price = (TextView) view.findViewById(R.id.tv_price);
				holder.sale_num = (TextView) view
						.findViewById(R.id.tv_sale_count);
				holder.delete = (ImageView) view.findViewById(R.id.iv_delete);
				holder.alpha_view = view.findViewById(R.id.alpha_view);
				holder.line = view.findViewById(R.id.line);
				view.setTag(holder);
			}
			holder = (ProViewHolder) view.getTag();
			ProductCollectionBean bean = mProductCollectionBeans.get(arg0);
			
			if (bean.getTag() == 1) {
				holder.no_more.setVisibility(View.VISIBLE);
				holder.layout.setVisibility(View.INVISIBLE);
				if(mProductCollectionBeans.size() == 1) holder.no_more.setText("您还没有收藏任何商品哦！");
			} else {
				holder.no_more.setVisibility(View.INVISIBLE);
				holder.layout.setVisibility(View.VISIBLE);
				holder.name.setText(mProductCollectionBeans.get(arg0).getName());
				
				//判断商品是否已下架
				if(bean.getIs_sell().equals("0")){
					holder.alpha_view.setVisibility(View.VISIBLE);
					holder.price.setVisibility(View.GONE);
					holder.line.setVisibility(View.GONE);
					holder.sale_num.setText("宝贝已经下架！");
					holder.sale_num.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				}else if(bean.getIs_sell().equals("1")){
					holder.alpha_view.setVisibility(View.INVISIBLE);
					holder.price.setVisibility(View.VISIBLE);
					holder.line.setVisibility(View.VISIBLE);
					holder.sale_num.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
					holder.price.setText("￥ "+ mProductCollectionBeans.get(arg0).getPrice());
					holder.sale_num.setText("成交 "+ mProductCollectionBeans.get(arg0).getSale_count()+ " 件");
				}
				mImageLoader.displayImage(StringUtils.fixImageUrl(mProductCollectionBeans.get(arg0).getIcon()), holder.icon);

				holder.delete.setTag(bean);

				holder.delete.setOnClickListener(deleteClickListener);
			}
			return view;
		}

	}

	class ProViewHolder {
		TextView name;
		TextView price;
		TextView sale_num;
		ImageView icon;
		ImageView delete;
		View layout;
		View alpha_view;
		TextView no_more;
		View line;
	}

	class FacColAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			Log.d(TAG1, "get Factory count = " + mFactoryCollectionBeans.size());
			return mFactoryCollectionBeans.size();
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
			FacViewHolder holder = null;
			if (view == null) {
				holder = new FacViewHolder();
				view = mInflater.inflate(R.layout.layout_fac_col_item, null);
				holder.no_more = (TextView) view.findViewById(R.id.tv_no_more);
				holder.layout = view.findViewById(R.id.layout);
				holder.name = (TextView) view.findViewById(R.id.factory_name);
				holder.score = (TextView) view.findViewById(R.id.score);
				holder.icon = (ImageView) view.findViewById(R.id.icon);
				holder.delete = (ImageView) view.findViewById(R.id.iv_delete);
				holder.displayPro1 = (ImageView) view.findViewById(R.id.iv_display_1);
				holder.displayPro2 = (ImageView) view.findViewById(R.id.iv_display_2);
				holder.displayPro3 = (ImageView) view.findViewById(R.id.iv_display_3);
				view.setTag(holder);
			}
			holder = (FacViewHolder) view.getTag();
			FactoryCollectionBean bean = mFactoryCollectionBeans.get(arg0);
			if (bean.getTag() == 1) {
				holder.no_more.setVisibility(View.VISIBLE);
				holder.layout.setVisibility(View.INVISIBLE);
				if(mFactoryCollectionBeans.size() == 1) holder.no_more.setText("您还没有收藏任何店铺哦！");
			} else {
				holder.no_more.setVisibility(View.INVISIBLE);
				holder.layout.setVisibility(View.VISIBLE);
				holder.delete.setTag(bean);
				List<ProductCollectionBean> list = bean.getDisplayProducts();
				holder = (FacViewHolder) view.getTag();

				Log.d(TAG1,
						"Factory getView :name = " + bean.getName()
								+ ",icon = " + bean.getIcon() + ",score = "
								+ bean.getScore());
				holder.name.setText(bean.getName());
				holder.score.setText("满意度 ： " + bean.getScore());
				
				mImageLoader.displayImage(StringUtils.fixImageUrl(bean.getIcon()), holder.icon);
				for(int i = 0; i<list.size(); i++){
					if(i == 0){
						mImageLoader.displayImage(StringUtils.fixImageUrl(list.get(0).getIcon()),holder.displayPro1);
						holder.displayPro1.setTag(list.get(0).getId());
						holder.displayPro1.setOnClickListener(productClickListener);
						holder.displayPro1.setVisibility(View.VISIBLE);
					}else if(i == 1){
						mImageLoader.displayImage(StringUtils.fixImageUrl(list.get(1).getIcon()),holder.displayPro2);
						holder.displayPro2.setTag(list.get(1).getId());
						holder.displayPro2.setOnClickListener(productClickListener);
						holder.displayPro3.setVisibility(View.VISIBLE);
					}else if(i == 2){
						mImageLoader.displayImage(StringUtils.fixImageUrl(list.get(2).getIcon()),holder.displayPro3);
						holder.displayPro3.setTag(list.get(2).getId());
						holder.displayPro3.setOnClickListener(productClickListener);
						holder.displayPro3.setVisibility(View.VISIBLE);
					}
				}

				holder.delete.setTag(bean);//
				holder.icon.setTag(bean.getSeller_id());//设置店铺id
				holder.name.setTag(bean.getSeller_id());//设置店铺id

				holder.delete.setOnClickListener(deleteClickListener);//删除
				holder.icon.setOnClickListener(factoryClickListener);
				holder.name.setOnClickListener(factoryClickListener);
			}
			return view;
		}

	}

	class FacViewHolder {
		TextView name;
		TextView score;
		ImageView delete;
		ImageView icon;
		ImageView displayPro1;
		ImageView displayPro2;
		ImageView displayPro3;
		View layout;
		TextView no_more;
	}
	

	OnClickListener deleteClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			final Object o = arg0.getTag();
			
			final DeleteCollectionDialog dialog = new DeleteCollectionDialog(MyCollectionsActivity.this);
			dialog.setOnclickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(arg0.getId() == R.id.button_ok){
						cancelColletion(o);
						dialog.dismiss();
					}else if(arg0.getId() == R.id.button_cancel){
						dialog.dismiss();
					}
				}
			});
			dialog.show();
		}
	};

	OnClickListener factoryClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Bundle bundle = new Bundle();
			bundle.putString(FactoryActivity.ID, (String) arg0.getTag());
			ActivitySwitch.openActivity(FactoryActivity.class, bundle,MyCollectionsActivity.this);
		}
	};

	OnClickListener productClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Bundle bundle = new Bundle();
			bundle.putString(ProductDeatil.INTENT_KEY, (String) arg0.getTag());
			ActivitySwitch.openActivity(ProductDeatil.class, bundle,MyCollectionsActivity.this);
		}
	};
	
	/**
	 * 取消收藏
	 * 
	 * @param selectorGoodLists
	 **/
	private String ids;
	private void cancelColletion(final Object o) {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在提交...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "cancel collection message result:" + content);
				String type = JSONParseUtils.getString(content, "type");
				String msg = JSONParseUtils.getString(content, "msg");
				Toast.makeText(MyCollectionsActivity.this, "已删除",Toast.LENGTH_SHORT).show();
				if (Integer.valueOf(type) > 0) {
					if (o instanceof ProductCollectionBean) {
						mProductCollectionBeans.remove(o);
						mProCloAdapter.notifyDataSetChanged();
					} else if (o instanceof FactoryCollectionBean) {
						mFactoryCollectionBeans.remove(o);
						mFacColAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, " cancel collection onFailure: content:" + content);
				Toast.makeText(MyCollectionsActivity.this, "数据提交失败",Toast.LENGTH_SHORT).show();
			}
		};
		
		if (o instanceof ProductCollectionBean) {
			ids = ((ProductCollectionBean) o).getCommodity_id();
			Log.d(TAG1, "good id = "+ids);
		} else if (o instanceof FactoryCollectionBean) {
			ids = ((FactoryCollectionBean) o).getSeller_id();
		}
		RequestParams params = new RequestParams();
		params.put("uid", user.id);
		params.put("ids", ids);
		HttpClient.CancelCollection(mHandler, params);
	}
}
