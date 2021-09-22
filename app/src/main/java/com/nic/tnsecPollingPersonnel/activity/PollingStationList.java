package com.nic.tnsecPollingPersonnel.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
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
import com.nic.tnsecPollingPersonnel.adapter.PollingStationAdapter;
import com.nic.tnsecPollingPersonnel.api.Api;
import com.nic.tnsecPollingPersonnel.api.ServerResponse;
import com.nic.tnsecPollingPersonnel.databinding.PollingStationListBinding;
import com.nic.tnsecPollingPersonnel.dialog.MyDialog;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PollingStationList extends AppCompatActivity implements MyDialog.myOnClickListener, Api.ServerResponseListener, View.OnClickListener {


    private PollingStationListBinding pollingStationListBinding;
    Animation smalltobig, stb2;
    final Handler handler = new Handler();
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private RecyclerView recyclerView;
    private PollingStationAdapter adapter;
    public dbData dbData = new dbData(this);
    ArrayList<ElectionProject> electionProjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pollingStationListBinding = DataBindingUtil.setContentView(this, R.layout.polling_station_list);
        pollingStationListBinding.setActivity(this);
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
        recyclerView = pollingStationListBinding.pollingStationList;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        dbData.open();
        electionProjects = new ArrayList<>();
        electionProjects=dbData.getAll_PollingStationList();

/*
        Collections.sort(electionProjects, new Comparator<ElectionProject>() {
            public int compare(ElectionProject lhs, ElectionProject rhs) {
                return Integer.parseInt(lhs.getPolling_station_no()) - Integer.parseInt(rhs.getPolling_station_no());
            }
        });
*/
        adapter = new PollingStationAdapter(PollingStationList.this, electionProjects);
        recyclerView.setAdapter(adapter);
    }


    public void updateUnCheckStatus(ArrayList<ElectionProject> electionList, int position){
        electionProjects = new ArrayList<>();
        electionProjects = new ArrayList<ElectionProject>(electionList);
        electionProjects.get(position).setSave_status("No");
        electionProjects.get(position).setIsChecked_status("No");
        electionProjects.get(position).setUnChecked_status("No");
        adapter.notifyDataSetChanged();
    }
    public void updateYesStatus(ArrayList<ElectionProject> electionList, int position){
        electionProjects = new ArrayList<>();
        electionProjects = new ArrayList<ElectionProject>(electionList);
        electionProjects.get(position).setSave_status("No");
        electionProjects.get(position).setIsChecked_status("Yes");
        electionProjects.get(position).setUnChecked_status("No");
        adapter.notifyDataSetChanged();
    }
    public void updateNoStatus(ArrayList<ElectionProject> electionList, int position){
        electionProjects = new ArrayList<>();
        electionProjects = new ArrayList<ElectionProject>(electionList);
        electionProjects.get(position).setSave_status("No");
        electionProjects.get(position).setIsChecked_status("No");
        electionProjects.get(position).setUnChecked_status("Yes");
        adapter.notifyDataSetChanged();
    }
    public void remarkStatus(ArrayList<ElectionProject> electionList, int position, String remark){
        electionProjects = new ArrayList<>();
        electionProjects = new ArrayList<ElectionProject>(electionList);
        electionProjects.get(position).setSave_status("No");
        electionProjects.get(position).setGet_remark_text(remark);
        adapter.notifyDataSetChanged();
    }

    public void showHomeScreen() {
        Intent intent = new Intent(PollingStationList.this, Dashboard.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
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
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }
    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
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

        } catch (JSONException e) {
            e.printStackTrace();
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


}
