package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.R;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class GameChaseTheBall extends AppCompatActivity {

    private FrameLayout gameFrame;
    private int frameHeight, frameWidth,initialFrameWidth;
    private LinearLayout startLayout;
    private ImageView box,black,orange,pink;
    private Drawable imageBoxRight, imageBoxLeft;
    private int boxSize;
    private float boxX,boxY;
    private float blackX,blackY;
    private float orangeX,orangeY;
    private float pinkX,pinkY;
    private TextView scoreLabel, highScoreLabel;
    private int score,highScore,timeCount;
    private SharedPreferences settings;
    private Timer timer;
    private Handler handler = new Handler();
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean pink_flg = false;
    private AlarmsDBhelperClass mBhelperClass;
    private SQLiteDatabase db;
    private MediaPlayer mp;
    private String songPath;
    private int hour,min;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_chase_the_ball);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        box = findViewById(R.id.box);
        black = findViewById(R.id.black);
        orange = findViewById(R.id.orange);
        pink = findViewById(R.id.pink);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);
        imageBoxLeft = getResources().getDrawable(R.drawable.box_left);
        imageBoxRight = getResources().getDrawable(R.drawable.box_right);

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
        Log.d("",""+songPath);
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

    }

    public void changePos(){

        //Add TimeCount
        timeCount += 20;

        //Orange
        orangeY +=12;

        float orangeCenterX = orangeX + orange.getWidth()/2;
        float orangeCenterY = orangeY + orange.getHeight()/2;

        if (hitCheck(orangeCenterX,orangeCenterY)){
            orangeY = frameHeight + 100;
            score += 10;
        }

        if (orangeY > frameHeight){
            orangeY = -100;
            orangeX = (float) Math.floor(Math.random() * (frameWidth - orange.getWidth()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //Pink
        if (!pink_flg && timeCount % 10000 == 0){
            pink_flg = true;
            pinkY = 20;
            pinkX = (float) Math.floor(Math.random() * (frameWidth - pink.getWidth()));
        }
        if (pink_flg){
            pinkY += 20;
            float pinkCenterX = pinkX + pink.getWidth() / 2;
            float pinkCenterY = pinkY + pink.getHeight() / 2;

            if (hitCheck(pinkCenterX,pinkCenterY)){
                pinkY = frameHeight + 30;
                score += 30;
                //Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100){
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
            }
            if (pinkY > frameHeight) pink_flg = false;
            pink.setX(pinkX);
            pink.setY(pinkY);
        }

        //Black
        blackY += 18;
        float blackCenterX = blackX + black.getWidth() / 2;
        float blackCenterY = blackY + black.getHeight() / 2;

        if (hitCheck(blackCenterX,blackCenterY)){
            blackY = frameHeight + 100;

            //Change Frame Width
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);

            if (frameWidth <= boxSize){
                //Game Over, Restart
                gameOver();
            }
        }
        if(blackY > frameHeight){
            blackY = -100;
            blackX = (float) Math.floor(Math.random() * (frameWidth - black.getWidth()));
        }

        black.setX(blackX);
        black.setY(blackY);

        //Move Box
        if(action_flg){
            //Touching
            boxX +=12;
            box.setImageDrawable(imageBoxRight);
        }else{
            //Releasing
            boxX += -14;
            box.setImageDrawable(imageBoxLeft);
        }

        //CheckBox position
        if(boxX < 0){
            boxX = 0;
            box.setImageDrawable(imageBoxRight);
        }
        if (frameWidth - boxSize < boxX){
            boxX = frameWidth - boxSize;
            box.setImageDrawable(imageBoxLeft);
        }

        box.setX(boxX);
        scoreLabel.setText("Score :"+score);
    }

    public boolean hitCheck(float x, float y){
        if (boxX <= x && x <= boxX + boxSize &&
                boxY <= y && y <= frameHeight){
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameW){
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameW;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver(){
        //Stop Timer
        timer.cancel();
        timer = null;
        changeFrameWidth(initialFrameWidth);
        startGame(getCurrentFocus());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg){
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;
            }else if (event.getAction() == MotionEvent.ACTION_UP){
                action_flg = false;
            }
        }
        if (score > 300){
            mp.stop();
            finish();
        }
        return true;
    }

    public void startGame(View view){
        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);

        if (frameHeight == 0){
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();

            initialFrameWidth = frameWidth;
            boxSize = box.getHeight();
            boxX = box.getX();
            boxY = box.getY();
        }

        frameWidth = initialFrameWidth;

        box.setX(0.0f);
        black.setY(3000.0f);
        orange.setY(3000.0f);
        pink.setY(3000.0f);

        blackY = black.getY();
        orangeY = orange.getY();
        pinkY = pink.getY();

        box.setVisibility(View.VISIBLE);
        black.setVisibility(View.VISIBLE);
        orange.setVisibility(View.VISIBLE);
        pink.setVisibility(View.VISIBLE);

        timeCount = 0;
        score =0;
        scoreLabel.setText("SCORE : 0");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        },0,20);

    }
}
