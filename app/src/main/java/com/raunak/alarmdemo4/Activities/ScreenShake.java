package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.HelperClasses.ShakeEventListener;
import com.raunak.alarmdemo4.R;

public class ScreenShake extends AppCompatActivity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    AlarmsDBhelperClass mBhelperClass;
    SQLiteDatabase db;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shake);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        getSupportActionBar().hide();
        mBhelperClass = new AlarmsDBhelperClass(this);
        db= mBhelperClass.getWritableDatabase();
        Intent intent = getIntent();
        final String mode = intent.getStringExtra("mode");
        Log.d("tggg",""+mode);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Ringtone r = RingtoneManager.getRingtone(this, notification);
        r.play();
        Toast.makeText(this, "Alarm Ringing", Toast.LENGTH_SHORT).show();

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                r.stop();
                ContentValues values = new ContentValues();
                values.put("status","OFF");
                db.update("alarms",values,"alarm_mode=?",new String[]{mode});
                Toast.makeText(getApplicationContext(),"Alarm Stopped!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
