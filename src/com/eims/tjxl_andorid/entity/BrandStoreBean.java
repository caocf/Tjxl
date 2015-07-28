package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 列表商品实体类
 * @author kuangyong
 *
 */
public class BrandStoreBean extends BaseBean {
	private String uid;//品牌馆的会员ID，点击图片，跳转到店铺首页用
	private String brand_name;//品牌馆名称
	private String brand_img_url_m;//品牌馆图片

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getBrand_img_url_m() {
		return brand_img_url_m;
	}

	public void setBrand_img_url_m(String brand_img_url_m) {
		this.brand_img_url_m = brand_img_url_m;
	}
}
