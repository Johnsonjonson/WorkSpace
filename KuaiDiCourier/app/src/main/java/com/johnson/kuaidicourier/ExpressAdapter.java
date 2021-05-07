package com.johnson.kuaidicourier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.johnson.kuaidicourier.bean.Express;

import java.util.List;

// ① 创建Adapter
public class ExpressAdapter extends RecyclerView.Adapter<ExpressAdapter.VH>{
    // 事件回调监听
    private OnItemClickListener onItemClickListener;

    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView expressId;
//        public final TextView expressStart;
//        public final TextView expressEnd;
        public final TextView expressFee;
        public final TextView expressTime;
//        public final LinearLayout parkLayout;
//        public final ImageView parkToggle;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public VH(View v) {
            super(v);
            expressId = (TextView) v.findViewById(R.id.express_id);
//            expressStart = (TextView) v.findViewById(R.id.express_start);
//            expressEnd = (TextView) v.findViewById(R.id.express_end);
            expressFee = (TextView) v.findViewById(R.id.express_fee);
            expressTime = (TextView) v.findViewById(R.id.express_time);
        }
    }

    private List<Express> mDatas;
    private Activity context;
    public ExpressAdapter(List<Express> data,Activity context) {
        this.mDatas = data;
        this.context = context;
    }

    //③ 在Adapter中实现3个方法
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(VH holder, int position) {
        Express express = mDatas.get(position);
        holder.expressId.setText("ID"+express.getId());
//        holder.expressStart.setText("起点："+express.getStart());
//        holder.expressEnd.setText("终点："+express.getEnd());
        holder.expressFee.setText(""+express.getFee()+"元");
        holder.expressTime.setText(""+express.getTime());
//        int status = parkItem.getStatus();
//        boolean itemSelect = parkItem.isSelect();
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.express_item, parent, false);
        return new VH(v);
    }

    // ① 定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    // ② 定义一个设置点击监听器的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
