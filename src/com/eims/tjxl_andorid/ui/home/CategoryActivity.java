/**
 * 
 */
package com.eims.tjxl_andorid.ui.home;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.utils.ViewHolder;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 所有类目
 * @Author zd
 * @date 2015年4月15日 上午9:02:30
 *************************************************************************** 
 */
public class CategoryActivity extends BaseActivity {

	private HeadView headView;
	private ListView listView;
	private ArrayList<String> types;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_layout);
		findview();
		setActionBar();
		types = new ArrayList<String>();
		types.add("苏州展厅");
		types.add("杭州展厅");
		types.add("广州展厅");
		types.add("深圳展厅");
		listView.setAdapter(new CateAdapter());
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("全部类目");
		headView.setGobackVisible();
		headView.setRightGone();
	}

	private void findview() {
		headView = (HeadView) findViewById(R.id.head);
		listView = (ListView) findViewById(R.id.listview);
	}

	class CateAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return types == null ? 0 : types.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return types.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertview, ViewGroup arg2) {
			if (convertview == null) {
				convertview = View.inflate(getApplication(),
						R.layout.all_type_item_layout, null);
			}
			TextView typeName = ViewHolder.get(convertview, R.id.type_name);
			TextView typeDesc = ViewHolder.get(convertview, R.id.type_desc);
			typeName.setText(types.get(position));
			typeDesc.setText("男鞋、运动鞋、皮鞋");
			return convertview;
		}

	}

}
