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
import com.eims.tjxl_andorid.entity.ListProductBean;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.MathUtil;
import com.eims.tjxl_andorid.utils.ResourceUtils;

import java.util.ArrayList;

/**
 * 列表适配器
 * Created by kuangyong on 2015/6/25.
 */
public class ProductListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<ListProductBean> data;//商品列表数据

    public ProductListAdapter(Context context, ArrayList<ListProductBean> data){
        this.context=context;
        this.data=data;
    }

    public void setData(ArrayList<ListProductBean> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.prolist_lv_item,null);
            holder=new MyHolder();
            holder.iv_pro=(ImageView)convertView.findViewById(R.id.iv_pro);
//            holder.tv_pro_name=(TextView)convertView.findViewById(R.id.tv_pro_name);
            holder.tv_fac_name=(TextView)convertView.findViewById(R.id.tv_fac_name);
            holder.tv_pro_num=(TextView)convertView.findViewById(R.id.tv_pro_num);
            holder.tv_brand_name=(TextView)convertView.findViewById(R.id.tv_brand_name);
            holder.tv_value_name=(TextView)convertView.findViewById(R.id.tv_value_name);
//            holder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            holder.tv_sprice=(TextView)convertView.findViewById(R.id.tv_sprice);
            convertView.setTag(holder);
        }else{
            holder= (MyHolder)convertView.getTag();
        }
        ImageManager.Load(data.get(position).getMain_img_m(), holder.iv_pro);
//        holder.tv_pro_name.setText(data.get(position).getCommodity_name());
//        //原价
//        SpannableString sp = null;
//        String price="原价"+MathUtil.priceForAppWithSign(data.get(position).getPrice());
//        sp = new SpannableString(price);
//        sp.setSpan(new StrikethroughSpan(), 0, price.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        holder.tv_price.setText(sp);
        //特价
        Spanned sprice=Html.fromHtml(ResourceUtils.changeStringColor("#C30001",MathUtil.priceForAppWithSign(data.get(position).getSprice())));
        holder.tv_sprice.setText(sprice);
        holder.tv_fac_name.setText(data.get(position).getF_factory_name());
        holder.tv_pro_num.setText(data.get(position).getCommodity_code());
        holder.tv_brand_name.setText(data.get(position).getBrand_name());
        holder.tv_value_name.setText(data.get(position).getSpec_name());
        return convertView;
    }

    static class MyHolder{
        ImageView iv_pro;//商品图片
        //        TextView tv_pro_name;//商品名称
//        TextView tv_price;//原价
        TextView tv_sprice;//特价
        TextView tv_fac_name;//厂家名称
        TextView tv_pro_num;//商品编号
        TextView tv_brand_name;//品牌名称
        TextView tv_value_name;//规格名称
    }
}
