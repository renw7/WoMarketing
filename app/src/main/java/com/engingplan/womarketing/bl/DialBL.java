package com.engingplan.womarketing.bl;


import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *    author : Android
 *    github : https://github.com/renw7/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 通话业务层
 */
public class DialBL {
    //通话功能
    public Intent call(String phoneno){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:"+phoneno);
        intent.setData(data);
        return intent;
    }

    //获取时间
    /*
    public String time(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        // 获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    */
    
    //发送短信
    public void sendMessage(String phoneNo, String message, PendingIntent sentPI){
        SmsManager smsManager = SmsManager.getDefault();
        if (message.length()<=70) {
            smsManager.sendTextMessage(phoneNo, null, message, sentPI, null);
        }else{
            List<String> message1 = smsManager.divideMessage(message);
            for(String dividemessage: message1) {
                smsManager.sendTextMessage(phoneNo,null, dividemessage,sentPI,null);
            }
        }
    }
}