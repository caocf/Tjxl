package com.eims.tjxl_andorid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.StarFactoryBean;
import com.eims.tjxl_andorid.utils.ImageManager;

import java.util.ArrayList;

/**
 * 加盟厂家图片图适配器
 * Created by kuangyong on 2015/6/25.
 */
public class FactoryImgGridAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> data;//

    public FactoryImgGridAdapter(Context context, ArrayList<String> data){
        this.context=context;
        this.data=data;
    }

    public void setData(ArrayList<String> data){
        this.data=data;
    }

    @Override
    public int getCount() {
        return (null==data || 0==data.size())?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder=null;
        if(null==convertView){
            convertView= LayoutInflater.from(context).inflate(R.layout.factory_gv_item,null);
            holder=new MyHolder();
            holder.iv_pro=(ImageView)convertView.findViewById(R.id.iv_pro);
            convertView.setTag(holder);
        }else{
            holder= (MyHolder)convertView.getTag();
        }
        ImageManager.Load(data.get(position), holder.iv_pro);
        return convertView;
    }

    static class MyHolder{
        ImageView iv_pro;//图片
    }
}
