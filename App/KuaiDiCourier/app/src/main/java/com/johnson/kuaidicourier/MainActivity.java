package com.johnson.kuaidicourier;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.johnson.kuaidicourier.bean.Express;
import com.johnson.kuaidicourier.service.APIService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView tvExpressNum;
    private TextView tvTotalFee;
    private RecyclerView recyclerView;

    private ArrayList<Express> expressList  = new ArrayList<Express>();
    private LinearLayoutManager layoutManager;
    private ExpressAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestAllExpress();
    }

    private void initView() {
        tvExpressNum = findViewById(R.id.tv_express_num);
        tvTotalFee = findViewById(R.id.tv_total_fee);
        recyclerView = findViewById(R.id.recycler_view);
        initRecyclerView();
    }

    @SuppressLint("WrongConstant")
    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
//        layoutManager.set
        //        mRecyclerView.setLayoutManager(mLayoutManager);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        mAdapter = new ExpressAdapter(expressList,this);
        mAdapter.setOnItemClickListener(new ExpressAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(View view, int position) {
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        });
        recyclerView.setAdapter(mAdapter);
        //设置分隔线
//        recyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void requestAllExpress() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8547/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> allCall = apiService.getAllExpress();
        allCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    String body = response.body();
                    JSONArray expresses = new JSONArray(body);
                    for (int i = 0; i < expresses.length() ; i++) {
                        JSONObject express = expresses.getJSONObject(i);
                        Gson gson  = new Gson();
                        Express expressInfo = gson.fromJson(express.toString(), Express.class);
                        expressList.add(expressInfo);
                    }
                    mAdapter.notifyDataSetChanged();
                    updateInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    private void updateInfo() {
        if(expressList.size() > 0){
            double totalFee = 0;
            for (Express express : expressList) {
                totalFee = totalFee + express.getFee();
            }
            tvExpressNum.setText("包裹数量："+ expressList.size()+"个");
            tvTotalFee.setText("金额总数："+ totalFee+"元");
        }else{
            tvExpressNum.setText("包裹数量："+ 0+"个");
            tvTotalFee.setText("金额总数："+ 0+"元");
        }
    }
}