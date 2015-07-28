package com.eims.tjxl_andorid.entity;

import android.R.integer;

public class RefundGoodSizeBean {
	public static final int TYPE_REFUND_SELECT = 1;//申请退款时商品类型
	public static final int TYPE_REFUND_BILL = 2;//退款清单中的商品类型
	public static final int TYPE_EXCHANGE_SEND = 3;//申请换货时退回的商品
	public static final int TYPE_EXCHANGE_RECEIVE = 4;//退款换货时换回的商品
	
	//商品属性
	public String commodity_price;//": 199,
	public String quantity;//": 32,
	public String good_code;//": "143390948961814",
	public String spec_name_value;//": "颜色:蓝色，尺码:39"
	public String total_stock;//库存
	
	//自定义属性
	public String order_id;//该货品所属订单的id
	public String uniqueKey;//用于唯一标识一个商品，该属性值由订单id和货品id组成，两者由"-"连接，其中订单id可用""代替
	public int type = TYPE_REFUND_SELECT;
	public int quantity_ori = -1;//用于记录改尺码最开始的商品数，add by lzh
	public boolean isSelcetd = false;//用于标记该尺码商品是否被选择,-1表示未被选择，1表示被选择，add by lzh
	public RefundGoodBean goodBeanBelongTo;//该货品所属的商品
	
	public String getUniqueKey() {
		return uniqueKey;
	}
	public void setUniqueKey(String uniqueKey) {
		this.uniqueKey = uniqueKey;
	}
	public String getCommodity_price() {
		return commodity_price;
	}
	public void setCommodity_price(String commodity_price) {
		this.commodity_price = commodity_price;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getGood_code() {
		return good_code;
	}
	public void setGood_code(String good_code) {
		this.good_code = good_code;
	}
	public String getSpec_name_value() {
		return spec_name_value;
	}
	public void setSpec_name_value(String spec_name_value) {
		this.spec_name_value = spec_name_value;
	}
	public int getQuantity_ori() {
		return quantity_ori;
	}
	public void setQuantity_ori(int quantity_ori) {
		this.quantity_ori = quantity_ori;
	}
	public boolean isSelcetd() {
		return isSelcetd;
	}
	public void setSelcetd(boolean isSelcetd) {
		this.isSelcetd = isSelcetd;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getTotal_stock() {
		return total_stock;
	}
	public void setTotal_stock(String total_stock) {
		this.total_stock = total_stock;
	}
	public RefundGoodBean getGoodBeanBelongTo() {
		return goodBeanBelongTo;
	}
	public void setGoodBeanBelongTo(RefundGoodBean goodBeanBelongTo) {
		this.goodBeanBelongTo = goodBeanBelongTo;
	}
	
}
