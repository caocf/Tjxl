package com.eims.tjxl_andorid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.util.Log;

import org.apache.http.util.EncodingUtils;

public class FileUtils {
	
	public long getFileSizes(File f) {// 取得文件大小
		long s = 0;

		FileInputStream fis = null;
		try {
			if (f.exists()) {
				fis = new FileInputStream(f);
				s = fis.available();
			} else {
				f.createNewFile();
				System.out.println("文件不存在");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return s;
	}

	// 递归
	public static long getFileSize(File f) {// 取得文件夹大小
		if(!f.isDirectory()){
			f.mkdirs();
			Log.d("zhiheng"," has mkdir successful :"+f.mkdirs());
		}
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public long getlist(File f) {// 递归求取目录文件个数
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;

	}

	public static String ReadTxtFile(String StrFile,Context context) {
		String res = "";
		try {
			// 读取raw文件夹中的txt文件,将它放入输入流中
			// InputStream in = getResources().openRawResource(R.raw.ansi);
			// 读取assets文件夹中的txt文件,将它放入输入流中
			InputStream in = context.getResources().getAssets().open(StrFile);
			// 获得输入流的长度
			int length = in.available();
			// 创建字节输入
			byte[] buffer = new byte[length];
			// 放入字节输入中
			in.read(buffer);
			// 获得编码格式
			res = EncodingUtils.getString(buffer, "UTF-8");
			// 关闭输入流
			in.close();
		} catch (Exception e) {
			Log.i("x", "错误" + e.getMessage());
		}
		return res;
	}
}
