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
 * @date 2015年6月27日  上午9:30:14
 *************************************************************************** 
 */
public class ShopCartBean {
	
	
	public String type;
	public String msg;
	public List<ShopBean> data;
	
	public class ShopBean{
		//店铺名称
		public String f_factory_name;
		public String  seller_id;
		public List<Good> commodityList;
		public boolean isSelect=false;
		public int shopCount=0;//统计所有商品中货品的个数
		public int shopTempCount=0;//临时统计所有商品中选中货品的个数
	}
	//购物车商品信息
	public class Good{
		public String  commodity_id;
		public String  commodity_name;
		public String  main_img_m;
		public String  min_batch;
		public String  seller_id;
		public  List<GoodStandard> goodList;
		public boolean isSelect=false;
		public int totalQuantity;//统计选中货品的总数量
		public int tempCount=0;//统计该商品下被选中的货品的个数
		public String desc;//用于提示不满足起批量
	}
	//商品规格
	public class GoodStandard{

		public String  commodity_id;
		public String  good_code;
		public String  goods_stock;
		public String  price;
		public String  quantity;
		public String  spec_name_value;
		public String  sprice;
		public boolean isSelect=false;
		public boolean isCount=false;//改货品是否被计数
		
	}

}
