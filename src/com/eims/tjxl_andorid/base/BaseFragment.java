package com.eims.tjxl_andorid.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.eims.tjxl_andorid.utils.MD5;
import com.eims.tjxl_andorid.weght.SelectListItemWindow;

import java.util.ArrayList;

/**
 *  Created by kuangyong on 2015/7/1.
 */
public class BaseFragment extends Fragment{
    protected static final String LAYOUT_ID="layout_id";
    protected View layoutView;
    protected int layoutId;
    protected SelectListItemWindow listItemWindow;
    protected Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView=inflater.inflate(layoutId, container,false);
        return layoutView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            layoutId =  getArguments().getInt(LAYOUT_ID);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();
    }

    protected View findViewById(int id){
        return layoutView.findViewById(id);
    }

    protected void showListPopBelowView(Context context,View downview,ArrayList<String> data,AdapterView.OnItemClickListener itemsOnClick){
        int width=downview.getMeasuredWidth();
        int height=width;
        listItemWindow=new SelectListItemWindow(context,width,height,data,itemsOnClick);
        listItemWindow.showAsDropDown(downview);
    }

    protected void showToast(String showText){
        if(null!=context){
            Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 是否显示密码
     * @param textView
     */
    protected void changeTextPassWord(EditText textView,boolean isPassWord){
        if(isPassWord){
            textView.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//显示密码
        }else{
            textView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//隐藏密码
        }
        Editable etable = textView.getText();

        Selection.setSelection(etable, etable.length());
    }

    protected String md5(String content){
        return MD5.md5(content);
    }

}
