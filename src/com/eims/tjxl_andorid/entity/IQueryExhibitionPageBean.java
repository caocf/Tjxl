package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * 展厅数据
 * Created by kuangyong on 2015/6/30.
 */
public class IQueryExhibitionPageBean extends BaseBean{

    private ArrayList<ExhibitionBean> data;//展厅集合

    public ArrayList<ExhibitionBean> getData() {
        return data;
    }

    public void setData(ArrayList<ExhibitionBean> data) {
        this.data = data;
    }

    public static IQueryExhibitionPageBean explainJson(String json,Context context){
        try {
        	if(!json.contains("data")){
        		return null;
        	}
            Gson gson=new Gson();
            IQueryExhibitionPageBean bean=gson.fromJson(json,IQueryExhibitionPageBean.class);
            if(null!=bean){
                if (bean.getType() != 1) {
                    Toast.makeText(context, bean.getMsg(),
                            Toast.LENGTH_LONG).show();
                    return null;
                }
            }
            return bean;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
