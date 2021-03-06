package com.nic.tnsecPollingPersonnel.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tnsecpollingpersonnel";
    private static final int DATABASE_VERSION = 1;


    public static final String ACTIVITY_TYPE_LIST = "activity_type_list";
    public static final String ACTIVITY_LIST = "activity_list";
    public static final String POLLING_STATION_LIST = "polling_station_list";
    public static final String SAVED_POLLING_STATION_LIST = "saved_polling_station_list";
    public static final String SAVE_DATA = "save_data";
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + ACTIVITY_TYPE_LIST + " ("
                + "activity_type TEXT," +
                "activity_type_desc TEXT," +
                "display_order TEXT)");
        db.execSQL("CREATE TABLE " + ACTIVITY_LIST + " ("
                + "activity_id TEXT," +
                "activity_description TEXT," +
                "activity_by TEXT," +
                "activity_type TEXT)");
        db.execSQL("CREATE TABLE " + POLLING_STATION_LIST + " ("
                + "lbpolling_station_no TEXT," +
                "polling_booth_name TEXT," +
                "llpolling_booth_name TEXT," +
                "polling_booth_id TEXT," +
                "dcode TEXT," +
                "save_status TEXT," +
                "is_checked TEXT," +
                "un_checked TEXT," +
                "get_remark_text TEXT," +
                "bcode TEXT," +
                "pvcode TEXT," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + SAVED_POLLING_STATION_LIST + " ("
                + "activity_by TEXT," +
                "ro_zone_id TEXT," +
                "ro_level_id TEXT," +
                "dcode TEXT," +
                "bcode TEXT," +
                "lb_type TEXT," +
                "polling_booth_id TEXT," +
                "activity_id TEXT," +
                "activity_remark TEXT," +
                "lbpolling_station_no TEXT," +
                "polling_booth_name TEXT," +
                "llpolling_booth_name TEXT," +
                "pvname TEXT," +
                "activity_name TEXT," +
                "activity_type TEXT," +
                "activity_type_name TEXT," +
                "activity_status TEXT)");
        db.execSQL("CREATE TABLE " + SAVE_DATA + " ("
                + "ro_zone_id TEXT," +
                "polling_booth_id TEXT," +
                "polling_station_no TEXT," +
                "polling_booth_name TEXT," +
                "pvname TEXT," +
                "activity_id TEXT," +
                "activity_description TEXT," +
                "activity_type TEXT," +
                "activity_status TEXT," +
                "activity_remark TEXT)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_TYPE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + POLLING_STATION_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + SAVED_POLLING_STATION_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_DATA);
            onCreate(db);
        }
    }


}
