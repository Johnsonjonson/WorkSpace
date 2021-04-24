package com.hc.mixthebluetooth.fragment;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hc.basiclibrary.ioc.OnClick;
import com.hc.basiclibrary.ioc.ViewById;
import com.hc.basiclibrary.ioc.ViewByIds;
import com.hc.basiclibrary.titleBasic.DefaultNavigationBar;
import com.hc.basiclibrary.viewBasic.BasFragment;
import com.hc.bluetoothlibrary.DeviceModule;
import com.hc.mixthebluetooth.DataAdapter;
import com.hc.mixthebluetooth.DataBean;
import com.hc.mixthebluetooth.R;
import com.hc.mixthebluetooth.activity.CommunicationActivity;
import com.hc.mixthebluetooth.activity.tool.Analysis;
import com.hc.mixthebluetooth.customView.CheckBoxSample;
import com.hc.mixthebluetooth.customView.PopWindowFragment;
import com.hc.mixthebluetooth.recyclerData.FragmentMessAdapter;
import com.hc.mixthebluetooth.recyclerData.itemHolder.FragmentMessageItem;
import com.hc.mixthebluetooth.storage.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentMessage extends BasFragment {

    @ViewByIds(value = {R.id.edit_message_fragment,R.id.loop_time_message_fragment},name = {"mDataET","mLoopET"})
    private EditText mDataET,mLoopET;

    @ViewById(R.id.recycler_message_fragment)
    private RecyclerView mRecyclerView;

    @ViewByIds(value = {R.id.pull_message_fragment,R.id.fold_switch_message_fragment},name = {"mPullBT","mFoldSwitch"})
    private ImageView mPullBT,mFoldSwitch;

    @ViewByIds(value = {R.id.size_read_message_fragment,R.id.size_send_message_fragment,R.id.size_unsent_message_fragment,R.id.send_message_fragment}
    ,name = {"mReadNumberTV","mSendNumberTv","mUnsentNumberTv","mSendBt"})
    private TextView mReadNumberTV,mSendNumberTv,mUnsentNumberTv,mSendBt;

    @ViewByIds(value = {R.id.read_hint_message_fragment,R.id.unsent_hint_message_fragment,R.id.fold_layout_message_fragment},name = {"mReadingHint","mUnsentHint","mFoldLayout"})
    private LinearLayout mReadingHint,mUnsentHint,mFoldLayout;

    @ViewById(R.id.loop_check_message_fragment)
    private CheckBoxSample mCheckLoopSend;

    @ViewById(R.id.tool_switch)
    private Switch switch1;
    @ViewById(R.id.lv_data)
    private ListView listView;

    private DefaultNavigationBar mTitle;//activity的头部

    private Runnable mRunnable;//循环发送的线程

    private Handler mHandler;

    private FragmentMessAdapter mAdapter;

    private List<FragmentMessageItem> mDataList = new ArrayList<>();

    private DeviceModule module;

    private Storage mStorage;

    private int mUnsentNumber = 0;

    private boolean isShowMyData;
    private boolean isShowTime;

    private boolean isSendHex;
    private boolean isReadHex;

    private int mFoldLayoutHeight = 0;
    private Context mContext;
    private ArrayList<DataBean> dataList = new ArrayList<>();
    private String mFilename = new File(Environment.getExternalStorageState() + "/json/data.txt").getName();

    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_message;
    }


    /*
     * 将时间戳转换为时间
     *
     * s就是时间戳
     */

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //如果它本来就是long类型的,则不用写这一步
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public void saveDatas(ArrayList<DataBean> dataList) throws JSONException,
            IOException {
        JSONArray array = new JSONArray();
        for (DataBean c : dataList)
            array.put(c.toJSON());

        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFilename,
                    Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            Log.d("Johnson",array.toString());
            writer.write(array.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public ArrayList<DataBean> loadDatas() throws IOException, JSONException {
        ArrayList<DataBean> datas = new ArrayList<DataBean>();
        BufferedReader reader = null;
        try {
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            Log.d("Johnson",jsonString.toString());
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String kaiguan = jsonObject.optString("kaiguan", null);
                String time = jsonObject.optString("time", "");
                datas.add(new DataBean(kaiguan,time));
            }
        } catch (FileNotFoundException e) {

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return datas;
    }

    @Override
    public void initAll() {
        try {
            dataList = loadDatas();
            DataAdapter adapter=new DataAdapter(mContext,R.layout.daka_item,dataList);
            listView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String msg = "0";
                if(b){
                    msg = "1";
                }
//                toast("A按钮被点击      "+msg);
                try {
                    saveDatas(dataList);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!mTitle.getParams().mRightText.equals("已连接")){
                    toast("当前状态不可以发送数据");
                    switch1.setChecked(!b);
                    return;
                }
                long timecurrentTimeMillis = System.currentTimeMillis();
                String time = stampToDate(timecurrentTimeMillis+"");
                String kaiguan = "";
                if (b){
                    kaiguan = "打开";

                }else{
                    kaiguan = "关闭";
                }
                dataList.add(new DataBean(kaiguan,time));
                DataAdapter adapter=new DataAdapter(mContext,R.layout.daka_item,dataList);
                listView.setAdapter(adapter);
                sendData(msg);
            }
        });
        initRecycler();
        initEditView();
        initFoldLayout();
    }

    @Override
    public void initAll(View view, Context context) {
        mStorage = new Storage(context);
        mContext = context;
        setListState();
        super.initAll(view, context);
    }

    @Override
    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void updateState(int state) {

        switch (state){
            case CommunicationActivity.FRAGMENT_STATE_1:
                mReadingHint.setVisibility(View.VISIBLE);
                break;
            case CommunicationActivity.FRAGMENT_STATE_2:
                mReadingHint.setVisibility(View.GONE);
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
                    mDataList.add(new FragmentMessageItem(Analysis.getByteToString(data,isReadHex), isShowTime?Analysis.getTime():null, false, module,isShowMyData));
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(mDataList.size());
                    mReadNumberTV.setText(String.valueOf(Integer.parseInt(mReadNumberTV.getText().toString())+Analysis.lengthArray(data)));
                }
                break;
            case CommunicationActivity.FRAGMENT_STATE_NUMBER:
                mSendNumberTv.setText(String.valueOf(Integer.parseInt(mSendNumberTv.getText().toString())+((int) o)));
                setUnsentNumberTv();
                break;
            case CommunicationActivity.FRAGMENT_STATE_SEND_SEND_TITLE:
                mTitle = (DefaultNavigationBar) o;
                break;
        }

    }

    @OnClick({R.id.send_message_fragment,R.id.clear_message_fragment,R.id.pull_message_fragment,R.id.fold_switch_message_fragment,
                R.id.loop_check_message_fragment,R.id.loop_text_message_fragment})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.send_message_fragment:
                setSendData();
                break;
            case R.id.clear_message_fragment:
                if (mSendBt.getText().toString().equals("发送"))
                    mDataET.setText("");
                else
                    toast("连续发送中，不能清除发送区的数据");
                break;
            case R.id.pull_message_fragment:
                mPullBT.setImageResource(R.drawable.pull_up);
                new PopWindowFragment(view, getActivity(), new PopWindowFragment.DismissListener() {
                    @Override
                    public void onDismissListener() {
                        mPullBT.setImageResource(R.drawable.pull_down);
                        setListState();
                    }

                    @Override
                    public void clearRecycler() {
                        mDataList.clear();
                        mReadNumberTV.setText(String.valueOf(0));
                        mSendNumberTv.setText(String.valueOf(0));
                        mUnsentNumber = 0;
                        mAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case R.id.fold_switch_message_fragment:
                setFoldLayout();
                break;
            case R.id.loop_text_message_fragment:
            case R.id.loop_check_message_fragment:
                if (Integer.parseInt(mLoopET.getText().toString()) <10){
                    toast("设置时间必须大于10，不然速度过快无法发送");
                    return;
                }
                if (mCheckLoopSend.isChecked() && mSendBt.getText().toString().equals("停止"))
                    setSendData();
                mCheckLoopSend.toggle();
                break;

        }
    }

    private void sendData(String msg){
        if (!mTitle.getParams().mRightText.equals("已连接")){
            toast("当前状态不可以发送数据");
            return;
        }
        sendData(new FragmentMessageItem(true, Analysis.getBytes(msg, false), null, true, module, false));
    }

    private void setSendData() {
        if (!mTitle.getParams().mRightText.equals("已连接")){
            toast("当前状态不可以发送数据");
            return;
        }
        if (mDataET.getText().toString().equals("")){
            toast("不能发送空数据");
            return;
        }
        if (!mCheckLoopSend.isChecked()) {
            sendData(new FragmentMessageItem(isSendHex, Analysis.getBytes(mDataET.getText().toString().replaceAll(" ", ""), isSendHex), isShowTime ? Analysis.getTime() : null, true, module, isShowMyData));
        }else {
            try {
                Integer.parseInt(mLoopET.getText().toString());
            }catch (Exception e){
                e.printStackTrace();
                toast("时间输入不规范");
                return;
            }
            if (mSendBt.getText().toString().equals("发送")){
                mSendBt.setText("停止");
                final int time = Integer.parseInt(mLoopET.getText().toString());
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        sendData(new FragmentMessageItem(isSendHex, Analysis.getBytes(mDataET.getText().toString().replaceAll(" ", ""), isSendHex), isShowTime ? Analysis.getTime() : null, true, module, isShowMyData));
                        mHandler.postDelayed(this,time);
                    }
                };
                mHandler.post(mRunnable);
            }else {
                mSendBt.setText("发送");
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    private void setFoldLayout() {
        if ((int)mFoldSwitch.getTag() == R.drawable.pull_down){
            mFoldSwitch.setImageResource(R.drawable.pull_up);
            mFoldSwitch.setTag(R.drawable.pull_up);
            Analysis.changeViewHeightAnimatorStart(mFoldLayout,mFoldLayoutHeight,0);
        }else{
            mFoldSwitch.setImageResource(R.drawable.pull_down);
            mFoldSwitch.setTag(R.drawable.pull_down);
            Analysis.changeViewHeightAnimatorStart(mFoldLayout,0,mFoldLayoutHeight);
        }
    }

    private void setListState() {
        isShowMyData = mStorage.getData(PopWindowFragment.KEY_DATA);
        isShowTime = mStorage.getData(PopWindowFragment.KEY_TIME);
        isSendHex = mStorage.getData(PopWindowFragment.KEY_HEX_SEND);
        isReadHex = mStorage.getData(PopWindowFragment.KEY_HEX_READ);
        if (isSendHex && mDataET.getHint().toString().trim().equals("任意字符")){
            mDataET.setHint("只可以输入16进制数据");
            mDataET.setText(Analysis.changeHexString(true,mDataET.getText().toString().trim()));
        }else if (!isSendHex && mDataET.getHint().toString().trim().equals("只可以输入16进制数据")){
            mDataET.setHint("任意字符");
            mDataET.setText(Analysis.changeHexString(false,mDataET.getText().toString().trim()));
        }
    }

    private void setUnsentNumberTv(){
        int number = Integer.parseInt(mSendNumberTv.getText().toString());
        if ((mUnsentNumber-number)>2000){
            if (mUnsentHint.getVisibility() == View.GONE)
                mUnsentHint.setVisibility(View.VISIBLE);
        }else if ((mUnsentNumber-number)<=0){
            if (mUnsentHint.getVisibility() == View.VISIBLE)
                mUnsentHint.setVisibility(View.GONE);
        }
        if (mUnsentHint.getVisibility() == View.VISIBLE)
            mUnsentNumberTv.setText(String.valueOf(mUnsentNumber-number));
    }

    private void sendData(FragmentMessageItem item) {
        if (mHandler == null)
            return;
        Message message = mHandler.obtainMessage();
        message.what = CommunicationActivity.DATA_TO_MODULE;
        message.obj = item;
        mHandler.sendMessage(message);
        if (isShowMyData) {
            mDataList.add(item);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.smoothScrollToPosition(mDataList.size());
        }

        //发送完计数
        int number = Analysis.changeHexString(true, mDataET.getText().toString().replaceAll(" ", "")).length()/3;
        if (isSendHex)
            number = number%2 == 0?number/2:(number+1)/2;
        mUnsentNumber += number;
    }

    private void initRecycler(){
        mAdapter = new FragmentMessAdapter(getContext(),mDataList,R.layout.item_message_fragment);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initEditView() {
        mDataET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                log("charSequence is "+charSequence+" start is "+start+" before is "+before+" count is "+count);
                if (isSendHex)
                    Analysis.setHex(charSequence.toString(),start,before,count,mDataET);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initFoldLayout() {
        mFoldSwitch.setTag(R.drawable.pull_down);
        mLoopET.setText(String.valueOf(500));
        mFoldLayout.post(new Runnable() {
            @Override
            public void run() {
                mFoldLayoutHeight = mFoldLayout.getHeight();
            }
        });
    }



}
