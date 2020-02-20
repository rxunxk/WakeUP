package com.raunak.alarmdemo4.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raunak.alarmdemo4.Activities.ScreenShake;
import com.raunak.alarmdemo4.Activities.SnoozeActivity;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    String mode;

    @Override
    public void onReceive(Context context, Intent intent) {
        mode = intent.getStringExtra("mode");
        if (mode.equals("quick")){
            Intent intent1 = new Intent(context, SnoozeActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }else {
            Intent intent2 = new Intent(context, ScreenShake.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }
}
