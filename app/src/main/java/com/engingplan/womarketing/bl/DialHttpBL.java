package com.engingplan.womarketing.bl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.engingplan.womarketing.util.ConstantsUtil;
import com.engingplan.womarketing.util.OkHttpClientUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * author : Android
 * github : https://github.com/renw7/AndroidProject
 * time   : 2018/10/31
 * desc   : 拨打任务http请求 bl层
 */
public class DialHttpBL {


    /**
     * 异步http调用  获取任务数据
     *
     * @param param
     */
    public void getTaskDataAsyn(Map param, Context context, String number) {

        String url = ConstantsUtil.URL_TASK_DATA;
        if ("2".equals(number)) {
            url = ConstantsUtil.URL_HOME + "/tbltaskdata/selectSpePost";
        }

        OkHttpClientUtils.getInstance().doPostAsyn(url, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                System.out.println("getTaskDataAllAsyn response===" + response);

                ArrayList<Map> recordList = new ArrayList<>();

                try {
                    JSONObject root = new JSONObject(response);
                    String code = root.getString("code");
                    if ("200".equals(code)) {
                        JSONObject record = root.getJSONObject("data");

                        JSONObject custInfo = new JSONObject(record.getString("custInfo"));
                        String custInfoContent = "姓名：" + custInfo.getString("name") + "\n"
                                + "性别：" + custInfo.getString("sex") + "\n"
                                + "当前套餐：" + custInfo.getString("当前套餐") + "\n"
                                + "ARPU值：" + custInfo.getString("ARPU值") + "\n"
                                + "近三个月月均使用流量：" + custInfo.getString("近三个月月均使用流量") + "\n";

                        Map map = new HashMap();
                        map.put("dataId", record.getString("dataId"));
                        map.put("serialNumber", record.getString("serialNumber"));
                        map.put("custInfo", custInfoContent);

                        map.put("productId", record.getString("productId"));
                        map.put("productName", record.getString("productName"));
                        map.put("voiceContent", record.getString("voiceContent"));
                        map.put("smsContent", record.getString("smsContent"));

                        recordList.add(map);

                    } else if ("400".equals(code)) {  //{"code":400,"success":false,"data":null,"msg":"无数据返回"}

                    } else {
                        throw new Exception("连接异常[" + code + "]");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

                //返回给ui层
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", recordList);
                intent.putExtras(bundle); //向广播接收器传递数据
                intent.setAction("SELECTTASKDATA");
                context.sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {

                System.out.println(response);
            }
        });
    }


    /**
     * 通话结束 插入记录通话
     *
     * @param param
     */
    public void putCallRecordAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_CALL_RECORD, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                System.out.println("记录通话response=====" + response);

                //返回新增通话记录的recordId给ui层
                Intent intent = new Intent();
                intent.putExtra("recordId", response);
                intent.setAction("INSERTCALLRECORD");
                context.sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }

    /**
     * 点击完成，通话记录表记录用户意向  目前不需要
     *
     * @param param
     */
    public void putCallResultAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_CALL_UPDATE, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                System.out.println(response);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }

    /**
     * 发送短信，添加短信发送记录
     *
     * @param param
     */
    public void putSmsAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_SMS_INFO, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                System.out.println("putSmsSuccess" + response);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }

    /**
     * 修改任务信息，添加拨打状态和员工id
     *
     * @param param
     */
    public void updateCallStatesAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_TASKDATA_UPDATE, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                Intent intent = new Intent();
                intent.setAction("UPDATETASKDATA");
                context.sendBroadcast(intent);

                System.out.println("updateCallStatesAsyn" + response);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }

    /**
     * 解锁任务信息
     *
     * @param param
     */
    public void updateDataUnLockAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_TASKDATA_UNLOCK, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                Intent intent = new Intent();
                intent.setAction("UPDATETASKDATAUNLOCK");
                context.sendBroadcast(intent);

                System.out.println("updateDataUnLockAsyn" + response);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }

}
