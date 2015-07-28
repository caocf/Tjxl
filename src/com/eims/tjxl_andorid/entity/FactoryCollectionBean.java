package com.eims.tjxl_andorid.entity;

import java.util.ArrayList;
import java.util.List;

public class FactoryCollectionBean {

	private String id;//收藏表Id
	private String name;
	private String score;
	private String icon;
	private String display_imgs;
	private String seller_id;//店铺Id
	private List<ProductCollectionBean> displayProducts;
	
	private int tag;
	
	public FactoryCollectionBean(){
		displayProducts = new ArrayList<ProductCollectionBean>();
	}
	
	public FactoryCollectionBean(int tag){
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

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDisplay_imgs() {
		return display_imgs;
	}

	public void setDisplay_imgs(String display_imgs) {
		this.display_imgs = display_imgs;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	
	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public List<ProductCollectionBean> getDisplayProducts() {
		return displayProducts;
	}

	public void setDisplayProducts(List<ProductCollectionBean> displayProducts) {
		this.displayProducts = displayProducts;
	}
	
	
	
}

