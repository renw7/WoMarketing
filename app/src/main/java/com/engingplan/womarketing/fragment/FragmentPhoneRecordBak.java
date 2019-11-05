package com.engingplan.womarketing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.engingplan.womarketing.ui.R;


public class FragmentPhoneRecordBak extends Fragment {
    private TextView tv;

    public static FragmentPhoneRecordBak newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FragmentPhoneRecordBak fragment = new FragmentPhoneRecordBak();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragmentphonerecord, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = (TextView) view.findViewById(R.id.textView2);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.get("name").toString();
            tv.setText(name);
        }
    }
}
