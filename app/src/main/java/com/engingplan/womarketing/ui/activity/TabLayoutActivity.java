package com.engingplan.womarketing.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import com.engingplan.womarketing.bl.TabLayoutBL;
import com.engingplan.womarketing.ui.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabLayoutActivity extends Activity {

    ListView listview1 = null;

    ListView listview2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tab_layout);

        TabHost tab = findViewById(android.R.id.tabhost);


        //初始化TabHost容器
        tab.setup();
        //在TabHost创建标签，然后设置：标题／图标／标签页布局
        tab.addTab(tab.newTabSpec("tab1").setIndicator("全部通话" , null).setContent(R.id.tab1));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("意向通话" , null).setContent(R.id.tab2));


        listview1 = findViewById(R.id.listview1);//获取列表视图
        listview2 = findViewById(R.id.listview2);//获取列表视图


        // 动态注册
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("abc");
        registerReceiver(mReceiver, intentFilter);


        //调逻辑层取后台数据
        TabLayoutBL okHttpDemoBL = new TabLayoutBL();
        //传递参数
        Map param = new HashMap<>();
        param.put("taskId", "101");
//        okHttpDemoBL.getUserInfoConditionAsyn(param, this.getApplicationContext());
    }


    private BroadcastReceiver mReceiver = new DataReceiver();
    class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Map<String, String>> list = (List)intent.getExtras().get("list");
            System.out.println("size="+list.size());

            SimpleAdapter adapter = new SimpleAdapter(TabLayoutActivity.this, list, R.layout.activity_tablayout_item,
                    new String[]{"staffId", "staffName", "staffNo", "staffPwd"}, new int[]{R.id.userId, R.id.userName, R.id.userSex, R.id.userBirthday});
            listview1.setAdapter(adapter);
            listview2.setAdapter(adapter);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);     //注销广播接收器
    }
}
