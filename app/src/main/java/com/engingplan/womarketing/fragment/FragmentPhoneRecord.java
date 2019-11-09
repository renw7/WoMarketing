package com.engingplan.womarketing.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.engingplan.womarketing.bl.OkHttpDemoBL;
import com.engingplan.womarketing.ui.activity.CallDetailActivity;
import com.engingplan.womarketing.ui.activity.R;
import com.engingplan.womarketing.util.ConstantsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentPhoneRecord extends Fragment {

    //全部通话listview
    private ListView listViewAll = null;

    //意向通话listview
    private ListView listViewIntent = null;

    private int staffId;

    LocalBroadcastManager broadcastManager = null;

    SwipeRefreshLayout swipeRefresh1 = null;

    SwipeRefreshLayout swipeRefresh2 = null;

    public static FragmentPhoneRecord newInstance(String name) {
        Log.i(ConstantsUtil.LOG_TAG_FRAGMENT, "实例化framgmet" + name);
        FragmentPhoneRecord fragment = new FragmentPhoneRecord();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tab_layout, container, false);
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabHost tab = view.findViewById(android.R.id.tabhost);

        //初始化TabHost容器
        tab.setup();
        //在TabHost创建标签，然后设置：标题／图标／标签页布局
        tab.addTab(tab.newTabSpec("tab1").setIndicator("全部通话", null).setContent(R.id.tab1));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("意向通话", null).setContent(R.id.tab2));

        listViewAll = view.findViewById(R.id.listview1);//获取列表视图
        listViewIntent = view.findViewById(R.id.listview2);//获取列表视图

        //选择号码，跳转到详情
        listViewIntent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Map<String, String> map = (Map) adapterView.getItemAtPosition(pos);
                Intent it = new Intent(getContext(), CallDetailActivity.class);
                it.putExtra("recordId", map.get("recordId"));
                startActivity(it);
            }
        });

        listViewAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                Map<String, String> map = (Map) adapterView.getItemAtPosition(pos);
//                Map<String,String> map2= new HashMap();
//                map2.put("createUser",map.get("createUser"));

                Intent it = new Intent(getContext(), CallDetailActivity.class);
                it.putExtra("recordId", map.get("recordId"));
                startActivity(it);
            }
        });

        //获取staffId，并给坤神
        Bundle bundle = getActivity().getIntent().getExtras();
        staffId = bundle.getInt("staffId");
        Log.i(ConstantsUtil.LOG_TAG_FRAGMENT, "staffId:" + staffId);

        // 动态注册
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.CALL_RECORD_RECEIVER);
        broadcastManager.registerReceiver(mReceiver, intentFilter);


        //实现页面下拉刷新 全部通话
        swipeRefresh1 = view.findViewById(R.id.swipeRefresh1);
        swipeRefresh1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeRefresh1.setRefreshing(false);
            }
        });

        //实现页面下拉刷新 意向通话
        swipeRefresh2 = view.findViewById(R.id.swipeRefresh2);
        swipeRefresh2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeRefresh2.setRefreshing(false);
            }
        });

        //onViewCreated初始化fragment时候调用后台加载listview数据
        loadData();
    }


    private void loadData(){
        //调逻辑层取后台数据
        OkHttpDemoBL okHttpDemoBL = new OkHttpDemoBL();

        //传递参数
        Map param = new HashMap<>();
        param.put("staffId", String.valueOf(staffId));
        okHttpDemoBL.getUserInfoAllAsyn(param, getContext());
    }



    private BroadcastReceiver mReceiver = new DataReceiver();
    class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Map<String, String>> list = (List) intent.getExtras().get("list");
            System.out.println("size=" + list.size());

            SimpleAdapter adapterAll = new SimpleAdapter(getContext(), list, R.layout.activity_record,
                    new String[]{"serialNumber", "startTime"}, new int[]{R.id.call_number, R.id.call_time});

            listViewAll.setAdapter(adapterAll);
            //判断
            List<Map<String, String>> list2 = new ArrayList();
            for (int i = 0; i < list.size(); i++) {
                if ((list.get(i).get("resultCode").equals("2"))) {
                    list2.add(list.get(i));
                }
            }
            SimpleAdapter adapterIntent = new SimpleAdapter(getContext(), list2, R.layout.activity_record,
                    new String[]{"serialNumber", "startTime"}, new int[]{R.id.call_number, R.id.call_time});
            listViewIntent.setAdapter(adapterIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }


}
