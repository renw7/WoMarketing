package com.engingplan.womarketing.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.engingplan.womarketing.bl.OkHttpDemoBL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallDetailActivity extends Activity {
    TextView detail_nun=null;
    TextView task_name = null;
    TextView detail_startime = null;
    TextView detail_endtime = null;
    TextView detail_result = null;
    TextView remark =null;
    String  taskId=null;
    String  serialNumber=null;
    String  dataId=null;
    String  staffId=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_detail);
       detail_nun = findViewById(R.id.detail_nun);
       task_name = findViewById(R.id.detail_task);
       detail_startime = findViewById(R.id.detail_startime);
       detail_endtime = findViewById(R.id.detail_endtime);
       detail_result = findViewById(R.id.detail_result);
       remark = findViewById(R.id.detail_remark);

        Bundle bundle = this.getIntent().getExtras();
        String recordId=bundle.getString("recordId");
        //        返回键
        Button btn1 = findViewById(R.id.detail_return);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //跳转再次拨打界面，传递参数号码
        Button btn2 = findViewById(R.id.detail_recall);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(CallDetailActivity.this, DialActivity.class);

                it.putExtra("serialNumber",serialNumber);
                it.putExtra("taskId",Long.parseLong(taskId));
                it.putExtra("taskId",Long.parseLong(dataId));
                it.putExtra("taskId",Long.parseLong(staffId));
                startActivity(it);
            }

        });

        //接收
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("det");
        getApplicationContext().registerReceiver(mReceiver, intentFilter);

        OkHttpDemoBL okHttpDemoBL = new OkHttpDemoBL();
        //传递参数
        Map param = new HashMap<>();
//        param.put("username", "张三");
        String url="http://119.29.106.248/tblcallrecord/info?recordId="+recordId;
        okHttpDemoBL.getUserInfoAllAsyn2(param, url,this.getApplicationContext());
        }

    private BroadcastReceiver mReceiver = new CallDetailActivity.DataReceiver();
    class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Map<String, String>> list = (List)intent.getExtras().get("list");
            System.out.println("size="+list.size());
            detail_nun.setText(list.get(0).get("serialNumber"));
            task_name.setText(list.get(0).get("taskName"));
            detail_startime.setText(list.get(0).get("startTime"));
            detail_endtime.setText(list.get(0).get("endTime"));
            detail_result.setText(list.get(0).get("resultDesc"));
            remark.setText(list.get(0).get("remark"));
            serialNumber=list.get(0).get("serialNumber");
            taskId=list.get(0).get("taskId");
            dataId=list.get(0).get("dataId");
            dataId=list.get(0).get("staffId");
            System.out.println(dataId);
        }
    }
    }

