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
 * @date 2015年6月23日  下午6:02:57
 *************************************************************************** 
 */
public class VipBean {
	
	public String type;
	public List<VipInfo> msg;
	
	public class VipInfo{
		public String s_address_info;
		public String s_adv_img;
		public String s_store_name;
	}

}
