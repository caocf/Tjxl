package com.eims.tjxl_andorid.entity;

import android.text.TextUtils;

import java.io.File;


/**图片数据**/
public class ImageData{
	
	public String fileName;
	public String imgUrl;

	public ImageData(String fileName, String imgUrl){
		
		this.fileName = fileName;
		this.imgUrl = imgUrl;
	}
	
	public boolean localFileExist(){
		if(TextUtils.isEmpty(fileName)){
			return false;
		}
		
		File file = new File(fileName);
		boolean exits = file.exists();
		file = null;
		return exits;
	}

	/**
	 * 判断两个文件名是否一致
	 * @param imgFile
	 * @return
	 */
	public boolean equalsFile(String imgFile) {
		if(TextUtils.isEmpty(fileName) || TextUtils.isEmpty(imgFile)){
			return false;
		}
		return fileName.equals(imgFile);
	}
}
