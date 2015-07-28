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
 * @date 2015年6月24日  上午9:45:20
 *************************************************************************** 
 */
public class HomeBean {

	public List<Brand> bdList;
	public List<StarFactory> fcList;
	public List<HotGood> htList;
	public List<SampleGood> smList;
	public List<SaleGood> spList;
	public List<ExGood> exList;

	/**品牌馆**/
	public class Brand{
		public String commodity_id;
		public String commodity_name;
		public String description;
		public String id;
		public String img;
		public String type;
		
	}
	/**明星厂家**/
	public class StarFactory{
		public String commodity_id;
		public String commodity_name;
		public String description;
		public String id;
		public String img;
		public String type;
		
	}
	
	/**热卖商品**/
	public class HotGood{
		public String commodity_id;
		public String commodity_name;
		public String description;
		public String id;
		public String img;
		public String type;
		public String is_del;
		public String is_sample;
		public String is_sell;

		
	}
	/**样品推荐**/
	public class SampleGood{
		public String commodity_id;
		public String commodity_name;
		public String description;
		public String id;
		public String img;
		public String type;
		public String is_del;
		public String is_sample;
		public String is_sell;
		
	}
	/**特价专区**/
	public class SaleGood{
		public String commodity_id;
		public String commodity_name;
		public String description;
		public String id;
		public String img;
		public String type;
		public String is_del;
		public String is_sample;
		public String is_sell;
		
	}
	/**实体展厅**/
	public class ExGood{
		public String commodity_id;
		public String commodity_name;
		public String id;
		public String img;
		public String type;
		public String is_del;
		public String is_sample;
		public String is_sell;
	}

}
