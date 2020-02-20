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
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.raunak.alarmdemo4.Adapters.AlarmAdapter;
import com.raunak.alarmdemo4.HelperClasses.AlarmsDBhelperClass;
import com.raunak.alarmdemo4.HelperClasses.MFabButtons;
import com.raunak.alarmdemo4.Interfaces.AlarmRecyclerViewListener;
import com.raunak.alarmdemo4.R;
import com.raunak.alarmdemo4.Recievers.AlarmReceiver;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomeFragment extends Fragment implements AlarmRecyclerViewListener,TimePickerDialog.OnTimeSetListener{

    private static final int SYSTEM_ALERT_WINDOW_CODE = 100;

    private FloatingActionButton mAlarmAddButton,fab_button_1,fab_button_2;
    private SQLiteDatabase db;
    private AlarmsDBhelperClass mAlarmsDBhelperClass;
    private ArrayList<Integer> requestCodes = new ArrayList<>();
    private ArrayList<String> nameArrayList = new ArrayList<>();
    private ArrayList <String> modeArrayList = new ArrayList<>();
    private ArrayList<String> repeatArrayList = new ArrayList<>();
    private ArrayList<String> hoursArrayList = new ArrayList<>();
    private ArrayList<String> minArrayList = new ArrayList<>();
    private ArrayList<String> status = new ArrayList<>();
    private AlarmAdapter alarmAdapter = new AlarmAdapter(hoursArrayList,minArrayList,modeArrayList,repeatArrayList,nameArrayList,status,this);
    private MFabButtons mFab;
    private int quickHour;
    private int quickMin;

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
        RecyclerView recyclerView = getView().findViewById(R.id.alarmList);
        ImageView emptyImageView = getView().findViewById(R.id.empty_view);
        SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        TextView txtEmpty = getView().findViewById(R.id.txtEmpty);
        final DialogFragment timePicker = new TimePickerFragment(this);

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
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#76a6ef"));

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
        setType();
        FloatingActionButton btn = getView().findViewById(R.id.fab_button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.show(getChildFragmentManager(),null);
            }
        });

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
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    public boolean getAlarm(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT * FROM alarms", new String[]{});
        boolean rowExists;
        if (cursor.moveToFirst()) {
            do {
                requestCodes.add(cursor.getInt(cursor.getColumnIndex("alarm_id")));
                nameArrayList.add(cursor.getString(cursor.getColumnIndex("alarm_name")));
                modeArrayList.add(cursor.getString(cursor.getColumnIndex("alarm_mode")));
                repeatArrayList.add(cursor.getString(cursor.getColumnIndex("alarm_repeat")));
                hoursArrayList.add(Integer.toString(cursor.getInt(cursor.getColumnIndex("hours"))));
                minArrayList.add(Integer.toString(cursor.getInt(cursor.getColumnIndex("minutes"))));
                Log.d("Everything is Ok","OK");
                status.add(cursor.getString(cursor.getColumnIndex("status")));
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
            values.put("status","ON");
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
            values.put("status","OFF");
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
            //Creating an intent to invoke the onReceive method  in the custom receiver class, just to display notifications.
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
            //A pending intent is used to execute some work in the future with our applications permissions.
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),requestCodes.get(position),intent,0);
            //Now RTC_WAKEUP means if the device is Switched off turn it on.
            //getTimeInMillis() will get get the time in Milliseconds
            //Schedule an alarm to be delivered precisely at the stated time.In my case it's the calendar's getTimeMillis() method. which is providing the correct time in milliseconds.
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
        } else {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext(), AlarmReceiver.class);
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

    private void setType() {
        mFab = new MFabButtons(getContext(), fab_button_1, fab_button_2, mAlarmAddButton);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        quickHour = hour;
        quickMin = minute;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        mAlarmsDBhelperClass.insertAlarm("","⚡","",hour,minute,"ON",0,db);
        startAlarm(c,0,true,hour+minute+1);
    }
}