/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import android.R.integer;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月24日 下午2:49:54
 *************************************************************************** 
 */
public class ProductBean {

	public String id;
	public String price;//原价
	public String sprice;//折扣价
	public String min_batch;//最小批发量
	public String image;
	public String spec_name;//描述
	public String name;
	public String commodity_code;
	public String commodity_name;//
	public String saleNumber = "0";
	public int objectType = 1;//1表示正常对象，-1表示空对象，用于解决listview显示不完全的问题
	
	public ProductBean() {};
	public ProductBean(int objectType) {
		this.objectType = objectType;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSprice() {
		return sprice;
	}
	public void setSprice(String sprice) {
		this.sprice = sprice;
	}
	public String getMin_batch() {
		return min_batch;
	}
	public void setMin_batch(String min_batch) {
		this.min_batch = min_batch;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSpec_name() {
		return spec_name;
	}
	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCommodity_code() {
		return commodity_code;
	}
	public void setCommodity_code(String commodity_code) {
		this.commodity_code = commodity_code;
	}
	public String getCommodity_name() {
		return commodity_name;
	}
	public void setCommodity_name(String commodity_name) {
		this.commodity_name = commodity_name;
	}
	public String getSaleNumber() {
		return saleNumber;
	}
	public void setSaleNumber(String saleNumber) {
		this.saleNumber = saleNumber;
	}
  

}
