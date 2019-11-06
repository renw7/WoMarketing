package com.engingplan.womarketing.bl;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


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
public class TabLayoutBL {

    //云服务器地址
    private String url = "http://119.29.106.248/tblcallrecord/page";

    private String url2 = "http://10.52.200.150/tblstaffinfo/page";

    private String url3 = "http://10.52.200.150/tblstaffinfo/page";

    //本机测试地址
//    private String url = "http://10.52.200.149/tbltaskdata/selectOneGet";

    /**
     * 异步http调用
     * @param param
     */
    public void getUserInfoAllAsyn(Map param, Context context){
        OkHttpClientUtils.getInstance().doGetAsyn(url, param, new OkHttpClientUtils.NetWorkCallBack(){
            @Override
            public void onSuccess(String response) {
                ArrayList<Map> list = json2List(response);
                System.out.println("onsucc list.size="+list.size());
                //下面通过异常方式返回给ui层
                Intent intent = new Intent(ConstantsUtil.ACTION_APP_INNER_BROADCAST);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list);
                intent.putExtras(bundle);       //向广播接收器传递数据
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
            }
        });

    }


    /**
     * 异步http调用
     * @param param
     */
    public void getUserInfoConditionAsyn(Map param, Context context){
        OkHttpClientUtils.getInstance().doPostAsyn(url, param, new OkHttpClientUtils.NetWorkCallBack(){
            @Override
            public void onSuccess(String response) {
                ArrayList<Map> list = json2List(response);

                //下面通过异常方式返回给ui层
                Intent intent = new Intent(ConstantsUtil.ACTION_APP_INNER_BROADCAST);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", list);
                intent.putExtras(bundle);       //向广播接收器传递数据
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }

            @Override
            public void onFail(String response) {
                System.out.println(response);
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
                    map.put("recordId", record.getString("recordId"));
                    map.put("serialNumber", record.getString("serialNumber"));
                    map.put("taskId", record.getString("taskId"));
                    map.put("startTime", record.getString("startTime"));
                    recordList.add(map);
                }
            }
            else{
                throw new Exception("连接异常["+code+"]");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return recordList;
        }
    }


}