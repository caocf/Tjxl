/**
 * 
 */
package com.eims.tjxl_andorid.ui.user;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.ProductGridAdapter;
import com.eims.tjxl_andorid.adapter.RegisterHistoryAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.IQuerySmCommodityBean;
import com.eims.tjxl_andorid.entity.ListProductBean;
import com.eims.tjxl_andorid.entity.RegistedListBean;
import com.eims.tjxl_andorid.entity.RegistedListPage;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshScrollView;
import com.google.gson.Gson;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年6月23日 下午3:40:52
 *************************************************************************** 
 */
public class RegisterListActivity extends BaseActivity {

	protected static final String TAG = "RegisterListActivity";
	private HeadView head;
	private int pageIndex = 1;
	private int pageSize = 6;// 每页多少条数据
	private ArrayList<RegistedListBean> data;
	private RegistedListPage bean;
	private RegisterHistoryAdapter adapter;
	private com.eims.tjxl_andorid.weght.MyListView lvregisterhistory;
	private PullToRefreshScrollView svregisterhistory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_viplist_layout);
		initview();
	}

	private void initview() {
		initActionBar();
		setPullRefreshView();
		getDataList();
	}

	private void initActionBar() {
		head = (HeadView) findViewById(R.id.head);
		this.svregisterhistory = (PullToRefreshScrollView) findViewById(R.id.sv_register_history);
		this.lvregisterhistory = (MyListView) findViewById(R.id.lv_register_history);
		head.setText("已注册鞋店");
		head.setRightGone();
	}

	private void setPullRefreshView() {
		svregisterhistory.setEnabled(false);
		// 设定上下拉刷新
		// lv_exhibition.setMode(Mode.BOTH);
		svregisterhistory.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		svregisterhistory.getLoadingLayoutProxy().setLastUpdatedLabel(
				Utils.getCurrTiem());
		svregisterhistory.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		svregisterhistory.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		svregisterhistory.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		svregisterhistory
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
					// 下拉刷新
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase refreshView) {
						svregisterhistory
								.getLoadingLayoutProxy(true, false)
								.setLastUpdatedLabel(
										"最近更新: "
												+ Utils.Long2DateStr_detail(System
												.currentTimeMillis()));
						pageIndex = 1;
						if (bean != null) {
							data.clear();
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
		svregisterhistory.setVisibility(View.INVISIBLE);
	}

	/**
	 * 获取网络数据
	 */
	private void getDataList() {
		CustomResponseHandler mHandler = new CustomResponseHandler(
				mContext, true, "正在加载...") {
			@Override
			public void onSuccess(String content) {
				svregisterhistory.onRefreshComplete();
				super.onSuccess(content);
				if (LogUtil.isDebug)
					Log.i(TAG, "已注册店铺数据：" + content);
				bean = RegistedListPage.explainJson(content,mContext);
				if (null!=bean) {
					svregisterhistory.setVisibility(View.VISIBLE);
					update(bean);
				}else{
					showToast("网络错误，请检查网络之后再重试...");
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				svregisterhistory.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				svregisterhistory.onRefreshComplete();
				showToast("网络错误，请检查网络之后再重试...");
			}
		};
		RequestParams params = new RequestParams();
		params.put("curPage", pageIndex + "");
		params.put("pageSize", pageSize + "");
		HttpClient.QueryVIPList(mHandler, params);
	}

	private void update(RegistedListPage mGoodsBean) {
		if (mGoodsBean == null || mGoodsBean.getData() == null)
			return;
		if (data == null) {
			data = new ArrayList<RegistedListBean>();
		}
		if (pageIndex == 1) {
			data.clear();
			svregisterhistory.setMode(PullToRefreshBase.Mode.BOTH);
		}
		data.addAll(mGoodsBean.getData());
		if (adapter == null) {
			adapter = new RegisterHistoryAdapter(mContext, data);
			lvregisterhistory.setAdapter(adapter);
		} else {
			adapter.setData(data);
			adapter.notifyDataSetChanged();
		}
		if (mGoodsBean.getData().size() < pageSize) {
			svregisterhistory.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
			showToast("数据加载完成");
		}
	}
}
