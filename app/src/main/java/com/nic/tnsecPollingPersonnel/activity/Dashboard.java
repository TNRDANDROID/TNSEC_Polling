package com.nic.tnsecPollingPersonnel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.tnsecPollingPersonnel.DataBase.DBHelper;
import com.nic.tnsecPollingPersonnel.DataBase.dbData;
import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.adapter.ActivityListAdapter;
import com.nic.tnsecPollingPersonnel.adapter.ActivityTypeAdapter;
import com.nic.tnsecPollingPersonnel.adapter.CommonAdapter;
import com.nic.tnsecPollingPersonnel.api.Api;
import com.nic.tnsecPollingPersonnel.api.ApiService;
import com.nic.tnsecPollingPersonnel.api.ServerResponse;
import com.nic.tnsecPollingPersonnel.constant.AppConstant;
import com.nic.tnsecPollingPersonnel.databinding.DashboardBinding;
import com.nic.tnsecPollingPersonnel.dialog.MyDialog;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;
import com.nic.tnsecPollingPersonnel.support.ProgressHUD;
import com.nic.tnsecPollingPersonnel.utils.UrlGenerator;
import com.nic.tnsecPollingPersonnel.utils.Utils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Dashboard extends AppCompatActivity implements MyDialog.myOnClickListener, Api.ServerResponseListener, View.OnClickListener {


    private DashboardBinding dashboardBinding;
    Animation smalltobig, stb2;
    final Handler handler = new Handler();
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    public dbData dbData = new dbData(this);
    private ArrayList<ElectionProject> activityTypeList = new ArrayList<>();
    private ArrayList<ElectionProject> activityArrayList = new ArrayList<>();
    private List<ElectionProject> activityType = new ArrayList<>();
    private List<ElectionProject> activityTypeOrdered = new ArrayList<>();
    private List<ElectionProject> activityList = new ArrayList<>();
    private List<ElectionProject> activityListOrdered = new ArrayList<>();

    private ProgressHUD progressHUD;
    private RecyclerView recyclerActivityType;
    private RecyclerView recyclerActivityList;
    private ActivityTypeAdapter activityTypeAdapter;
    private ActivityListAdapter activityListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.dashboard);
        dashboardBinding.setActivity(this);
        /*WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());*/
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        smalltobig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);
        stb2 = AnimationUtils.loadAnimation(this, R.anim.stb2);

        prefManager = new PrefManager(this);


        Animation anim = new ScaleAnimation(
                0.95f, 1f, // Start and end values for the X axis scaling
                0.95f, 1f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        anim.setRepeatMode(Animation.INFINITE);
        anim.setRepeatCount(Animation.INFINITE);

        recyclerActivityType = dashboardBinding.activityTypeList;
        recyclerActivityType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recyclerActivityType.setItemAnimator(new DefaultItemAnimator());
        recyclerActivityType.setNestedScrollingEnabled(false);

        recyclerActivityList = dashboardBinding.activityList;
        recyclerActivityList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerActivityList.setItemAnimator(new DefaultItemAnimator());
        recyclerActivityList.setNestedScrollingEnabled(false);

        dashboardBinding.name.setText(prefManager.getZonalPartyName());
        dashboardBinding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    String Activity_type = activityType.get(position).getActivity_type();
                    prefManager.setActivityType(Activity_type);
                    loadActivityListSpinner(Activity_type);
                    dashboardBinding.activitySpinner.setSelection(0);
                }else {
                    prefManager.setActivityType("");
                    prefManager.setActivityId("");
                    prefManager.setActivityName("");
                    dashboardBinding.activitySpinner.setSelection(0);
                    dashboardBinding.activitySpinner.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dashboardBinding.activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    String Activity_id = activityList.get(position).getActivity_id();
                    String Activity_name = activityList.get(position).getActivity_description();
                    prefManager.setActivityId(Activity_id);
                    prefManager.setActivityName(Activity_name);
                }else {
                    prefManager.setActivityId("");
                    prefManager.setActivityName("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dashboardBinding.viewServerData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isOnline()) {
                    getPollingList();
                } else {
                    Utils.showAlert(Dashboard.this, "Your Internet seems to be Offline.Data can be viewed only in Online mode.");
                }
            }
        });

        loadActivityTypeSpinner();
        syncButtonVisibility();

    }
    public void getPollingList() {
        try {
            new ApiService(this).makeJSONObjectRequest("PollingList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), PollingListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject PollingListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.pollingListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("PollingList", "" + dataSet);
        return dataSet;
    }

    public void syncButtonVisibility(){
        dbData.open();
        if(dbData.getSavedDetails().size()!=0){
            dashboardBinding.syncData.setVisibility(View.VISIBLE);
        }
        else {
            dashboardBinding.syncData.setVisibility(View.GONE);
        }
    }
    public void loadActivityListSpinner(String activity_type) {
        Cursor ActList = null;
        ActList = db.rawQuery("SELECT * FROM " + DBHelper.ACTIVITY_LIST + " where activity_type = '" + activity_type + "'order by activity_description desc", null);

        activityArrayList.clear();

        if (ActList.getCount() > 0) {
            if (ActList.moveToFirst()) {
                do {
                    ElectionProject list = new ElectionProject();
                    String ACTIVITY_ID = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_ID));
                    String ACTIVITY_DESCRIPTION = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_DESCRIPTION));
                    String ACTIVITY_BY = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_BY));
                    String ACTIVITY_TYPE = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE));

                    list.setActivity_id(ACTIVITY_ID);
                    list.setActivity_description(ACTIVITY_DESCRIPTION);
                    list.setActivity_by(ACTIVITY_BY);
                    list.setActivity_type(ACTIVITY_TYPE);

                    activityArrayList.add(list);
                } while (ActList.moveToNext());
            }
            Log.d("activityListsize", "" + activityArrayList.size());

        }
//        Collections.sort(activityArrayList, (lhs, rhs) -> lhs.getActivity_description().compareTo(rhs.getActivity_description()));

        if(activityArrayList.size() > 0) {
            activityListAdapter = new ActivityListAdapter(Dashboard.this, activityArrayList);
            recyclerActivityList.setAdapter(activityListAdapter);
            recyclerActivityList.scheduleLayoutAnimation();

        }else {

        }
    }

/*
    public void loadActivityListSpinner(String activity_type) {
        Cursor ActList = null;
        ActList = db.rawQuery("SELECT * FROM " + DBHelper.ACTIVITY_LIST + " where activity_type = '" + activity_type + "'order by activity_description asc", null);

        activityList.clear();
        activityListOrdered.clear();

        if (ActList.getCount() > 0) {
            if (ActList.moveToFirst()) {
                do {
                    ElectionProject list = new ElectionProject();
                    String ACTIVITY_ID = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_ID));
                    String ACTIVITY_DESCRIPTION = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_DESCRIPTION));
                    String ACTIVITY_BY = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_BY));
                    String ACTIVITY_TYPE = ActList.getString(ActList.getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE));

                    list.setActivity_id(ACTIVITY_ID);
                    list.setActivity_description(ACTIVITY_DESCRIPTION);
                    list.setActivity_by(ACTIVITY_BY);
                    list.setActivity_type(ACTIVITY_TYPE);

                    activityListOrdered.add(list);
                } while (ActList.moveToNext());
            }
            Log.d("activityListsize", "" + activityListOrdered.size());

        }
        Collections.sort(activityListOrdered, (lhs, rhs) -> lhs.getActivity_description().compareTo(rhs.getActivity_description()));
        ElectionProject ListValue = new ElectionProject();
        ListValue.setActivity_description("Select Activity");
        activityList.add(ListValue);
        for (int i = 0; i < activityListOrdered.size(); i++) {
            ElectionProject habList = new ElectionProject();
            String ACTIVITY_ID = activityListOrdered.get(i).getActivity_id();
            String ACTIVITY_DESCRIPTION = activityListOrdered.get(i).getActivity_description();
            String ACTIVITY_BY = activityListOrdered.get(i).getActivity_by();
            String ACTIVITY_TYPE = activityListOrdered.get(i).getActivity_type();

            habList.setActivity_id(ACTIVITY_ID);
            habList.setActivity_description(ACTIVITY_DESCRIPTION);
            habList.setActivity_by(ACTIVITY_BY);
            habList.setActivity_type(ACTIVITY_TYPE);

            activityList.add(habList);
        }
        dashboardBinding.activitySpinner.setAdapter(new CommonAdapter(this, activityList, "activityList"));
    }
*/

    public void loadActivityTypeSpinner() {
        Cursor TypeList = null;
        TypeList = db.rawQuery("SELECT * FROM " + DBHelper.ACTIVITY_TYPE_LIST , null);

        activityTypeList.clear();

        if (TypeList.getCount() > 0) {
            if (TypeList.moveToFirst()) {
                do {
                    ElectionProject list = new ElectionProject();
                    String ACTIVITY_TYPE = TypeList.getString(TypeList.getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE));
                    String ACTIVITY_TYPE_DESC = TypeList.getString(TypeList.getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE_DESC));
                    String DISPLAY_ORDER = TypeList.getString(TypeList.getColumnIndexOrThrow(AppConstant.DISPLAY_ORDER));

                    list.setActivity_type(ACTIVITY_TYPE);
                    list.setActivity_type_desc(ACTIVITY_TYPE_DESC);
                    list.setDisplay_order(DISPLAY_ORDER);

                    activityTypeList.add(list);
                } while (TypeList.moveToNext());
            }
            Log.d("activityTypespinnersize", "" + activityTypeList.size());

        }
        Collections.sort(activityTypeList, (lhs, rhs) -> lhs.getActivity_type_desc().compareTo(rhs.getActivity_type_desc()));
        if(activityTypeList.size() > 0) {
            activityTypeAdapter = new ActivityTypeAdapter(Dashboard.this, activityTypeList);
            recyclerActivityType.setAdapter(activityTypeAdapter);
            recyclerActivityType.scrollToPosition(0);
        }else {

        }
    }
/*
    public void loadActivityTypeSpinner() {
        Cursor TypeList = null;
        TypeList = db.rawQuery("SELECT * FROM " + DBHelper.ACTIVITY_TYPE_LIST , null);

        activityType.clear();
        activityTypeOrdered.clear();

        if (TypeList.getCount() > 0) {
            if (TypeList.moveToFirst()) {
                do {
                    ElectionProject list = new ElectionProject();
                    String ACTIVITY_TYPE = TypeList.getString(TypeList.getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE));
                    String ACTIVITY_TYPE_DESC = TypeList.getString(TypeList.getColumnIndexOrThrow(AppConstant.ACTIVITY_TYPE_DESC));
                    String DISPLAY_ORDER = TypeList.getString(TypeList.getColumnIndexOrThrow(AppConstant.DISPLAY_ORDER));

                    list.setActivity_type(ACTIVITY_TYPE);
                    list.setActivity_type_desc(ACTIVITY_TYPE_DESC);
                    list.setDisplay_order(DISPLAY_ORDER);

                    activityTypeOrdered.add(list);
                } while (TypeList.moveToNext());
            }
            Log.d("activityTypespinnersize", "" + activityTypeOrdered.size());

        }
        Collections.sort(activityTypeOrdered, (lhs, rhs) -> lhs.getActivity_type_desc().compareTo(rhs.getActivity_type_desc()));
        ElectionProject ListValue = new ElectionProject();
        ListValue.setActivity_type_desc("Select Type");
        activityType.add(ListValue);
        for (int i = 0; i < activityTypeOrdered.size(); i++) {
            ElectionProject activityTypeList = new ElectionProject();
            String ACTIVITY_TYPE = activityTypeOrdered.get(i).getActivity_type();
            String ACTIVITY_TYPE_DESC = activityTypeOrdered.get(i).getActivity_type_desc();
            String DISPLAY_ORDER = activityTypeOrdered.get(i).getDisplay_order();

            activityTypeList.setActivity_type(ACTIVITY_TYPE);
            activityTypeList.setActivity_type_desc(ACTIVITY_TYPE_DESC);
            activityTypeList.setDisplay_order(DISPLAY_ORDER);

            activityType.add(activityTypeList);
        }
        dashboardBinding.typeSpinner.setAdapter(new CommonAdapter(this, activityType, "activityTypeList"));
    }
*/




    public void showActivityScreen() {
        if ((dashboardBinding.typeSpinner.getSelectedItem() != null) &&(!"Select Type".equalsIgnoreCase(activityType.get(dashboardBinding.typeSpinner.getSelectedItemPosition()).getActivity_type_desc()))
                && (activityType.get(dashboardBinding.typeSpinner.getSelectedItemPosition()).getActivity_type_desc() != null)){
            if ((dashboardBinding.activitySpinner.getSelectedItem() != null) &&(!"Select Activity".equalsIgnoreCase(activityList.get(dashboardBinding.activitySpinner.getSelectedItemPosition()).getActivity_description()))
                    && (activityList.get(dashboardBinding.activitySpinner.getSelectedItemPosition()).getActivity_description() != null)){
                showPollingActivityScreen();
            }else{
                Utils.showAlert(Dashboard.this,"Select Activity! ");
            }
        }
        else{
            Utils.showAlert(Dashboard.this,"Select Type! ");
        }
    }
    public void showPollingActivityScreen() {
        Intent intent = new Intent(Dashboard.this, PollingStationList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
    }
    public void showPendingScreen() {
        Intent intent = new Intent(Dashboard.this, PendingListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
    }

    private void dataFromServer() {
        Intent intent = new Intent(this, ViewServerDataScreen.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }


    @Override
    public void onButtonClick(AlertDialog alertDialog, String type) {
        alertDialog.dismiss();
        if ("Exit".equalsIgnoreCase(type)) {
            onBackPressed();
        } else {

            Intent intent = new Intent(this, LoginScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("EXIT", false);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }


    public void logout() {
        dbData.open();
        ArrayList<ElectionProject> Count = dbData.getSavedDetails();
        if (!Utils.isOnline()) {
            Utils.showAlert(this, "Logging out while offline may leads to loss of data!");
        } else {
            if (!(Count.size() > 0)) {
                closeApplication();
            } else {
                Utils.showAlert(this, "Sync all the data before logout!");
            }
        }
    }

    public void closeApplication() {
        new MyDialog(this).exitDialog(this, "Are you sure you want to Logout?", "Logout");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                new MyDialog(this).exitDialog(this, "Are you sure you want to exit ?", "Exit");
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("PollingList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertPollingListTask().execute(jsonObject);
                }
                Log.d("PollingListRes", "" + responseDecryptedBlockKey);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class InsertPollingListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.deleteSavedPollingStationList();
            dbData.open();
            ArrayList<ElectionProject> all_kvvtListCount = dbData.getAll_ServerList();
            if (all_kvvtListCount.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ElectionProject viewServerListData = new ElectionProject();
                        try {
                            viewServerListData.setActivity_by(jsonArray.getJSONObject(i).getString("activity_by"));
                            viewServerListData.setRo_zone_id(jsonArray.getJSONObject(i).getString("ro_zone_id"));
                            viewServerListData.setRo_level_id(jsonArray.getJSONObject(i).getString("ro_level_id"));
                            viewServerListData.setDcode(jsonArray.getJSONObject(i).getString("dcode"));
                            viewServerListData.setBcode(jsonArray.getJSONObject(i).getString("bcode"));
                            viewServerListData.setLb_type(jsonArray.getJSONObject(i).getString("lb_type"));
                            viewServerListData.setPolling_booth_id(jsonArray.getJSONObject(i).getString("polling_booth_id"));
                            viewServerListData.setActivity_id(jsonArray.getJSONObject(i).getString("activity_id"));
                            viewServerListData.setActivity_remark(jsonArray.getJSONObject(i).getString("activity_remark"));
                            viewServerListData.setLbpolling_station_no(jsonArray.getJSONObject(i).getString("lbpolling_station_no"));
                            viewServerListData.setPolling_booth_name(jsonArray.getJSONObject(i).getString("polling_booth_name"));
                            viewServerListData.setLlpolling_booth_name(jsonArray.getJSONObject(i).getString("llpolling_booth_name"));
                            viewServerListData.setPvname(jsonArray.getJSONObject(i).getString("pvname"));
                            viewServerListData.setActivity_name(jsonArray.getJSONObject(i).getString("activity_name"));
                            viewServerListData.setActivity_type(jsonArray.getJSONObject(i).getString("activity_type_id"));
                            viewServerListData.setActivity_type_name(jsonArray.getJSONObject(i).getString("activity_type_name"));
                            viewServerListData.setActivity_status(jsonArray.getJSONObject(i).getString("activity_status"));
                            dbData.insertViewServerData(viewServerListData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressHUD = ProgressHUD.show(Dashboard.this, "Downloading", true, false, null);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(progressHUD!=null){
                progressHUD.cancel();
            }
            dataFromServer();

        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }


    @Override
    public void onClick(View view) {

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        syncButtonVisibility();
    }

}
