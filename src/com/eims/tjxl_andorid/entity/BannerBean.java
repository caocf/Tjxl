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
 * @date 2015年7月15日  下午2:27:36
 *************************************************************************** 
 */
public class BannerBean {
	
	public List<BannerItem> data;
	public class BannerItem{

		public String ids; //商品id
		public String img;//图片地址
		public String skip_type;//跳转类型
		public String url;
	}

}
