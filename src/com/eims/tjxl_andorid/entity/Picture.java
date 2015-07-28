package com.eims.tjxl_andorid.entity;

public class Picture {
	public int id;
	public String path;

	public Picture(int id, String path) {
		if (id > 0) {
			this.id = id;
		}
		this.path = path;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}