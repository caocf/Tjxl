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
 *************************************************************************** 
 */
public class GoodDetail  implements Serializable{

    //商品信息
	public GoodInfo pdMap;
	
	public List<String> imgArr;
	
	public List<GoodColor> colorList;
	
	public List<GoodSize> goodList;
	
	public List<GoodPropert> propertyList;
	
	public ShoeUser userMap;
	
	public class GoodPropert  implements Serializable{
		public String commodity_id;
		public String id;
		public String property_name;
		public String property_name_id;
		public String property_value;
		public String property_value_id;
	}
   
   public class GoodInfo implements Serializable{
	   //商品名称
	   public String commodity_name;
	   //折扣价
	   public String sprice;
	   //起批数
	   public String min_batch;
	   //已售
	   public String total_sell;
	   
	   public String brand_name;
	   
	   public String commodity_code;
	   
	   public String id;
	   
	   public String user_id;
	   public String content;//图文详情
	   
   }
   
   public class GoodColor implements Serializable{	   
	   public String commodity_code;
	   public String commodity_id;
	   public String goods_code;
	   public String goods_color;
	   public String goods_color_img;
	   public String goods_price;
	   public String goods_stock;
	   public String goods_weight;
	   public String id;
	   public String spec_name;
	   public String spec_name_ids;
	   public String spec_value;
	   public String spec_value_ids;
	   public String sprice;
   }
   //商品规格  ---颜色对应的尺码
   public class GoodSize implements Serializable{	
	   
	   public String commodity_code;
	   public String commodity_id;
	   public String goods_code;
	   public String goods_color;
	   public String goods_color_img;
	   public String goods_price;
	   public String goods_stock;//库存
	   public String goods_weight;
	   public String id;
	   public String spec_name;
	   public String spec_name_ids;
	   public String spec_value;
	   public String spec_value_ids;
	   public String sprice;
	   //shu  liang
	   public int quantity;
   }
   
   public  class ShoeUser  implements Serializable{
	   public String province_name;
	   public String city_name;
	   public String f_qq;
	   public String id;
	   public String f_factory_name;
	   public String head_img;//店铺头像
   }
   
}
