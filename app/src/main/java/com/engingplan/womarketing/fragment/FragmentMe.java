package com.engingplan.womarketing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.engingplan.womarketing.ui.activity.R;
import com.engingplan.womarketing.ui.dialog.ConfirmDialog;

import java.util.Map;


public class FragmentMe extends Fragment {
    private TextView tv;

    public static FragmentMe newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name", name);
        FragmentMe fragment = new FragmentMe();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_person, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FrameLayout button = view.findViewById(R.id.fl_person_data_changepwd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfirmDialog confirmDialog = new ConfirmDialog(getContext(), "修改密码", "确定", "取消");

                confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                    @Override
                    public void doConfirm(String oldpwd,String newpwd,String newpwdc) {


                    }
                    @Override
                    public void doCancel() {
                        Toast.makeText(getContext(), "取消修改!", Toast.LENGTH_SHORT).show();
                    }
                });
                confirmDialog.show();


            }
        });

    }
}
