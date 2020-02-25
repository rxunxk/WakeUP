package com.raunak.alarmdemo4.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmsDBhelperClass extends SQLiteOpenHelper {

    //Two constants for the db name and the version of the db.
    private static final String DBNAME = "AlarmsDB";
    private static final int VERSION = 1;
    public String databasePath = "";
    public static final String ALARM_ID = "alarm_id";
    public static final String ALARM_NAME = "alarm_name";
    public static final String ALARM_MODE = "alarm_mode";
    public static final String ALARM_REPEAT = "alarm_repeat";
    public static final String ALARM_MUSIC = "alarm_music";
    public static final String ALARM_HOURS = "hours";
    public static final String ALARM_MINS = "mins";
    public static final String ALARM_STATUS = "alarm_status";
    public static final String ALARM_USERID = "user_id";

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
        String alarm_table_sql = "CREATE TABLE alarms("+ALARM_ID+" INTEGER PRIMARY KEY,"
                +ALARM_NAME+" TEXT,"
                +ALARM_MODE+" TEXT,"
                +ALARM_REPEAT+" TEXT,"
                +ALARM_MUSIC+" TEXT,"
                +ALARM_HOURS+" INTEGER,"
                +ALARM_MINS+" INTEGER,"
                +ALARM_STATUS+" TEXT,"
                +ALARM_USERID+" TEXT)";
        sqLiteDatabase.execSQL(alarm_table_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void insertAlarm(String alarmName, String alarmMode, String alarmRepeat,String alarmMusic, int hours, int minutes, String status, int userId, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ALARM_NAME, alarmName);
        contentValues.put(ALARM_MODE, alarmMode);
        contentValues.put(ALARM_REPEAT, alarmRepeat);
        contentValues.put(ALARM_MUSIC,alarmMusic);
        contentValues.put(ALARM_HOURS, hours);
        contentValues.put(ALARM_MINS, minutes);
        contentValues.put(ALARM_STATUS, status);
        contentValues.put(ALARM_USERID, userId);

        db.insert("alarms", null, contentValues);
    }
}

