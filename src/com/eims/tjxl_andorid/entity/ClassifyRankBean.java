package com.eims.tjxl_andorid.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClassifyRankBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id;
	private String rank;
	private String category_name;
	private String pid;
	List<ClassifyRankBean> classifyBeansRank2;

	public ClassifyRankBean() {
		classifyBeansRank2 = new ArrayList<ClassifyRankBean>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<ClassifyRankBean> getClassifyBeansRank2() {
		return classifyBeansRank2;
	}

	public void setClassifyBeansRank2(List<ClassifyRankBean> classifyBeansRank2) {
		this.classifyBeansRank2 = classifyBeansRank2;
	}
}
