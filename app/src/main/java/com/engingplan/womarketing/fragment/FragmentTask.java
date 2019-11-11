package com.engingplan.womarketing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.engingplan.womarketing.ui.R;
import com.engingplan.womarketing.ui.activity.TaskListActivity;
import com.engingplan.womarketing.util.ConstantsUtil;


public class FragmentTask extends Fragment {
    private int staffId;

    public static FragmentTask newInstance(String name) {
        Log.i(ConstantsUtil.LOG_TAG_FRAGMENT, "实例化framgment" + name);
        FragmentTask fragment = new FragmentTask();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_taskmain, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //获取staffId，给拨打功能页面传参
        Bundle bundle = getActivity().getIntent().getExtras();
        staffId = bundle.getInt("staffId");
        Log.i(ConstantsUtil.LOG_TAG_FRAGMENT, "获得staffId=" + staffId);

        //4G业务对应任务页面
        ImageView imageFourg = view.findViewById(R.id.imageFourg);
        imageFourg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);
                it.putExtra("staffId", staffId);
                it.putExtra("taskType", ConstantsUtil.TASK_TYPE_4G);
                startActivity(it);
            }
        });

        //流包量业务对应任务页面
        ImageView imageFlow = view.findViewById(R.id.imageFlow);
        imageFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);
                it.putExtra("staffId", staffId);
                it.putExtra("taskType", ConstantsUtil.TASK_TYPE_FLOW);
                startActivity(it);
            }
        });

        //宽带套餐业务对应任务页面
        ImageView imageWideband = view.findViewById(R.id.imageWideband);
        imageWideband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);
                it.putExtra("staffId", staffId);
                it.putExtra("taskType", ConstantsUtil.TASK_TYPE_BROAD);
                startActivity(it);
            }
        });

        //融合套餐业务对应任务页面
        ImageView imageMix = view.findViewById(R.id.imageMix);
        imageMix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getContext(), TaskListActivity.class);
                it.putExtra("staffId", staffId);
                it.putExtra("taskType", ConstantsUtil.TASK_TYPE_MIX);
                startActivity(it);
            }
        });
    }
}
