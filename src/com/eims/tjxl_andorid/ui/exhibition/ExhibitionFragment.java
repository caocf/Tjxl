/**
 * 
 */
package com.eims.tjxl_andorid.ui.exhibition;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BannerAdapter;
import com.eims.tjxl_andorid.adapter.ProductGridAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.base.ImageDataLoader;
import com.eims.tjxl_andorid.entity.BannerBean;
import com.eims.tjxl_andorid.entity.IQuerySmCommodityBean;
import com.eims.tjxl_andorid.entity.ListProductBean;
import com.eims.tjxl_andorid.ui.product.GlobalClassifySearchActivity;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.MyGridView;
import com.eims.tjxl_andorid.weght.flow.CircleFlowIndicator;
import com.eims.tjxl_andorid.weght.flow.ViewFlow;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshScrollView;
import com.google.gson.Gson;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 * 货品互换
 */
public class ExhibitionFragment extends BaseLazyFragment {

	public static ExhibitionFragment exhibitionFragment;
	private View inflate;
	private ViewFlow mViewFlow;
	private CircleFlowIndicator indicator;
	private FrameLayout frameLayout;
	private BannerAdapter bannerAdapter;
	private ArrayList<BannerBean.BannerItem> imagePath;
	private final int AD_WIDTH = 480;
	private final int AD_HEIGHT = 251;
	private MyGridView gv_product;
	private PullToRefreshScrollView sv_product;
	private ImageButton imgBtn_left;
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	private TextView tv_headview_title;
	private int pageIndex = 1;
	private int pageSize = 6;// 每页多少条数据
	private IQuerySmCommodityBean bean;
	private ArrayList<ListProductBean> productList;// 商品列表
	private ProductGridAdapter adapterGv;// 缩略图适配器

	public static ExhibitionFragment getInstance() {
		if (exhibitionFragment == null) {
			exhibitionFragment = new ExhibitionFragment();
		}
		return exhibitionFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.exhibition_hall_layout, null);
		findviews();
		getDataList();
		setPullRefreshView();
		lazyload();
		return inflate;
	}

	@Override
	protected void lazyload() {
		// TODO Auto-generated method stub
		if (!isPrepared || !isVisible) {
			return;
		}
		setLinsenter();
		isPrepared = false;
		ImageDataLoader loader=new ImageDataLoader(getActivity(),ImageDataLoader.SHOE_PAGE);
		loader.setOnLoaderCompleteListener(new ImageDataLoader.OnLoaderCompleteListener() {
			@Override
			public void onComplete(ArrayList<BannerBean.BannerItem> imagePath) {
				ExhibitionFragment.this.imagePath = imagePath;
				initBannerPager();
			}
		});
//		loadBanner("6");
	}

	private void findviews() {
		// TODO Auto-generated method stub
		imgBtn_left = (ImageButton) inflate.findViewById(R.id.left_menu_btn);
		tv_headview_title = (TextView) inflate.findViewById(R.id.tv_headview_title);
		gv_product = (MyGridView) inflate.findViewById(R.id.gv_product);
		sv_product = (PullToRefreshScrollView) inflate.findViewById(R.id.sv_exhibition);
		frameLayout = (FrameLayout) inflate.findViewById(R.id.view_flow_frame);
		mViewFlow = (ViewFlow) inflate.findViewById(R.id.viewflow);
		indicator = (CircleFlowIndicator) inflate
				.findViewById(R.id.viewflowindic);
		tv_headview_title.setText("货品互换");
		gv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									final int position, long arg3) {
				goProductDeatil(productList.get(position).getId());
			}
		});
		// 控件初始化完成
		isPrepared = true;

		adapterGv = new ProductGridAdapter(getActivity(), null);
		gv_product.setAdapter(adapterGv);
	}

	private void initBannerPager() {
		// TODO Auto-generated method stub
		WindowManager manager = getActivity().getWindowManager();
		Point point = AppContext.getPICSize(manager);
		int PIC_WID = point.x;// 屏幕的宽
		int bitmapWidth = AD_WIDTH;
		int bitmapHeight = AD_HEIGHT;
		int wedgitWidth = PIC_WID;
		// 根据容器的宽度去缩放图片的高度
		int scallHeight = AppContext.getScallZoomHeight(manager, bitmapHeight,
				bitmapWidth, wedgitWidth);
		// System.out.println("x:"+point.x+"------Y:"+point.y+"高度："+scallHeight);
		frameLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, scallHeight));

		bannerAdapter = new BannerAdapter(getActivity());
		bannerAdapter.addItem(imagePath);
		mViewFlow.setmSideBuffer(imagePath.size());
		mViewFlow.setAdapter(bannerAdapter);
		mViewFlow.setFlowIndicator(indicator);
		mViewFlow.setTimeSpan(3000);
		mViewFlow.setSelection(0); // 设置初始位置
		mViewFlow.startAutoFlowTimer();
	}

	private void setLinsenter() {
		imgBtn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 显示分类
				ActivitySwitch.openActivity(GlobalClassifySearchActivity.class, null,
						getActivity());
			}
		});
	}

	private void setPullRefreshView() {
		sv_product.setEnabled(false);
		// 设定上下拉刷新
		// lv_exhibition.setMode(Mode.BOTH);
		sv_product.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		sv_product.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		sv_product.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		sv_product.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		sv_product.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		sv_product.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			// 下拉刷新
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				sv_product.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
						"最近更新: "
								+ Utils.Long2DateStr_detail(System
								.currentTimeMillis()));
				pageIndex = 1;
				if (bean != null) {
					if(null==productList){
						productList=new ArrayList<ListProductBean>();
					}
					productList.clear();
				}
				getDataList();
			}

			// 加载更多
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 加载更多
				if (bean != null && bean.getData().size() == pageSize) {
					pageIndex++;
					getDataList();
				}
			}
		});
	}

	private void goProductDeatil(String goodId){
		Bundle bundle=new Bundle();
		bundle.putString(ProductDeatil.INTENT_KEY, goodId);
		bundle.putInt(ProductDeatil.INTENT_KEY_FROM, ProductDeatil.FROM_PRODUCT_HUHUAN);
		ActivitySwitch.openActivity(ProductDeatil.class, bundle, getActivity());
	}

	/**
	 * 获取网络数据
	 */
	private void getDataList() {
		CustomResponseHandler mHandler = new CustomResponseHandler(
				getActivity(), true, "正在加载...") {
			@Override
			public void onSuccess(String content) {
				sv_product.onRefreshComplete();
				super.onSuccess(content);
				if (LogUtil.isDebug)
					Log.i(TAG, "鞋样推荐数据：" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Toast.makeText(getActivity(), "数据获取失败", Toast.LENGTH_LONG).show();
					return;
				}
				bean = new Gson().fromJson(content, IQuerySmCommodityBean.class);
				update(bean);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				sv_product.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				sv_product.onRefreshComplete();
				Toast.makeText(getActivity(), "网络错误",
						Toast.LENGTH_LONG).show();
			}
		};
		RequestParams params = new RequestParams();
		params.put("curPage", pageIndex + "");
		params.put("pageSize", pageSize + "");
		HttpClient.iQuerySmCommodity(mHandler, params);
	}

	public void update(){
		Log.i("onResume", "进来了");
		if(null!=productList){
			Log.i("item","进来了");
			for (int i=0;i<productList.size();i++){
				Log.i("货品互换的item",productList.get(i).toString());
			}
		}
//		if (adapterGv == null) {
//			adapterGv = new ProductGridAdapter(getActivity(), productList);
//			gv_product.setAdapter(adapterGv);
//		} else {
			adapterGv.setData(productList);
			adapterGv.notifyDataSetChanged();
//		}
	}

	private void update(IQuerySmCommodityBean mGoodsBean) {
		if (mGoodsBean == null || mGoodsBean.getData() == null|| mGoodsBean.getData().size() == 0){
			Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
			return;
		}
		if (productList == null) {
			productList = new ArrayList<ListProductBean>();
		}
		if (pageIndex == 1) {
			productList.clear();
			sv_product.setMode(PullToRefreshBase.Mode.BOTH);
		}
		for (int i = 0; i < mGoodsBean.getData().size(); i++) {
			productList.add(mGoodsBean.getData().get(i));
		}
		if (adapterGv == null) {
			adapterGv = new ProductGridAdapter(getActivity(), productList);
			gv_product.setAdapter(adapterGv);
		} else {
			adapterGv.setData(productList);
			adapterGv.notifyDataSetChanged();
		}
		if (mGoodsBean.getData().size() < pageSize) {
			sv_product.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
			Toast.makeText(getActivity(),"数据加载完成",Toast.LENGTH_SHORT).show();
		}
	}
}
