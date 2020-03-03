package com.raunak.alarmdemo4.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AlarmsDBhelperClass extends SQLiteOpenHelper {

    //constants for the db name, version of the db and table names
    private static final String DBNAME = "AlarmsDB";
    private static final int VERSION = 1;
    public static final String ALARM_ID = "alarm_id";
    public static final String ALARM_NAME = "alarm_name";
    public static final String ALARM_MODE = "alarm_mode";
    public static final String ALARM_REPEAT = "alarm_repeat";
    public static final String MUSIC_PATH = "musicPath";
    public static final String ALARM_HOURS = "hours";
    public static final String ALARM_MINS = "minutes";
    public static final String ALARM_STATUS = "alarm_status";
    public static final String ALARM_USERID = "user_id";

    //Default Constructor
    public AlarmsDBhelperClass(Context context) {
        super(context, DBNAME, null, VERSION);
//        databasePath = context.getDatabasePath("wl.db").getPath();
    }

    //Overriding methods of the parent class
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String drop_table_if_exits = "DROP TABLE IF EXISTS alarms;";
        sqLiteDatabase.execSQL(drop_table_if_exits);
        String alarm_table_sql = "CREATE TABLE IF NOT EXISTS alarms ("
                +ALARM_ID+" INTEGER PRIMARY KEY,"
                +ALARM_NAME+" TEXT,"
                +ALARM_MODE+" TEXT,"
                +ALARM_REPEAT+" TEXT,"
                +MUSIC_PATH+" TEXT,"
                +ALARM_HOURS+" INTEGER,"
                +ALARM_MINS+" INTEGER,"
                +ALARM_STATUS+" TEXT,"
                +ALARM_USERID+" TEXT)";
        sqLiteDatabase.execSQL(alarm_table_sql);
        Log.d("working","table created!");
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM alarms", null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    //Custom method for inserting new rows into tables
    public void insertAlarm(String alarmName, String alarmMode, String alarmRepeat,String alarmMusic, int hours, int mins, String status, String userId, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALARM_NAME, alarmName);
        contentValues.put(ALARM_MODE, alarmMode);
        contentValues.put(ALARM_REPEAT, alarmRepeat);
        contentValues.put(MUSIC_PATH,  alarmMusic);
        contentValues.put(ALARM_HOURS, hours);
        contentValues.put(ALARM_MINS, mins);
        contentValues.put(ALARM_STATUS, status);
        contentValues.put(ALARM_USERID, userId);

        db.insert("alarms", null, contentValues);
    }
}

