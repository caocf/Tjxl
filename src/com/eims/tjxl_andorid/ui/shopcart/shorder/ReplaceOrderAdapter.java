/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart.shorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseXAdapter;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.entity.ReplaceOrderBean.ReplaceItemBean;
import com.eims.tjxl_andorid.entity.ReturnGoodBean.ReturnItemBean;
import com.eims.tjxl_andorid.utils.LogUtil;



/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年7月1日  上午10:32:31
 *************************************************************************** 
 */
public class ReplaceOrderAdapter extends BaseXAdapter<ReplaceItemBean>{

	/**
	 * @param context
	 */
	private String uid;
	public ReplaceOrderAdapter(Context context,String uid) {
		super(context);
		// TODO Auto-generated constructor stub
		this.uid=uid;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReplaceItemBean data = mListData.get(position);
	//	LogUtil.d("adpter",data.status_name);
		ViewHolder viewHolder = null;
		 if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.replace_list_item, parent, false);
            viewHolder.orderElectricItemView = (ReplaceOrderItemView) convertView.findViewById(R.id.orderreplaceItemView);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		
		 viewHolder.orderElectricItemView.setData(data);
     	 viewHolder.orderElectricItemView.setUid(uid);
//		 viewHolder.orderElectricItemView.setModuleType(mModuleType);
//		 viewHolder.orderElectricItemView.setOrderEquipmentSignItemViewCoallBack(mCoallBack);
        return convertView;
	}
	private class ViewHolder {
		private ReplaceOrderItemView orderElectricItemView;
	}

}
