package com.eims.tjxl_andorid.ui.home.area;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BrandStoreGridAdapter;
import com.eims.tjxl_andorid.adapter.BrandStoreListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.entity.BrandStoreBean;
import com.eims.tjxl_andorid.entity.IQueryBrandStorePageBean;
import com.eims.tjxl_andorid.ui.product.FactoryActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshGridView;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshListView;
import com.google.gson.Gson;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 * 品牌馆 Created by kuangyong on 2015/6/24.
 */
public class BrandHallFragment extends Fragment {
	private static final String TAG = "BrandHallFragment";
	private static final String KEY_WROD = "key_word";
	private PullToRefreshListView lv_pro;
	private PullToRefreshGridView gv_pro;
	private int pageIndex = 1;
	private int pageSize = 6;// 每页多少条数据
	private IQueryBrandStorePageBean bean;
	private ArrayList<BrandStoreBean> productList;// 商品列表
	private BrandStoreGridAdapter adapterGv;// 缩略图适配器
	private BrandStoreListAdapter adapterLv;// 列表适配器
	private String keyWord;

	/**
	 * @param keyword
	 *            搜索关键字
	 * @return
	 */
	public static BrandHallFragment newInstance(String keyword) {
		BrandHallFragment fragment = new BrandHallFragment();
		Bundle args = new Bundle();
		args.putString(KEY_WROD, keyword);
		fragment.setArguments(args);
		return fragment;
	}

	public BrandHallFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			keyWord = getArguments().getString(KEY_WROD);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_discount_area,
				container, false);
		lv_pro = (PullToRefreshListView) view.findViewById(R.id.lv_pro);
		gv_pro = (PullToRefreshGridView) view.findViewById(R.id.gv_pro);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setPullRefreshView();
		getDataList();
	}

	private void setPullRefreshView() {
		lv_pro.setEnabled(false);
		// 设定上下拉刷新
		// lv_pro.setMode(Mode.BOTH);
		lv_pro.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lv_pro.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		lv_pro.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		lv_pro.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		lv_pro.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		lv_pro.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			// 下拉刷新
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				lv_pro.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
						"最近更新: "
								+ Utils.Long2DateStr_detail(System
								.currentTimeMillis()));
				pageIndex = 1;
				if (bean != null) {
					if (productList == null) {
						productList = new ArrayList<BrandStoreBean>();
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
		lv_pro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int position, long arg3) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString(FactoryActivity.ID, productList.get(position - 1).getUid());
				ActivitySwitch.openActivity(FactoryActivity.class, bundle, getActivity());
			}
		});

		gv_pro.getLoadingLayoutProxy(false, true).setPullLabel("往下拉更新数据...");
		gv_pro.getLoadingLayoutProxy(false, true).setReleaseLabel("放开更新数据...");
		gv_pro.getLoadingLayoutProxy(false, true)
				.setRefreshingLabel("正在载入中...");
		gv_pro.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									final int position, long arg3) {
				Bundle bundle = new Bundle();
				bundle.putString(FactoryActivity.ID, productList.get(position).getUid());
				ActivitySwitch.openActivity(FactoryActivity.class, bundle, getActivity());
			}
		});
		gv_pro.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				gv_pro.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
						"最近更新: "
								+ Utils.Long2DateStr_detail(System
										.currentTimeMillis()));
				pageIndex = 1;
				if (bean != null) {
					if (productList == null) {
						productList = new ArrayList<BrandStoreBean>();
					}
					productList.clear();
				}
				getDataList();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				if (bean != null && bean.getData().size() == pageSize) {
					pageIndex++;
					getDataList();
				}
//				else{
//					lv_pro.onRefreshComplete();
//					Toast.makeText(getActivity(), "没有更多的数据",
//							Toast.LENGTH_LONG).show();
//				}
			}
		});
		showListOrGrid(false);
	}

	/**
	 * 获取网络数据
	 */
	private void getDataList() {
		CustomResponseHandler mHandler = new CustomResponseHandler(
				getActivity(), true, "正在加载...") {
			@Override
			public void onSuccess(String content) {
				gv_pro.onRefreshComplete();
				lv_pro.onRefreshComplete();
				super.onSuccess(content);
				if (LogUtil.isDebug)
					Log.i(TAG, "品牌馆数据：" + content);
				bean = new Gson()
						.fromJson(content, IQueryBrandStorePageBean.class);
				if (null!=bean&&bean.getType() != 1) {
					Toast.makeText(getActivity(), bean.getMsg(),
							Toast.LENGTH_LONG).show();
					return;
				}else if(null==bean){
					Toast.makeText(getActivity(), "网络错误",
							Toast.LENGTH_LONG).show();
					return;
				}
				update(bean);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				gv_pro.onRefreshComplete();
				lv_pro.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				gv_pro.onRefreshComplete();
				lv_pro.onRefreshComplete();
				Toast.makeText(getActivity(), "网络错误",
						Toast.LENGTH_LONG).show();
			}
		};
		RequestParams params = new RequestParams();
		params.put("curPage", pageIndex + "");
		params.put("pageSize", pageSize + "");
		params.put("kword", keyWord + "");
		HttpClient.iQueryBrandStorePage(mHandler, params);
	}

	private void update(IQueryBrandStorePageBean mGoodsBean) {
		if (mGoodsBean == null || mGoodsBean.getData() == null|| mGoodsBean.getData().size() == 0){
			Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
			return;
		}
		if (productList == null) {
			productList = new ArrayList<BrandStoreBean>();
		}
		if (pageIndex == 1) {
			productList.clear();
			gv_pro.setMode(PullToRefreshBase.Mode.BOTH);
			lv_pro.setMode(PullToRefreshBase.Mode.BOTH);
		}
		for (int i = 0; i < mGoodsBean.getData().size(); i++) {
			productList.add(mGoodsBean.getData().get(i));
		}
		if (adapterGv == null) {
			adapterGv = new BrandStoreGridAdapter(getActivity(), productList);
			gv_pro.setAdapter(adapterGv);
		} else {
			adapterGv.setData(productList);
			adapterGv.notifyDataSetChanged();
		}
		if (adapterLv == null) {
			adapterLv = new BrandStoreListAdapter(getActivity(), productList);
			lv_pro.setAdapter(adapterLv);
		} else {
			adapterLv.setData(productList);
			adapterLv.notifyDataSetChanged();
		}
		if (mGoodsBean.getData().size() < pageSize) {
			gv_pro.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
			lv_pro.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
			Toast.makeText(getActivity(), "数据加载完成", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 是否显示列表形式
	 *
	 * @param isList
	 */
	public void showListOrGrid(boolean isList) {
		if (isList) {// 显示列表
			gv_pro.setVisibility(View.INVISIBLE);
			lv_pro.setVisibility(View.VISIBLE);
		} else {// 显示缩略图
			gv_pro.setVisibility(View.VISIBLE);
			lv_pro.setVisibility(View.INVISIBLE);
		}
	}

}
