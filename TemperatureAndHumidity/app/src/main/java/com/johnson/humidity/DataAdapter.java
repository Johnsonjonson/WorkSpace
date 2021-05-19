package com.johnson.humidity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.johnson.humidity.bean.DataBean;

import java.util.List;

public class DataAdapter extends ArrayAdapter<DataBean> {
    private int resourceId;

    public DataAdapter(@NonNull Context context, int resource, @NonNull List<DataBean> objects) {
        super(context, resource, objects);
        resourceId=resource;
    }

    // convertView 参数用于将之前加载好的布局进行缓存
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DataBean data=getItem(position); //获取当前项的Fruit实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        ViewHolder viewHolder;
        if (convertView==null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ViewHolder();
            viewHolder.tvTime=view.findViewById(R.id.daka_time);
            viewHolder.tvWendu=view.findViewById(R.id.tv_wendu);
            viewHolder.tvShidu=view.findViewById(R.id.tv_shidu);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        viewHolder.tvTime.setText(data.getTime());
        viewHolder.tvWendu.setText("温度："+data.getWendu());
        viewHolder.tvShidu.setText("湿度："+data.getShidu());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView tvTime;
        TextView tvWendu;
        TextView tvShidu;
    }
}
