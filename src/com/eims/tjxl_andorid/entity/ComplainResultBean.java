package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.eims.tjxl_andorid.base.BaseBean;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 申诉结果
 * Created by kuangyong on 2015/6/30.
 */
public class ComplainResultBean extends BaseBean{
    private static final String TAG="ComplainResultBean";
    private ComplainBean data;//申诉结果

    public ComplainBean getData() {
        return data;
    }

    public void setData(ComplainBean data) {
        this.data = data;
    }

    public static ComplainResultBean explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            if(LogUtil.isDebug){
                Log.i(TAG,"申诉结果---"+json);
            }
            ComplainResultBean bean=gson.fromJson(json,ComplainResultBean.class);
            if(null!=bean){
                if (bean.getType() <=0) {
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
