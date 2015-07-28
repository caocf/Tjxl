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
public class ReturnGoodAdapter extends BaseXAdapter<ReturnItemBean>{

	/**
	 * @param context
	 */
	private String uid;
	public ReturnGoodAdapter(Context context,String uid) {
		super(context);
		// TODO Auto-generated constructor stub
		this.uid=uid;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ReturnItemBean data = mListData.get(position);

		ViewHolder viewHolder = null;
		 if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.retun_list_item, parent, false);
            viewHolder.orderElectricItemView = (ReturnGoodItemView) convertView.findViewById(R.id.orderreturnItemView);
            
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
		
		 viewHolder.orderElectricItemView.setData(data);
     	  viewHolder.orderElectricItemView.setUid(uid);

        return convertView;
	}
	private class ViewHolder {
		private ReturnGoodItemView orderElectricItemView;
	}

}
