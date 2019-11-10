package com.engingplan.womarketing.bl;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.engingplan.womarketing.util.ConstantsUtil;
import com.engingplan.womarketing.util.OkHttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *    author : Android
 *    github : https://github.com/renw7/AndroidProject
 *    time   : 2018/10/31
 *    desc   : http请求demo bl层
 */
public class OkHttpDemoBL {

    //云服务器地址  查询listview表单
//    private String url = "http://119.29.106.248/tblcallrecord/page";
    //本机测试地址
//    private String url = "http://10.52.200.150/tbluserinfo/page";

    /**
     * 异步http调用
     * @param param
     */
    public void getUserInfoAllAsyn(Map param, Context context){
        OkHttpClientUtils.getInstance().doGetAsyn(ConstantsUtil.CALL_RECORD_RECEIVER, param, new OkHttpClientUtils.NetWorkCallBack(){
            @Override
            public void onSuccess(String response) {
                ArrayList<Map> list = json2List(response);

                //下面通过异常方式返回给ui层
                Intent intent = new Intent(ConstantsUtil.CALL_RECORD_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list);
                intent.putExtras(bundle);       //向广播接收器传递数据
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                Log.e(ConstantsUtil.LOG_TAG_BL, "response=" + response);
            }
        });

    }


    private ArrayList<Map> json2List(String result){
        ArrayList<Map> recordList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(result);
            String code = root.getString("code");

            if ("200".equals(code)) {
                JSONArray jsonArray = root.getJSONObject("data").getJSONArray("records");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject record = jsonArray.getJSONObject(i);
                    Map map = new HashMap();
                    map.put("createUser", record.getString("createUser"));
                    map.put("recordId", String.valueOf(record.getInt("recordId")));
                    map.put("createTime", record.getString("createTime"));
                    map.put("updateUser", record.getString("updateUser"));
                    map.put("updateTime", record.getString("updateTime"));
                    map.put("status", record.getString("status"));
                    map.put("isDeleted", record.getString("isDeleted"));
                    map.put("serialNumber", record.getString("serialNumber"));
                    map.put("taskId", String.valueOf(record.getInt("taskId")));
                    map.put("startTime", record.getString("startTime"));
                    map.put("endTime", record.getString("endTime"));
                    map.put("resultCode", record.getString("resultCode"));
                    map.put("productId", record.getString("productId"));
                    map.put("callTimes", record.getString("callTimes"));
                    map.put("remark", record.getString("remark"));
                    map.put("staffId", String.valueOf(record.getInt("staffId")));
                    map.put("resultDesc", record.getString("resultDesc"));
                    map.put("taskName", record.getString("taskName"));
                    map.put("dataId", String.valueOf(record.getInt("dataId")));
                    recordList.add(map);
                }
            }
            else{
                throw new Exception("连接异常["+code+"]");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(ConstantsUtil.LOG_TAG_BL, "e=" + e.getMessage());
        } finally {
            return recordList;
        }
    }



    public void getUserInfoAllAsyn2(Map param, Context context){
        OkHttpClientUtils.getInstance().doGetAsyn(ConstantsUtil.URL_CALL_REROD_INFO, param, new OkHttpClientUtils.NetWorkCallBack(){
            @Override
            public void onSuccess(String response) {
                ArrayList<Map> list = json2List(response);

                //下面通过异常方式返回给ui层
                Intent intent = new Intent(ConstantsUtil.CALL_RECORD_INFO_RECEIVER);
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


}