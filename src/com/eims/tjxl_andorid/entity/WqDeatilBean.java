/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import java.util.List;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年7月7日  下午8:40:41
 *************************************************************************** 
 */
public class WqDeatilBean {
	
	public WqInfo map;
	
	public List<WqComment> list;
	
	public class WqInfo{
		public String commodity_id;
		public String commodity_img_m;
		public String commodity_name;
		public String description;
		public String expect_way;
		public String seller_id;
		public String time0;
		public String time2;
		public String time3;
		public String up_status_name;
		public String up_type_name;
		public String uygur_power_statu;//根据这个这个字段判断维权是否通过
		public String up_status;//用于判断是否出现撤销维权
//		uygur_power_code :维权编号
//		commodity_img_m ：商品图片
//		commodity_name ：商品名称
//		commodity_id ：商品id
//		up_type_name ：维权类型
//		expect_way ：期望处理方式
//		description ：维权说明
//		up_status_name ：维权状态
//		time0 ：申请维权时间
//		time2 ：鞋联介入处理时间
//		time3 ：维权完成时间
	}
	
	public static class WqComment{

		public  String addtime;
		public  String  content;
		public  String  user_type;//会员类型(1卖家，2买家，3管理员)
	}

}
