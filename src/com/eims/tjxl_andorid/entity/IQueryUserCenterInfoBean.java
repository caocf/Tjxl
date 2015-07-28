package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * Created by kuangyong on 2015/6/27.
 */
public class IQueryUserCenterInfoBean extends BaseBean{
    private int xiaoxi_count;          //未读消息个数
    private int commodity_count;       //收藏商品个数
    private int store_count;           //收藏店铺个数
    private int  pay_count;            //待付款个数
    private int dai_count;             //待发货个数
    private int shou_count;            //待收货个数
    private int ping_count;            //待评论个数
    private int rep_count;             //售后个数
    private int com_qty;               //购物车总数量
    private String id;                 //会员ID
    private String username;           //用户名
    private String  head_img;          //头像

    public int getXiaoxi_count() {
        return xiaoxi_count;
    }

    public void setXiaoxi_count(int xiaoxi_count) {
        this.xiaoxi_count = xiaoxi_count;
    }

    public int getCommodity_count() {
        return commodity_count;
    }

    public void setCommodity_count(int commodity_count) {
        this.commodity_count = commodity_count;
    }

    public int getStore_count() {
        return store_count;
    }

    public void setStore_count(int store_count) {
        this.store_count = store_count;
    }

    public int getPay_count() {
        return pay_count;
    }

    public void setPay_count(int pay_count) {
        this.pay_count = pay_count;
    }

    public int getDai_count() {
        return dai_count;
    }

    public void setDai_count(int dai_count) {
        this.dai_count = dai_count;
    }

    public int getShou_count() {
        return shou_count;
    }

    public void setShou_count(int shou_count) {
        this.shou_count = shou_count;
    }

    public int getPing_count() {
        return ping_count;
    }

    public void setPing_count(int ping_count) {
        this.ping_count = ping_count;
    }

    public int getRep_count() {
        return rep_count;
    }

    public void setRep_count(int rep_count) {
        this.rep_count = rep_count;
    }

    public int getCom_qty() {
        return com_qty;
    }

    public void setCom_qty(int com_qty) {
        this.com_qty = com_qty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }
}
