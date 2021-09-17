package com.nic.tnsecPollingPersonnel.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.nic.tnsecPollingPersonnel.constant.AppConstant;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import java.util.ArrayList;


public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context) {
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteReadOnlyDatabaseException e) {

        }

    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
    public ArrayList<ElectionProject> getAll_ActivityType() {

        ArrayList<ElectionProject> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.ACTIVITY_TYPE_LIST,null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ElectionProject card = new ElectionProject();
                    card.setActivity_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE)));
                    card.setActivity_type_desc(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE_DESC)));
                    card.setDisplay_order(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISPLAY_ORDER)));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<ElectionProject> getAll_ActivityList() {

        ArrayList<ElectionProject> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.ACTIVITY_LIST,null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ElectionProject card = new ElectionProject();
                    card.setActivity_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_ID)));
                    card.setActivity_description(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_DESCRIPTION)));
                    card.setActivity_by(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_BY)));
                    card.setActivity_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE)));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<ElectionProject> getAll_PollingStationList() {

        ArrayList<ElectionProject> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.ACTIVITY_LIST,null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ElectionProject card = new ElectionProject();
                    card.setActivity_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_ID)));
                    card.setActivity_description(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_DESCRIPTION)));
                    card.setActivity_by(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_BY)));
                    card.setActivity_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE)));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<ElectionProject> getSavedDetails() {

        ArrayList<ElectionProject> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.SAVE_DATA ,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    ElectionProject card = new ElectionProject();

                    card.setRo_zone_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.RO_ZONE_ID)));
                    card.setPolling_booth_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_BOOTH_ID)));
                    card.setActivity_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_ID)));
                    card.setActivity_remark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_REMARK)));
                    cards.add(card);
                }
            }
        } catch (Exception e) {
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ElectionProject insertActivityType(ElectionProject electionProject) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.ACTIVITY_TYPE, electionProject.getActivity_type());
        values.put(AppConstant.ACTIVITY_TYPE_DESC, electionProject.getActivity_type_desc());
        values.put(AppConstant.DISPLAY_ORDER, electionProject.getDisplay_order());

        long id = db.insert(DBHelper.ACTIVITY_TYPE_LIST,null,values);
        Log.d("Inserted_id_type", String.valueOf(id));

        return electionProject;
    }
    public ElectionProject insertActivityList(ElectionProject electionProject) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.ACTIVITY_ID, electionProject.getActivity_id());
        values.put(AppConstant.ACTIVITY_DESCRIPTION, electionProject.getActivity_description());
        values.put(AppConstant.ACTIVITY_BY, electionProject.getActivity_by());
        values.put(AppConstant.ACTIVITY_TYPE, electionProject.getActivity_type());

        long id = db.insert(DBHelper.ACTIVITY_LIST,null,values);
        Log.d("Inserted_id_activity", String.valueOf(id));

        return electionProject;
    }


    public void deleteAllTables(){
        deleteActivity_type_list();
        deleteActivity_list();
        deletePollingStationList();
        deleteSaveData();
    }


    public void deleteActivity_type_list() {
        db.execSQL("delete from " + DBHelper.ACTIVITY_TYPE_LIST);
    }
    public void deleteActivity_list() {
        db.execSQL("delete from " + DBHelper.ACTIVITY_LIST);
    }
    public void deletePollingStationList() {
        db.execSQL("delete from " + DBHelper.POLLING_STATION_LIST);
    }
    public void deleteSaveData() {
        db.execSQL("delete from " + DBHelper.SAVE_DATA);
    }

}
