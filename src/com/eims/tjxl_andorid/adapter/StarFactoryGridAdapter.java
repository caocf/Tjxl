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
 * 明星厂家缩略图适配器
 * Created by kuangyong on 2015/6/25.
 */
public class StarFactoryGridAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<StarFactoryBean> data;//明星厂家列表数据

    public StarFactoryGridAdapter(Context context, ArrayList<StarFactoryBean> data){
        this.context=context;
        this.data=data;
    }

    public void setData(ArrayList<StarFactoryBean> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.factory_list_gv_item,null);
            holder=new MyHolder();
            holder.iv_pro=(ImageView)convertView.findViewById(R.id.iv_pro);
            holder.tv_num=(TextView)convertView.findViewById(R.id.tv_num);
            convertView.setTag(holder);
        }else{
            holder= (MyHolder)convertView.getTag();
        }
        ImageManager.Load(data.get(position).getBrand_img_url_m(), holder.iv_pro);
        holder.tv_num.setText(position+1+"");
        return convertView;
    }

    static class MyHolder{
        ImageView iv_pro;//图片
        TextView tv_num;//名称
    }
}
