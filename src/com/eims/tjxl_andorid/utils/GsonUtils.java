package com.eims.tjxl_andorid.utils;

import com.google.gson.Gson;

/**
 * 
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: Gson解析工具类
 * @Author zd
 * @date 2015年4月14日 上午11:05:33
 *************************************************************************** 
 */
public class GsonUtils {

	public static <T> T json2bean(String result, Class<T> clazz) {
		Gson gson = new Gson();
		T t = gson.fromJson(result, clazz);
		return t;
	}

}
