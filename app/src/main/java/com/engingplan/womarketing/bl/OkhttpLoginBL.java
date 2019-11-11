package com.engingplan.womarketing.bl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.engingplan.womarketing.util.ConstantsUtil;
import com.engingplan.womarketing.util.OkHttpClientUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class OkhttpLoginBL {

    public void loginUser(Map param, Context context) {
        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_CHECK_USER, param, new OkHttpClientUtils.NetWorkCallBack() {
            @Override
            public void onSuccess(String response) {
                HashMap map = json2List(response);

                //下面通过异常方式返回给ui层
                Intent intent = new Intent(ConstantsUtil.LOGIN_ACTIVITY_RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putSerializable("map", map);
                intent.putExtras(bundle);
                //向广播接收器传递数据
                context.sendBroadcast(intent);
            }
            @Override
            public void onFail(String response) {
                Log.e(ConstantsUtil.LOG_TAG_BL, "response=" + response);
            }
        });

    }

    private HashMap json2List(String result) {

        HashMap map = null;

        try {
            JSONObject root = new JSONObject(result);
            String code = root.getString("code");

            if ("200".equals(code)) {
                //json字符串解码，将数据放到map中
                map = new HashMap();
                JSONArray jsonArray = root.getJSONObject("data").getJSONArray("records");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject record = jsonArray.getJSONObject(i);
                    map.put("staffName", record.getString("staffName"));
                    map.put("staffNo", record.getString("staffNo"));
                    map.put("userName", record.getString("staffUsername"));
                    map.put("passWord", record.getString("staffPwd"));
                    map.put("staffId", record.getInt("staffId"));
                    Log.i(ConstantsUtil.LOG_TAG_BL, "map");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }
}
