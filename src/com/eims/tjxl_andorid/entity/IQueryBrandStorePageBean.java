package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

import java.util.ArrayList;

/**
 * 品牌馆数据
 */
public class IQueryBrandStorePageBean extends BaseBean {
	private ArrayList<BrandStoreBean> data;//品牌馆列表

	public ArrayList<BrandStoreBean> getData() {
		if(null == data){
			data = new ArrayList<BrandStoreBean>();
		}
		return data;
	}

	public void setData(ArrayList<BrandStoreBean> data) {
		this.data = data;
	}
	
}
