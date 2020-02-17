package com.raunak.alarmdemo4.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class AlarmsDBhelperClass extends SQLiteOpenHelper {

    //Two constants for the db name and the version of the db.
    private static final String DBNAME = "AlarmsDB";
    private static final int VERSION = 1;
    public String databasePath = "";

    //Default Constructor
    public AlarmsDBhelperClass(Context context) {
        super(context, DBNAME, null, VERSION);
        databasePath = context.getDatabasePath("wl.db").getPath();
    }

    //Overriding methods of the parent class
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String drop_table_if_exits = "DROP TABLE IF EXISTS alarms;";
        sqLiteDatabase.execSQL(drop_table_if_exits);
        String alarm_table_sql = "CREATE TABLE alarms(alarm_id INTEGER PRIMARY KEY,alarm_name TEXT, alarm_mode TEXT, alarm_repeat TEXT,hours INTEGER,minutes INTEGER,status TEXT,user_id TEXT)";
        sqLiteDatabase.execSQL(alarm_table_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertAlarm(String alarmName, String alarmMode, String alarmRepeat, int hours, int minutes, String status, int userId, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("alarm_name", alarmName);
        contentValues.put("alarm_mode", alarmMode);
        contentValues.put("alarm_repeat", alarmRepeat);
        contentValues.put("hours", hours);
        contentValues.put("minutes", minutes);
        contentValues.put("status", status);
        contentValues.put("user_id", userId);

        db.insert("alarms", null, contentValues);
    }
}

