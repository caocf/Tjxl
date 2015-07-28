/**
 *
 */
package com.eims.tjxl_andorid.ui.exhibition;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.ExhibitionTextListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ExhibitionBean;
import com.eims.tjxl_andorid.entity.IQueryExhibitionPageBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshScrollView;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 * *************************************************************************
 *
 * @Version 1.0
 * @ClassName:
 * @Description: 展厅分类
 * @Author zd
 * @date 2015年4月16日 上午11:26:33
 * **************************************************************************
 */
public class ExhibitionCateActivity extends BaseActivity {

    public static final String EXHIBITION_LIST = "exhibition_list";
    private HeadView headView;
    private MyListView listView;
    private int pageIndex = 1;
    private int pageSize = 20;// 每页多少条数据
    private PullToRefreshScrollView sv_exhibition;
    private ArrayList<ExhibitionBean> productList;// 商品列表
    private IQueryExhibitionPageBean bean;
    private ExhibitionTextListAdapter adapterLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exhibition_category_layout);
        findview();
        setActionBar();
        setPullRefreshView();
        getIQueryExhibitionPage();
    }

    private void setActionBar() {
        // TODO Auto-generated method stub
        headView.setText("展厅分类");
        headView.setGobackVisible();
        // headView.setRightResource();
    }

    private void findview() {
        headView = (HeadView) findViewById(R.id.head);
        listView = (MyListView) findViewById(R.id.listview);
        sv_exhibition = (PullToRefreshScrollView) findViewById(R.id.sv_exhibition);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String exhibition_id=productList.get(position).getId();
                if(!TextUtils.isEmpty(exhibition_id)){
                    Bundle bundle=new Bundle();
                    bundle.putString(ExhibitionDetailActivity.EXHIBITION_ID,exhibition_id);
                    ActivitySwitch.openActivity(ExhibitionDetailActivity.class, bundle,ExhibitionCateActivity.this);
                }
            }
        });
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
        CustomResponseHandler handler=new CustomResponseHandler(mContext,true,"正在加载..."){
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
            adapterLv = new ExhibitionTextListAdapter(mContext, productList);
            listView.setAdapter(adapterLv);
        } else {
            adapterLv.setData(productList);
            adapterLv.notifyDataSetChanged();
        }
        if (mGoodsBean.getData().size() < pageSize) {
            Toast.makeText(mContext,"数据加载完成",Toast.LENGTH_SHORT).show();
            sv_exhibition.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }
}
