package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.HelperClasses.ShakeEventListener;
import com.raunak.alarmdemo4.R;

import java.io.IOException;
import java.util.Calendar;

public class ScreenShake extends AppCompatActivity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    AlarmsDBhelperClass mBhelperClass;
    SQLiteDatabase db;
    int hour,min;
    MediaPlayer mp;
    String songPath;
    private View decorView;

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
        //Hiding System Navigation & Status bars
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if (visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });
        //to show activity on lockScreen.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        getSupportActionBar().hide();
        mBhelperClass = new AlarmsDBhelperClass(this);
        db= mBhelperClass.getWritableDatabase();
        Intent intent = getIntent();
        final String mode = intent.getStringExtra("mode");
        Log.d("tggg",""+mode);


        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        mBhelperClass = new AlarmsDBhelperClass(this);
        db= mBhelperClass.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+AlarmsDBhelperClass.MUSIC_PATH+" FROM alarms WHERE "+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
        if (cursor.moveToFirst()) {
            songPath = cursor.getString(cursor.getColumnIndex(AlarmsDBhelperClass.MUSIC_PATH));
        }else{
            Log.d("not working!","not working!");
        }
        cursor.close();
        if (songPath.equals("song1")){
            mp = MediaPlayer.create(this,R.raw.alarm_tone1);
            mp.start();
        }else if (songPath.equals("song2")){
            mp = MediaPlayer.create(this,R.raw.alarm_tone2);
            mp.start();
        }else {
            try {
                mp = new MediaPlayer();
                mp.setDataSource(songPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        }
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            @Override
            public void onShake() {
                mp.stop();
                ContentValues values = new ContentValues();
                values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
                db.update("alarms",values,""+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
                Toast.makeText(getApplicationContext(),"Alarm Stopped!",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars(){
        return (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
