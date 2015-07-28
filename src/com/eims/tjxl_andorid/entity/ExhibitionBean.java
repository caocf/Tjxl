package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

import java.util.ArrayList;

/**
 * 展厅
 * Created by kuangyong on 2015/6/30.
 */
public class ExhibitionBean extends BaseBean{
    private String id;//展厅id
    private String exhibition;//展厅名称
    private ArrayList<String> imgList;//图片集合，第一张是大图，其它的都是小图

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExhibition() {
        return exhibition;
    }

    public void setExhibition(String exhibition) {
        this.exhibition = exhibition;
    }

    public ArrayList<String> getImgList() {
        return imgList;
    }

    public void setImgList(ArrayList<String> imgList) {
        this.imgList = imgList;
    }
}
