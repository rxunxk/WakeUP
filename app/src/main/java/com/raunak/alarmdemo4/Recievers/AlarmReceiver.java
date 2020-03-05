package com.raunak.alarmdemo4.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raunak.alarmdemo4.Activities.GameChaseTheBall;
import com.raunak.alarmdemo4.Activities.ScreenShake;
import com.raunak.alarmdemo4.Activities.SnoozeActivity;

public class AlarmReceiver extends BroadcastReceiver {
    String mode;

    @Override
    public void onReceive(Context context, Intent intent) {
        mode = intent.getStringExtra("mode");

        Log.d("Mode : ",""+mode);

        if (mode.equals("quick")){
            Intent intentQuick = new Intent(context, SnoozeActivity.class);
            intentQuick.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentQuick);
        }else if(mode.equals("E")){
            Intent intentSmart = new Intent(context, ScreenShake.class);
            intentSmart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentSmart);
        }else if (mode.equals("R")){
            Intent gameIntent = new Intent(context, GameChaseTheBall.class);
            gameIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(gameIntent);
        }else if (mode.equals("D")){

        }
    }
}
