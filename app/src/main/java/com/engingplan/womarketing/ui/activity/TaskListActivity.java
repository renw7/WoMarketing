package com.engingplan.womarketing.ui.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.engingplan.womarketing.bl.OkHttpTaskBL;
import com.engingplan.womarketing.ui.R;
import com.engingplan.womarketing.util.ConstantsUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskListActivity extends Activity {

    private ListView listView;
    private int taskType;
    private long staffId;
    private long taskId;
    private HashMap<String,String> map;
    private List<Map<String, String>> list;
    private SwipeRefreshLayout swipeRefresh;
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasklist);


        //2、实例化手势管理的对象,用于管理屏幕监听事件onTouchEvent(event)传递过来的手势动作
         gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
             @Override
             public boolean onDown(MotionEvent e) {
                 System.out.println("onDown");

                 return false;
             }

             @Override
             public void onShowPress(MotionEvent e) {
                 System.out.println("onShowPress");
             }

             @Override
             public boolean onSingleTapUp(MotionEvent e) {
                 System.out.println("onSingleTapUp");
                 return false;
             }

             @Override
             public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
                 System.out.println("onScroll");
                 return false;
             }

             @Override
             public void onLongPress(MotionEvent e) {
                 System.out.println("onLongPress");

             }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                //监听手势的移动 到底是从左往右还是从右往左
                //e1:手指按下的移动事件.

                float e1X = e1.getRawX();
                float e1Y = e1.getRawY();
                // e2 : 手指移动的动作事件.
                float e2X = e2.getRawX();
                float e2Y = e2.getRawY();

                System.out.println("e1X:" + e1X + " e1Y:"+e1Y);
                System.out.println("e2X:" + e2X + " e2Y:"+e2Y);
                // 跳转判断方式一:手指滑动的X轴方向的如果小于50，无效果，单位是px
                if (Math.abs(e2X - e1X) < 50) {
                    Toast.makeText(getApplicationContext(),
                            "左右滑动小于50px", Toast.LENGTH_SHORT).show();
                    return false;
                    // 跳转判断方式二:比较e2,e1得到的Y值，获取两数绝对值判断是否上下滑动
                } else if (Math.abs(e2Y - e1Y) > 200) {
                    Toast.makeText(getApplicationContext(), "手势上下滑动", Toast.LENGTH_SHORT)
                            .show();
                    return false;
                    // 比较e2,e1得到的Y值，获取两数绝对值判断是否左右滑动
                } else if (Math.abs(e2X - e1X) > 50) {
                    // 判断是否左滑
                    if ((e2X - e1X) > 0) {
                        Toast.makeText(getApplicationContext(), "右滑", Toast.LENGTH_SHORT)
                                .show();

                        // 判断是否右滑
                    } else {
                        Toast.makeText(getApplicationContext(), "左滑", Toast.LENGTH_SHORT)
                                .show();

                    }
                    return true;
                }

                //跳转判断方式三: 直接进行左右比值判断 稍微一有滑动就发生跳转
                if(e1.getX()-e2.getX()>0){

                    //子类实现跳转的方法
//                    showNextPage();
                }
                if(e1.getX()-e2.getX()<0){

                    //子类实现跳转的方法
//                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });


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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //3、通过手势处理类,接收多种类型的事件,用作处理
        System.out.println("onTouchEvent");
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
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


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        x1 = event.getX();
//        y1 = event.getY();
//        System.out.println("x1="+x1+" y1="+y1);
//        //继承了Activity的onTouchEvent方法，直接监听点击事件
//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            //当手指按下的时候
//            x1 = event.getX();
//            y1 = event.getY();
//        }
//        if(event.getAction() == MotionEvent.ACTION_UP) {
//            //当手指离开的时候
//            x2 = event.getX();
//            y2 = event.getY();
//            System.out.println("x1="+x1+" y1="+y1);
//            System.out.println("x2="+x2+" y2="+y2);
//
//            if(y1 - y2 > 50) {
//                Toast.makeText(this, "向上滑", Toast.LENGTH_SHORT).show();
//            } else if(y2 - y1 > 50) {
//                Toast.makeText(this, "向下滑", Toast.LENGTH_SHORT).show();
//            } else if(x1 - x2 > 50) {
//                Toast.makeText(this, "向左滑", Toast.LENGTH_SHORT).show();
//            } else if(x2 - x1 > 50) {
//                Toast.makeText(this, "向右滑", Toast.LENGTH_SHORT).show();
//            }
//        }
//        return super.onTouchEvent(event);
//    }
}
