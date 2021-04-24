package com.hc.mixthebluetooth.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.hailong.biometricprompt.fingerprint.FingerprintCallback;
import com.hailong.biometricprompt.fingerprint.FingerprintVerifyManager;
import com.hc.mixthebluetooth.R;

public class LoginActivity extends FragmentActivity {

    private FingerprintVerifyManager.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        builder = new FingerprintVerifyManager.Builder(LoginActivity.this);
        builder.callback(fingerprintCallback)
                .fingerprintColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary))
                .build();
    }

    public void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private FingerprintCallback fingerprintCallback = new FingerprintCallback() {
        @Override
        public void onSucceeded() {
            Toast.makeText(LoginActivity.this, getString(R.string.biometricprompt_verify_success), Toast.LENGTH_SHORT).show();
//            LoginActivity.this.startActivity(new Intent(this,NFCActivity.class));

            changeActivity();
        }

        @Override
        public void onFailed() {
            Toast.makeText(LoginActivity.this, getString(R.string.biometricprompt_verify_failed), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onUsepwd() {
            Toast.makeText(LoginActivity.this, getString(R.string.fingerprint_usepwd), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
//            Toast.makeText(LoginActivity.this, getString(R.string.fingerprint_cancel), Toast.LENGTH_SHORT).show();
//            FingerprintVerifyManager.Builder builder = new FingerprintVerifyManager.Builder(LoginActivity.this);
//            builder.callback(fingerprintCallback)
//                    .fingerprintColor(ContextCompat.getColor(LoginActivity.this, R.color.colorPrimary))
            builder.build();
        }

        @Override
        public void onHwUnavailable() {
            Toast.makeText(LoginActivity.this, getString(R.string.biometricprompt_finger_hw_unavailable), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNoneEnrolled() {
            //弹出提示框，跳转指纹添加页面
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle(getString(R.string.biometricprompt_tip))
                    .setMessage(getString(R.string.biometricprompt_finger_add))
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.biometricprompt_finger_add_confirm), (new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_FINGERPRINT_ENROLL);
                            startActivity(intent);
                        }
                    }
                    ))
                    .setPositiveButton(getString(R.string.biometricprompt_cancel), (new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
                    ))
                    .create().show();
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
//        if builder.
//        builder.build();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        builder.build();
    }
}
