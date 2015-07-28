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
 * @date 2015年6月30日  下午5:49:40
 *************************************************************************** 
 */

public class OrderDetailBean  implements Serializable{

	
	public List<OrderGoodBean> data;
	public OrderInfo paMap;

	public  class  OrderInfo  implements Serializable{

		public String id;//订单ID
		public String addtime;
		public String f_factory_name;
		public String f_qq;
		public String logistics_en;//快递公司英文名
		public String logistics_fare;//y运费
		public String logistics_no;//物流单号
		public String order_code;
		public String order_remark;
		public String order_status_time2;//付款时间
		public String order_status_time4;//确认收货时间
		public String order_status_time5;//交易完成时间
		public String order_status_time6;//订单关闭时间
		public String receipt_address;//收货地址
		public String receipt_mobile;
		public String receipt_user;
		public String seller_id;//店铺ID
		public String status;
		public String status_name;//订单状态
		public String total_price;
		public String comments_status;//订单评论状态，1：已评论，0：未评论
	}
	

	public static class  OrderGoodBean  implements Serializable{

		public String commodity_id;
		public String commodity_img_m;
		public String commodity_name;
		public List<OrderGoodSizeBean> goodList;
	}
	

	public class OrderGoodSizeBean  implements Serializable{

//		 "commodity_price": 199,
//         "quantity": 1,
//         "spec_name_value": "颜色:红色，尺码:40"
		public String commodity_price;
		public String quantity;
		public String spec_name_value;
		public String good_code;
		
		public int quantity_ori = -1;//用于记录改尺码最开始的商品数，add by lzh
//		public int isSelcetd = -1;//用于标记该尺码商品是否被选择,-1表示未被选择，1表示被选择，add by lzh
	}
	
	

}
