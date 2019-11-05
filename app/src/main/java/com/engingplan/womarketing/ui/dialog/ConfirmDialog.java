package com.engingplan.womarketing.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.engingplan.womarketing.ui.R;

public class ConfirmDialog extends Dialog {

    private Context context;
    private String title;
    private String confirmButtonText;
    private String cacelButtonText;
    private ClickListenerInterface clickListenerInterface;
    private EditText oldPwd;
    private EditText newPwd;
    private EditText newPwdC;



    public interface ClickListenerInterface {

        public void doConfirm(String oldpwd, String newpwd, String newpwdc);

        public void doCancel();
    }

    public ConfirmDialog(Context context, String title, String confirmButtonText, String cacelButtonText) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.title = title;
        this.confirmButtonText = confirmButtonText;
        this.cacelButtonText = cacelButtonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView= inflater.inflate(R.layout.activity_person_dialog, null);
        setContentView(dialogView);

        TextView tvTitle = (TextView) dialogView.findViewById(R.id.title);
        TextView tvConfirm = (TextView) dialogView.findViewById(R.id.confirm);
        TextView tvCancel = (TextView) dialogView.findViewById(R.id.cancel);
        oldPwd = dialogView.findViewById(R.id.old_password);
        newPwd = dialogView.findViewById(R.id.new_password);
        newPwdC = dialogView.findViewById(R.id.new_password_confirm);


        tvTitle.setText(title);
        tvConfirm.setText(confirmButtonText);
        tvCancel.setText(cacelButtonText);

        tvConfirm.setOnClickListener(new clickListener());
        tvCancel.setOnClickListener(new clickListener());

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.height=(int) (d.widthPixels * 0.7);
        dialogWindow.setAttributes(lp);
    }

    public void setClicklistener(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
            case R.id.confirm:
                String oldpwd = oldPwd.getText().toString();
                String newpwd = newPwd.getText().toString();
                String newpwdc = newPwdC.getText().toString();

                Toast.makeText(context, oldpwd, Toast.LENGTH_SHORT).show();
                clickListenerInterface.doConfirm(oldpwd,newpwd,newpwdc);

                dismiss();
                break;
            case R.id.cancel:
                clickListenerInterface.doCancel();
                dismiss();
                break;
            }
        }
    }

}