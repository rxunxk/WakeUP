package com.raunak.alarmdemo4.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.HelperClasses.DelayGenerator;
import com.raunak.alarmdemo4.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    private final int GOAL = 10;
    private int score = 0;
    int delayInterval =2;
    Button btnOptionA,btnOptionB,btnOptionC,btnOptionD;
    TextView txtQuestion;
    AlarmsDBhelperClass DBClass;
    SQLiteDatabase db;
    ArrayList<String> optionA,optionB,optionC,optionD,question,answerArrayList;
    MediaPlayer mp;
    String songPath,answer;
    int hour,min;
    Random random;
    int correctColor = Color.parseColor("#03a685");
    int wrongColor = Color.parseColor("#ff9191");

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnOptionA = findViewById(R.id.btnOptionA);
        btnOptionB = findViewById(R.id.btnOptionB);
        btnOptionC = findViewById(R.id.btnOptionC);
        btnOptionD = findViewById(R.id.btnOptionD);
        txtQuestion = findViewById(R.id.txtQuiz);
        optionA = new ArrayList<>();
        optionB = new ArrayList<>();
        optionC = new ArrayList<>();
        optionD = new ArrayList<>();
        question = new ArrayList<>();
        answerArrayList = new ArrayList<>();
        random = new Random();
        final Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        DBClass = new AlarmsDBhelperClass(this);
        db = DBClass.getWritableDatabase();
        getAlarm(db);
        setRandomQuestion();

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
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


        btnOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOptionA.getText().equals(answer)){
                    btnOptionA.setBackgroundResource(R.drawable.quiz_correct_background);
                    btnOptionA.setTextColor(correctColor);
                    score++;
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionA.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionA.setTextColor(Color.BLACK);
                        }
                    });
                }else{
                    btnOptionA.setBackgroundResource(R.drawable.quiz_wrong_background);
                    btnOptionA.setTextColor(wrongColor);
                    btnOptionA.startAnimation(shake);
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionA.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionA.setTextColor(Color.BLACK);
                        }
                    });
                }
                if (score == GOAL){
                    mp.stop();
                    Toast.makeText(Quiz.this, "Alarm Stopped!", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
                    db.update("alarms",values,""+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
                    finish();
                }
            }
        });

        btnOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOptionB.getText().equals(answer)){
                    btnOptionB.setBackgroundResource(R.drawable.quiz_correct_background);
                    btnOptionB.setTextColor(correctColor);
                    score++;
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionB.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionB.setTextColor(Color.BLACK);
                        }
                    });
                }else{
                    btnOptionB.setBackgroundResource(R.drawable.quiz_wrong_background);
                    btnOptionB.setTextColor(wrongColor);
                    btnOptionB.startAnimation(shake);
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionB.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionB.setTextColor(Color.BLACK);
                        }
                    });
                }
                if (score == GOAL){
                    mp.stop();
                    Toast.makeText(Quiz.this, "Alarm Stopped!", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
                    db.update("alarms",values,""+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
                    finish();
                }
            }
        });

        btnOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOptionC.getText().equals(answer)){
                    btnOptionC.setBackgroundResource(R.drawable.quiz_correct_background);
                    btnOptionC.setTextColor(correctColor);
                    score++;
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionC.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionC.setTextColor(Color.BLACK);
                        }
                    });
                }else{
                    btnOptionC.setBackgroundResource(R.drawable.quiz_wrong_background);
                    btnOptionC.setTextColor(wrongColor);
                    btnOptionC.startAnimation(shake);
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionC.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionC.setTextColor(Color.BLACK);
                        }
                    });
                }
                if (score == GOAL){
                    mp.stop();
                    Toast.makeText(Quiz.this, "Alarm Stopped!", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
                    db.update("alarms",values,""+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
                    finish();
                }
            }
        });

        btnOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnOptionD.getText().equals(answer)){
                    btnOptionD.setBackgroundResource(R.drawable.quiz_correct_background);
                    btnOptionD.setTextColor(correctColor);
                    score++;
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionD.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionD.setTextColor(Color.BLACK);
                        }
                    });
                }else{
                    btnOptionD.setBackgroundResource(R.drawable.quiz_wrong_background);
                    btnOptionD.setTextColor(wrongColor);
                    btnOptionD.setAnimation(shake);
                    DelayGenerator.delay(delayInterval, new DelayGenerator.DelayCallback() {
                        @Override
                        public void afterDelay() {
                            setRandomQuestion();
                            btnOptionD.setBackgroundResource(R.drawable.quiz_button_border);
                            btnOptionD.setTextColor(Color.BLACK);
                        }
                    });
                }
                if (score == GOAL){
                    mp.stop();
                    Toast.makeText(Quiz.this, "Alarm Stopped!", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
                    db.update("alarms",values,""+AlarmsDBhelperClass.ALARM_HOURS+"=? AND "+AlarmsDBhelperClass.ALARM_MINS+"=?",new String[]{""+hour,""+min});
                    finish();
                }
            }
        });
    }


    public void getAlarm(SQLiteDatabase db){
        Cursor c = db.rawQuery("SELECT * FROM "+AlarmsDBhelperClass.TABLE_QUESTION,new String[]{});
        if (c.moveToFirst()){
            do {
                question.add(c.getString(c.getColumnIndex(AlarmsDBhelperClass.QUESTION_QUESTION)));
                answerArrayList.add(c.getString(c.getColumnIndex(AlarmsDBhelperClass.QUESTION_ANSWER)));
                optionA.add(c.getString(c.getColumnIndex(AlarmsDBhelperClass.QUESTION_A)));
                optionB.add(c.getString(c.getColumnIndex(AlarmsDBhelperClass.QUESTION_B)));
                optionC.add(c.getString(c.getColumnIndex(AlarmsDBhelperClass.QUESTION_C)));
                optionD.add(c.getString(c.getColumnIndex(AlarmsDBhelperClass.QUESTION_D)));
            } while (c.moveToNext());
        }
        c.close();
    }

    public void setRandomQuestion(){
        int randomNumber = random.nextInt(50);
        txtQuestion.setText(question.get(randomNumber));
        btnOptionA.setText(optionA.get(randomNumber));
        btnOptionB.setText(optionB.get(randomNumber));
        btnOptionC.setText(optionC.get(randomNumber));
        btnOptionD.setText(optionD.get(randomNumber));
        answer = answerArrayList.get(randomNumber);
    }
}
