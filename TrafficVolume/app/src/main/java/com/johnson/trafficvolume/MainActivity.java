package com.johnson.trafficvolume;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lidroid.xutils.exception.DbException;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import com.lidroid.xutils.DbUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    //图表相关
    private TimeSeries series;
    private XYMultipleSeriesDataset mDataset;
    private GraphicalView chart;
    private XYMultipleSeriesRenderer renderer;
    private Context context;
    private double yMax = 20;//y轴最大值，根据不同传感器变化
    private double xMax = 100;//一屏显示测量次数
    private double yMin = 0;
    private double xMin = 0;

    public  int index = 0;//指示这段时间一共写入了多少个数据
    //在这里可以设置缓冲区的长度，用于求平均数
    double[] buffer = new double[500];//半秒钟最多放500个数
    public int INTERVAL = 500;//每半秒求一次平均值
    public double AVERAGE = 0;//存储平均值

    Handler avgHandler;
    Thread avgThread;
    DbUtils db ;
    int sensor_id = 0;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        avgHandler = new AveHandler();
        db = DbUtils.create(getApplicationContext());
        Intent intent = getIntent();

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date thisDay = new Date();
        calendar = new GregorianCalendar();
        try {
            calendar.setTime(format.parse(format.format(thisDay)));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int wtd = intent.getIntExtra("wtd", Sensor.TYPE_ACCELEROMETER);
        sensor_id = wtd;
//        avgThread = new Thread(runnable);//定期更新平均值的线程启动
//        avgThread.start();
        //初始化图表
//        xMin = new Date().getTime() - TimeChart.DAY * 1 / (24*3);
//        xMax = new Date().getTime() + TimeChart.DAY * 1 / (24*3);
//        xMin = System.currentTimeMillis()/1000;
//        xMax = System.currentTimeMillis()/1000 + 3600;
        Log.d("Johnson",xMin + "  xMax = "+xMax);

        initChart("时间", "车流量",0,xMax,0,yMax);

    }

    private void fillData() {
        long value = new Date().getTime() - 3 * TimeChart.DAY;
        for (int i = 0; i < 100; i++) {
            series.add(new Date(value + i * TimeChart.DAY / 4), i);
        }
    }

    /**
     * 初始化图表
     */
    private void initChart(String xTitle,String yTitle,double minX,double maxX,double minY,double maxY){
        //这里获得main界面上的布局，下面会把图表画在这个布局里面
        LinearLayout layout = (LinearLayout)findViewById(R.id.chart);
        //这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线
        series = new TimeSeries("历史曲线");
        //创建一个数据集的实例，这个数据集将被用来创建图表
        mDataset = new XYMultipleSeriesDataset();
        //将点集添加到这个数据集中
        mDataset.addSeries(series);

        //以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
        int lineColor = Color.RED;
        PointStyle style = PointStyle.CIRCLE;
        renderer = buildRenderer(lineColor, style, true);

        //设置好图表的样式
        setChartSettings(renderer, xTitle,yTitle,
                minX, maxX, //x轴最小最大值
                minY, maxY, //y轴最小最大值
                Color.BLACK, //坐标轴颜色
                Color.BLACK//标签颜色
        );

        //生成图表
        chart = ChartFactory.getTimeChartView(this, mDataset, renderer,"HH:MM");
fillData();
        //将图表添加到布局中去
        layout.addView(chart, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        //设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(color);
        r.setPointStyle(style);
        r.setFillPoints(fill);
        r.setLineWidth(4);//这是线宽
        renderer.addSeriesRenderer(r);

        return renderer;
    }

    /**
     * 初始化图表
     * @param renderer
     * @param xTitle
     * @param yTitle
     * @param xMin
     * @param xMax
     * @param yMin
     * @param yMax
     * @param axesColor
     * @param labelsColor
     */
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle,
                                    double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor) {
        //有关对图表的渲染可参看api文档
        renderer.setChartTitle("000000000000");//设置标题
        renderer.setChartTitleTextSize(40);
//        renderer.setXAxisMin(xMin);//设置x轴的起始点
//        renderer.setXAxisMax(xMin+20);//设置一屏有多少个点
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setBackgroundColor(Color.BLACK);
        renderer.setLabelsColor(Color.YELLOW);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setShowGrid(true);
        renderer.setGridColor(Color.BLUE);//设置格子的颜色
        renderer.setXLabels(1000);//没有什么卵用
        renderer.setYLabels(20);//把y轴刻度平均分成多少个
        renderer.setLabelsTextSize(25);
        renderer.setXTitle(xTitle);//x轴的标题
        renderer.setYTitle(yTitle);//y轴的标题
        renderer.setAxisTitleTextSize(30);
        renderer.setYLabelsAlign(Paint.Align.RIGHT);
        renderer.setPointSize((float) 4);
        renderer.setShowLegend(false);//说明文字
        renderer.setLegendTextSize(20);
        renderer.setMargins(new int[] { 50, 45, 10, 0 });// 设置图表的外边框(上/左/下/右)
        renderer.setMarginsColor(Color.WHITE);

    }

    /**
     * 一个子线程，没隔固定时间计算这段时间的平均值，并给textView赋值
     */
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            System.out.println("线程已经启动");
            while(true){
                try {
                    Thread.sleep(INTERVAL);//没隔固定时间求平均数
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    avgThread = new Thread(runnable);
                    avgThread.start();
                }
                if(index!=0){
                    double sum = 0;
                    for (int i=0;i<index;i++) {
                        sum+=buffer[i];
                        //高精度加法
//				sum = MathTools.add(sum, d);
                    }
                    AVERAGE = sum/new Double(index);
                    index=0;//让下标恢复
                }
                avgHandler.sendEmptyMessage(1);
                //高精度除法，还能四舍五入
//			AVERAGE = MathTools.div(sum, buffer.length, 4);
            }
        }
    };

    /**
     * 每隔固定时间给平均值赋值，并且更新图表的操作
     * @author love fang
     *
     */
    class AveHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            setAverageView();//显示平均值
            updateChart();//更新图表，非常重要的方法
            //把当前值存入数据库

            Accelerate_info accelerate_info = new Accelerate_info(System.currentTimeMillis(), AVERAGE, sensor_id);
            try {
                db.save(accelerate_info);//将当前平均值存入数据库
            } catch (DbException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("保存失败");
            }
        }
    }


    /**
     * 更新平均值的显示值
     */
    public void setAverageView(){
//        if(sumText==null)return;
//        sumText.setText(AVERAGE+"");
    }

    /**
     * 接受当前传感器的测量值，存到缓存区中去，并将下标加一
     * @param data
     */
    public void giveAverage(double data){
        buffer[index]=data;
        index++;
    }

    private int addX = -1;
    private double addY = 0;
    /**
     * 更新图表的函数，其实就是重绘
     */
    private void updateChart() {

        //设置好下一个需要增加的节点

        addY = AVERAGE;//需要增加的值
        addY = Math.random();//需要增加的值
        //移除数据集中旧的点集
        mDataset.removeSeries(series);

        //判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
        int length = series.getItemCount();
        if (length > 5000) {//设置最多5000个数
            length = 5000;
        }

        //注释掉的文字为window资源管理器效果

        //将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
//	     for (int i = 0; i < length; i++) {
//		     xv[i] = (int) series.getX(i) + 1;
//		     yv[i] = (int) series.getY(i);
//	     }

        //点集先清空，为了做成新的点集而准备
//	     series.clear();

        //将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
        //这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
        //每一个新点坐标都后移一位
        Date date = calendar.getTime();
        Log.d("Johnson",System.currentTimeMillis()+"");
        long value = new Date().getTime() - 3 * TimeChart.DAY;
        series.add(new Date(value* TimeChart.DAY / 4), addY);//最重要的一句话，以xy对的方式往里放值
//	     for (int k = 0; k < length; k++) {
//	         series.add(xv[k], yv[k]);//把之前的数据放进去
//	     }
        if(addX>xMax){
            renderer.setXAxisMin(addX-xMax);
            renderer.setXAxisMax(addX);
        }


        //重要：在数据集中添加新的点集
        mDataset.addSeries(series);

        //视图更新，没有这一步，曲线不会呈现动态
        //如果在非UI主线程中，需要调用postInvalidate()，具体参考api
        chart.invalidate();
    }
}