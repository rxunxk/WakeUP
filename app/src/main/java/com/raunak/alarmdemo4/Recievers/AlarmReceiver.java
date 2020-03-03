package com.raunak.alarmdemo4.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.raunak.alarmdemo4.Activities.ScreenShake;
import com.raunak.alarmdemo4.Activities.SnoozeActivity;

public class AlarmReceiver extends BroadcastReceiver {
    String mode;

    @Override
    public void onReceive(Context context, Intent intent) {
        mode = intent.getStringExtra("mode");

        if (mode.equals("quick")){
            Intent intentQuick = new Intent(context, SnoozeActivity.class);
            intentQuick.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentQuick);
        }else {
            Intent intentSmart = new Intent(context, ScreenShake.class);
            intentSmart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentSmart);
        }
    }
}
