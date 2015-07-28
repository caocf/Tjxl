package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 申诉
 * Created by kuangyong on 2015/6/30.
 */
public class ComplainBean extends BaseBean{
    private String appeal_pwd;//申诉密码
    private String appeal_code;//申诉账号

    public String getAppeal_pwd() {
        return appeal_pwd;
    }

    public void setAppeal_pwd(String appeal_pwd) {
        this.appeal_pwd = appeal_pwd;
    }

    public String getAppeal_code() {
        return appeal_code;
    }

    public void setAppeal_code(String appeal_code) {
        this.appeal_code = appeal_code;
    }
}
