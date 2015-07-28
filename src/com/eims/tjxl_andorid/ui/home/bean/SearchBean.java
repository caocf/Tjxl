/**
 * 
 */
package com.eims.tjxl_andorid.ui.home.bean;

import java.util.Comparator;
import java.util.Date;

import android.util.Log;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月22日  上午11:46:05
 *************************************************************************** 
 */
public class SearchBean{
	
	public String type;
	public String searchText;
	public long date;
	public static Comparator<SearchBean> comparator = new Comparator<SearchBean>() {

		@Override
		public int compare(SearchBean arg0, SearchBean arg1) {
			if(arg0 != null && arg1 != null){
				Log.d("SearchBean", arg0.getSearchText()+":date1 ="+arg0.getDate()+","+arg1.getSearchText()+"date2 = "+arg1.getDate());
				if (arg0.getDate() > arg0.getDate()) {
					 return 1;
				}else if (arg0.getDate() < arg0.getDate()) {
					return -1;
				}
			}
			return 0;
		}
	};
	
	public SearchBean(){
		type = "";
		searchText = "";
		date = 0;
	}
	
	public SearchBean(String searchText){
		this.searchText = searchText;
		this.type = "";
		this.date = 0;
	}

	public boolean isEquel(SearchBean bean){
		if(this.searchText.equals(bean.searchText)){
			return true;
		}else {
			return false;
		}
	}
	public String toStringPart(){
		return ""+"-"+searchText;
	}
	@Override
	public String toString() {
		return ""+"-"+searchText+"-"+date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}


	public long getDate() {
		return date;
	}


	public void setDate(long date) {
		this.date = date;
	}
	
}
