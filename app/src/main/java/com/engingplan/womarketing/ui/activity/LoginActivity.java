package com.engingplan.womarketing.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.engingplan.womarketing.bl.OkhttpLoginBL;
import com.engingplan.womarketing.util.ConstantsUtil;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText staffUsername;
    private EditText staffPwd;
    private Button buttonLogin;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        staffUsername = (EditText) findViewById(R.id.et_login_phone);
        staffPwd = (EditText) findViewById(R.id.et_login_password);

        //动态注册广播接收器
        Log.i(ConstantsUtil.LOG_TAG_ACTIVITY,"registerReceiver");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantsUtil.LOGIN_ACTIVITY_RECEIVER);
        registerReceiver(myReceiver, intentFilter);

        toggleButton = (ToggleButton) findViewById(R.id.pwd_show);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    //如果选中，显示密码
                    staffPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {

                    //否则隐藏密码
                    staffPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                staffPwd.postInvalidate();

                //切换后将EditText光标置于末尾
                CharSequence charSequence = staffPwd.getText();
                if (charSequence instanceof Spannable) {
                    Spannable spanText = (Spannable) charSequence;
                    Selection.setSelection(spanText, charSequence.length());
                }
            }
        });

        buttonLogin = (Button) findViewById(R.id.btn_login_commit);
        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String staffUserName = staffUsername.getText().toString().trim();
                String staffPWD = staffPwd.getText().toString().trim();

                if (TextUtils.isEmpty(staffUserName) || TextUtils.isEmpty(staffPWD)) {
                    Toast.makeText(LoginActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //将用户名和密码封装到Map文件中，用于工具类使用
                Map param = new HashMap();
                param.put("staffUsername", staffUserName);
                param.put("staffPwd", staffPWD);

                //调用OkhttpLoginBL
                OkhttpLoginBL okhttpLoginBL = new OkhttpLoginBL();
                okhttpLoginBL.loginUser(param, LoginActivity.this);

            }
        });
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            HashMap map = (HashMap<String, String>) intent.getExtras().get("map");
            if (map != null && !map.isEmpty()) {

                //从服务器返回的数据中取出指定参数赋值给相应变量
                String staffName = (String) map.get("staffName");
                String staffNo = (String) map.get("staffNo");
                String userName = (String) map.get("staffUsername");
                String passWord = (String) map.get("staffPwd");
                Integer staffId = (Integer) map.get("staffId");
                Log.i(ConstantsUtil.LOG_TAG_ACTIVITY,"登录成功");

                //传参，跳转页面
                Intent it = new Intent(LoginActivity.this, HomeActivity.class);
                it.putExtra("staffName", staffName);
                it.putExtra("staffNo", staffNo);
                it.putExtra("userName", userName);
                it.putExtra("passWord", passWord);
                it.putExtra("staffId", staffId);
                startActivity(it);
            } else {

                //登录失败时，用户名和密码输入框清空，Toast提示用户
                staffUsername.setText("");
                staffPwd.setText("");
                Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入！", Toast.LENGTH_SHORT).show();
                Log.i(ConstantsUtil.LOG_TAG_ACTIVITY,"登录失败");
            }
        }
    };

    //动态注销广播接收器
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}

