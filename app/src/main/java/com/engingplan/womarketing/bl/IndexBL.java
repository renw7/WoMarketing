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

public class IndexBL {

    private String url = "http://119.29.106.248";
    private int todayFinishNum;
    private int todayIntentNum;
    private int todayIncompNum;
    private int weekFinishNum;
    private int weekIncompNum;
    private int weekIntentNum;
    private String ACTION_APP_BROADCAST = "today";
    private String ACTION_APP_BROADCAST2 = "week";
    private int staffId;

    //接受传来的staffId
    public IndexBL(int staffId) {
        this.staffId = staffId;
    }

    /**
     * 这个方法用来从后台调取数据
     */

    public void setDataFirst(LocalBroadcastManager broadcastManager) {


        String url1 = url + "/tblcallrecord/statistics?staffId=" + staffId;
        String url2 = url + "/tblcallrecord/statisticsweek?staffId=" + staffId;
        OkHttpClient client = new OkHttpClient();
        Request request1 = new Request.Builder().url(url1).get().build();
        Request request2 = new Request.Builder().url(url2).get().build();
        Call call1 = client.newCall(request1);
        Call call2 = client.newCall(request2);
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("调取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                    JSONObject o = (JSONObject) jsonArray.get(0);
                    todayFinishNum = (Integer) o.get("finishNum");
                    todayIntentNum = (Integer) o.get("intentNum");
                    todayIncompNum = (Integer) o.get("incompNum");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("取出的数值是：" + todayFinishNum);

                Intent intent = new Intent();
                intent.setAction(ACTION_APP_BROADCAST);
                intent.putExtra("finishNum", todayFinishNum);
                intent.putExtra("intentNum", todayIntentNum);
                intent.putExtra("incompNum", todayIncompNum);
                intent.putExtra("type",1);
                broadcastManager.sendBroadcast(intent);
            }

        });
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("调取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
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
                    JSONObject o = (JSONObject) jsonArray.get(0);
                    weekFinishNum = (Integer) o.get("finishNum");
                    weekIntentNum = (Integer) o.get("intentNum");
                    weekIncompNum = (Integer) o.get("incompNum");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent();
                intent.setAction(ACTION_APP_BROADCAST);
                intent.putExtra("finishNum", weekFinishNum);
                intent.putExtra("intentNum", weekIntentNum);
                intent.putExtra("incompNum", weekIncompNum);
                intent.putExtra("type",2);
                broadcastManager.sendBroadcast(intent);
            }


        });


    }
}
