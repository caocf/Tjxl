package com.eims.tjxl_andorid.entity;

public class RefundDetailBean {
	private String return_time1;
	private String logistics_code;//物流单号
	private String return_time0;//申请提交时间
	private String return_time2;//卖家发货时间
	private String return_time3;//卖家确认收货时间
	private String commodity_img_m;
	private String logistics_name;//物流公司名称
	private String return_time8;//退款申请完成时间
	private String total_price;//卖家需退款金额
	private String commodity_name;//退款商品名称
	private String id;//退款订单ID
	private String statu_name;//状态名称
	private String return_statu;//售后状态(0申请售后 1卖家同意售后 2买家发货 3卖家确认收货 4卖家退款[不要] 5卖家拒绝 6退款关闭  7申请超时[不要] 8完成时间) ',
	private String description;
	private String return_code;//退款编号
	
	public String getReturn_statu() {
		return return_statu;
	}
	public void setReturn_statu(String return_statu) {
		this.return_statu = return_statu;
	}
	public String getReturn_time1() {
		return return_time1;
	}
	public void setReturn_time1(String return_time1) {
		this.return_time1 = return_time1;
	}
	
	public String getReturn_time2() {
		return return_time2;
	}
	public void setReturn_time2(String return_time2) {
		this.return_time2 = return_time2;
	}
	public String getLogistics_code() {
		return logistics_code;
	}
	public void setLogistics_code(String logistics_code) {
		this.logistics_code = logistics_code;
	}
	public String getReturn_time0() {
		return return_time0;
	}
	public void setReturn_time0(String return_time0) {
		this.return_time0 = return_time0;
	}
	public String getReturn_time3() {
		return return_time3;
	}
	public void setReturn_time3(String return_time3) {
		this.return_time3 = return_time3;
	}
	public String getCommodity_img_m() {
		return commodity_img_m;
	}
	public void setCommodity_img_m(String commodity_img_m) {
		this.commodity_img_m = commodity_img_m;
	}
	
	public String getLogistics_name() {
		return logistics_name;
	}
	public void setLogistics_name(String logistics_name) {
		this.logistics_name = logistics_name;
	}
	public String getReturn_time8() {
		return return_time8;
	}
	public void setReturn_time8(String return_time8) {
		this.return_time8 = return_time8;
	}
	public String getTotal_price() {
		return total_price;
	}
	public void setTotal_price(String total_price) {
		this.total_price = total_price;
	}
	public String getCommodity_name() {
		return commodity_name;
	}
	public void setCommodity_name(String commodity_name) {
		this.commodity_name = commodity_name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStatu_name() {
		return statu_name;
	}
	public void setStatu_name(String statu_name) {
		this.statu_name = statu_name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	
	
}
