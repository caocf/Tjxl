package com.eims.tjxl_andorid.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.BrandStoreBean;
import com.eims.tjxl_andorid.utils.ImageManager;

import java.util.ArrayList;

/**
 * 品牌馆列表适配器
 * Created by kuangyong on 2015/6/25.
 */
public class BrandStoreListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<BrandStoreBean> data;//品牌馆列表数据

    public BrandStoreListAdapter(Context context, ArrayList<BrandStoreBean> data){
        this.context=context;
        this.data=data;
    }

    public void setData(ArrayList<BrandStoreBean> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.brandlist_lv_item,null);
            holder=new MyHolder();
            holder.iv_pro=(ImageView)convertView.findViewById(R.id.iv_pro);
            holder.tv_pro_name=(TextView)convertView.findViewById(R.id.tv_pro_name);
            holder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            holder.tv_sprice=(TextView)convertView.findViewById(R.id.tv_sprice);
            convertView.setTag(holder);
            //隐藏
            holder.tv_price.setVisibility(View.GONE);
            holder.tv_sprice.setVisibility(View.GONE);
        }else{
            holder= (MyHolder)convertView.getTag();
        }
        Log.d("zhiheng", "iamge url :"+data.get(position).getBrand_img_url_m());
        ImageManager.Load(data.get(position).getBrand_img_url_m(), holder.iv_pro);
        holder.tv_pro_name.setText(data.get(position).getBrand_name());
        return convertView;
    }

    static class MyHolder{
        ImageView iv_pro;//商品图片
        TextView tv_pro_name;//商品名称
        TextView tv_price;//原价
        TextView tv_sprice;//特价
    }
}
