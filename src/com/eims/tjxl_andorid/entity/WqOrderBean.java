/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import java.util.List;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 维权 订单
 * @Author zd
 * @date 2015年7月6日  上午10:31:14
 *************************************************************************** 
 */
public class WqOrderBean {
	

	
//      	from_order_type=0 （订单维权详情）
//			from_order_type=1 （换货维权详情）
//			from_order_type=2 （退货维权详情）
//			from_order_type=3 （评论维权详情）
	
	public List<WqOrderItemBean> data;

	public class WqOrderItemBean {
		public String addtime;//申请时间
		public String from_order_type;
		public String id;//维权表id
		public String order_type_name;//维权订单类型
		public String s_username;//卖家用户名
		public String type_name;//维权类型
		public String up_status_name;//维权状态
		public String uygur_power_code;//维权编号
		public String from_order_code;//
		public String from_order_id;//退货订单id
	}
}
