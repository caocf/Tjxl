package com.eims.tjxl_andorid.entity;

public class ProductCollectionBean {

	private String id;//收藏列表ID
	private String name;
	private String icon;
	private String price;
	private String sprice;
	private String sale_count;
	private String is_sell;
	private String commodity_id;//商品ID
	
	private int tag;
	
	public ProductCollectionBean(){
	}
	
	public ProductCollectionBean(int tag){
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public String getSale_count() {
		return sale_count;
	}

	public void setSale_count(String sale_count) {
		this.sale_count = sale_count;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getIs_sell() {
		if(is_sell == null){
			is_sell = "";
		}
		return is_sell;
	}

	public void setIs_sell(String is_sell) {
		this.is_sell = is_sell;
	}

	public String getCommodity_id() {
		return commodity_id;
	}

	public void setCommodity_id(String commodity_id) {
		this.commodity_id = commodity_id;
	}
	
}
