/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import java.util.List;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description:  退货实体
 * @Author zd
 * @date 2015年7月2日  下午3:51:16
 *************************************************************************** 
 */
public class ReturnGoodBean  extends ShouhouOrderItemBean{
	
    public List<ReturnItemBean> data;
//     status_name  退货状态（0申请中，1卖家同意，2买家发货，3卖家确认收货，5卖家拒绝，6退款关闭，8已完成）
//    return_statu：0申请售后 1卖家同意售后 2买家发货 3卖家确认收货  5卖家拒绝 6退款关闭   8完成时间
//    total_price：调整价格之后的退货总金额
//    rp:是否可以维权 等于1可以维权
	public class ReturnItemBean{
		public String commodity_img_m;
		public String f_factory_name;
		public String id;
		public String order_code;//订单编号
		public String quantity;
		public String return_statu;
		public String return_time0;
		public String rp;
		public String statu_name;
		public String total_price;
		public String uygur_power_id;//维权类型
		public String is_reject;
		public String is_out;
		public String reject_desc;//驳回原因
	}

	
}
