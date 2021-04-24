package com.johnson.trafficvolume;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONObject;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RtChartsActivity extends AppCompatActivity {

    private Timer timer = new Timer();
    private GraphicalView chart;
    private TextView textview;
    private TimerTask task;
    private int addY = -1;
    private int total = 0;
    private int enter = 0;
    private int exit = 0;
    private int park = 0;
    private long addX;
    /**
     * 曲线数量
     */
    private static final int SERIES_NR = 1;
    private static final String TAG = "message";
    private TimeSeries series1;
    private XYMultipleSeriesDataset dataset1;
    private Handler handler;
    private Random random = new Random();
    /**
     * 时间数据
     */
    Date[] xcache = new Date[20];
    /**
     * 数据
     */
    int[] ycache = new int[20];

    private double yMax = 50;//y轴最大值，根据不同传感器变化
    private double yMin = 0;
    private XYMultipleSeriesRenderer renderer;
    private TextView totalTv;
    private TextView enterTv;
    private TextView exitTv;
    private TextView parkTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rt_charts);
        LinearLayout layout = (LinearLayout) findViewById(R.id.chart1);
        totalTv = (TextView) findViewById(R.id.total);
        enterTv = (TextView) findViewById(R.id.enter);
        exitTv = (TextView) findViewById(R.id.exit);
        parkTv = (TextView) findViewById(R.id.park_num);
        //生成图表
        chart = ChartFactory.getTimeChartView(this, getDateDemoDataset(), getDemoRenderer(), "hh:mm:ss");
        layout.addView(chart, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //刷新图表
                updateChart();
                totalTv.setText("景区车流量总数：" + total);
                enterTv.setText("进入景区车辆数：" + enter);
                exitTv.setText("离开景区车辆数：" + exit);
                parkTv.setText("景区剩余车位数：" + park);
                super.handleMessage(msg);
            }
        };
        task = new TimerTask() {
            @Override
            public void run() {
                getData();
            }
        };
        timer.schedule(task, 2 * 1000, 1000);
    }

    private void updateChart() {
        //设定长度为20
        int length = series1.getItemCount();
        if (length >= 20) length = 20;
//        addY=random.nextInt()%10;
        addX = new Date().getTime();

        //将前面的点放入缓存
        for (int i = 0; i < length; i++) {
            xcache[i] = new Date((long) series1.getX(i));
            ycache[i] = (int) series1.getY(i);
        }
        series1.clear();
        series1.add(new Date(addX), addY);
        for (int k = 0; k < length; k++) {
            series1.add(xcache[k], ycache[k]);
        }

        if (addY > yMax) {
            yMax = addY + 20;
        }
        if (addY < yMin) {
            yMin = addY;
        }
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        //在数据集中添加新的点集
        dataset1.removeSeries(series1);
        dataset1.addSeries(series1);
        //曲线更新
        chart.invalidate();
    }

    /**
     * 设定如表样式
     *
     * @return
     */
    private XYMultipleSeriesRenderer getDemoRenderer() {
        renderer = new XYMultipleSeriesRenderer();
        renderer.setChartTitle("今日车流量情况");//标题
        renderer.setChartTitleTextSize(40);
        renderer.setXTitle("时间");    //x轴说明
        renderer.setYTitle("车流量");//y轴的标题
        renderer.setAxisTitleTextSize(30);
        renderer.setAxesColor(Color.BLACK);
        renderer.setLabelsTextSize(25);    //数轴刻度字体大小
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLegendTextSize(20);    //曲线说明
        renderer.setXLabelsColor(Color.BLACK);
        renderer.setYLabelsColor(0, Color.BLACK);
        renderer.setShowLegend(false);
        renderer.setYLabels(10);//把y轴刻度平均分成多少个
        renderer.setPointSize((float) 4);
        renderer.setMargins(new int[]{100, 100, 100, 40});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.BLUE);
        r.setChartValuesTextSize(25);
        r.setChartValuesSpacing(3);
        r.setLineWidth(4);//这是线宽
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(true);
        r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
        renderer.setMarginsColor(Color.WHITE);
        renderer.setPanEnabled(false, false);
        renderer.setShowGrid(true);
        renderer.setYAxisMax(yMax);
        renderer.setYAxisMin(yMin);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setInScroll(true);  //调整大小
        return renderer;
    }

    /**
     * 数据对象
     *
     * @return
     */
    private XYMultipleSeriesDataset getDateDemoDataset() {
        dataset1 = new XYMultipleSeriesDataset();
        final int nr = 1;
        long value = new Date().getTime();
        Random r = new Random();
        for (int i = 0; i < SERIES_NR; i++) {
            series1 = new TimeSeries("Demo series " + (i + 1));
            for (int k = 0; k < nr; k++) {
                series1.add(new Date(value + k * 1000), 0);
            }
            dataset1.addSeries(series1);
        }
//        Log.i(TAG, dataset1.toString());
        return dataset1;
    }

    @Override
    public void onDestroy() {
        //当结束程序时关掉Timer
        timer.cancel();
        super.onDestroy();
    }

    ;

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://129.204.232.210:8538/").build();
        APIService apiService = retrofit.create(APIService.class);
        Call<String> dakaCall = apiService.getData();
        dakaCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String body = "";
                try {
                    String body1 = response.body();
                    body = body1;
                    JSONObject jsonObject = new JSONObject(body);
                    enter = jsonObject.optInt("enter", 0);
                    exit = jsonObject.optInt("exit", 0);
                    total = jsonObject.optInt("total", 0);
                    park = jsonObject.optInt("park", 0);
                    addY = total;
                    Message message = new Message();
                    message.what = 200;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(RtChartsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

}