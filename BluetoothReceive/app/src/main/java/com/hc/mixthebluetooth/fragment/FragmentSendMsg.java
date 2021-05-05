package com.hc.mixthebluetooth.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    @ViewById(R.id.tv_data)
    private TextView tvData;
    @ViewById(R.id.tv_id)
    private TextView tvID;
    @ViewById(R.id.tv_test)
    private TextView tvTest;
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

    @Override
    public void readData(int state,Object o, final byte[] data) {
        switch (state){
            case CommunicationActivity.FRAGMENT_STATE_DATA:
                if (module == null) {
                    module = (DeviceModule) o;
                }
                if (data != null) {
                    byte id = data[0];
                    byte xueyang = data[1];
                    String ids = String.valueOf(id);
                    String xueyangs = String.valueOf(xueyang);
                    String byteToString = Analysis.getByteToString(data, true);
                    FragmentMessageItem messageItem =  new FragmentMessageItem(Analysis.getByteToString(data,true), true?Analysis.getTime():null, false, module,false);
                    String data1 = messageItem.getData();
                    tvID.setText("ID："+ids);
                    tvData.setText("血氧："+xueyangs);
                    tvTest.setText("测试："+byteToString);

                    //                    mDataList.add(new FragmentMessageItem(Analysis.getByteToString(data,isReadHex), isShowTime?Analysis.getTime():null, false, module,isShowMyData));
//                    mAdapter.notifyDataSetChanged();
//                    mRecyclerView.smoothScrollToPosition(mDataList.size());
//                    mReadNumberTV.setText(String.valueOf(Integer.parseInt(mReadNumberTV.getText().toString())+Analysis.lengthArray(data)));
                }
                break;
            case CommunicationActivity.FRAGMENT_STATE_NUMBER:
                String valueOf = String.valueOf(o);
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
    }
}
