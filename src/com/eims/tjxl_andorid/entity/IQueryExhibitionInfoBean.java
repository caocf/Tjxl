package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Created by kuangyong on 2015/6/30.
 */
public class IQueryExhibitionInfoBean extends BaseBean{
    private ExhibitionInfoBean data;

    public ExhibitionInfoBean getData() {
        return data;
    }

    public void setData(ExhibitionInfoBean data) {
        this.data = data;
    }

    public static IQueryExhibitionInfoBean explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            IQueryExhibitionInfoBean bean=gson.fromJson(json,IQueryExhibitionInfoBean.class);
            if(null!=bean){
                if (bean.getType()<= 0) {
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
