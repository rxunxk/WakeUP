package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import com.arbelkilani.clock.Clock;
import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import java.io.IOException;
import java.util.Calendar;
import static com.raunak.alarmdemo4.R.*;

public class SnoozeActivity extends AppCompatActivity{

    AlarmsDBhelperClass mBhelperClass;
    SQLiteDatabase db;
    SwipeButton btnSwipe;
    Clock mClock;
    int hour,min;
    int flag =0;
    MediaPlayer mp = new MediaPlayer();
    MediaPlayer ring = new MediaPlayer();
    MediaPlayer ring2 = new MediaPlayer();
    String songPath;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_snooze);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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
        }else{
            Log.d("not working!","not working!");
        }
        cursor.close();
        if(songPath.equals("Default song 1")){
            flag = 1;
            ring = MediaPlayer.create(this,raw.alarm_tone1);
            ring.start();
        }else if (songPath.equals("Default song 2")){
            flag = 2;
            ring2 = MediaPlayer.create(this,raw.alarm_tone2);
            ring2.start();
        }else{
            flag = 3;
            try {
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

        btnSwipe.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if(flag ==1){
                    ring.stop();
                }else if(flag == 2){
                    ring2.stop();
                }else{
                    mp.stop();
                }
                Toast.makeText(getApplicationContext(), "Alarm Stopped", Toast.LENGTH_SHORT).show();
                ContentValues values = new ContentValues();
                values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
                db.update("alarms",values,""+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
                finish();
            }
        });
    }
}
