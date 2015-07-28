/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseXAdapter;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;



/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年7月1日  上午10:32:31
 *************************************************************************** 
 */
public class OrderListAdapter extends BaseXAdapter<OrderItemBean>{

	/**
	 * @param context
	 */
	private String uid;
	private String username;
	public OrderListAdapter(Context context,String uid,String username) {
		super(context);
		this.uid=uid;
		this.username=username;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderItemBean data = mListData.get(position);
		ViewHolder viewHolder = null;
		 if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_list_item, parent, false);
            viewHolder.orderElectricItemView = (OrderItemView) convertView.findViewById(R.id.orderElectricItemView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		 viewHolder.orderElectricItemView.setData(data);
     	 viewHolder.orderElectricItemView.setUid(uid);
     	 viewHolder.orderElectricItemView.setPosition(position);
     	 viewHolder.orderElectricItemView.setUsername(username);
//		 viewHolder.orderElectricItemView.setModuleType(mModuleType);
//		 viewHolder.orderElectricItemView.setOrderEquipmentSignItemViewCoallBack(mCoallBack);
        return convertView;
	}
	private class ViewHolder {
		private OrderItemView orderElectricItemView;
	}

}
