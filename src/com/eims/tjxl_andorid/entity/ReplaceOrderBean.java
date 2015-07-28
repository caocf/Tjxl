/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import java.util.List;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 换货订单
 * @Author zd
 * @date 2015年7月3日  下午2:20:34
 *************************************************************************** 
 */
public class ReplaceOrderBean {
	
	public List<ReplaceItemBean> data;
	
	public class ReplaceItemBean{
//	    "commodity_img_m": "http://192.168.3.66:8080/xlw/attached/image/2015/20150226/201502261752506971.jpg",
//        "f_factory_home": "http:192.168.3.65:8080/xlw/",
//        "hp": 1,
//        "id": 17,
//        "is_out": 1,
//        "quantity": 3,
//        "repace_time0": "2015-06-02 17:20:01",
//        "replace_code": "RH20150602172001201",
//        "replace_statu": 2,
//        "statu_name": "待付款",
//        "total_price": 0.01,
//        "uygur_power_id": -1
		public String commodity_img_m;
		public String f_factory_home;
		public String hp;
		public String id;
		public String is_out;
		public String quantity;
		public String repace_time0;
		public String replace_code;
		public String replace_statu;
		public String statu_name;
		public String total_price;
		public String uygur_power_id;
	}

}
