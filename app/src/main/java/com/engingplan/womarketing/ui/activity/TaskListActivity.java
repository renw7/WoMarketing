package com.engingplan.womarketing.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.Nullable;


import com.engingplan.womarketing.bl.OkHttpTaskBL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends Activity {


    private ListView listView;
    public int taskType;
    public long staffId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);
        listView = findViewById(R.id.listView);

        //动态注册广播接收器
        System.out.println("registerReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("TASK_LIST_ACTIVITY_RECEIVER");
        //报Activity has leaked IntentReceiver或者receiver is not registered错误，是找不到当前context对象，
        //在registerReceiver(myReceiver, intentFilter)前+getApplicationContext()，注意在这加下面注销的时候也要加，否则报当前receiver没有被注册。
        getApplicationContext().registerReceiver(myReceiver, intentFilter);

        // 获取传来页面的参数
        Bundle bundle = this.getIntent().getExtras();
        System.out.println(bundle.getString("taskType"));
        staffId=(long)bundle.getInt("staffId");
        taskType = Integer.valueOf(bundle.getString("taskType"));
        System.out.println("taskType:" + taskType+"staffId"+staffId);

        //实例化bl对象，调用bl对象的getUserInfoAllAsyn方法。
        OkHttpTaskBL okHttpTaskBL = new OkHttpTaskBL();
        Map param = new HashMap<>();
        param.put("taskType", String.valueOf(taskType));
        okHttpTaskBL.tasklistPostUserInfoAllAsyn(param, this.getApplicationContext());

        //返回对应上级页面
        ImageButton back = findViewById(R.id.ImageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //跳坤神的页面，并传递taskId,staffId
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("The item setOnItemClickListener id" + i);
                Intent it = new Intent(TaskListActivity.this, DialActivity.class);
                int weiShu = i + 1;
                long lieBiaoTask_id = (long)Integer.valueOf(String.valueOf(taskType) + "0" + String.valueOf(weiShu));
                it.putExtra("taskId", lieBiaoTask_id);
                it.putExtra("number", "1");
                it.putExtra("staffId", staffId);
                System.out.println("taskId:" + lieBiaoTask_id);
                startActivity(it);
            }
        });
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            List<Map<String, String>> list = (List<Map<String, String>>) intent.getExtras().get("list");
            listView = findViewById(R.id.listView);
            listView.setAdapter(new SimpleAdapter(TaskListActivity.this, list,
                    R.layout.activity_tasklist_item, new String[]{"taskName","taskStatus"}, new int[]{R.id.textViewTaskName,R.id.textViewTaskStatus}));

        }
    };


    public void imageViewonClick(View v) {

        Intent it = new Intent(TaskListActivity.this, TaskDetailsActivity.class);
        int weishu = listView.getPositionForView(v) + 1;
        int xiangqingtask_id = Integer.valueOf(String.valueOf(taskType) + "0" + String.valueOf(weishu));
        it.putExtra("taskId", xiangqingtask_id);
        System.out.println("taskId:" + xiangqingtask_id);
        startActivity(it);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("destroy");
        //在registerReceiver(myReceiver, intentFilter)前+getApplicationContext()，注意在这加下面注销的时候也要加，否则报当前receiver没有被注册。
        getApplicationContext().unregisterReceiver(myReceiver);     //注销广播接收器
    }
}
