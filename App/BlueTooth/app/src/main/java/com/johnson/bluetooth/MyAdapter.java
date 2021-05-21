package com.johnson.bluetooth;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final ItemClickListener mListener;
    private ArrayList<MacBean> mDataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName;
        public TextView mTvMac;
        public MyViewHolder(View view) {
            super(view);
            mTvName = view.findViewById(R.id.tv_name);
            mTvMac = view.findViewById(R.id.tv_mac);
        }
    }

    public MyAdapter(ArrayList<MacBean> mDataSet,ItemClickListener listener) {
        this.mDataSet = mDataSet;
        this.mListener = listener;
    }

    //加载item的布局，并且创建ViewHolder实例
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = v.findViewById(R.id.tv_mac);
                mListener.onItemClick(textView.getText().toString());
                Log.d("RecyclerView",textView.getText().toString());


            }
        });
        return vh;
    }

    //给item填充数据
    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        holder.mTvName.setText("名称："+mDataSet.get(position).getName());
        holder.mTvMac.setText(mDataSet.get(position).getMac());
    }

    public void insertData(MacBean mac){
        mDataSet.add(mac);
        notifyItemInserted(mDataSet.size()+1);
    }

    //返回Item的个数
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

