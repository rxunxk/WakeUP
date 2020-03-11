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

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

    private static final int SYSTEM_ALERT_WINDOW_CODE = 100;
    private final int FRAGMENT_HOME_REQUEST_CODE =1;
    private FloatingActionsMenu fabMenu;
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
        }

        //Initializing RecyclerView, DatabaseHelperClass, FAB button, The ON OFF switch & the empty ImageView
        mAlarmsDBhelperClass = new AlarmsDBhelperClass(getContext());
        com.getbase.floatingactionbutton.FloatingActionButton fab_button_1 = getView().findViewById(R.id.fab_action1);
        com.getbase.floatingactionbutton.FloatingActionButton fab_button_2 = getView().findViewById(R.id.fab_action2);
        fabMenu = getView().findViewById(R.id.fab_main);

        recyclerView = getView().findViewById(R.id.alarmList);
        emptyImageView = getView().findViewById(R.id.empty_view);
        SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        txtEmpty = getView().findViewById(R.id.txtEmpty);
        timePicker = new TimePickerFragment(this);

        //fab event handling
        fab_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getChildFragmentManager(),null);
            }
        });
        fab_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    if (fabMenu.isShown()) {
                        fabMenu.setVisibility(View.GONE);
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!fabMenu.isShown()) {
                        fabMenu.setVisibility(View.VISIBLE);
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
        if (requestCode == FRAGMENT_HOME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                songPath = data.getStringExtra("SongName");
                if (songPath.equals("")){
                    songPath = "song1";
                }
                mAlarmsDBhelperClass.insertAlarm("quick alarm", "Q", "", songPath, quickHour, quickMin, "ON", "", db);
                startAlarm(c1,"Q",quickHour+quickMin+1);
                alarmAdapter.notifyDataSetChanged();
            }
        }else if (requestCode ==  ADD_ALARM_REQUEST_CODE){
            if (data != null) {
                Calendar c = Calendar.getInstance();
                int hour = data.getIntExtra("hours",0);
                int min = data.getIntExtra("mins",0);
                c.set(Calendar.HOUR_OF_DAY,hour);
                c.set(Calendar.MINUTE,min);
                c.set(Calendar.SECOND,0);
                if (c.getTimeInMillis() < System.currentTimeMillis())
                    c.add(Calendar.DAY_OF_YEAR, 1);
                startAlarm(c,data.getStringExtra("mode"),hour+min+1);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        alarmAdapter.notifyDataSetChanged();
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
    }

    //RecyclerView's onLongClick()
    @Override
    public void onLongItemClick(int position) {
        int requestCode = Integer.parseInt(hoursArrayList.get(position)) + Integer.parseInt(minArrayList.get(position)) + 1;
        //Deleting the row from the database
        /*db.delete("alarms", "alarm_name=?", new String[]{"" + nameArrayList.get(position)});*/
        db.delete("alarms","hours=? AND minutes=?",new String[]{""+hoursArrayList.get(position),""+minArrayList.get(position)});
        cancelAlarm(requestCode);
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
        Snackbar snackbar = Snackbar.make(txtEmpty, "Alarm Deleted!", Snackbar.LENGTH_SHORT);
        snackbar.show();
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
            startAlarm(c,modeArrayList.get(position),requestCode);
        }else{
            ContentValues values = new ContentValues();
            values.put(AlarmsDBhelperClass.ALARM_STATUS,"OFF");
            db.update("alarms",values,"alarm_name=?",new String[]{""+nameArrayList.get(position)});
            cancelAlarm(requestCode);
        }
    }

    @Override
    public void onModeClicked(int position,String m) {
    }

    public void startAlarm(Calendar c,String mode,int requestCode){
        if (!mode.equals("Q")) {
            //Getting a System service for the alarm to check the current time with the Alarm set time.
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            //Creating an intent to invoke the onReceive method  in the custom receiver class, just to display notifications
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            intent.putExtra("mode",mode);
            //A pending intent is used to execute some work in the future with our applications permissions.
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCode,intent,0);
            //Now RTC_WAKEUP means if the device is Switched off turn it on.
            //getTimeInMillis() will get get the time in Milliseconds
            //Schedule an alarm to be delivered precisely at the stated time.In my case it's the calendar's getTimeMillis() method. which is providing the correct time in milliseconds.
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        } else {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            intent.putExtra("mode",mode);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCode,intent,0);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        }
    }

    private void cancelAlarm(int requestCode){
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);
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
}