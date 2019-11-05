package com.engingplan.womarketing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.engingplan.womarketing.ui.activity.R;


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
        View view = inflater.inflate(R.layout.activity_fragmenttask, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = (TextView) view.findViewById(R.id.textView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            tv.setText(name);
        }
    }
}
