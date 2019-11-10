package com.engingplan.womarketing.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.engingplan.womarketing.bl.OkHttpTaskBL;
import com.engingplan.womarketing.util.ConstantsUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends AppCompatActivity {

    private ListView listView;
    private int taskType;
    private long staffId;
    private long taskId;
    private HashMap<String,String> map;
    private List<Map<String, String>> list;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);

        listView = findViewById(R.id.listView);
        map = new HashMap();

        //动态注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.TASK_LIST_ACTIVITY_RECEIVER);
        //报Activity has leaked IntentReceiver或者receiver is not registered错误，是找不到当前context对象，
        //在registerReceiver(myReceiver, intentFilter)前+getApplicationContext()，注意在这加下面注销的时候也要加，否则报当前receiver没有被注册。
        getApplicationContext().registerReceiver(myReceiver, intentFilter);

        // 获取传来页面的参数
        Bundle bundle = this.getIntent().getExtras();
        Log.i(ConstantsUtil.LOG_TAG_ACTIVITY, bundle.getString("taskType"));
        staffId = (long)bundle.getInt("staffId");
        taskType = Integer.valueOf(bundle.getString("taskType"));
        Log.i(ConstantsUtil.LOG_TAG_ACTIVITY, "taskType:" + taskType+" staffId:"+staffId);

        //调用逻辑层请求数据
        loadData();

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
                Log.i(ConstantsUtil.LOG_TAG_ACTIVITY,"The item setOnItemClickListener id" + i);
                Intent it = new Intent(TaskListActivity.this, DialActivity.class);
//                int weiShu = i + 1;
//                long lieBiaoTask_id = (long)Integer.valueOf(String.valueOf(taskType) + "0" + String.valueOf(weiShu));
                map = (HashMap<String, String>) list.get(i);
                taskId = Integer.valueOf(map.get(String.valueOf(i)));
                it.putExtra("taskId", taskId);
                it.putExtra("number", ConstantsUtil.CALL_TIMES_FIRST);
                it.putExtra("staffId", staffId);
                Log.i(ConstantsUtil.LOG_TAG_ACTIVITY,"taskId:" + taskId);
                startActivity(it);
            }
        });

        //实现页面下拉刷新
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeRefresh.setRefreshing(false);
            }
        });
    }


    private void loadData(){
        //实例化bl对象，调用bl对象的getUserInfoAllAsyn方法。
        OkHttpTaskBL okHttpTaskBL = new OkHttpTaskBL();
        Map param = new HashMap<>();
        param.put("taskType", String.valueOf(taskType));
        okHttpTaskBL.tasklistPostUserInfoAllAsyn(param, this.getApplicationContext());
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            list= (List<Map<String, String>>) intent.getExtras().get("list");
            //装适配器
            listView = findViewById(R.id.listView);
            listView.setAdapter(new SimpleAdapter(TaskListActivity.this, list,
                    R.layout.activity_tasklist_item, new String[]{"taskName", "taskStatus"}, new int[]{R.id.textViewTaskName, R.id.textViewTaskStatus}));

        }
    };


    public void imageViewonClick(View v) {
        Intent it = new Intent(TaskListActivity.this, TaskDetailsActivity.class);
//        int weishu = listView.getPositionForView(v) + 1;
//        int xiangqingtask_id = Integer.valueOf(String.valueOf(taskType) + "0" + String.valueOf(weishu));
        map = (HashMap<String, String>) list.get(listView.getPositionForView(v));
        taskId = Integer.valueOf(map.get(String.valueOf(listView.getPositionForView(v))));
        it.putExtra("taskId", taskId);
        Log.i(ConstantsUtil.LOG_TAG_ACTIVITY,"taskId:" + taskId);
        startActivity(it);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ConstantsUtil.LOG_TAG_ACTIVITY, "destroy");
        //在registerReceiver(myReceiver, intentFilter)前+getApplicationContext()，注意在这加下面注销的时候也要加，否则报当前receiver没有被注册。
        getApplicationContext().unregisterReceiver(myReceiver);     //注销广播接收器
    }
}
