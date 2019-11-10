package com.engingplan.womarketing.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.engingplan.womarketing.bl.IndexBL;
import com.engingplan.womarketing.ui.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;


public class FragmentIndex extends Fragment {


    private String ACTION_APP_BROADCAST = "com.engingplan.womarketing.fragment";
    LocalBroadcastManager broadcastManager;
    private String newStaffName;
    private int todayFinishNum;
    private int todayIntentNum;
    private int todayIncompNum;
    private int staffId;
    BarChart barChart = null;
    BarChart barChart1 = null;

    public static FragmentIndex newInstance(String name) {
        FragmentIndex fragment = new FragmentIndex();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragmentindex, container, false);
        //这里实例化广播管理者
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //获取登录页面传来的姓名
        Intent intentf=getActivity().getIntent();
        Bundle bundle=intentf.getExtras();
        String staffName = bundle.getString("staffName");
        staffId = bundle.getInt("staffId");
        TextView textview =view.findViewById(R.id.textView3);
        this.newStaffName="欢迎您！"+staffName;
        textview.setText(this.newStaffName);


        //这里注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_APP_BROADCAST);
        broadcastManager.registerReceiver(mReceiver, intentFilter);
        //实例化IndexBL调取方法
        IndexBL indexBL = new IndexBL(staffId);
        indexBL.setDataFirst(broadcastManager);
        barChart = view.findViewById(R.id.ChartTest);//定义界面控件
        barChart1 = view.findViewById(R.id.ChartTest1);//定义界面控件
        barChart = initBarChart(barChart);//调用方法初始化柱状图
        barChart1 = initBarChart(barChart1);//调用方法初始化柱状图


    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver mReceiver = new MyReceiver2();

    class MyReceiver2 extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            todayFinishNum = intent.getIntExtra("finishNum", 0);
            todayIntentNum = intent.getIntExtra("intentNum",0);
            todayIncompNum = intent.getIntExtra("incompNum", 0);
            BarData barData = setbarData();//调用方法初始化数据
            BarData barData1 = setbarData();//调用方法初始化数据
            barChart.setData(barData);//将数据用到柱状图上显示
            barChart1.setData(barData1);//将数据用到柱状图上显示
            barChart.invalidate();//在柱状图填充数据以后进行刷新
            barChart1.invalidate();//在柱状图填充数据以后进行刷新
            Log.i("MyReceiver", "收到..." + todayFinishNum);
        }
    }



    public BarData setbarData() {

        List<BarEntry> entries = new ArrayList<>();//定义一个entries的集合用来存放数据

            entries.add(new BarEntry(1,todayFinishNum));
            entries.add(new BarEntry(2,todayIncompNum));
            entries.add(new BarEntry(3,todayIntentNum));

        BarDataSet barDataSet = new BarDataSet(entries, null);//设置数据集
        BarData barData = new BarData(barDataSet);//把数据集放到barData对象里
        barData.setBarWidth(0.7f);//每个柱子的宽度
        barDataSet.setColors(new int[]{Color.rgb(255, 117, 6),
                Color.rgb(255, 149, 63),
                Color.rgb(255, 179, 18),});

        return barData;


    }

    //这个方法用来初始化柱状图
    public BarChart initBarChart(BarChart barChart) { // 传入barchart实例

        barChart.setDescription(null);//设置柱状图的描述
        barChart.setDrawBarShadow(false);//设置每个柱子的阴影不显示
        barChart.setDrawValueAboveBar(true);//设置每个柱子的数值显示
        barChart.animateY(2500);//数据显示动画，从左往右依次显示
        //barChart.getXAxis().setDrawLabels(false);//设置不显示横坐标
        barChart.getAxisRight().setDrawLabels(false);//设置不显示右侧纵坐标
        barChart.getAxisLeft().setDrawLabels(false);//设置不显示左侧纵坐标

        XAxis xAxis = barChart.getXAxis();//获取柱状图X轴
        YAxis yAxisLeft = barChart.getAxisLeft();//获取柱状图左侧Y轴
        YAxis yAxisRight = barChart.getAxisRight();//获取柱状图右侧Y轴

        setAxis(xAxis, yAxisLeft);//调用方法设置柱状图的轴线

        return barChart;//这里返回初始化完成的柱状图实例
    }

    //这个方法用来设置柱状图的X,Y轴
    public void setAxis(XAxis xAxis, YAxis leftYaxis) {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴在柱状图底部显示
        xAxis.setAxisLineWidth(1);//设置X轴宽度
        xAxis.setAxisMinimum(0);//设置X轴从0开始
        xAxis.setDrawAxisLine(true);//设置X轴显示轴线
        xAxis.setDrawGridLines(false);//设置X轴的表格线不显示
        xAxis.setEnabled(true);//设置X轴显示
        xAxis.setSpaceMax(3);//设置柱间间距

        final List<String> xValue = new ArrayList<>();

        xValue.add("");//index = 0 的位置的数据在IndexAxisValueFormatter中时不显示的。
        xValue.add("已完成任务");
        xValue.add("未完成任务");
        xValue.add("有意向客户");
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValue));//设置x轴标签格式化器


        leftYaxis.setAxisMinimum(0);//设置左侧Y轴从0开始
        leftYaxis.setDrawGridLines(false);//设置左侧Y轴的表格线不显示
        leftYaxis.setDrawAxisLine(true);//设置左侧Y轴显示轴线
        leftYaxis.setAxisLineWidth(1);//设置左侧Y轴宽度
        leftYaxis.setEnabled(true);//设置左侧Y轴显示

//        rightYaxis.setAxisMinimum(0);//设置右侧Y轴从0开始
//        rightYaxis.setDrawGridLines(false);//设置右侧Y轴的表格线不显示
//        rightYaxis.setDrawAxisLine(true);//设置右侧Y轴显示轴线
//        rightYaxis.setAxisLineWidth(1);//设置右侧Y轴宽度
//        rightYaxis.setEnabled(true);//设置右侧Y轴显示


    }
}

