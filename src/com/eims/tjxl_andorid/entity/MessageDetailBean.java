package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * 消息详情
 * Created by kuangyong on 2015/6/30.
 */
public class MessageDetailBean extends BaseBean{
    private String id;//消息表ID
    private String send_title;//标题
    private String send_time;//发送时间
    private String send_content;//发送内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSend_title() {
        return send_title;
    }

    public void setSend_title(String send_title) {
        this.send_title = send_title;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getSend_content() {
        return send_content;
    }

    public void setSend_content(String send_content) {
        this.send_content = send_content;
    }
}
