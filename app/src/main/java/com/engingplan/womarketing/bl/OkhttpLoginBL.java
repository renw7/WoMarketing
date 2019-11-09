package com.engingplan.womarketing.bl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.engingplan.womarketing.util.OkHttpClientUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class OkhttpLoginBL {
    //云服务器地址，查询员工信息表单
    private String path = "http://119.29.106.248:80/tblstaffinfo/checkuser";

    public void loginUser(Map param, String path, Context context) {
        OkHttpClientUtils.getInstance().doPostAsyn(path, param, new OkHttpClientUtils.NetWorkCallBack() {
            @Override
            public void onSuccess(String response) {

                HashMap map = json2List(response);
                //隐式意图发送广播给ui层
                Intent intent = new Intent("LOGIN_ACTIVITY_RECEIVER");
                Bundle bundle = new Bundle();
                bundle.putSerializable("map", map);
                intent.putExtras(bundle);
                context.sendBroadcast(intent);
            }
            @Override
            public void onFail(String response) {
                System.out.println(response);
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
                String TAG = "";
                JSONArray jsonArray = root.getJSONObject("data").getJSONArray("records");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject record = jsonArray.getJSONObject(i);
                    map.put("staffName", record.getString("staffName"));
                    map.put("staffNo", record.getString("staffNo"));
                    map.put("userName", record.getString("staffUsername"));
                    map.put("passWord", record.getString("staffPwd"));
                    map.put("staffId", record.getInt("staffId"));
                    Log.i(TAG, "map");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return map;
        }
    }
}
