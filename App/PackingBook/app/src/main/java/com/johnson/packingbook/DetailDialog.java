package com.johnson.packingbook;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailDialog extends AlertDialog {

    private final Builder builder;

    public DetailDialog(Context context, int themeResId) {
        super(context, themeResId);
        builder = new AlertDialog.Builder(context);

// 获取布局

        View view2 = View.inflate(context, themeResId, null);

// 获取布局中的控件

        final TextView yuyueStatus = view2.findViewById(R.id.tv_yuyue_status);

        final TextView parkName = view2.findViewById(R.id.tv_park_name);
        final TextView remainTime = view2.findViewById(R.id.yuyue_remain_time);

        final Button btnConfirm = view2.findViewById(R.id.btn_confirm);

// 设置参数

        builder.setTitle("停车详情").setIcon(R.drawable.ic_launcher_background).setView(view2);

// 创建对话框

        builder.create().show();

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });
    }
}
