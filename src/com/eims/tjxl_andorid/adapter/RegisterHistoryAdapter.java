package com.eims.tjxl_andorid.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.ListProductBean;
import com.eims.tjxl_andorid.entity.RegistedListBean;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.MathUtil;
import com.eims.tjxl_andorid.utils.ResourceUtils;

/**
 * 已注册的店铺列表适配器
 * Created by kuangyong on 2015/6/25.
 */
public class RegisterHistoryAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<RegistedListBean> data;//店铺列表数据

    public RegisterHistoryAdapter(Context context, ArrayList<RegistedListBean> data){
        this.context=context;
        this.data=data;
    }

    public void setData(ArrayList<RegistedListBean> data){
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
            convertView= LayoutInflater.from(context).inflate(R.layout.registerlist_item,null);
            holder=new MyHolder();
            holder.iv_register=(ImageView)convertView.findViewById(R.id.iv_register);
            holder.tv_num=(TextView)convertView.findViewById(R.id.tv_num);
            holder.tv_name=(TextView)convertView.findViewById(R.id.tv_name);
            holder.tv_adress=(TextView)convertView.findViewById(R.id.tv_adress);
            holder.endline=(View)convertView.findViewById(R.id.endline);
            convertView.setTag(holder);
        }else{
            holder= (MyHolder)convertView.getTag();
        }
        int visible=position==data.size()-1?View.VISIBLE:View.GONE;
        holder.endline.setVisibility(visible);
        ImageManager.Load(data.get(position).getS_adv_img(), holder.iv_register);
        holder.tv_num.setText(position + 1 + "");
        holder.tv_name.setText(data.get(position).getS_store_name());
        holder.tv_adress.setText(data.get(position).getS_address_info());
        return convertView;
    }

    static class MyHolder{
        TextView tv_num;//序号
        TextView tv_name;//厂家名称
        TextView tv_adress;//地址
        ImageView iv_register;//图片
        View endline;
    }
}
