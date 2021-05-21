package com.hacknife.sample.widget;

import android.content.Context;

import com.hacknife.briefness.BindLayout;
import com.hacknife.sample.BriefnessInjector;
import com.hacknife.sample.R;
import com.hacknife.sample.widget.base.Dialog;

@BindLayout(R.layout.dialog_connected)
public class ConnectedDialog extends Dialog<ConnectedDialogBriefnessor> {
    String content;

    public ConnectedDialog(Context context) {
        super(context);
    }

    public void onCancelClick() {
        dismiss();
    }

    public interface OnConnectedDialogListener {
        void onConfirm();
    }

    OnConnectedDialogListener onConnectedDialogListener;

    public ConnectedDialog content(String content) {
        this.content = content;
        return this;
    }

    @Override
    protected void initView() {
        BriefnessInjector.injector(briefnessor.tv_content, content);
    }

    public ConnectedDialog setOnConnectedDialogListener(OnConnectedDialogListener onConnectDialogListener) {
        this.onConnectedDialogListener = onConnectDialogListener;
        return this;
    }

    public void onConfirmClick() {
        dismiss();
        if (onConnectedDialogListener != null)
            onConnectedDialogListener.onConfirm();
    }
}
