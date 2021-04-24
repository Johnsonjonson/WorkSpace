package com.hc.mixthebluetooth.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.hc.basiclibrary.ioc.OnClick;
import com.hc.basiclibrary.ioc.ViewById;
import com.hc.basiclibrary.titleBasic.DefaultNavigationBar;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.customView.PopWindowFragment;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;

public class FragmentSendMsg extends BasFragment {
    private Handler mHandler;
    private DeviceModule module;

//    @ViewById(R.id.switch1)
//    private Switch switch1;
    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_send_message;
    }

    @Override
    public void initAll() {
//        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                String msg = "0";
//                if(b){
//                    msg = "1";
//                }
//                toast("A按钮被点击      "+msg);
//                sendData(new FragmentMessageItem(true, Analysis.getBytes("0", true), null, true, module, false));
//            }
//        });
    }

    @Override
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void readData(int state,Object o, final byte[] data) {
        switch (state){
            case CommunicationActivity.FRAGMENT_STATE_DATA:
                if (module == null) {
                    module = (DeviceModule) o;
                }
                if (data != null) {
//                    mDataList.add(new FragmentMessageItem(Analysis.getByteToString(data,isReadHex), isShowTime?Analysis.getTime():null, false, module,isShowMyData));
//                    mAdapter.notifyDataSetChanged();
//                    mRecyclerView.smoothScrollToPosition(mDataList.size());
//                    mReadNumberTV.setText(String.valueOf(Integer.parseInt(mReadNumberTV.getText().toString())+Analysis.lengthArray(data)));
                }
                break;
            case CommunicationActivity.FRAGMENT_STATE_NUMBER:
//                mSendNumberTv.setText(String.valueOf(Integer.parseInt(mSendNumberTv.getText().toString())+((int) o)));
//                setUnsentNumberTv();
                break;
            case CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE:
//                mTitle = (DefaultNavigationBar) o;
                break;
        }

    }

    private void sendData(FragmentMessageItem item) {
        if (mHandler == null)
            return;
        Message message = mHandler.obtainMessage();
        message.what = CommunicationActivity.DATA_TO_MODULE;
        message.obj = item;
        mHandler.sendMessage(message);
//        if (isShowMyData) {
//            mDataList.add(item);
//            mAdapter.notifyDataSetChanged();
//            mRecyclerView.smoothScrollToPosition(mDataList.size());
//        }
//
//        //发送完计数
//        int number = Analysis.changeHexString(true, mDataET.getText().toString().replaceAll(" ", "")).length()/3;
//        if (isSendHex)
//            number = number%2 == 0?number/2:(number+1)/2;
//        mUnsentNumber += number;
    }
}
