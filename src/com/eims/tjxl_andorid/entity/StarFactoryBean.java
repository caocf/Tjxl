package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 列表商品实体类
 * @author kuangyong
 *
 */
public class StarFactoryBean extends BaseBean {
	private String uid;//厂家会员ID，点击图片进入厂家店铺用
	private String f_factory_name;//厂家名称
	private String brand_img_url_m;//厂家图片

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getF_factory_name() {
		return f_factory_name;
	}

	public void setF_factory_name(String f_factory_name) {
		this.f_factory_name = f_factory_name;
	}

	public String getBrand_img_url_m() {
		return brand_img_url_m;
	}

	public void setBrand_img_url_m(String brand_img_url_m) {
		this.brand_img_url_m = brand_img_url_m;
	}
}
