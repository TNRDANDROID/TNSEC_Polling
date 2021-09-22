package com.nic.tnsecPollingPersonnel.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteReadOnlyDatabaseException;
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
            cursor = db.rawQuery("select * from "+DBHelper.POLLING_STATION_LIST+ " order by pvname asc , lbpolling_station_no asc",null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ElectionProject pollingStationList = new ElectionProject();
                    pollingStationList.setPolling_station_no(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.LPOLLING_STATION_NO)));
                    pollingStationList.setPolling_booth_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_BOOTH_NAME)));
                    pollingStationList.setPolling_booth_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_BOOTH_ID)));
                    pollingStationList.setDcode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    pollingStationList.setBcode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    pollingStationList.setPvcode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    pollingStationList.setPvname(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));
                    pollingStationList.setLlpolling_booth_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.LLPOLLING_BOOTH_NAME)));
                    pollingStationList.setSave_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.save_status)));
                    pollingStationList.setIsChecked_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.IS_CHECKED)));
                    pollingStationList.setUnChecked_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.UN_CHECKED)));
                    pollingStationList.setGet_remark_text(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.GET_REMARK_TEXT)));

                    cards.add(pollingStationList);
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

    public ArrayList<ElectionProject> getAll_ServerList() {
        ArrayList<ElectionProject> cards = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from "+DBHelper.SAVED_POLLING_STATION_LIST+ " order by pvname asc , lbpolling_station_no asc",null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ElectionProject pollingStationList = new ElectionProject();
                    pollingStationList.setActivity_by(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_by")));
                    pollingStationList.setRo_zone_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("ro_zone_id")));
                    pollingStationList.setRo_level_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("ro_level_id")));
                    pollingStationList.setDcode(cursor.getString(cursor
                            .getColumnIndexOrThrow("dcode")));
                    pollingStationList.setBcode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    pollingStationList.setLb_type(cursor.getString(cursor
                            .getColumnIndexOrThrow("lb_type")));
                    pollingStationList.setPolling_booth_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("polling_booth_id")));
                    pollingStationList.setActivity_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_id")));
                    pollingStationList.setActivity_remark(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_remark")));
                    pollingStationList.setLbpolling_station_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("lbpolling_station_no")));
                    pollingStationList.setPolling_booth_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("polling_booth_name")));
                    pollingStationList.setPvname(cursor.getString(cursor
                            .getColumnIndexOrThrow("pvname")));
                    pollingStationList.setActivity_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_name")));
                    pollingStationList.setActivity_type_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_type_name")));
                    pollingStationList.setActivity_type(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_type")));
                    pollingStationList.setActivity_status(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_status")));

                    cards.add(pollingStationList);


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
    public ArrayList<ElectionProject> getServerList(String activity_type, String activity_id) {

        ArrayList<ElectionProject> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;

        selection = " activity_type = ? and activity_id = ? ";
        selectionArgs = new String[]{activity_type,activity_id};

        try {
/*
            cursor = db.query(DBHelper.SAVED_POLLING_STATION_LIST,
                    new String[]{"*"}, selection,selectionArgs, null, null, " order by pvname asc , lbpolling_station_no asc");
*/
            cursor = db.rawQuery("SELECT * FROM " + DBHelper.SAVED_POLLING_STATION_LIST + " where activity_type = '" + activity_type + "' and activity_id = '" + activity_id + "' order by pvname asc , lbpolling_station_no asc", null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    ElectionProject pollingStationList = new ElectionProject();
                    pollingStationList.setActivity_by(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_by")));
                    pollingStationList.setRo_zone_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("ro_zone_id")));
                    pollingStationList.setRo_level_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("ro_level_id")));
                    pollingStationList.setDcode(cursor.getString(cursor
                            .getColumnIndexOrThrow("dcode")));
                    pollingStationList.setBcode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    pollingStationList.setLb_type(cursor.getString(cursor
                            .getColumnIndexOrThrow("lb_type")));
                    pollingStationList.setPolling_booth_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("polling_booth_id")));
                    pollingStationList.setActivity_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_id")));
                    pollingStationList.setActivity_remark(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_remark")));
                    pollingStationList.setLbpolling_station_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("lbpolling_station_no")));
                    pollingStationList.setPolling_booth_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("polling_booth_name")));
                    pollingStationList.setPvname(cursor.getString(cursor
                            .getColumnIndexOrThrow("pvname")));
                    pollingStationList.setActivity_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_name")));
                    pollingStationList.setActivity_type_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_type_name")));
                    pollingStationList.setActivity_type(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_type")));
                    pollingStationList.setActivity_status(cursor.getString(cursor
                            .getColumnIndexOrThrow("activity_status")));

                    cards.add(pollingStationList);


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
            cursor = db.rawQuery("select * from "+DBHelper.SAVE_DATA+ " order by pvname asc , polling_station_no asc" ,null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    ElectionProject card = new ElectionProject();

                    card.setRo_zone_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.RO_ZONE_ID)));
                    card.setPolling_booth_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_BOOTH_ID)));
                    card.setPolling_station_no(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_STATION_NO)));
                    card.setPolling_booth_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_BOOTH_NAME)));
                    card.setPvname(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));
                    card.setActivity_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_ID)));
                    card.setActivity_description(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_DESCRIPTION)));
                    card.setActivity_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE)));
                    card.setActivity_remark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_REMARK)));
                    card.setYes_no(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_STATUS)));
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
    public ArrayList<ElectionProject> getSavedDetailsPolingStationNumber(String polling_booth_id, String activity_id, String activity_type) {

        ArrayList<ElectionProject> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection = null;
        String[] selectionArgs = null;

        selection = "polling_booth_id = ? and activity_id = ? and activity_type = ? ";
        selectionArgs = new String[]{polling_booth_id,activity_id,activity_type};

        try {
            cursor = db.query(DBHelper.SAVE_DATA,
                    new String[]{"*"}, selection,selectionArgs, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {

                    ElectionProject card = new ElectionProject();

                    card.setRo_zone_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.RO_ZONE_ID)));
                    card.setPolling_booth_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_BOOTH_ID)));
                    card.setPolling_station_no(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_STATION_NO)));
                    card.setPolling_booth_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.POLLING_BOOTH_NAME)));
                    card.setPvname(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));
                    card.setActivity_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_ID)));
                    card.setActivity_description(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_DESCRIPTION)));
                    card.setActivity_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE)));
                    card.setActivity_remark(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_REMARK)));
                    card.setYes_no(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.ACTIVITY_STATUS)));
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

    public ElectionProject insertPollingStationList(ElectionProject electionProject) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.LPOLLING_STATION_NO, electionProject.getPolling_station_no());
        values.put(AppConstant.POLLING_BOOTH_NAME, electionProject.getPolling_booth_name());
        values.put(AppConstant.POLLING_BOOTH_ID, electionProject.getPolling_booth_id());
        values.put(AppConstant.DISTRICT_CODE, electionProject.getDcode());
        values.put(AppConstant.BLOCK_CODE, electionProject.getBcode());
        values.put(AppConstant.PV_CODE, electionProject.getPvcode());
        values.put(AppConstant.PV_NAME, electionProject.getPvname());
        values.put(AppConstant.LLPOLLING_BOOTH_NAME, electionProject.getLlpolling_booth_name());
        values.put(AppConstant.save_status, electionProject.getSave_status());
        values.put(AppConstant.IS_CHECKED, electionProject.getIsChecked_status());
        values.put(AppConstant.UN_CHECKED, electionProject.getUnChecked_status());
        values.put(AppConstant.GET_REMARK_TEXT, electionProject.getGet_remark_text());

        long id = db.insert(DBHelper.POLLING_STATION_LIST,null,values);
        Log.d("Inserted_PollingStation", String.valueOf(id));

        return electionProject;
    }
    public ElectionProject insertViewServerData(ElectionProject electionProject) {

        ContentValues values = new ContentValues();
        values.put("activity_by",electionProject.getActivity_by());
        values.put("ro_zone_id",electionProject.getRo_zone_id());
        values.put("ro_level_id",electionProject.getRo_level_id());
        values.put("dcode",electionProject.getDcode());
        values.put("bcode",electionProject.getBcode());
        values.put("lb_type",electionProject.getLb_type());
        values.put("polling_booth_id",electionProject.getPolling_booth_id());
        values.put("activity_id",electionProject.getActivity_id());
        values.put("activity_remark",electionProject.getActivity_remark());
        values.put("lbpolling_station_no",electionProject.getLbpolling_station_no());
        values.put("polling_booth_name",electionProject.getPolling_booth_name());
        values.put("llpolling_booth_name",electionProject.getLlpolling_booth_name());
        values.put("pvname",electionProject.getPvname());
        values.put("activity_name",electionProject.getActivity_name());
        values.put("activity_type",electionProject.getActivity_type());
        values.put("activity_type_name",electionProject.getActivity_type_name());
        values.put("activity_status",electionProject.getActivity_status());

        long id = db.insert(DBHelper.SAVED_POLLING_STATION_LIST,null,values);
        Log.d("Inserted_serverData", String.valueOf(id));

        return electionProject;
    }


    public void deleteAllTables(){
        deleteActivity_type_list();
        deleteActivity_list();
        deletePollingStationList();
        deleteSavedPollingStationList();
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
    public void deleteSavedPollingStationList() {
        db.execSQL("delete from " + DBHelper.SAVED_POLLING_STATION_LIST);
    }
    public void deleteSaveData() {
        db.execSQL("delete from " + DBHelper.SAVE_DATA);
    }

}
