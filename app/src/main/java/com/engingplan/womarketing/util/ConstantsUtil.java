package com.engingplan.womarketing.util;

public interface ConstantsUtil {
    /*==============广播接收器命名===========*/
    String ACTION_APP_INNER_BROADCAST = "com.chinaunicom.marketing.ui.activity.DataReceiver";

    String TASK_LIST_ACTIVITY_RECEIVER = "com.engingplan.womarketing.ui.activity.TaskListActivity.BroadcastReceiver";

    String TASK_DETAIL_ACTIVITY_RECEIVER = "com.engingplan.womarketing.ui.activity.TaskDetailsActivity.BroadcastReceiver";

    String PERSON_INFO_RECEIVER = "com.engingplan.womarketing.fragment.FragmentMe.BroadcastReceiver";

    String CALL_RECORD_RECEIVER = "com.engingplan.womarketing.fragment.FragmentPhoneRecord.BroadcastReceiver";

    String CALL_RECORD_INFO_RECEIVER = "com.engingplan.womarketing.fragment.CallDetailActivity.BroadcastReceiver";

    String LOGIN_ACTIVITY_RECEIVER = "com.engingplan.womarketing.fragment.LoginActivity.BroadcastReceiver";


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

    //BI层日志
    String LOG_TAG_BL = "market.bl";

    //下载工具类日志
    String LOG_TAG_DOWN_UTIL = "market.uitl.download";

    /*==============拨打次数================*/
    //首次拨打
    String CALL_TIMES_FIRST = "1";

    //二次回拨
    String CALL_TIMES_SECOND = "2";

    /*==============下载状态================*/
    //开始
    int flag_start = 0;

    //正在下载
    int flag_doing = 1;

    //下载完成
    int flag_finish = 2;


    /*==============服务地址url================*/
    //腾讯云服务器ip
    String URL_HOME = "http://119.29.106.248";
//    String URL_HOME = "http://10.52.200.150";

    //根据任务类型获取任务明细
    String URL_TASK_LIST = URL_HOME + "/tbltaskinfo/postInfoPage1";

    //根据任务id查询任务详情
    String URL_TASK_DETAIL = URL_HOME + "/tbltaskinfo/postInfoPage2";

    //修改密码
    String URL_UPDATE_PWD = URL_HOME + "/tblstaffinfo/updatePwd";

    //通话信息
    String URL_CALL_REROD_LIST = URL_HOME + "tblcallrecord/page";

    //通话详情
    String URL_CALL_REROD_INFO = URL_HOME + "/tblcallrecord/info";

    //考核下载
    String URL_WORK_REPORT = URL_HOME + "/tblworkreport/page";

    //登录验证
    String URL_CHECK_USER = URL_HOME + "/tblstaffinfo/checkuser";


}
