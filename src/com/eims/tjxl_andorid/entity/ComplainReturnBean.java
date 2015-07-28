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
public class ComplainReturnBean extends BaseBean{
    private static final String TAG="ComplainReturnBean";
    private String data;//申诉账号
    private String username;//登陆账号
    private String password;//登陆密码
    private String content;//申诉失败原因

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static ComplainReturnBean explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            if(LogUtil.isDebug){
                Log.i(TAG,"申诉结果---"+json);
            }
            ComplainReturnBean bean=gson.fromJson(json,ComplainReturnBean.class);
            if(null==bean){
                Toast.makeText(context, "网络错误",
                        Toast.LENGTH_LONG).show();
                return null;
            }
            return bean;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
