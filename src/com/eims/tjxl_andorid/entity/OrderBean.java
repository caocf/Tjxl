/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import java.io.Serializable;
import java.util.List;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月30日  下午2:15:34
 *************************************************************************** 
 */
public class OrderBean {
	
	public List<OrderItemBean> data;
	
	public  class OrderItemBean implements Serializable{
		public String addtime;
		public String f_factory_name;
		public String id;
		public String order_code;
		public String status;
		public String status_name;
		public String commodity_img_m;
		public String total_price;
		public String quantity;
		public String logistics_en;//快递公司英文名
		public String logistics_no;//物流单号
		public String seller_id;//卖家ID
		public String comments_status;//该订单是否已评价，1：已评价，0：未评价
		//public List<ShoeGoodItemBean> detaList;
	}
//	public class ShoeGoodItemBean{
//		public String commodity_id;
//		public String commodity_img_m;
//		public String commodity_name;
//		public String quantity;
//		
//	}

}
