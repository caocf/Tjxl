package com.eims.tjxl_andorid.entity;

import android.content.Context;
import android.widget.Toast;

import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.base.BaseBean;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 加盟厂家
 * Created by kuangyong on 2015/6/28.
 */
public class IUserInfo extends BaseBean{
    private FactoryInfoBean data;//个人资料
    private UserMap userMap;//工商信息
    private OurMap ourMap;//联系我们

    public UserMap getUserMap() {
        return userMap;
    }

    public void setUserMap(UserMap userMap) {
        this.userMap = userMap;
    }

    public OurMap getOurMap() {
        return ourMap;
    }

    public void setOurMap(OurMap ourMap) {
        this.ourMap = ourMap;
    }

    public FactoryInfoBean getData() {
        return data;
    }

    public void setData(FactoryInfoBean data) {
        this.data = data;
    }
    public static IUserInfo explainJson(String json,Context context){
        try {
            Gson gson=new Gson();
            IUserInfo bean=gson.fromJson(json,IUserInfo.class);
            if(null!=bean){
                if (bean.getType() <= 0) {
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
