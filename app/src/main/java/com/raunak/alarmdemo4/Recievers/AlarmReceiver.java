package com.raunak.alarmdemo4.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.raunak.alarmdemo4.Activities.ScreenShake;
import com.raunak.alarmdemo4.Activities.SnoozeActivity;
import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {
    String type,name,mode,repeat,hour,min;

    @Override
    public void onReceive(Context context, Intent intent) {
        type = intent.getStringExtra("type");
        name = intent.getStringExtra("name");
        mode = intent.getStringExtra("mode");
        hour = intent.getStringExtra("hour");
        min = intent.getStringExtra("min");
        repeat = intent.getStringExtra("repeat");
        Log.d("tgg",""+name);
        if (Objects.equals(type, "quick")){
            Intent intent1 = new Intent(context, SnoozeActivity.class);
            intent1.putExtra("hour",hour);
            intent1.putExtra("min",min);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }else {
            Intent intent2 = new Intent(context, ScreenShake.class);
            intent2.putExtra("name",name);
            intent2.putExtra("repeat",repeat);
            intent2.putExtra("mode",mode);
            intent2.putExtra("hour",hour);
            intent2.putExtra("min",min);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent2);
        }
    }
}
