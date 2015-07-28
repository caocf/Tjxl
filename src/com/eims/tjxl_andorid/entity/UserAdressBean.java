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
 * @date 2015年6月29日  上午9:50:23
 *************************************************************************** 
 */
public class UserAdressBean  implements Serializable{
	
	public String type;
	public String msg;
	public List<AdressBean> data;

	public  class  AdressBean implements Serializable{

		public String address;
		public String address_show;
		public String area_id;
		public String city_id;
		public String consignee_name;
		public String consignee_phone;
		public String df_delivery_address_id;
		public String id;
		public String postal_code;
		public String province_id;
		public String user_id;
	}
}
