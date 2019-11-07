package com.engingplan.womarketing.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.engingplan.womarketing.ui.activity.LoginActivity;
import com.engingplan.womarketing.ui.activity.R;
import com.engingplan.womarketing.ui.dialog.ConfirmDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class FragmentMe extends Fragment {
    private TextView tv;
    private String staffno_text;//工号
    private static String TAG = "FragmentMe";
    //后端服务器接口
    private String url="http://119.29.106.248/tblstaffinfo/updatePwd";
    private String staffno;
    private String password;
    private Context mContext;

    public static FragmentMe newInstance(String name) {
        FragmentMe fragment = new FragmentMe();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_activity_main, container, false);//我的布局
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //从这里开始

        mContext=getContext();
        Intent intentf=getActivity().getIntent();
        Bundle bundle=intentf.getExtras();
        Integer staffno_intent = bundle.getInt("staffNo");
        this.password = bundle.getString("passWord");
        this.staffno=staffno_intent.toString();
        TextView staffNo_textview=view.findViewById(R.id.person_id);
        this.staffno_text="工号:"+staffno;
        staffNo_textview.setText(this.staffno_text);


        FrameLayout button_upwd = view.findViewById(R.id.fl_person_data_changepwd);
        button_upwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepwd_dialog();
            }
        });

        FrameLayout button_exit = view.findViewById(R.id.fl_person_data_exit);
        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit_dialog();
            }
        });

        //从这里结束
    }



    //从这里开始

    //修改密码对话框
    private void updatepwd_dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View v = inflater.inflate(R.layout.updatepwd_dialog, null);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText old_pwd=v.findViewById(R.id.old_password);
                EditText new_pwd=v.findViewById(R.id.new_password);
                EditText new_pwd_c=v.findViewById(R.id.new_password_confirm);
                String s_old_pwd=old_pwd.getText().toString();
                String s_new_pwd=new_pwd.getText().toString();
                String s_new_pwd_c=new_pwd_c.getText().toString();
                update_pwd_http(s_old_pwd,s_new_pwd,s_new_pwd_c);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "取消修改!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    //退出对话框
    private void exit_dialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View v = inflater.inflate(R.layout.exit_dialog, null);
        Button btn_sure = (Button) v.findViewById(R.id.dialog_btn_sure);
        Button btn_cancel = (Button) v.findViewById(R.id.dialog_btn_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    //网络通信
    private void update_pwd_http(String s_old_pwd,String s_new_pwd,String s_new_pwd_c){



        //TextUtils 简易字符串工具类
        if (TextUtils.isEmpty(s_old_pwd) || TextUtils.isEmpty(s_new_pwd)||TextUtils.isEmpty(s_new_pwd_c)) {
            Toast.makeText(mContext, "输入不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!s_old_pwd.equals(password)){
            Toast.makeText(mContext, "原始密码输入错误!", Toast.LENGTH_SHORT).show();
            return;
        }else if(s_new_pwd.equals(password)){
            Toast.makeText(mContext, "新密码不能与原始密码相同!", Toast.LENGTH_SHORT).show();
            return;
        }else if (!s_new_pwd.equals(s_new_pwd_c)){
            Toast.makeText(mContext, "新密码两次输入不一致!", Toast.LENGTH_SHORT).show();
            return;
        }

        //实例化OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                //读取超时
                .readTimeout(10, TimeUnit.SECONDS)
                //写入超时
                .writeTimeout(10, TimeUnit.SECONDS)
                //连接超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        //实例化JSONObject对象
        JSONObject jsonObject = new JSONObject();
        //JSON数据编码
        try {
            jsonObject.put("staffNo", staffno);
            jsonObject.put("staffPwd", s_new_pwd_c);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String req = jsonObject.toString();
        //设置请求体
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), req);
        //设置请求方法
        final Request request = new Request.Builder()
                //请求方式
                .post(body)
                //请求接口地址
                .url(url)
                .build();

        //4.创建call对象
        Call call = client.newCall(request);
        //执行异步调用
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "请求失败......");

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "请求失败......", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "请求成功......");
                //从应答对象中返回应答体
                ResponseBody responseBody = response.body();
                // 获得JSON字符串
                final String responseString = responseBody.string();
                Log.i(TAG, responseString);

                try {
                    //JSON解码
                    JSONObject jsonObject1 = new JSONObject(responseString);
                    //取应答体中code  200成功否则失败
                    String code = jsonObject1.getString("code");
                    if ("200".equals(code)) {
                        Log.i(TAG,"密码修改成功！！");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "密码修改成功！！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.i(TAG,"密码修改失败！！");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mContext, "密码修改失败！！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }



    //从这里结束
}
