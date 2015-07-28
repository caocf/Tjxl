package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

import java.util.ArrayList;

public class IQuerySpCommodityBean extends BaseBean {
	private ArrayList<ListProductBean> data;//商品列表

	public ArrayList<ListProductBean> getData() {
		if(null == data){
			data = new ArrayList<ListProductBean>();
		}
		return data;
	}

	public void setData(ArrayList<ListProductBean> data) {
		this.data = data;
	}
	
}
