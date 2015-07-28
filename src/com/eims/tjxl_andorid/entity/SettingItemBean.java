package com.eims.tjxl_andorid.entity;

public class SettingItemBean {
	private String name;
	private String value = "";
	private boolean isShowArrow;// 是否显示跳转箭头
	private int tag;

	public SettingItemBean(String name,String value,boolean isShowArrow,int tag) {
		this.name = name;
		this.value = value;
		this.isShowArrow = isShowArrow;
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean getIsShowArrow() {
		return isShowArrow;
	}

	public void setIsShowArrow(boolean isShowArrow) {
		this.isShowArrow = isShowArrow;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

}
