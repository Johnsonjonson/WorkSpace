package com.hc.mixthebluetooth.recyclerData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.bean.User;

import java.util.List;

/**
 * Created by Jackie on 2015/12/18. 
 * RecyclerView Adapter 
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.RecyclerViewHolder> {
    private OnItemClickListener mOnItemClickListener;
    private Context mContext;
    private List<User> mNumberList;

    public UserAdapter(Context context, List<User> numberList) {
        this.mContext = context;
        this.mNumberList = numberList;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public UserAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder holder = new RecyclerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.user_item_layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final UserAdapter.RecyclerViewHolder holder, final int position) {
        User user = mNumberList.get(position);
        holder.numberTextView.setText(user.getName());
        // 如果设置了回调，则设置点击事件 
        if (mOnItemClickListener != null) {
            holder.numberTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }

        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberList.remove(position);
                //通知刷新 
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNumberList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView numberTextView;
        ImageButton deleteImageButton;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            numberTextView = (TextView) itemView.findViewById(R.id.number_item);
            deleteImageButton = (ImageButton) itemView.findViewById(R.id.number_item_delete);
        }
    }
}