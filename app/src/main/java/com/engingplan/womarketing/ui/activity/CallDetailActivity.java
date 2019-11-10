package com.engingplan.womarketing.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.engingplan.womarketing.bl.OkHttpDemoBL;
import com.engingplan.womarketing.ui.R;
import com.engingplan.womarketing.util.ConstantsUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallDetailActivity extends AppCompatActivity {
    TextView detailNun = null;
    TextView taskName = null;
    TextView detailStarTime = null;
    TextView detailEndTime = null;
    TextView detailResult = null;
    TextView remark = null;
    TextView detailRecall = null;

    String taskId = null;
    String serialNumber = null;
    String dataId = null;
    String staffId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailNun = findViewById(R.id.detail_nun);
        taskName = findViewById(R.id.detail_task);
        detailStarTime = findViewById(R.id.detail_startime);
        detailEndTime = findViewById(R.id.detail_endtime);
        detailResult = findViewById(R.id.detail_result);
        remark = findViewById(R.id.detail_remark);
        detailRecall = findViewById(R.id.detail_recall);

        Bundle bundle = this.getIntent().getExtras();
        String recordId = bundle.getString("recordId");
        String tabSource = bundle.getString("tabSource");

        Log.i(ConstantsUtil.LOG_TAG_ACTIVITY, "recordId=" + recordId);

        //1代表从全部通话页面跳转来，隐藏布局中的拨打控件
        if ("1".equals(tabSource))
            detailRecall.setVisibility(View.GONE);


        //跳转再次拨打界面，传递参数号码
        Button btn2 = findViewById(R.id.detail_recall);
        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent it = new Intent(CallDetailActivity.this, DialActivity.class);
                it.putExtra("number", ConstantsUtil.CALL_TIMES_SECOND);
                it.putExtra("taskId", Long.parseLong(taskId));
                it.putExtra("dataId", dataId);
                it.putExtra("staffId", Long.parseLong(staffId));
                startActivity(it);
            }

        });

        //注册广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.CALL_RECORD_INFO_RECEIVER);
        getApplicationContext().registerReceiver(mReceiver, intentFilter);


        OkHttpDemoBL okHttpDemoBL = new OkHttpDemoBL();
        //传递参数
        Map param = new HashMap<>();
        param.put("recordId", recordId);
        okHttpDemoBL.getUserInfoAllAsyn2(param, this.getApplicationContext());
    }

    private BroadcastReceiver mReceiver = new CallDetailActivity.DataReceiver();
    class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Map<String, String>> list = (List)intent.getExtras().get("list");
            Log.i(ConstantsUtil.LOG_TAG_ACTIVITY,"size="+list.size());

            detailNun.setText(list.get(0).get("serialNumber"));
            taskName.setText(list.get(0).get("taskName"));
            detailStarTime.setText(list.get(0).get("startTime"));
            detailEndTime.setText(list.get(0).get("endTime"));
            detailResult.setText(list.get(0).get("resultDesc"));
            remark.setText(list.get(0).get("remark"));

            //ui赋值
            serialNumber = list.get(0).get("serialNumber");
            taskId = list.get(0).get("taskId");
            dataId = list.get(0).get("dataId");
            staffId = list.get(0).get("staffId");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:   //返回键的id
                this.finish();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

