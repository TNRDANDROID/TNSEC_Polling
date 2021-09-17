package com.nic.tnsecPollingPersonnel.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "electionproject";
    private static final int DATABASE_VERSION = 1;


    public static final String RO_USER_TABLE_NAME = "RO_UserTable";
    public static final String POLLING_STATION_IMAGE = "Polling_Station_Image";
    public static final String SAVE_EMP_DETAILS = "SaveEmpDetails";
    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("CREATE TABLE " + RO_USER_TABLE_NAME + " ("
                + "district_code INTEGER," +
                "district_name TEXT," +
                "localbody_no INTEGER," +
                "localbody_name TEXT," +
                "ro_user_name TEXT," +
                "ro_mobile_no TEXT," +
                "localbody_type TEXT," +
                "localbody_abbr TEXT)");

        db.execSQL("CREATE TABLE " + POLLING_STATION_IMAGE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
               /* "dcode INTEGER," +
                "bcode INTEGER," +*/
                "image BLOB," +
                "lat TEXT," +
                "long TEXT," +
                "image_description TEXT)");
        db.execSQL("CREATE TABLE " + SAVE_EMP_DETAILS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "pp_id TEXT," +
                "empcode_type TEXT," +
                "empcode TEXT," +
                "name_of_staff TEXT," +
                "dept_org_name TEXT," +
                "gender TEXT," +
                "photo_available TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + RO_USER_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + POLLING_STATION_IMAGE);
            db.execSQL("DROP TABLE IF EXISTS " + SAVE_EMP_DETAILS);
            onCreate(db);
        }
    }


}
