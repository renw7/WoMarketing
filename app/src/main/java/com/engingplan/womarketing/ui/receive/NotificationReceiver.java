package com.engingplan.womarketing.ui.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.engingplan.womarketing.ui.activity.HomeActivity;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent appIntent = new Intent(context, HomeActivity.class);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(appIntent);
    }

}
