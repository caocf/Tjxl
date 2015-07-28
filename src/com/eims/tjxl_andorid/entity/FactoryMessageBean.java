package com.eims.tjxl_andorid.entity;

public class FactoryMessageBean {

//	"miao_a": "0",
//    "uid": "55",
//    "fuwu_a": "0",
//    "wuliu": "3.0",
//    "fuwu": "3.0",
//    "address": "浙江省杭州市下城区110",
//    "miao": "3.0",
//    "head_img": "http://d3.java.shovesoft.com/xlw/upload/imageCut/20150515/1431656948305.png",
//    "wuliu_a": "0",
//    "factory_name": "turbo",
//    "qq": "456987456"
	private String factory_name;
	private String address;
	private String head_img;
	private String miao_a;
	private String fuwu_a;
	private String wuliu_a;
	private String miao;
	private String fuwu;
	private String wuliu;
	private String uid;//店铺id
	private String qq;
	private String total_sell = "0";
	public String getFactory_name() {
		return factory_name;
	}
	public void setFactory_name(String factory_name) {
		this.factory_name = factory_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHead_img() {
		return head_img;
	}
	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}
	public String getMiao_a() {
		return miao_a;
	}
	public void setMiao_a(String miao_a) {
		this.miao_a = miao_a;
	}
	public String getFuwu_a() {
		return fuwu_a;
	}
	public void setFuwu_a(String fuwu_a) {
		this.fuwu_a = fuwu_a;
	}
	public String getWuliu_a() {
		return wuliu_a;
	}
	public void setWuliu_a(String wuliu_a) {
		this.wuliu_a = wuliu_a;
	}
	public String getMiao() {
		return miao;
	}
	public void setMiao(String miao) {
		this.miao = miao;
	}
	public String getFuwu() {
		return fuwu;
	}
	public void setFuwu(String fuwu) {
		this.fuwu = fuwu;
	}
	public String getWuliu() {
		return wuliu;
	}
	public void setWuliu(String wuliu) {
		this.wuliu = wuliu;
	}
	public String getId() {
		return uid;
	}
	public void setId(String id) {
		this.uid = id;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getTotal_sell() {
		return total_sell;
	}
	public void setTotal_sell(String total_sell) {
		this.total_sell = total_sell;
	}
	
}
