package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.arbelkilani.clock.Clock;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.R;
import java.io.IOException;
import java.util.Calendar;
import static com.raunak.alarmdemo4.R.*;

public class SnoozeActivity extends AppCompatActivity{

    AlarmsDBhelperClass mBhelperClass;
    SQLiteDatabase db;
    SwipeButton btnSwipe;
    Clock mClock;
    int hour,min;
    MediaPlayer mp;
    String songPath;
    private View decorView;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_snooze);
        //Making the activity go full screen.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        btnSwipe = findViewById(id.btnSwipe);
        mClock = findViewById(id.clock);
        mBhelperClass = new AlarmsDBhelperClass(this);
        db= mBhelperClass.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+AlarmsDBhelperClass.MUSIC_PATH+" FROM alarms WHERE "+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
        if (cursor.moveToFirst()) {
            songPath = cursor.getString(cursor.getColumnIndex(AlarmsDBhelperClass.MUSIC_PATH));
        }
        cursor.close();
        if(songPath.equals("song1")){
            mp = MediaPlayer.create(this, R.raw.alarm_tone1);
            mp.start();
        }else if (songPath.equals("song2")){
            mp = MediaPlayer.create(this,R.raw.alarm_tone2);
            mp.start();
        }else{
            try {
                mp = new MediaPlayer();
                mp.setDataSource(songPath);
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
        }

        btnSwipe.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                mp.stop();
                Toast.makeText(getApplicationContext(), "Alarm Stopped", Toast.LENGTH_SHORT).show();
                ContentValues values = new ContentValues();
                values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
                db.update("alarms",values,""+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
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

    //Now overriding the physical buttons so the user can't escape XD.
    //Recent Button
    @Override
    protected void onPause() {
        super.onPause();
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(getTaskId(), 0);
    }
    //Back Button
    @Override
    public void onBackPressed() {
    }

    //Volume buttons
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            mp.setVolume(1.0f,1.0f);
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}