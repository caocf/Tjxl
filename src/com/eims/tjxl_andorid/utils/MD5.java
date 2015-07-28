/**
 * 
 */
package com.eims.tjxl_andorid.utils;

import java.security.MessageDigest;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月22日  下午4:29:26
 *************************************************************************** 
 */
public class MD5 {
	
	public static String md5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'b', 'c', 'd', 'e', 'f' };
		try {
		byte[] strTemp = s.getBytes();
		MessageDigest mdTemp = MessageDigest.getInstance("MD5");
		mdTemp.update(strTemp);
		byte[] md = mdTemp.digest();
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
		byte byte0 = md[i];
		str[k++] = hexDigits[byte0 >>> 4 & 0xf];
		str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
		} catch (Exception e) {
		return null;
		}
		}

}
