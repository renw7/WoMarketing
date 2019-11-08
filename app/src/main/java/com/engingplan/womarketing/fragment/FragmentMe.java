package com.engingplan.womarketing.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.engingplan.womarketing.bl.UpdatePwdBL;
import com.engingplan.womarketing.ui.activity.LoginActivity;
import com.engingplan.womarketing.ui.activity.R;
import com.engingplan.womarketing.util.ConstantsUtil;

import java.util.HashMap;
import java.util.Map;



public class FragmentMe extends Fragment {
    private String staffNo;
    private String password;
    private Context mContext;
    private UpdatePwdBL updatePwdBL;

    LocalBroadcastManager broadcastManager;

    public static FragmentMe newInstance(String name) {
        Log.i(ConstantsUtil.LOG_TAG_FRAGMENT, "实例化framgmet" + name);
        FragmentMe fragment = new FragmentMe();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_my_main, container, false);//我的布局
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //获取登录员工的信息
        mContext = getContext();
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        staffNo = String.valueOf(bundle.getInt("staffNo"));
        password = bundle.getString("passWord");
        TextView staffNoView = view.findViewById(R.id.person_id);
        staffNoView.setText("工号:" + staffNo);

        FrameLayout buttonUptPwd = view.findViewById(R.id.fl_person_data_changepwd);
        buttonUptPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatepwdDialog();
            }
        });

        FrameLayout buttonExit = view.findViewById(R.id.fl_person_data_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitDialog();
            }
        });


        // 动态注册
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.PERSON_INFO_RECEIVER);
        broadcastManager.registerReceiver(mReceiver, intentFilter);

        //网络通信实例
        updatePwdBL = new UpdatePwdBL();

        //从这里结束
    }

    //广播接收器
    private BroadcastReceiver mReceiver = new ResultReceiver();
    class ResultReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getExtras().getString("RESULT");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(mReceiver);
    }


    //从这里开始

    //修改密码对话框
    private void updatepwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View v = inflater.inflate(R.layout.updatepwd_dialog, null);
        Button btnSure = v.findViewById(R.id.dialog_btn_sure);
        Button btnCancel = v.findViewById(R.id.dialog_btn_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText oldPwdView = v.findViewById(R.id.old_password);
                EditText newPwdView = v.findViewById(R.id.new_password);
                EditText newPwdConfVIew = v.findViewById(R.id.new_password_confirm);
                String oldPwd = oldPwdView.getText().toString();
                String newPwd = newPwdView.getText().toString();
                String newPwdConfirm = newPwdConfVIew.getText().toString();
                updatePwdHttp(staffNo, oldPwd, newPwd, newPwdConfirm);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "取消修改!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }


    //退出对话框
    private void exitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View v = inflater.inflate(R.layout.activity_exit_dialog, null);
        Button btnSure = v.findViewById(R.id.dialog_btn_sure);
        Button btnCancel = v.findViewById(R.id.dialog_btn_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(v);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }



    //网络通信
    private void updatePwdHttp(String staffno, String oldPwd, String newPwd, String newPwdConfirm){
        //TextUtils 简易字符串工具类
        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)||TextUtils.isEmpty(newPwdConfirm)) {
            Toast.makeText(mContext, "输入不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!oldPwd.equals(password)){
            Toast.makeText(mContext, "原始密码输入错误!", Toast.LENGTH_SHORT).show();
            return;
        }else if(newPwd.equals(password)){
            Toast.makeText(mContext, "新密码不能与原始密码相同!", Toast.LENGTH_SHORT).show();
            return;
        }else if (!newPwd.equals(newPwdConfirm)){
            Toast.makeText(mContext, "新密码两次输入不一致!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map param = new HashMap();
        param.put("staffNo", staffNo);
        param.put("staffPwd", newPwdConfirm);

        updatePwdBL.updateStaffPwd(param, getContext());

    }



    //从这里结束
}
