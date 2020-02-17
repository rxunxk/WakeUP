package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.raunak.alarmdemo4.R;

public class SnoozeActivity extends AppCompatActivity {

    SwipeButton btnSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnSwipe = findViewById(R.id.btnSwipe);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();
        Toast.makeText(this, "Alarm Ringing", Toast.LENGTH_SHORT).show();

        btnSwipe.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                r.stop();
                Toast.makeText(getApplicationContext(), "Alarm Stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
