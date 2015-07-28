/**
 * 
 */
package com.eims.tjxl_andorid.ui.product.pop;

import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.CityAdressBean.Adress;
import com.eims.tjxl_andorid.entity.CityAdressBean.City;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月26日  下午2:26:54
 *************************************************************************** 
 */
public class CityAdapter extends  BaseAdapter{

	public List<City> provinceList;
	private Activity context;
	
	public CityAdapter(List<City> provinces,Activity context){
		this.context=context;
		this.provinceList=provinces;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return provinceList==null?0:provinceList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int postion) {
		// TODO Auto-generated method stub
		return provinceList.get(postion);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int postion) {
		// TODO Auto-generated method stub
		return postion;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int postion, View converview, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if(converview==null){
			  converview=View.inflate(context, R.layout.spinner_item, null);
		    }
		City adress = provinceList.get(postion);
		   TextView  name=(TextView) converview.findViewById(R.id.text_name);
		   name.setText(adress.cn);
		return converview;
	}

	
}
