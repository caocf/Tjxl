package com.eims.tjxl_andorid.ui.exhibition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BannerAdapter;
import com.eims.tjxl_andorid.adapter.ExhibitionListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.base.ImageDataLoader;
import com.eims.tjxl_andorid.entity.BannerBean;
import com.eims.tjxl_andorid.entity.ExhibitionBean;
import com.eims.tjxl_andorid.entity.IQueryExhibitionPageBean;
import com.eims.tjxl_andorid.ui.home.area.BrandHallFragment;
import com.eims.tjxl_andorid.ui.home.area.DiscountAreaFragment;
import com.eims.tjxl_andorid.ui.home.area.SellingProductsFragment;
import com.eims.tjxl_andorid.ui.home.area.ShoePatternFragment;
import com.eims.tjxl_andorid.ui.home.area.StarFactoryFragment;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;
import com.eims.tjxl_andorid.weght.flow.CircleFlowIndicator;
import com.eims.tjxl_andorid.weght.flow.ViewFlow;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshScrollView;

import loopj.android.http.RequestParams;

/**
 * 实体展厅 Created by kuangyong on 2015/6/24.
 */
public class ExhibitionActivity extends BaseActivity{
	private ImageView ivmain;// 顶部图片
	private HeadView head;
	private MyListView lv_exhibition;
	private PullToRefreshScrollView sv_exhibition;
	// 标志位，标志已经初始化完成。
	private int pageIndex = 1;
	private int pageSize = 6;// 每页多少条数据
	private ArrayList<ExhibitionBean> productList;// 商品列表
	private IQueryExhibitionPageBean bean;
	private ExhibitionListAdapter adapterLv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_exhibition_list);
		findviews();
		initheadview();
		setPullRefreshView();
		getIQueryExhibitionPage();
		ImageDataLoader loader=new ImageDataLoader(mContext,ImageDataLoader.EXHIBITION_PAGE);
		loader.setOnLoaderCompleteListener(new ImageDataLoader.OnLoaderCompleteListener() {
			@Override
			public void onComplete(ArrayList<BannerBean.BannerItem> imagePath) {
				if (null != imagePath && imagePath.size() != 0) {
					final BannerBean.BannerItem item = imagePath.get(0);
					ImageManager.Load(item.img, ivmain);
					ivmain.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							ImageDataLoader.gotoWitchOne(ExhibitionActivity.this, item);
						}
					});
				}
			}
		});
	}

	private void initheadview() {
		head.setText("实体展厅");
		head.setGobackVisible();
		head.setRightResource();
	}


	private void findviews() {
		// TODO Auto-generated method stub
		lv_exhibition = (MyListView) findViewById(R.id.lv_exhibition);
		sv_exhibition = (PullToRefreshScrollView) findViewById(R.id.sv_exhibition);
		head = (HeadView) findViewById(R.id.head);
		// 控件初始化完成
	}

	private void setPullRefreshView() {
		sv_exhibition.setEnabled(false);
		// 设定上下拉刷新
		// lv_exhibition.setMode(Mode.BOTH);
		sv_exhibition.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		sv_exhibition.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		sv_exhibition.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		sv_exhibition.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		sv_exhibition.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		sv_exhibition.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			// 下拉刷新
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				sv_exhibition.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
						"最近更新: "
								+ Utils.Long2DateStr_detail(System
								.currentTimeMillis()));
				pageIndex = 1;
				if (bean != null) {
					productList.clear();
				}
				getIQueryExhibitionPage();
			}

			// 加载更多
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 加载更多
				if (bean != null && bean.getData().size() == pageSize) {
					pageIndex++;
					getIQueryExhibitionPage();
				}
			}
		});
	}


	/**
	 * 获取展厅列表数据
	 */
	private void getIQueryExhibitionPage() {
		CustomResponseHandler handler=new CustomResponseHandler(this,true,"正在加载..."){
			@Override
			public void onSuccess(String content) {
				sv_exhibition.onRefreshComplete();
				super.onSuccess(content);
				if (LogUtil.isDebug)
					Log.i(TAG, "展厅数据：" + content);
				bean = IQueryExhibitionPageBean.explainJson(content,mContext);
				if(null!=bean){
					update(bean);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				sv_exhibition.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				sv_exhibition.onRefreshComplete();
				Toast.makeText(mContext, "网络错误",
						Toast.LENGTH_LONG).show();
			}
		};
		RequestParams params=new RequestParams();
		params.put("curPage",pageIndex+"");
		params.put("pageSize", pageSize + "");
		HttpClient.iQueryExhibitionPage(handler, params);
	}

	private void update(IQueryExhibitionPageBean mGoodsBean) {
		if (mGoodsBean == null || mGoodsBean.getData() == null)
			return;
		if (productList == null) {
			productList = new ArrayList<ExhibitionBean>();
		}
		if (pageIndex == 1) {
			productList.clear();
			sv_exhibition.setMode(PullToRefreshBase.Mode.BOTH);
		}
		productList.addAll(mGoodsBean.getData());
		if (adapterLv == null) {
			adapterLv = new ExhibitionListAdapter(this, productList);
			lv_exhibition.setAdapter(adapterLv);
		} else {
			adapterLv.setData(productList);
			adapterLv.notifyDataSetChanged();
		}
		if (mGoodsBean.getData().size() < pageSize) {
			//	Toast.makeText(this,"数据加载完成",Toast.LENGTH_SHORT).show();
			sv_exhibition.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		}
	}
}
