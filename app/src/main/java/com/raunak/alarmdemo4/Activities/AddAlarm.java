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
import com.nex3z.togglebuttongroup.MultiSelectToggleGroup;
import com.raunak.alarmdemo4.Fragments.ModeDialog;
import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.R;
import java.util.ArrayList;
import java.util.Set;

public class AddAlarm extends AppCompatActivity implements ModeDialog.radioClickListener {
    SQLiteDatabase db;
    AlarmsDBhelperClass helper;
    NumberPicker hourPicker,minutePicker,timeZonePicker;
    Button btnRepeat,btnTone;
    Button btnMode;
    ArrayList<String> repeatDays, nameArrayList;
    MultiSelectToggleGroup multi;
    EditText edtLabel;
    Button btnSave;
    //variables for storing values in the database
    String name, mode,songName;
    int hours, mins;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        helper = new AlarmsDBhelperClass(this);
        db = helper.getReadableDatabase();
        hourPicker = findViewById(R.id.hoursPicker);
        minutePicker = findViewById(R.id.minutesPicker);
        timeZonePicker = findViewById(R.id.timeZonePicker);
        multi = findViewById(R.id.repeat);
        btnMode = findViewById(R.id.mode);
        edtLabel = findViewById(R.id.edtLabel);
        btnTone = findViewById(R.id.btnTone);
        btnSave = findViewById(R.id.btnSave);
        nameArrayList = new ArrayList<>();

        hourPicker.setMaxValue(12);
        hourPicker.setMinValue(0);

        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);

        timeZonePicker.setMaxValue(2);
        timeZonePicker.setMinValue(1);
        timeZonePicker.setDisplayedValues(new String[]{"AM","PM"});

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

        /*multi.setOnCheckedChangeListener(new MultiSelectToggleGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedStateChanged(MultiSelectToggleGroup group, int checkedId, boolean isChecked) {
                Log.d("selected",""+ Arrays.toString(checkedIDArray));
            }
        });*/

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtLabel.getText().toString();
                hours = hourPicker.getValue();
                mins = minutePicker.getValue();

                if (timeZonePicker.getValue() == 1){
                    helper.insertAlarm(name, mode, getRepeat(),songName, hours, mins,"ON","", db);
                }else{
                    hours += 12;
                    helper.insertAlarm(name, mode, getRepeat(),songName, hours, mins,"ON","", db);
                }
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

    public String getRepeat(){
        StringBuilder sb = new StringBuilder();
        Set<Integer> set = multi.getCheckedIds();
        Integer[] checkedIDArray = set.toArray(new Integer[set.size()]);
        for (int i = 0 ; i < checkedIDArray.length ; i++ ){
            switch(checkedIDArray[i]){
                case 2131362109:
                    sb.append("SUN ");
                    break;
                case 2131361988:
                    sb.append("MON ");
                    break;
                case 2131362148:
                    sb.append("TUE ");
                    break;
                case 2131362173:
                    sb.append("WED ");
                    break;
                case 2131362133:
                    sb.append("THU ");
                    break;
                case 2131361945:
                    sb.append("FRI ");
                    break;
                case 2131362059:
                    sb.append("SAT ");
                    break;
                default:
                    break;
            }
        }
        String s = sb.toString();
        return s;
    }
}