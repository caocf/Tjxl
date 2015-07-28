package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

import java.util.ArrayList;

/**
 * 明星厂家数据
 */
public class IQueryStarFactoryPageBean extends BaseBean {
	private ArrayList<StarFactoryBean> data;//明星厂家列表

	public ArrayList<StarFactoryBean> getData() {
		if(null == data){
			data = new ArrayList<StarFactoryBean>();
		}
		return data;
	}

	public void setData(ArrayList<StarFactoryBean> data) {
		this.data = data;
	}
	
}
