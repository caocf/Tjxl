/**
 * 
 */
package com.eims.tjxl_andorid.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseXAdapter;
import com.eims.tjxl_andorid.ui.home.bean.SearchBean;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.ViewHolder;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月22日  上午11:44:03
 *************************************************************************** 
 */
public class SearchAdapter  extends BaseAdapter{

	
	private Context mContext;
	private List<SearchBean> mDatas;
	public SearchAdapter(Context context,List<SearchBean> mDatas) {
	
		this.mContext=context;
		this.mDatas=mDatas;
	}
	
	@Override
	public int getCount() {
		return mDatas==null?0:mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LogUtil.d("getView", "------");
		if(convertView==null){
			convertView=View.inflate(mContext, R.layout.search_layout_item, null);
		}
		SearchBean searchBean = mDatas.get(position);//倒序排列搜索结果
		TextView searchText=ViewHolder.get(convertView,R.id.search_text);
		searchText.setText(searchBean.searchText);
		
		return convertView;
	}


	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */



	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */


}
