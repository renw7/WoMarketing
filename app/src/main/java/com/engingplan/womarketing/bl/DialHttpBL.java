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

    //本机
    public static String URL="http://10.52.200.149";

    //服务器
    //public static String URL="http://119.29.106.248";

    //任务数据表
    //private String url_task_data = "http://10.52.200.150/tblstaffinfo/page";

    //任务信息表
    //private String url_task_info = "http://10.52.200.150/tblstaffinfo/page";

    //获取任务数据
    //get方式
    private String url_task_data_local_get = "http://10.52.200.149/tbltaskdata/selectOneGet?taskId=101";
    //post方式
    private String url_task_data =URL+"/tbltaskdata/selectOnePost";
    //获取指定任务数据
    //private String url_task_data_spe =URL+"/tbltaskdata/selectSpePost";

    //修改任务数据
    //post方式
    private String url_taskdata_update = URL+"/tbltaskdata/update";

    //获取任务信息
    //get方式
    private String url_task_info_local_get = "http://10.52.200.149/tbltaskinfo/selectOneGet?taskId=101";
    //post方式
    private String url_task_info=URL+ "/tbltaskinfo/selectOnePost";

    //通话记录表 post方式
    private String url_call_record = URL+ "/tblcallrecord/saveCall";

    //通话记录表 post方式  修改记录
    private String url_call_update = URL+ "/tblcallrecord/updateCall";

    //短信记录表 post方式
    private String url_sms_info = URL+ "/tblsmsinfo/saveSms";


    /**
     * 异步http调用  获取任务数据
     *
     * @param param
     */
    public void getTaskDataAsyn(Map param, Context context,String number) {

        if("2".equals(number)){
            url_task_data=URL+"/tbltaskdata/selectSpePost";
        }

        OkHttpClientUtils.getInstance().doPostAsyn(url_task_data, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                System.out.println("getTaskDataAllAsyn response==="+response);
                //ArrayList<Map> list = json2List(response);

                ArrayList<Map> recordList = new ArrayList<>();

                try {
                    JSONObject root = new JSONObject(response);
                    String code = root.getString("code");
                    if ("200".equals(code)) {
                        JSONObject record = root.getJSONObject("data");
//                        if(record!=null && !record.isNull("name") ) {

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
                            //map.put("custInfo", record.getString("custInfo"));
                            //map.put("custInfo",record.getJSONObject("custInfo");
                            recordList.add(map);
//                        }else{
//                            Map map = new HashMap();
//                            map.put("isnull","null");
//                            recordList.add(map);
//                        }
                    } else if ("400".equals(code)){

                    } else {
                        throw new Exception("连接异常[" + code + "]");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }


                //下面通过异常方式返回给ui层
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", recordList);
                intent.putExtras(bundle); //向广播接收器传递数据
                intent.setAction(ConstantsUtil.ACTION_APP_INNER_BROADCAST);
                context.sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {

                System.out.println(response);
            }
        });
    }

    /**
     * json转List    查不到时显示{"code":200,"success":true,"data":{},"msg":"暂无承载数据"}
     */
    private ArrayList<Map> json2List(String result) {
        ArrayList<Map> recordList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(result);
            String code = root.getString("code");

            if ("200".equals(code)) {

                JSONObject record = root.getJSONObject("data");

                JSONObject custInfo = new JSONObject(record.getString("custInfo"));
                String custInfoContent="姓名："+custInfo.getString("name")+"\n"
                        +"性别："+custInfo.getString("sex")+"\n"
                        +"当前套餐："+custInfo.getString("当前套餐")+"\n"
                        +"ARPU值："+custInfo.getString("ARPU值")+"\n"
                        +"近三个月月均使用流量："+custInfo.getString("近三个月月均使用流量")+"\n";

                Map map = new HashMap();
                map.put("dataId", record.getString("dataId"));
                map.put("serialNumber", record.getString("serialNumber"));
                map.put("custInfo", custInfoContent);
                //map.put("custInfo", record.getString("custInfo"));
                //map.put("custInfo",record.getJSONObject("custInfo");
                recordList.add(map);

            } else {
                throw new Exception("连接异常[" + code + "]");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return recordList;
        }

    }

    /**
     * 异步http调用  获取任务信息
     *
     * @param param
     */
    public void getTaskInfoAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(url_task_info, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                ArrayList<Map> infoList = new ArrayList<>();

                try {
                    JSONObject root = new JSONObject(response);
                    String code = root.getString("code");

                    if ("200".equals(code)) {
                        JSONObject data = root.getJSONObject("data");

                        Map map = new HashMap();
                        map.put("productId", data.getString("productId"));
                        map.put("productName", data.getString("productName"));
                        map.put("voiceContent", data.getString("voiceContent"));
                        map.put("smsContent", data.getString("smsContent"));
                        infoList.add(map);
                    } else {
                        throw new Exception("连接异常[" + code + "]");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

                //下面通过异常方式返回给ui层
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("infoList", infoList);
                intent.putExtras(bundle); //向广播接收器传递数据
                intent.setAction(ConstantsUtil.ACTION_APP_INNER_BROADCAST);
                context.sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }


    /**
     * 通话结束 记录通话
     *
     * @param param
     */
    public void putCallRecordAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(url_call_record, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                System.out.println("记录通话response====="+response);

                //返回新增通话记录的recordId给ui层
                Intent intent = new Intent();
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("infoList", infoList);
                intent.putExtras(bundle); *///向广播接收器传递数据
                intent.putExtra("recordId",response);
                intent.setAction(ConstantsUtil.ACTION_APP_INNER_BROADCAST);
                context.sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }

    /**
     * 点击完成，通话记录表记录用户意向  不需要了
     *
     * @param param
     */
    public void putCallResultAsyn(Map param, Context context) {

        OkHttpClientUtils.getInstance().doPostAsyn(url_call_update, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                Log.d("putCallRecordSuccess", "成功");
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

        OkHttpClientUtils.getInstance().doPostAsyn(url_sms_info, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                Log.d("putSmsSuccess", "成功");
                System.out.println(response);
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

        OkHttpClientUtils.getInstance().doPostAsyn(url_taskdata_update, param, new OkHttpClientUtils.NetWorkCallBack() {

            @Override
            public void onSuccess(String response) {

                Log.d("putSmsSuccess", "成功");
                System.out.println(response);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });
    }

}
