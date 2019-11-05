package com.engingplan.womarketing.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.engingplan.womarketing.bl.OkHttpTaskBL;

import java.util.HashMap;
import java.util.Map;

public class TaskDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdetails);

        //动态注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("TASK_DETAIL_ACTIVITY_RECEIVER");
        getApplicationContext().registerReceiver(myReceiver, intentFilter);


        //获取传来页面意图的参数
        Bundle bundle=this.getIntent().getExtras();
        final int taskid=bundle.getInt("taskid");


        //实例化bl对象，调用bl对象的getUserInfoAllAsyn方法。
        OkHttpTaskBL okHttpTaskBL = new OkHttpTaskBL();
        Map param = new HashMap<>();
        param.put("taskId",String.valueOf(taskid));
        okHttpTaskBL.taskdetailgetUserInfoAllAsyn(param, this.getApplicationContext());
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            HashMap map = (HashMap<String,String>)intent.getExtras().get("map");
            TextView taskName=findViewById(R.id.taskName);
            TextView taskType=findViewById(R.id.taskType);
            TextView taskStatus=findViewById(R.id.taskStatus);
            TextView productId=findViewById(R.id.productId);
            TextView productName=findViewById(R.id.productName);
            TextView updateTime=findViewById(R.id.updateTime);
            TextView voiceContent=findViewById(R.id.voiceContent);
            TextView smsContent=findViewById(R.id.smsContent);

            taskName.setText((String)map.get("taskName"));
            taskType.setText((String)map.get("taskType"));
            taskStatus.setText((String)map.get("taskStatus"));
            productId.setText((String)map.get("productId"));
            productName.setText((String)map.get("productName"));
            updateTime.setText((String)map.get("updateTime"));
            voiceContent.setText((String)map.get("voiceContent"));
            smsContent.setText((String)map.get("smsContent"));



        }
    };
}
