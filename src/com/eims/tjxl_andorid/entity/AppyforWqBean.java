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
 * @date 2015年7月6日  下午4:10:35
 *************************************************************************** 
 */
public class AppyforWqBean {
	
	public List<GoodList> data;
	public String type;
	public String msg;
    public MapInfo map;
    public List<WqType> typeList;
    
    public class WqType{
    	public String id;
    	public String sort;
    	public String type_name;
    }
	public class GoodList{
		public String commodity_id;
		public String commodity_img_m;
		public String commodity_name;
		public List<GoodColorList> goodList;
	}
	public class GoodColorList{
		public String commodity_price;
		public String quantity;
		public String spec_name_value;
	}
	public class MapInfo{
		public String f_factory_name;
		public String id;
		public String total_price;
	}
}
