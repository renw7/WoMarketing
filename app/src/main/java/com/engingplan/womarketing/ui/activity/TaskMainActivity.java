package com.engingplan.womarketing.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TaskMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_taskmain);
        //4G业务对应任务页面
        ImageView imageFourg =findViewById(R.id.imageFourg);
        imageFourg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TaskMainActivity.this, TaskListActivity.class);

                it.putExtra("task_type","1");
                startActivity(it);
            }
        });

        //流包量业务对应任务页面
        ImageView imageFlow =findViewById(R.id.imageFlow);
        imageFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TaskMainActivity.this, TaskListActivity.class);

                it.putExtra("task_type","2");
                startActivity(it);
            }
        });

        //宽带套餐业务对应任务页面
        ImageView imageWideband =findViewById(R.id.imageWideband);
        imageWideband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TaskMainActivity.this, TaskListActivity.class);

                it.putExtra("task_type","3");
                startActivity(it);
            }
        });

        //融合套餐业务对应任务页面
        ImageView imageMix =findViewById(R.id.imageMix);
        imageMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(TaskMainActivity.this, TaskListActivity.class);

                it.putExtra("task_type","4");
                startActivity(it);
            }
        });

    }
}
