package com.raunak.alarmdemo4.HelperClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    public static final String QUESTION_ID = "question_id";
    public static final String QUESTION_QUESTION = "question";
    public static final String QUESTION_ANSWER = "answer";
    public static final String QUESTION_A  = "a";
    public static final String QUESTION_B = "b";
    public static final String QUESTION_C = "c";
    public static final String QUESTION_D = "d";
    public static final String TABLE_QUESTION = "questions";

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
        String question_table_sql = "CREATE TABLE IF NOT EXISTS "+TABLE_QUESTION+" (" +
                QUESTION_ID+" INTEGER PRIMARY KEY," +
                QUESTION_QUESTION+" TEXT," +
                QUESTION_ANSWER+" TEXT," +
                QUESTION_A+" TEXT," +
                QUESTION_B+" TEXT," +
                QUESTION_C+" TEXT," +
                QUESTION_D+" TEXT)";
        sqLiteDatabase.execSQL(alarm_table_sql);
        sqLiteDatabase.execSQL(question_table_sql);

        //feeding database with quiz questions, answers and their options.
        insertQuestion("'Natya - Shastra' the main source of India's classical dances was written by?","Bharat Muni","Abhinav Gupt","Tandu Muni","Nara Muni","Bharat Muni",sqLiteDatabase);
        insertQuestion("'Dandia' is a popular dance of?","Gujrat","Punjab","Tamil Nadu","Maharashtra","Gujrat",sqLiteDatabase);
        insertQuestion("The words 'Satyameva Jayate' inscribed below the base plate of the emblem of India are taken from","Mundak Upanishad","Rigveda","Satpath Brahmana","Mundak Upanishad","Ramayana",sqLiteDatabase);
        insertQuestion("The Rath Yatra at Puri is celebrated in honour of which Hindu deity","Jaganath","Ram","Jaganath","Shiva","Vishnu",sqLiteDatabase);
        insertQuestion("Entomology is the science that studies","Insects","Behavior of human beings","Insects","The formation of rocks","None of the above",sqLiteDatabase);
        insertQuestion("Hitler party which came into power in 1933 is known as","Nazi Party","Nazi Party","Ku-Klux-Klan","Labour Party","Democratic Party",sqLiteDatabase);
        insertQuestion("Epsom (England) is the place associated with","Horse racing","Horse racing","Polo","Shooting","Snooker",sqLiteDatabase);
        insertQuestion("Golf player Vijay Singh belongs to which country?","Fiji","USA","Fiji","India","UK",sqLiteDatabase);
        insertQuestion("First Afghan War took place in","1839","1839","1843","1833","1848",sqLiteDatabase);
        insertQuestion("Who is the father of Geometry?","Euclid","Aristotle","Euclid","Pythagoras","Kepler",sqLiteDatabase);
        insertQuestion("Who was known as Iron man of India?","Sardar Vallabhbhai Patel","Govind Ballabh Pant","Jawaharlal Nehru","Subhash Chandra Bose","Sardar Vallabhbhai Patel",sqLiteDatabase);
        insertQuestion("The Indian to beat the computers in mathematical wizardry is","Shakunthala Devi","Ramanujam","Rina Panigrahi","Raja Ramanna","Shakunthala Devi",sqLiteDatabase);
        insertQuestion("In which decade was the American Institute of Electrical Engineers (AIEE) founded?","1880s","1850s","1880s","1930s","1950s",sqLiteDatabase);
        insertQuestion("What is part of a database that holds only one type of information?","Field","Report","Field","Record","File",sqLiteDatabase);
        insertQuestion("'OS' computer abbreviation usually means ?","Operating System","Order of Significance","Open Software","Operating System","Optical Sensor",sqLiteDatabase);
        insertQuestion("Most modern TV's draw power even if turned off. The circuit the power is used in does what function?","Remote control","Sound","Remote control","Color balance","High voltage",sqLiteDatabase);
        insertQuestion("Which is a type of Electrically-Erasable Programmable Read-Only Memory?","Flash","Flash","Flange","Fury","FRAM",sqLiteDatabase);
        insertQuestion("Made from a variety of materials, such as carbon, which inhibits the flow of current...?","Resistor","Choke","Inductor","Resistor","Capacitor",sqLiteDatabase);
        insertQuestion("'DB' computer abbreviation usually means ?","Database","Database","Double Byte","Data Block","Driver Boot",sqLiteDatabase);
        insertQuestion("The Homolographic projection has the correct representation of","area","shape","area","baring","distance",sqLiteDatabase);
        insertQuestion("The great Victoria Desert is located in","Australia","Canada","West Africa","Australia","North America",sqLiteDatabase);
        insertQuestion("The intersecting lines drawn on maps and globes are","geographic grids","latitudes","longitudes","geographic grids","None of the above",sqLiteDatabase);
        insertQuestion("The landmass of which of the following continents is the least?","Australia","Africa","Asia","Australia","Europe",sqLiteDatabase);
        insertQuestion("Which of the following is tropical grassland?","Savannah","Taiga","Savannah","Pampas","Prairies",sqLiteDatabase);
        insertQuestion("The temperature increases rapidly after","ionosphere","ionosphere","exosphere","stratosphere","troposphere",sqLiteDatabase);
        insertQuestion("The humidity of the air depends upon","All of the above","temperature","location","weather","All of the above",sqLiteDatabase);
        insertQuestion("The nucleus of an atom consists of","protons and neutrons","electrons and neutrons","electrons and protons","protons and neutrons","All of the above",sqLiteDatabase);
        insertQuestion("The most electronegative element among the following is","fluorine","sodium","bromine","fluorine","oxygen",sqLiteDatabase);
        insertQuestion("Which of the following is not associated with the UNO?","ASEAN","ILO","WHO","ASEAN","All of the above",sqLiteDatabase);
        insertQuestion("Permanent Secretariat to coordinate the implementation of SAARC programme is located at","Kathmandu","Dhaka","New Delhi","Colombo","Kathmandu",sqLiteDatabase);
        insertQuestion("For how many days is a Test match scheduled?","five days","one day","100 overs","50 overs","five days",sqLiteDatabase);
        insertQuestion("Kapil Dev did not play for India during the World Cup tournament held in:","1975","1975","1979","1983","1987",sqLiteDatabase);
        insertQuestion("India's first win in a World Cup match was against?","West Indies","Sri Lanka","West Indies","East AFrica","England",sqLiteDatabase);
        insertQuestion("Who invented the BALLPOINT PEN?","Biro Brothers","Biro Brothers","Waterman Brothers","Bicc Brothers","Write Brothers",sqLiteDatabase);
        insertQuestion("What Galileo invented?","Thermometer","Barometer","Pendulum clock","Microscope","Thermometer",sqLiteDatabase);
        insertQuestion("What invention caused many deaths while testing it?","Parachute","Dynamite","Ladders","Race cars","Parachute",sqLiteDatabase);
        insertQuestion("In which decade was the telephone invented?","1870s","1850s","1860s","1870s","1880s",sqLiteDatabase);
        insertQuestion("What Benjamin Franklin invented?","Bifocal spectacles","Bifocal spectacles","Radio","Barometer","Hygrometer",sqLiteDatabase);
        insertQuestion("Wadia Institute of Himalayan Geology is located at","Dehradun","Delhi","Shimla","Dehradun","Kulu",sqLiteDatabase);
        insertQuestion("The headquarters of the National Power Training institute is located in","Faridabad","Pune","Bhopal","Faridabad","Lucknow",sqLiteDatabase);
        insertQuestion("The Indian Institute of Science is located at","Bangalore","Kerala","Madras","Bangalore","New Delhi",sqLiteDatabase);
        insertQuestion("Which city is known as 'Electronic City of India'?","Bangalore","Mumbai","Hyderabad","Guragon","Bangalore",sqLiteDatabase);
        insertQuestion("Indian School of Mines is located in","Dhanbad","Dhanbad","Asansol","Tatanagar","Rourkela",sqLiteDatabase);
        insertQuestion("Golden Temple is situated in","Amritsar","New Delhi","Agra","Amritsar","Mumbai",sqLiteDatabase);
        insertQuestion("Raja Ravi Verma, was famous in which of the fields?","Painting","Painting","Politics","Dance","Music",sqLiteDatabase);
        insertQuestion("The number of already named bones in the human skeleton is","206","200","206","212","218",sqLiteDatabase);
        insertQuestion("The ozone layer restricts","ultraviolet radiation","visible light","infrared radiation","x-rays","ultraviolet radiation",sqLiteDatabase);
        insertQuestion("The smallest state of India is","Goa","Rajasthan","Sikkim","Himachal Pradesh","Goa",sqLiteDatabase);
        insertQuestion("The origin of modern badminton is attributed to","Britain","India","Britain","France","Spain",sqLiteDatabase);
        insertQuestion("The name of Ronaldinho is associated with the game of","football","football","hockey","gymnastics","badminton",sqLiteDatabase);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS alarms");
        onCreate(sqLiteDatabase);
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

    public static void insertQuestion(String question, String answer, String ans_a, String ans_b, String ans_c, String ans_d, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUESTION_QUESTION,question);
        contentValues.put(QUESTION_ANSWER,answer);
        contentValues.put(QUESTION_A,ans_a);
        contentValues.put(QUESTION_B,ans_b);
        contentValues.put(QUESTION_C,ans_c);
        contentValues.put(QUESTION_D,ans_d);

        db.insert(TABLE_QUESTION,null,contentValues);
    }
}

