package com.raunak.alarmdemo4.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.raunak.alarmdemo4.Fragments.HomeFragment;
import com.raunak.alarmdemo4.Fragments.ModeDialog;
import com.raunak.alarmdemo4.Fragments.RepeatDialogFragment;
import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;

public class AddAlarm extends AppCompatActivity implements RepeatDialogFragment.onMultiChoiceListener, ModeDialog.radioClickListener {
    SQLiteDatabase db;
    AlarmsDBhelperClass helper;
    NumberPicker hourPicker;
    NumberPicker minutePicker;
    Button btnRepeat,btnTone;
    Button btnMode;
    ArrayList<String> repeatDays, nameArrayList;
    EditText edtLabel;
    Button btnSave;
    //variables for storing values in the database
    String name, mode, repeat,songName;
    int hours, mins;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        helper = new AlarmsDBhelperClass(this);
        db = helper.getReadableDatabase();
        hourPicker = findViewById(R.id.hoursPicker);
        minutePicker = findViewById(R.id.minutesPicker);
        btnRepeat = findViewById(R.id.repeat);
        btnMode = findViewById(R.id.mode);
        edtLabel = findViewById(R.id.edtLabel);
        btnTone = findViewById(R.id.btnTone);
        btnSave = findViewById(R.id.btnSave);
        hourPicker.setMaxValue(23);
        hourPicker.setMinValue(0);
        nameArrayList = new ArrayList<>();

        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        Cursor cursor = db.rawQuery("SELECT alarm_name FROM alarms", new String[]{});
        if (cursor.moveToFirst()) {
            do {
                nameArrayList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        edtLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edtLabel.getText().toString().length() <= 14) {
                    btnSave.setEnabled(true);
                } else {
                    edtLabel.setError("Alarm name should be less than 14 characters!");
                    btnSave.setEnabled(false);
                }
                if (!nameArrayList.isEmpty()) {
                    Log.d("tage",""+nameArrayList.size());
                    if (nameArrayList.contains(edtLabel.getText().toString())) {
                        edtLabel.setError("An alarm is created with a same name");
                        btnSave.setEnabled(false);
                    }
                    else{
                        btnSave.setEnabled(true);
                    }
                }
            }
        });

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment multiChoiceDialog = new RepeatDialogFragment();
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "Repeat days Dialog");
            }
        });

        btnMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment radioChoice = new ModeDialog();
                radioChoice.setCancelable(false);
                radioChoice.show(getSupportFragmentManager(), "Modes Dialog");
            }
        });

        btnTone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RingtoneSelector.class);
                startActivityForResult(intent,3);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                name = edtLabel.getText().toString();
                for (String s : repeatDays) {
                    sb.append(s);
                    sb.append(" ");
                }
                repeat = String.valueOf(sb);
                hours = hourPicker.getValue();
                mins = minutePicker.getValue();
                helper.insertAlarm(name, mode, repeat,songName, hours, mins,"ON",0, db);
                HomeFragment.SONG_NAME = songName;
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                songName = data.getStringExtra("SongName");
            }
        }
    }

    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedDaysList) {
        repeatDays = selectedDaysList;
    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onPositiveClick(String[] list, int position) {
        if (position == 0) {
            mode = "E";
        } else if (position == 1) {
            mode = "R";
        } else {
            mode = "D";
        }
    }

    @Override
    public void onNegativeClick() {
    }

    public void hideKeyboard(View view){
        InputMethodManager iim = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        iim.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}