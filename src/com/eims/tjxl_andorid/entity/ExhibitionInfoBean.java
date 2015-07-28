package com.eims.tjxl_andorid.entity;

import com.eims.tjxl_andorid.base.BaseBean;

/**
 * Created by kuangyong on 2015/6/30.
 */
public class ExhibitionInfoBean extends BaseBean{
    private String id;//展厅ID
    private String exhibition;//展厅名称
    private String small_url_m;//展厅图片
    private String description;//详情内容

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

    public String getSmall_url_m() {
        return small_url_m;
    }

    public void setSmall_url_m(String small_url_m) {
        this.small_url_m = small_url_m;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
