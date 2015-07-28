package com.eims.tjxl_andorid.entity;

import java.io.Serializable;

public class FilterSelectItem implements Serializable {
	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public String uncode;

	public String getUncode() {
		return uncode;
	}

	public void setUncode(String uncode) {
		this.uncode = uncode;
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
}
