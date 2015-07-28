package com.eims.tjxl_andorid.adapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.db.dbmodel.Product;
import com.eims.tjxl_andorid.entity.ListProductBean;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.MathUtil;
import com.eims.tjxl_andorid.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 浏览记录适配器
 * Created by kuangyong on 2015/6/25.
 */
public class BrowseHistoryListAdapter extends BaseAdapter{
    private Context context;
    private List<Product> data;//商品列表数据

    public BrowseHistoryListAdapter(Context context, List<Product> data){
        this.context=context;
        this.data=data;
    }

    public void setData(List<Product> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.browse_history_lv_item,null);
            holder=new MyHolder();
            holder.iv_pro=(ImageView)convertView.findViewById(R.id.iv_pro);
            holder.tv_pro_name=(TextView)convertView.findViewById(R.id.tv_pro_name);
            holder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            holder.tv_sprice=(TextView)convertView.findViewById(R.id.tv_sprice);
            convertView.setTag(holder);
        }else{
            holder= (MyHolder)convertView.getTag();
        }
        ImageManager.Load(data.get(position).img, holder.iv_pro);
        holder.tv_pro_name.setText(data.get(position).name);
        Spanned sprice=Html.fromHtml(ResourceUtils.changeStringColor("#C30001",MathUtil.priceForAppWithSign(data.get(position).price)));
        holder.tv_price.setText(sprice);
        holder.tv_sprice.setText("已销售"+data.get(position).sales_num+"双");
        return convertView;
    }

    static class MyHolder{
        ImageView iv_pro;//商品图片
        TextView tv_pro_name;//商品名称
        TextView tv_price;//价格
        TextView tv_sprice;//销量
    }
}
