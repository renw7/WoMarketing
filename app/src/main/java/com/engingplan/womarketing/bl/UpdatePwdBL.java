package com.engingplan.womarketing.bl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.engingplan.womarketing.util.ConstantsUtil;
import com.engingplan.womarketing.util.OkHttpClientUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class UpdatePwdBL {

    private String result;

    public void updateStaffPwd(Map param, Context context){
        OkHttpClientUtils.getInstance().doPostAsyn(ConstantsUtil.URL_UPDATE_PWD, param, new OkHttpClientUtils.NetWorkCallBack(){

            @Override
            public void onSuccess(String response) {
                try {
                    //JSON解码
                    JSONObject jsonObject1 = new JSONObject(response);
                    //取应答体中code  200成功否则失败
                    String code = jsonObject1.getString("code");
                    if ("200".equals(code)) {
                        result = "密码修改成功！！";
                    } else {
                        result = "密码修改失败！！";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    Log.i(ConstantsUtil.LOG_TAG_BL, "发送广播");
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("RESULT", result);
                    intent.putExtras(bundle); //向广播接收器传递数据
                    intent.setAction(ConstantsUtil.PERSON_INFO_RECEIVER);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                }


            }

            @Override
            public void onFail(String str) {
                result = "请求失败！！";
                //下面通过异常方式返回给ui层
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("RESULT", result);
                intent.putExtras(bundle); //向广播接收器传递数据
                intent.setAction(ConstantsUtil.PERSON_INFO_RECEIVER);
                context.sendBroadcast(intent);

            }
        });
    }
}
