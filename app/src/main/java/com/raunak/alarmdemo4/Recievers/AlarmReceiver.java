package com.raunak.alarmdemo4.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.raunak.alarmdemo4.Activities.SnoozeActivity;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context, SnoozeActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
