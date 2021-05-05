package com.hc.mixthebluetooth.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

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
    @ViewById(R.id.send_a)
    private Button btnSendA;
    @ViewById(R.id.send_a)
    private Button btnSendB;
    @ViewById(R.id.send_a)
    private Button btnSendC;

    private Handler mHandler;

    private DeviceModule module;
    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_send_message;
    }

    @Override
    public void initAll() {

    }

    @Override
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @OnClick({R.id.send_a,R.id.send_b,R.id.send_c})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.send_a:
                toast("A按钮被点击");
//                setSendData();
                sendData(new FragmentMessageItem(true, Analysis.getBytes("1", true), null, true, module, false));
                break;
            case R.id.send_b:
                toast("B 按钮被点击");
                sendData(new FragmentMessageItem(true, Analysis.getBytes("b", true), null, true, module, false));
//                if (mSendBt.getText().toString().equals("发送"))
//                    mDataET.setText("");
//                else
//                    toast("连续发送中，不能清除发送区的数据");
                break;
            case R.id.send_c:
                sendData(new FragmentMessageItem(true, Analysis.getBytes("c", true), null, true, module, false));
                toast("c按钮被点击");
                break;

        }
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
