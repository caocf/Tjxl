package com.eims.tjxl_andorid.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BrowseHistoryListAdapter;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.common.CommonConfrimCancelDialog;
import com.eims.tjxl_andorid.db.dao.ProductDao;
import com.eims.tjxl_andorid.db.dbmodel.Product;
import com.eims.tjxl_andorid.thread.MyTask;
import com.eims.tjxl_andorid.thread.MyTask.TaskCallback;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshListView;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshScrollView;

/**
 * 商品浏览记录
 * Created by kuangyong on 2015/7/4.
 */
public class BrowseHistoryActivity extends BaseActivity {
	private int pageIndex = 1;
	private int pageSize = 20;// 每页多少条数据
	private ArrayList<Product> productList;// 商品列表
	private BrowseHistoryListAdapter adapterLv;// 列表适配器
	private PullToRefreshScrollView sv_pro;
	private HeadView head;
	private MyListView listView;
	private static final int BROWSE_HISTORY_DEL=1;//删除浏览记录
	private static final String BROWSE_HISTORY_ID="browse_history_id";//删除id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_history);
        this.head = (HeadView) findViewById(R.id.head);
		sv_pro=(PullToRefreshScrollView) findViewById(R.id.sv_pro);
		listView=(MyListView) findViewById(R.id.listview);
        initheadview();
        setPullRefreshView();
        
        loadData();
    }
    
    
    
    private void initheadview() {
		head.setText("浏览过的商品");
		head.setGobackVisible();
		head.showRightText("清空", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ProductDao dao = new ProductDao();
				dao.delAllProduct(mContext);
				productList.clear();
				adapterLv.setData(productList);
				adapterLv.notifyDataSetChanged();
			}
		});
	}
    
    
    private void setPullRefreshView() {
		sv_pro.setEnabled(false);
		// 设定上下拉刷新
		// sv_pro.setMode(Mode.BOTH);
		sv_pro.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		sv_pro.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		sv_pro.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
		sv_pro.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
		sv_pro.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

		// 下拉刷新数据
		sv_pro.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			// 下拉刷新
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				sv_pro.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
						"最近更新: " + Utils.Long2DateStr_detail(System
								.currentTimeMillis()));
				pageIndex = 1;
				if (productList != null) {
					if (productList == null) {
						productList = new ArrayList<Product>();
					}
					productList.clear();
				}
				//getDataList();
				loadData();
			}

			// 加载更多
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 加载更多
				if (productList != null && productList.size() == pageSize) {
					pageIndex++;
					loadData();
				}
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				goProductDeatil(productList.get((position)).id);
			}
		});
		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intentHead = new Intent(mContext, CommonConfrimCancelDialog.class);
				intentHead.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_MESSAGE);
				intentHead.putExtra(BROWSE_HISTORY_ID, productList.get(i).id);
				startActivityForResult(intentHead, BROWSE_HISTORY_DEL);
				return true;
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			if(requestCode==BROWSE_HISTORY_DEL){
				if(null!=data){
					String id=data.getStringExtra(BROWSE_HISTORY_ID);
					iDelLeave(id);
				}
			}
		}
	}

	/**
	 * 删除一条浏览记录
	 * @param id
	 */
	private void iDelLeave(String id){
		boolean isDel=ProductDao.delProduct(id,mContext);
		if(isDel){
			int delIndex=-1;
			for (int i=0;i<productList.size();i++){
				if(id.equals(productList.get(i).id)){
					delIndex=i;
				}
			}
			if(-1!=delIndex){
				productList.remove(delIndex);
			}
			adapterLv.setData(productList);
			adapterLv.notifyDataSetChanged();
		}
	}

	private void goProductDeatil(String goodId){
		Bundle bundle=new Bundle();
		bundle.putString(ProductDeatil.INTENT_KEY, goodId);
		ActivitySwitch.openActivity(ProductDeatil.class, bundle, this);
	}
    
    private void showDataList(List<Product> products){
    	if (products == null )
			return;
		if (productList == null) {
			productList = new ArrayList<Product>();
		}
		if (pageIndex == 1) {
			productList.clear();
			sv_pro.setMode(PullToRefreshBase.Mode.BOTH);
		}
		productList.addAll(products);
		if (adapterLv == null) {
			adapterLv = new BrowseHistoryListAdapter(mContext, productList);
			listView.setAdapter(adapterLv);
		} else {
			adapterLv.setData(productList);
			adapterLv.notifyDataSetChanged();
		}
		if (products.size() < pageSize) {
			sv_pro.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
			Toast.makeText(mContext, "数据加载完成", Toast.LENGTH_SHORT).show();
		}
    }

    private void loadData(){
    	new MyTask(new TaskCallback() {
			
			@Override
			public Object run() {
				List<Product> products = ProductDao.getProductHistory(mContext, pageSize, pageIndex);
				return products;
			}
			
			@Override
			public void dataCallback(Object result) {
				sv_pro.onRefreshComplete();
				if(null != result){
					List<Product> products = (ArrayList<Product>)result;
					showDataList(products);
				}
			}
		}).execute(0);
    }
}

