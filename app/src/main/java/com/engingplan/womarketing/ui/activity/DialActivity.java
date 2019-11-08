package com.engingplan.womarketing.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.engingplan.womarketing.bl.DialBL;
import com.engingplan.womarketing.bl.DialHttpBL;
import com.engingplan.womarketing.util.ConstantsUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : Android
 * github : https://github.com/renw7/AndroidProject
 * time   : 2019/10/31
 * desc   : 拨打界面
 */
public final class DialActivity extends AppCompatActivity {


    String number;//标记从哪个页面跳转到此页面 1表示任务界面 2表示通话记录页面
    long staffId;//员工ID

    long taskId;//任务ID
    String dataId;//任务数据ID
    String serialNumber = "";//用户手机号码
    String custInfo = "用户画像";

    String voiceContent = "你好，我们是中国联通";
    String smsContent = "你好，我们是中国联通";
    String productName = ""; //推荐产品名称
    String productId = ""; //推荐产品id

    String recordId;//通话记录id
    String startTime = "";//通话开始时间
    String endTime = "";//通话结束时间
    String resultCode = "-1";//沟通结果，即用户意向 1-同意，2-拒绝，3-有意向，4-未接通 -5其他  -1表示没有进行选择
    long callTimes = 1;//通话次数
    String remark;//备注

    TextView serialNumberView;//用户手机号码显示

    Button custInfoView;//用户画像按钮
    Button voiceContentView;//话术按钮
    TextView custInfoDetail;//用户画像或者话术的具体内容

    TextView proRecommend;//产品推荐
    TextView userIntention;//用户意向
    EditText remarkContent;//备注

    View dialDial; //打电话按钮
    View dialSms; //发短信按钮
    View dialComplete;//完成按钮

    boolean haveCAll = false;//是否点击拨打按钮打过电话
    boolean onclick = true;//标记完成按钮是否可用

    DialHttpBL dialHttpBL = new DialHttpBL();//网络请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dial);

        //获取上一个页面传递的参数 taskId 上一个页面的标记
        getPara();

        //动态注册广播接收器 接收返回的数据
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SELECTTASKDATA");
        intentFilter.addAction("INSERTCALLRECORD");
        intentFilter.addAction("UPDATETASKDATA");
        registerReceiver(mReceiver, intentFilter);

        //网络请求
        networkRequestTaskData();

        //获取页面的控件
        serialNumberView = findViewById(R.id.serial_number);

        custInfoView = findViewById(R.id.dial_cust_info_btn);
        voiceContentView = findViewById(R.id.dial_voice_content_btn);
        custInfoDetail = findViewById(R.id.cust_info_detail);

        proRecommend = findViewById(R.id.dial_pro_recommend);
        userIntention = findViewById(R.id.dial_pro_intention);
        remarkContent = findViewById(R.id.dial_remark_content);

        dialDial = findViewById(R.id.dial_dial);
        dialSms = findViewById(R.id.dial_sms);
        dialComplete = findViewById(R.id.dial_complete);

        //用户画像按钮，点击显示用户画像
        custInfoView.setOnClickListener((view) -> {
                    custInfoDetail.setText(custInfo);
                }
        );

        //话术按钮，点击显示话术
        voiceContentView.setOnClickListener((view) -> {
                    custInfoDetail.setText(voiceContent);
                }
        );

        //滑动显示画像话术全部内容
        custInfoDetail.setMovementMethod(ScrollingMovementMethod.getInstance());

        //用户意向
        userIntention.setOnClickListener((view) -> {
                    userIntent(userIntention);
                }
        );

        //打电话按钮
        dialDial.setOnClickListener((view) -> {
                    dial(serialNumber);
                    haveCAll = true;
                }
        );

        //发短信按钮
        dialSms.setOnClickListener((view) -> {
                    sms(serialNumber, smsContent);
                }
        );

        //完成按钮
        dialComplete.setOnClickListener((view) -> {

                    if (onclick == false) {
                        return;
                    }

                    //获取用户输入的备注
                    remark = remarkContent.getText().toString();
                    /*if ("".equals(remark)) {
                        System.out.println("remarkContent======" + remark);
                    } else {
                        System.out.println("remarkContent======" + remark);
                    }*/

                    System.out.println("号码:" + serialNumber + ",任务id:" + taskId
                            + "通话开始时间:" + startTime + "通话结束时间:" + endTime
                            + "用户意向:" + resultCode + "拨打次数:" + callTimes
                            + "备注:" + remark);

                    //判断客服是否已拨打过电话
                    if (haveCAll == false) {
                        Toast.makeText(this, "您还未拨打电话与用户沟通", Toast.LENGTH_LONG).show();
                    } else {

                        //判断是否选择用户意向,若没有选择则提示选择用户意向，若选择则写入数据库
                        if (resultCode == "-1") {  //-1表示未选择
                            Toast.makeText(this, "请选择用户订购意向", Toast.LENGTH_LONG).show();
                        } else {

                            //获取通话开始结束时间
                            getTime();

                            //插入通话记录
                            insertCallRecord();

                            //如果用户没有接通，开始时间和结束时间一致，不修改任务数据
                            //如果电话接通，修改任务数据（修改是否被打状态，添加员工id，更新时间）
                            //修改任务数据成功之后再刷新显示下一条
                            if (!endTime.equals(startTime)) {
                                updateDataInfo();
                            }else{

                                //如果没有接通，修改数据锁定状态
                                updateDataUnLock();
                            }

                            //修改通话记录，记录用户意向
                            //updateCallRecord();
                        }
                    }
                }
        );

    }

    /**
     * 获取上一个页面传递的参数:任务id（taskId）、number等
     */
    void getPara() {

        Bundle bundle = getIntent().getExtras();
        number = bundle.getString("number");
        taskId = bundle.getLong("taskId");
        staffId = bundle.getLong("staffId");

        if ("2".equals(number)) {   //如果从通话记录页面跳转过来，获取任务数据id
            dataId = bundle.getString("dataId");
        }

        callTimes = Long.parseLong(number);
    }


    /**
     * 网络请求 请求任务数据
     */
    void networkRequestTaskData() {

        Map param = new HashMap<>();
        if ("1".equals(number)) {
            param.put("taskId", taskId);
        }

        if ("2".equals(number)) {
            param.put("dataId", dataId);
        }
        dialHttpBL.getTaskDataAsyn(param, this.getApplicationContext(), number);
    }

    /**
     * 广播接收
     */
    private BroadcastReceiver mReceiver = new DialActivity.DataReceiver();

    class DataReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "SELECTTASKDATA":

                    //获取任务数据
                    List<Map<String, String>> list = (List) intent.getExtras().get("list");
                    if (list != null && list.size() > 0) {
                        System.out.println("任务数据list====" + list);

                        Map map = list.get(0);
                        dataId = (String) map.get("dataId");
                        serialNumber = (String) map.get("serialNumber");
                        custInfo = (String) map.get("custInfo");
                        productId = (String) map.get("productId");
                        productName = (String) map.get("productName");
                        voiceContent = (String) map.get("voiceContent");
                        smsContent = (String) map.get("smsContent");

                        //初始化页面，控件实现查询得到的信息
                        initActivity();
                    }

                    //若任务已完成，提示，修改完成按钮的状态
                    if (list == null|| list.size() == 0) {
                        //Toast.makeText(DialActivity.this, "该任务已完成", Toast.LENGTH_LONG).show();

                        AlertDialog dialog=new AlertDialog.Builder(DialActivity.this)
                                .setIcon(R.drawable.pointer)
                                .setTitle("提示")
                                .setMessage("当前任务已完成")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onclick = false;
                                    }
                                })
                                .create();
                        dialog.show();
                    }
                    break;

                case "INSERTCALLRECORD":

                    //获取插入通话记录的记录id
                    String recordIds = intent.getExtras().getString("recordId");
                    if (recordIds != null) {
                        recordId = recordIds;
                    }
                    break;

                case "UPDATETASKDATA":

                    //修改任务数据完成以后再取下一条
                    if ("1".equals(number)) {

                        //刷新显示下一条数据
                        networkRequestTaskData();
                        initActivity();
                    } else if ("2".equals(number)) {

                        //有意向用户的再次拨打完成，返回有意向列表
                        finish();
                    }
                    break;

                default:
                    break;
            }

        }
    }

    /**
     * 刷新页面，控件显示查询得到的信息
     */
    void initActivity() {

        //设置客户的手机号、客户画像和产品id
        serialNumberView.setText(serialNumber);
        custInfoDetail.setText(custInfo);
        proRecommend.setText(productName);

        userIntention.setText("请选择");
        resultCode = "-1";
        remarkContent.setText("");
        remarkContent.clearFocus();
        haveCAll = false;

    }


    /**
     * 用户意向对话框
     */
    void userIntent(TextView userIntention) {

        AlertDialog dialog = new AlertDialog.Builder(DialActivity.this)
                .setTitle("客户订购情况")
                .setSingleChoiceItems(R.array.dialog_items, Integer.parseInt(resultCode), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String[] items = getResources().getStringArray(R.array.dialog_items);
                        String locationname = items[i];
                        userIntention.setText(locationname);
                        resultCode = "" + i;
                        //Toast.makeText(DialActivity.this, locationname, Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //resultCode = "";
                    }
                }).create();
        dialog.show();
    }

    /**
     * 拨打电话
     */
    void dial(String phoneno) {

        DialBL userBL = new DialBL();
        Intent intent = userBL.call(phoneno);
        //startTime = userBL.time();
        //System.out.println("开始时间" + startTime);
        startActivity(intent);
    }

    /**
     * 发送短信
     */
    void sms(String phoneno, String message) {

        DialBL userBL = new DialBL();
        Intent sentIntent = new Intent("SENT_SMS_ACTION");
        PendingIntent sentPI = PendingIntent.getBroadcast(DialActivity.this, 0, sentIntent, 0);
        userBL.sendMessage(phoneno, message, sentPI);

        DialActivity.this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {

                if (getResultCode() == Activity.RESULT_OK) {

                    Toast.makeText(DialActivity.this, "短信发送成功", Toast.LENGTH_SHORT).show();

                    //发送成功，数据库写入
                    insertSmsRecord();
                } else {

                    Toast.makeText(DialActivity.this, "短信发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new IntentFilter("SENT_SMS_ACTION"));
    }

    /**
     * 添加短信发送记录
     */
    void insertSmsRecord() {
        Map putparam = new HashMap<>();
        putparam.put("serialNumber", serialNumber);
        putparam.put("taskId", taskId);
        dialHttpBL.putSmsAsyn(putparam, DialActivity.this.getApplicationContext());
    }

    /**
     * 读取通话记录，获取通话开始时间和结束时间
     */
    void getTime() {
        @SuppressLint("MissingPermission") Cursor cursor = DialActivity.this.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);
        if (cursor.moveToLast()) {
            //号码
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            System.out.println(number);
            //呼叫类型
            String type;
            switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                case CallLog.Calls.INCOMING_TYPE:
                    type = "呼入";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    type = "呼出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    type = "未接";
                    break;
                default:
                    type = "挂断";//应该是挂断.根据我手机类型判断出的
                    break;
            }

            SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
            //呼叫时间
            startTime = sfd.format(date);
            //联系人
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
            //通话时间,单位:s
            String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
            int duration1 = Integer.parseInt(duration);
            endTime = sfd.format(date.getTime() + duration1 * 1000);


            System.out.println("startTime===========" + startTime);
            System.out.println("endTime===========" + endTime);
        }
    }

    /**
     * 点击完成插入一条记录
     */
    void insertCallRecord() {
        Map putparam = new HashMap<>();
        putparam.put("serialNumber", serialNumber);
        putparam.put("taskId", taskId);
        putparam.put("startTime", startTime);
        putparam.put("endTime", endTime);
        putparam.put("resultCode", resultCode);
        putparam.put("productId", productId);
        putparam.put("callTimes", callTimes);
        putparam.put("remark", remark);
        putparam.put("staffId", staffId);
        putparam.put("dataId", dataId);
        dialHttpBL.putCallRecordAsyn(putparam, DialActivity.this.getApplicationContext());
    }

    /**
     * 选择用户意向，点击完成，修改记录
     */
    /*void updateCallRecord() {
        Map putparam = new HashMap<>();
        putparam.put("recordId", recordId);
        dialHttpBL.putCallResultAsyn(putparam, DialActivity.this.getApplicationContext());
    }*/

    /**
     * 完成，修改任务数据，标记拨打状态和员工编号
     */
    void updateDataInfo() {
        Map putparam = new HashMap<>();
        putparam.put("dataId", dataId);
        putparam.put("isLock", 0);
        putparam.put("isCall", 1);
        putparam.put("staffId", staffId);
        dialHttpBL.updateCallStatesAsyn(putparam, DialActivity.this.getApplicationContext());
    }

    /**
     * 解锁任务数据  情况一：点击用户未接通，点击完成  情况二：拨打界面返回
     */
    void updateDataUnLock() {
        Map putparam = new HashMap<>();
        putparam.put("dataId", dataId);
        putparam.put("isLock", 0);
        dialHttpBL.updateDataUnLockAsyn(putparam, DialActivity.this.getApplicationContext());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //注销广播接收器
        unregisterReceiver(mReceiver);

        //任务解锁  没有打电话直接返回时解锁任务数据
        updateDataUnLock();
    }

}