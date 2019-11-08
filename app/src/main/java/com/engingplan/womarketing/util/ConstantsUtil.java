package com.engingplan.womarketing.util;

public interface ConstantsUtil {
    /*==============广播接收器命名===========*/
    String ACTION_APP_INNER_BROADCAST = "com.chinaunicom.marketing.ui.activity.DataReceiver";

    String TASK_LIST_ACTIVITY_RECEIVER = "com.engingplan.womarketing.ui.activity.TaskListActivity.BroadcastReceiver";

    String TASK_DETAIL_ACTIVITY_RECEIVER = "com.engingplan.womarketing.ui.activity.TaskDetailsActivity.BroadcastReceiver";


    //==============任务类型================*/
    //4G
    String TASK_TYPE_4G = "1";

    //流量
    String TASK_TYPE_FLOW = "2";

    //宽带
    String TASK_TYPE_BROAD = "3";

    //融合
    String TASK_TYPE_MIX = "4";


    /*==============日志tag================*/
    //fragment日志
    String LOG_TAG_FRAGMENT = "market.fragment";

    //activity日志
    String LOG_TAG_ACTIVITY = "market.activity";

    String LOG_TAG_BL = "market.bl";


    /*==============拨打次数================*/
    //首次拨打
    String CALL_TIMES_FIRST = "1";

    //二次回拨
    String CALL_TIMES_SECOND = "2";


    /*==============服务地址url================*/
    //根据任务类型获取任务明细
    String URL_TASK_LIST = "http://119.29.106.248/tbltaskinfo/postInfoPage1";

    String URL_TASK_DETAIL = "http://119.29.106.248/tbltaskinfo/postInfoPage2";

}
