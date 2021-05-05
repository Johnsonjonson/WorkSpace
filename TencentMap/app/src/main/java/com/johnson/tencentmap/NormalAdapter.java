package com.johnson.tencentmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.johnson.tencentmap.bean.Park;

import java.util.List;

// ① 创建Adapter
public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH>{
    // 事件回调监听
    private NormalAdapter.OnItemClickListener onItemClickListener;

    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        public final TextView parkName;
        public final LinearLayout parkLayout;
        public final ImageView parkToggle;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public VH(View v) {
            super(v);
            parkName = (TextView) v.findViewById(R.id.park_name);
            parkLayout = (LinearLayout) v.findViewById(R.id.park_layout_1);
            parkToggle = (ImageView) v.findViewById(R.id.park_toggle1);
            parkLayout.setBackground(v.getContext().getDrawable(R.drawable.layout_bg_normal));
        }
    }

    private List<Park> mDatas;
    private Activity context;
    public NormalAdapter(List<Park> data, Activity context) {
        this.mDatas = data;
        this.context = context;
    }

    //③ 在Adapter中实现3个方法
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final VH holder, int position) {
        Park parkItem = mDatas.get(position);
        holder.parkName.setText("停车位"+parkItem.getId());
        int status = parkItem.getStatus();
        boolean itemSelect = parkItem.isSelect();
        if(itemSelect){
            holder.parkLayout.setBackground(context.getDrawable(R.drawable.layout_bg));
        }else{
            holder.parkLayout.setBackground(context.getDrawable(R.drawable.layout_bg_normal));
        }
        if (status ==0){
            holder.parkToggle.setBackground(context.getApplication().getDrawable(R.drawable.park_kongxian_02e4f1));
        }else{
            holder.parkToggle.setBackground(context.getApplication().getDrawable(R.drawable.park_zhanyong_cdcdcd));
        }
        holder.parkLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                holder.parkLayout.setBackground(v.getContext().getDrawable(R.drawable.layout_bg));
//                Toast.makeText(, "", Toast.LENGTH_SHORT).show();
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            }
        });
        holder.parkLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                }
                //表示此事件已经消费，不会触发单击事件
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.park_item, parent, false);
        return new VH(v);
    }

    // ① 定义点击回调接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    // ② 定义一个设置点击监听器的方法
    public void setOnItemClickListener(NormalAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
