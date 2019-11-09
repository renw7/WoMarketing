package com.engingplan.womarketing.bl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.engingplan.womarketing.util.ConstantsUtil;
import com.engingplan.womarketing.util.OkHttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OkHttpTaskBL {

    /**
     * TASKLISTACTIVITY异步http调用
     *
     * @param param
     */
    public void tasklistPostUserInfoAllAsyn(Map param, Context context) {
        //无需再用变量接一下param,直接传递就可以。
        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_TASK_LIST, param, new OkHttpClientUtils.NetWorkCallBack() {
            @Override
            //返回结果是一个字符串类型的response
            public void onSuccess(String response) {
                ArrayList<Map> list = jsonList(response);
                //下面通过异常方式返回给ui层
                Intent intent = new Intent(ConstantsUtil.TASK_LIST_ACTIVITY_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list);
                intent.putExtras(bundle);       //向广播接收器传递数据
                context.sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                Log.e(ConstantsUtil.LOG_TAG_BL, "response=" + response);
            }
        });

    }

    /**
     * TASKDETAILSACTIVITY异步http调用
     *
     * @param param
     */
    public void taskdetailPostUserInfoAllAsyn(Map param, Context context) {
        //无需再用变量接一下param,直接传递就可以。
        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_TASK_DETAIL, param, new OkHttpClientUtils.NetWorkCallBack() {
            @Override
            //返回结果是一个字符串类型的response
            public void onSuccess(String response) {
                HashMap map = jsonMap(response);
                //下面通过异常方式返回给ui层
                Intent intent = new Intent(ConstantsUtil.TASK_DETAIL_ACTIVITY_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putSerializable("map", map);
                intent.putExtras(bundle);       //向广播接收器传递数据
                context.sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                Log.e(ConstantsUtil.LOG_TAG_BL, "response=" + response);
            }
        });

    }

    private ArrayList<Map> jsonList(String result) {
        ArrayList<Map> recordList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString("code");

            if ("200".equals(code)) {
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("records");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject record = jsonArray.getJSONObject(i);
                    Map map = new HashMap();
                    //放入taskName键值对
                    map.put("taskName", record.getString("taskName"));
                    String taskStatus = record.getString("taskStatus");
                    //数值转换后，放入taskStatus键值对
                    if (taskStatus.equals("5")) {
                        taskStatus = "未完成";
                    } else if (taskStatus.equals("6")) {
                        taskStatus = "已完成";
                    }
                    map.put("taskStatus", taskStatus);
                    //放入map的顺序与taskId对应关系的键值对
                    map.put(String.valueOf(i), record.getString("taskId"));
                    recordList.add(map);
                }
            } else {
                throw new Exception("连接异常[" + code + "]");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(ConstantsUtil.LOG_TAG_BL, "e=" + e.getMessage());
        } finally {
            return recordList;
        }
    }

    private HashMap jsonMap(String result) {
        HashMap map = new HashMap();
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString("code");
            if (code.equals("200")) {
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("records");
                //取数组中的一个值  暂时固定为第一个
                JSONObject record = jsonArray.getJSONObject(0);
                map.put("taskName", record.getString("taskName"));
                map.put("taskType", record.getString("taskType"));
                map.put("taskStatus", record.getString("taskStatus"));
                map.put("productId", record.getString("productId"));
                map.put("productName", record.getString("productName"));
                map.put("updateTime", record.getString("updateTime"));
                map.put("voiceContent", record.getString("voiceContent"));
                map.put("smsContent", record.getString("smsContent"));
            } else {
                throw new Exception("连接异常[" + code + "]");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(ConstantsUtil.LOG_TAG_BL, "e=" + e.getMessage());
        } finally {
            return map;
        }
    }

}
