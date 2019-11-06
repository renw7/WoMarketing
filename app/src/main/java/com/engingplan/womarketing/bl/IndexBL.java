package com.engingplan.womarketing.bl;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class IndexBL  {

     private String url = "http://119.29.106.248/tblcallrecord/statistics";
     private  int num;
     private String ACTION_APP_BROADCAST = "com.engingplan.womarketing.fragment";


    /**
     * 这个方法用来从后台调取数据
     */

    public void setDataFirst(LocalBroadcastManager broadcastManager) {


        String url1 = url + "?staffId=1001";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url1).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure( Call call,  IOException e) {
                System.out.println("调取失败");
            }

            @Override
            public void onResponse( Call call,  Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseString = responseBody.string();
                Log.d("调取的数据是：", responseString);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = jsonObject.getJSONObject("data").getJSONArray("records");
                    JSONObject o = (JSONObject)jsonArray.get(0);
                    num = (Integer) o.get("num");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("取出的数值是：" + num);

                Intent intent = new Intent();
                intent.setAction(ACTION_APP_BROADCAST);
                intent.putExtra("num", num);
                broadcastManager.sendBroadcast(intent);
            }
        });






    }




}
