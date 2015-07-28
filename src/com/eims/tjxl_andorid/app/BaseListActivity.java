/**
 * 
 */
package com.eims.tjxl_andorid.app;


import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.constans.Constans;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.xlistview.XListLayout;
import com.eims.tjxl_andorid.weght.xlistview.XListLayout.XListLayoutCallback;
import com.eims.tjxl_andorid.weght.xlistview.XListView;
import com.eims.tjxl_andorid.weght.xlistview.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: BaseListActivity
 * @Description:   列表数据展示（xlistview下拉刷新，上拉加载）
 * @Author zd
 * @date 2015年6月4日  上午9:15:44
 *************************************************************************** 
 */
public abstract class BaseListActivity extends BaseActivity {

	protected static final String TAG = "BaseListActivity";
	public XListLayout mXListLayout;
	public XListView mXListView;
	public BaseXAdapter mAdapter;
	public int mPageindex;
	private User user;

    protected void initXListView(){
        mXListLayout = (XListLayout) findViewById(R.id.xListLayout);
        mXListView = mXListLayout.getListView();
        mXListView.setPullLoadEnable(true);
        mXListView.setAdapter(mAdapter);
      
        mXListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
            	//mAdapter.deleteItem();
                loadData(Constans.ONREFRESH);
            }
            
            @Override
            public void onLoadMore() {
                loadData(Constans.ONLOADMORE);
                LogUtil.d(TAG,mAdapter.getCount()+"");
            	mXListView.setSelection(mAdapter.getCount());
            }
        });
        mXListLayout.setXListLayoutCallback(new XListLayoutCallback() {
			@Override
			public void onLoadClick() {
				loadData(Constans.ONREFRESH);
			}
		});
    }
    
    protected void loadData(int loadType){
    }

}
