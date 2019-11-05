package com.engingplan.womarketing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.engingplan.womarketing.ui.activity.R;
import com.engingplan.womarketing.ui.activity.TaskListActivity;


public class FragmentTask extends Fragment {
    private TextView tv;

    public static FragmentTask newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FragmentTask fragment = new FragmentTask();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activty_taskmain, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //4G业务对应任务页面
        ImageView imageFourg = view.findViewById(R.id.imageFourg);
        imageFourg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);

                it.putExtra("task_type","1");
                startActivity(it);
            }
        });

        //流包量业务对应任务页面
        ImageView imageFlow = view.findViewById(R.id.imageFlow);
        imageFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);

                it.putExtra("task_type","2");
                startActivity(it);
            }
        });

        //宽带套餐业务对应任务页面
        ImageView imageWideband = view.findViewById(R.id.imageWideband);
        imageWideband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);

                it.putExtra("task_type","3");
                startActivity(it);
            }
        });

        //融合套餐业务对应任务页面
        ImageView imageMix = view.findViewById(R.id.imageMix);
        imageMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);

                it.putExtra("task_type","4");
                startActivity(it);
            }
        });
    }
}
