package com.engingplan.womarketing.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.engingplan.womarketing.ui.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";
    private EditText staffusername;
    private EditText staffpwd;
    private TextView mTv;
    private Button btnlogin;
    private String path = "http://119.29.106.248:80/tblstaffinfo/checkuser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        staffusername = (EditText) findViewById(R.id.et_login_phone);
        staffpwd = (EditText) findViewById(R.id.et_login_password);

        btnlogin = (Button) findViewById(R.id.btn_login_commit);
        btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String staffUsername = staffusername.getText().toString().trim();
                String staffPwd = staffpwd.getText().toString().trim();

                if (TextUtils.isEmpty(staffUsername) || TextUtils.isEmpty(staffPwd)) {
                    Toast.makeText(LoginActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpClient client = new OkHttpClient.Builder()
                        //读取超时
                        .readTimeout(10, TimeUnit.SECONDS)
                        //写入超时
                        .writeTimeout(10, TimeUnit.SECONDS)
                        //连接超时
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .build();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("staffUsrname", staffUsername);
                    jsonObject.put("staffPwd", staffPwd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String req = jsonObject.toString();
                RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), req);

                final Request request = new Request.Builder()
                        //请求方式
                        .post(body)
                        //请求接口地址
                        .url(path)
                        .build();
                //4.创建call对象
                Call call = client.newCall(request);
                //5.使用call调用 enqueue完成网络请求
                call.enqueue(new Callback() {
                    //请求失败时调用此方法
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        Log.i(TAG, "请求失败......");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "请求失败......", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //请求成功时调用该方法
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        Log.i(TAG, "请求成功......");

                        ResponseBody responseBody = response.body();
                        // 获得JSON字符串
                        final String responseString = responseBody.string();

                        Log.i(TAG, responseString);

                        try {
                            JSONObject jsonObject1 = new JSONObject(responseString);

                            String code = jsonObject1.getString("code");

                            if ("200".equals(code)) {
                                String staffname = "";
                                int staffno = 0;
                                String username = "";
                                String password = "";
                                JSONArray jsonArray = jsonObject1.getJSONObject("data").getJSONArray("records");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject record = jsonArray.getJSONObject(i);
                                    staffname = record.getString("staffName");
                                    staffno = record.getInt("staffNo");
                                    username = record.getString("staffUsrname");
                                    password = record.getString("staffPwd");
                                }
                                Log.i(TAG,"登录成功");
                                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                intent.putExtra("staffname",staffname);
                                intent.putExtra("staffno",staffno);
                                intent.putExtra("username",username);
                                intent.putExtra("password",password);
                                startActivity(intent);
                            } else {
                                staffusername.setText("");
                                staffpwd.setText("");
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}



