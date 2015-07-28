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
 * @date 2015年6月25日  下午8:22:40
 *************************************************************************** 
 */
public class CityAdressBean {
	
	public List<Adress> data;
	
	//省
	public  class  Adress{
		 public String pId;
		 public String p_en;
		 public String pn;
		 public List<City> cities;
	} 
	//市
	 public class  City{
		 public String cId;
		 public String c_en;
		 public String cn;
		 public List<Areas> areas;
	 }
	 //显
	 public class Areas{
		 public String aId;
		 public String a_en;
		 public String an;
		 
	 } 

}
