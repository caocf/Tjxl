package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;
/**
 * 列表商品实体类
 * @author kuangyong
 *
 */
public class ListProductBean extends BaseBean {
	private String id;//商品ID
	private String commodity_name;//商品名称
	private String commodity_code;//商品编号
	private double price;//商品价格
	private double sprice;//商品特价(用这个价格)
	private String main_img_m;//商品图片
	private String f_factory_name;//厂家名称
	private String spec_name;//商品规格
	private String brand_name;//品牌名称

	public String getF_factory_name() {
		return f_factory_name;
	}

	public void setF_factory_name(String f_factory_name) {
		this.f_factory_name = f_factory_name;
	}

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCommodity_name() {
		return commodity_name;
	}
	public void setCommodity_name(String commodity_name) {
		this.commodity_name = commodity_name;
	}
	public String getCommodity_code() {
		return commodity_code;
	}
	public void setCommodity_code(String commodity_code) {
		this.commodity_code = commodity_code;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getSprice() {
		return sprice;
	}
	public void setSprice(double sprice) {
		this.sprice = sprice;
	}
	public String getMain_img_m() {
		return main_img_m;
	}
	public void setMain_img_m(String main_img_m) {
		this.main_img_m = main_img_m;
	}

	@Override
	public String toString() {
		return "ListProductBean{" +
				"id='" + id + '\'' +
				", commodity_name='" + commodity_name + '\'' +
				", commodity_code='" + commodity_code + '\'' +
				", price=" + price +
				", sprice=" + sprice +
				", main_img_m='" + main_img_m + '\'' +
				", f_factory_name='" + f_factory_name + '\'' +
				", spec_name='" + spec_name + '\'' +
				", brand_name='" + brand_name + '\'' +
				'}';
	}
}
