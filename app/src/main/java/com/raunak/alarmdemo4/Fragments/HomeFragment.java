package com.raunak.alarmdemo4.Fragments;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raunak.alarmdemo4.Activities.AddAlarm;
import com.raunak.alarmdemo4.Activities.RingtoneSelector;
import com.raunak.alarmdemo4.Adapters.AlarmAdapter;
import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.Interfaces.AlarmRecyclerViewListener;
import com.raunak.alarmdemo4.R;
import com.raunak.alarmdemo4.Recievers.AlarmReceiver;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Logger;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements AlarmRecyclerViewListener,TimePickerDialog.OnTimeSetListener{

    public static String SONG_NAME;
    private static final int SYSTEM_ALERT_WINDOW_CODE = 100;
    private final int FRAGMENT_HOME_REQUEST_CODE =1;
    private FloatingActionButton mAlarmAddButton,fab_button_1,fab_button_2,fab_button_3;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    private ImageView emptyImageView;
    private TextView txtEmpty;
    private AlarmsDBhelperClass mAlarmsDBhelperClass;
    private ArrayList<Integer> requestCodes = new ArrayList<>();
    private ArrayList<String> nameArrayList = new ArrayList<>();
    private ArrayList <String> modeArrayList = new ArrayList<>();
    private ArrayList<String> repeatArrayList = new ArrayList<>();
    private ArrayList<String> hoursArrayList = new ArrayList<>();
    private ArrayList<String> minArrayList = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();
    private AlarmAdapter alarmAdapter = new AlarmAdapter(hoursArrayList,minArrayList,modeArrayList,repeatArrayList,nameArrayList,status,this);
    private final int ADD_ALARM_REQUEST_CODE =2;
    private OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    private int fabTranslationY = 100;
    private boolean isFabMenuOpen = false;
    private int speed = 250;
    private int quickHour;
    private int quickMin;
    private String songPath;
    private DialogFragment timePicker;
    private Calendar c1 = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*checkPermission(Manifest.permission.SYSTEM_ALERT_WINDOW,SYSTEM_ALERT_WINDOW_CODE);*/
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.SYSTEM_ALERT_WINDOW},SYSTEM_ALERT_WINDOW_CODE);
            Toast.makeText(getContext(),"OK",Toast.LENGTH_SHORT).show();
        }

        //Initializing RecyclerView, DatabaseHelperClass, FAB button, The ON OFF switch & the empty ImageView
        mAlarmsDBhelperClass = new AlarmsDBhelperClass(getContext());
        mAlarmAddButton = getView().findViewById(R.id.btnAlarmADD);
        fab_button_1 = getView().findViewById(R.id.fab_button1);
        fab_button_2 = getView().findViewById(R.id.fab_button2);
        recyclerView = getView().findViewById(R.id.alarmList);
        emptyImageView = getView().findViewById(R.id.empty_view);
        SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        txtEmpty = getView().findViewById(R.id.txtEmpty);
        timePicker = new TimePickerFragment(this);

        setFabTranslationY();
        //fab event handling
        fab_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
            }
        });
        fab_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
                timePicker.show(getChildFragmentManager(),null);
                Log.d("good",""+songPath);
                Log.d("OKAY","timePicker executed");
            }
        });
        mAlarmAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuCheck();
                Intent mIntent = new Intent(getContext(), AddAlarm.class);
                startActivityForResult(mIntent, ADD_ALARM_REQUEST_CODE);
            }
        });
        //DividerItemDecoration class is used for getting a vertical line between rows of RecyclerView
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        //Getting a writable reference of the Database.
        db = mAlarmsDBhelperClass.getWritableDatabase();

        //Retrieving values from the database and storing them in custom ArrayLists
        boolean isDataEmpty = getAlarm(db);

        //SwipeRefreshLayout Initialization
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alarmAdapter.notifyDataSetChanged();
            }
        });

        //Checking if our arrayList is empty? if yes then display some empty list text or an image
        if (!isDataEmpty){
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
        }
        else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.GONE);
        }
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (fab_button_1.isShown()) {
                        fab_button_1.hide();
                        /*fab_button_2.hide();
                        mAlarmAddButton.hide();*/
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fab_button_1.isShown()) {
                        fab_button_1.show();
                        /*fab_button_2.show();
                        mAlarmAddButton.show();*/
                    }
                }
            }
        });

        //Warping up with the recyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(alarmAdapter);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FRAGMENT_HOME_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                songPath = data.getStringExtra("SongName");
                mAlarmsDBhelperClass.insertAlarm("","Q","",songPath,quickHour,quickMin,"ON","",db);
                startAlarm(c1,0,true,quickHour+quickMin+1);
                alarmAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    public boolean getAlarm(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM alarms", new String[]{});
        boolean rowExists;
        Log.d("HomeFragment",""+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                requestCodes.add(cursor.getInt(cursor.getColumnIndex(AlarmsDBhelperClass.ALARM_ID)));
                nameArrayList.add(cursor.getString(cursor.getColumnIndex(AlarmsDBhelperClass.ALARM_NAME)));
                modeArrayList.add(cursor.getString(cursor.getColumnIndex(AlarmsDBhelperClass.ALARM_MODE)));
                repeatArrayList.add(cursor.getString(cursor.getColumnIndex(AlarmsDBhelperClass.ALARM_REPEAT)));
                hoursArrayList.add(Integer.toString(cursor.getInt(cursor.getColumnIndex(AlarmsDBhelperClass.ALARM_HOURS))));
                minArrayList.add(Integer.toString(cursor.getInt(cursor.getColumnIndex(AlarmsDBhelperClass.ALARM_MINS))));
                status.add(cursor.getString(cursor.getColumnIndex(AlarmsDBhelperClass.ALARM_STATUS)));
                rowExists = true;
            } while (cursor.moveToNext());
        }else {
            rowExists = false;
        }
        cursor.close();
        return rowExists;
    }

    //RecyclerView's onClick()
    @Override
    public void onItemClick(int position) {
        Toast.makeText(getContext(), "Alarm Clicked !", Toast.LENGTH_SHORT).show();
    }

    //RecyclerView's onLongClick()
    @Override
    public void onLongItemClick(int position) {
        int requestCode = Integer.parseInt(hoursArrayList.get(position)) + Integer.parseInt(minArrayList.get(position)) + 1;
        //Deleting the row from the database
        db.delete("alarms", "alarm_name=?", new String[]{"" + nameArrayList.get(position)});

        if (nameArrayList.get(position).equals("")) {
            cancelAlarm(position, true, requestCode);
        } else {
            cancelAlarm(position, false, 0);
        }
        //Now deleting those values from the mainActivity i.e., ArrayList
        hoursArrayList.remove(position);
        minArrayList.remove(position);
        nameArrayList.remove(position);
        repeatArrayList.remove(position);
        modeArrayList.remove(position);

        if (hoursArrayList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            txtEmpty.setVisibility(View.VISIBLE);
            emptyImageView.setVisibility(View.VISIBLE);
        }
        //Updating the recyclerView
        alarmAdapter.notifyItemRemoved(position);

        //User Feedback
        Toast.makeText(getContext(), "Alarm Deleted !", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwitchClicked(boolean isStart, int position) {
        int requestCode = Integer.parseInt(hoursArrayList.get(position))+Integer.parseInt(minArrayList.get(position))+1;

        if(isStart){
            ContentValues values = new ContentValues();
            values.put(AlarmsDBhelperClass.ALARM_STATUS,"ON");
            db.update("alarms",values,"alarm_name=?",new String[]{""+nameArrayList.get(position)});
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursArrayList.get(position)));
            c.set(Calendar.MINUTE, Integer.parseInt(minArrayList.get(position)));
            c.set(Calendar.SECOND, 0);
            if(c.getTimeInMillis() < System.currentTimeMillis())
                c.add(Calendar.DAY_OF_YEAR,1);//run tomorrow
            if (nameArrayList.get(position).equals("")) {
                startAlarm(c,position,true,requestCode);
            } else {
                startAlarm(c,position,false,0);
            }
            Toast.makeText(getContext(),""+c.getTime(),Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
            db.update("alarms",values,"alarm_name=?",new String[]{""+nameArrayList.get(position)});
            if (nameArrayList.get(position).equals("")) {
                cancelAlarm(position,true,requestCode);
            } else {
                cancelAlarm(position,false,0);
            }
            Toast.makeText(getContext(),"Alarm Turned OFF !",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onModeClicked(int position,String m) {
        if (Objects.equals(m, "E")) {
            Toast.makeText(getContext(),"Mode: Easy",Toast.LENGTH_SHORT).show();
        }else if(Objects.equals(m, "R"))
            Toast.makeText(getContext(),"Mode : Regular",Toast.LENGTH_SHORT).show();
        else if(Objects.equals(m, "D")){
            Toast.makeText(getContext(), "Mode : Difficult", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "Standard Alarm", Toast.LENGTH_SHORT).show();
        }
    }

    public void startAlarm(Calendar c,int position,boolean isQuick,int requestCode){
        if (!isQuick) {
            //Getting a System service for the alarm to check the current time with the Alarm set time.
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            //Creating an intent to invoke the onReceive method  in the custom receiver class, just to display notifications
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            intent.putExtra("mode",modeArrayList.get(position));
            //A pending intent is used to execute some work in the future with our applications permissions.
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCodes.get(position),intent,0);
            //Now RTC_WAKEUP means if the device is Switched off turn it on.
            //getTimeInMillis() will get get the time in Milliseconds
            //Schedule an alarm to be delivered precisely at the stated time.In my case it's the calendar's getTimeMillis() method. which is providing the correct time in milliseconds.
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        } else {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            intent.putExtra("mode","quick");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCode,intent,0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }
    }

    public void cancelAlarm(int position,boolean isQuick,int requestCode){
        if (!isQuick) {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCodes.get(position),intent,0);
            alarmManager.cancel(pendingIntent);
        } else {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCode,intent,0);
            alarmManager.cancel(pendingIntent);
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        if (c.getTimeInMillis() < System.currentTimeMillis())
            c.add(Calendar.DAY_OF_YEAR, 1);
        c1 = c;
        quickHour = hour;
        quickMin = minute;
        Intent intent = new Intent(getContext(), RingtoneSelector.class);
        startActivityForResult(intent,FRAGMENT_HOME_REQUEST_CODE);

    }

    private void setFabTranslationY() {
        fab_button_2.setAlpha(0f);
        mAlarmAddButton.setAlpha(0f);

        fab_button_2.setTranslationY(fabTranslationY);
        mAlarmAddButton.setTranslationY(fabTranslationY);
    }

    private void menuCheck() {
        if (isFabMenuOpen) {
            fabMenuClose();
        } else {
            fabMenuOpen();
        }
    }

    private void fabMenuOpen() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(true);
        fabOpenAnimation(fab_button_2);
        fabOpenAnimation(mAlarmAddButton);
    }

    private void fabMenuClose() {
        isFabMenuOpen = !isFabMenuOpen;
        fabMainAnimation(false);
        fabCloseAnimation(fab_button_2);
        fabCloseAnimation(mAlarmAddButton);
    }
    private void fabCloseAnimation(FloatingActionButton fab) {
        fab.animate().translationY(fabTranslationY).alpha(0f).setInterpolator(overshootInterpolator).setDuration(speed).start();
    }

    private void fabOpenAnimation(FloatingActionButton fab) {
        fab.animate().translationY(0f).alpha(1f).setInterpolator(overshootInterpolator).setDuration(speed).start();
    }

    private void fabMainAnimation(boolean isOpen) {
        if (isOpen) {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(45f).setDuration(speed).start();
        } else {
            fab_button_1.animate().setInterpolator(overshootInterpolator).rotation(0f).setDuration(speed).start();
        }
    }
}