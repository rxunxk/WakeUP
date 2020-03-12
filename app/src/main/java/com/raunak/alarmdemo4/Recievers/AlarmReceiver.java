package com.raunak.alarmdemo4.Recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.raunak.alarmdemo4.Activities.GameChaseTheBall;
import com.raunak.alarmdemo4.Activities.Quiz;
import com.raunak.alarmdemo4.Activities.ScreenShake;
import com.raunak.alarmdemo4.Activities.SnoozeActivity;

public class AlarmReceiver extends BroadcastReceiver {
    String mode;

    @Override
    public void onReceive(Context context, Intent intent) {
        mode = intent.getStringExtra("mode");
        Log.d("OK","Alarm receiver called, mode is :"+mode);
        Log.d("Mode : ",""+mode);

        switch (mode) {
            case "Q":
                Log.d("", "quick Selected");
                Intent intentQuick = new Intent(context, SnoozeActivity.class);
                intentQuick.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentQuick);
                break;
            case "E":
                Log.d("", "Easy Selected");
                Intent intentSmart = new Intent(context, ScreenShake.class);
                intentSmart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentSmart);
                break;
            case "R":
                Log.d("", "Regular selected");
                Intent gameIntent = new Intent(context, GameChaseTheBall.class);
                gameIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(gameIntent);
                break;
            case "D":
                Intent quizIntent = new Intent(context, Quiz.class);
                quizIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(quizIntent);
                break;
        }
    }
}
