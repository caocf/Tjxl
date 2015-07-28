/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import java.util.List;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 物流 (http://www.aikuaidi.cn/api/ )
 * @Author zd
 * @date 2015年7月1日  下午7:54:04
 *************************************************************************** 
 */
public class LogisticsBean {
	
    //订单跟踪状态：status  
//	        0：查询出错（即errCode!=0），
//			1：暂无记录，
//			2：在途中，
//			3：派送中，
//			4：已签收，
//			5：拒收，
//			6：疑难件
//			7：退回
	
	public String errcode;
	public String id;
	public String message;
	public String name;
	public String order;
	public String status;
	public List<LogisticItemBean> data;
	
	public class LogisticItemBean{
//		 "content": "广东深圳公司宝安区马鞍山分部,快件已被 图片 签收",
//         "time": "2015-05-11 13:31:50"
		public String content;
		public String time;
	}

}
