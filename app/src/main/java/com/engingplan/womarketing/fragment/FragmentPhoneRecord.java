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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.engingplan.womarketing.bl.TabLayoutBL;
import com.engingplan.womarketing.ui.activity.R;
import com.engingplan.womarketing.util.ConstantsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentPhoneRecord extends Fragment {
    private ListView listview1 = null;

    private ListView listview2 = null;

    LocalBroadcastManager broadcastManager;

    SwipeRefreshLayout swipeRefresh;

    public static FragmentPhoneRecord newInstance(String name) {
        FragmentPhoneRecord fragment = new FragmentPhoneRecord();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_tab_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabHost tab = view.findViewById(android.R.id.tabhost);

        //初始化TabHost容器
        tab.setup();
        //在TabHost创建标签，然后设置：标题／图标／标签页布局
        tab.addTab(tab.newTabSpec("tab1").setIndicator("全部通话" , null).setContent(R.id.tab1));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("意向通话" , null).setContent(R.id.tab2));

        listview1 = view.findViewById(R.id.listview1);//获取列表视图
        listview2 = view.findViewById(R.id.listview2);//获取列表视图

//        swipeRefresh = view.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent, R.color.white, R.color.white);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData();
//                swipeRefresh.setRefreshing(false);
//            }
//        });

        // 动态注册
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.ACTION_APP_INNER_BROADCAST);
        broadcastManager.registerReceiver(mReceiver, intentFilter);

        //监听listview
        //item的点击事件，里面可以设置跳转并传值
        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = view.getId();
                System.out.println("id="+id);
                if (view instanceof TextView){
                    System.out.println("点击文本");
                }
                else if (view instanceof ImageView){
                    System.out.println("点击图片");
                }
                else
                    System.out.println("都不是");

                Toast.makeText(getActivity(), "第" + i + "行", Toast.LENGTH_LONG).show();
            }
        });



        loadData();
    }


    /**
     * 调用后台数据
     */
    private void loadData(){
        //调逻辑层取后台数据
        TabLayoutBL okHttpDemoBL = new TabLayoutBL();
//        //传递参数
        Map param = new HashMap<>();
        param.put("resultCode", "2");
        okHttpDemoBL.getUserInfoAllAsyn(param, getContext());
    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            List<Map<String, String>> list = (List)intent.getExtras().get("list");
            System.out.println("size="+list.size());

            SimpleAdapter adapter = new SimpleAdapter(getContext(), list, R.layout.activity_tablayout_item,
                    new String[]{"recordId", "serialNumber", "taskId", "startTime"}, new int[]{R.id.userId, R.id.userName, R.id.userSex, R.id.userBirthday});
            listview1.setAdapter(adapter);
            listview2.setAdapter(adapter);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }




}
