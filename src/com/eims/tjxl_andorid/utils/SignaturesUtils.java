/**
 * 
 */
package com.eims.tjxl_andorid.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 参数签名
 * @Author zd
 * @date 2015年6月22日 下午4:19:48
 *************************************************************************** 
 */
public class SignaturesUtils {

	/**
	 * 发送请求 参数排序签名
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static String getSignDataRQ(Map<String, Object> params)
			throws Exception {
		StringBuffer content = new StringBuffer();
		// 按照key做排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		String key, valueStr = "";
		for (int i = 0; i < keys.size(); i++) {
			key = (String) keys.get(i);
			if ("mKey".equals(key)) {
				continue;
			}
			valueStr = params.get(key).toString();// 值
			content.append(valueStr);// 值拼接成字符串
		}
		return MD5.md5(content.toString());// MD5把所有的值进行加密 然后拼接 密钥
	}

	public static String getSignData(HashMap<String, String> params) {

		StringBuffer content = new StringBuffer();
		// 按照key做排序
		try {
			List<String> keys = new ArrayList<String>(params.keySet());
			Collections.sort(keys);

			for (int i = 0; i < keys.size(); i++) {

				String key = (String) keys.get(i);
				System.out.println(keys);

				String valueStr = (String) params.get(key);
				valueStr=Des3.encode(valueStr); //编码中文，否则服务端生成的key与本地不同
				// 具体逻辑修改下面的路径进行拼接
				content.append(valueStr);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Log.i("content.toString()", "xie---" + content.toString());
		return MD5.md5(content.toString());

	}

}
