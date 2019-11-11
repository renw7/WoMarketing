package com.engingplan.womarketing.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.engingplan.womarketing.common.ActivityStackManager;

/**
 * author : Android
 * github : https://github.com/renw7/AndroidProject
 * time   : 2019/11/8
 * desc   : Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initActivity();
    }


    protected void initActivity() {
        ActivityStackManager.getInstance().onCreated(this);
    }


}
