package com.eims.tjxl_andorid.entity;

import java.io.Serializable;
import java.util.List;

public abstract class FilterItemBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static final int SIGN_CATEGORY_ITEM = 1;
	public static final int SIGN_PROPERTY_ITEM = 2;
	public static final int SIGN_BRAND_ITEM = 3;

	public int sign = -1;
	public String item_title;
	public String item_id;
	public List<FilterSelectItem> items;
	public int selecteItemIndex;//选中FilterSelectItem的位置

	public String getItem_title() {
		return item_title;
	}

	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

}
