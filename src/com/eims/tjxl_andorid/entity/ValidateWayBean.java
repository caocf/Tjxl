package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 验证方式
 * Created by kuangyong on 2015/7/1.
 */
public class ValidateWayBean extends BaseBean{

    private String phone;//手机号码
    private String is_verify_phone;//手机验证(0未验证，1已验证)
    private String email;//邮箱
    private String is_verify_email;//邮箱验证(0未验证，1已验证)

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_verify_phone() {
        return is_verify_phone;
    }

    public void setIs_verify_phone(String is_verify_phone) {
        this.is_verify_phone = is_verify_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_verify_email() {
        return is_verify_email;
    }

    public void setIs_verify_email(String is_verify_email) {
        this.is_verify_email = is_verify_email;
    }
}
