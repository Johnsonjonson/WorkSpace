package com.johnson.qrcodedemo;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InfoDialog extends AlertDialog {

    private final Builder builder;

    public InfoDialog(Context context, int themeResId) {
        super(context, themeResId);
        builder = new Builder(context);

// 获取布局

        View view2 = View.inflate(context, themeResId, null);

// 获取布局中的控件

        final EditText etChepai = view2.findViewById(R.id.et_chepai);

        final EditText etPhone = view2.findViewById(R.id.et_phone);

        final Button btnConfirm = view2.findViewById(R.id.btn_confirm);

// 设置参数

        builder.setTitle("车牌信息").setIcon(R.drawable.ic_launcher_background).setView(view2);

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
