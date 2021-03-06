package com.nic.tnsecPollingPersonnel.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.android.volley.VolleyError;
import com.nic.tnsecPollingPersonnel.DataBase.DBHelper;
import com.nic.tnsecPollingPersonnel.DataBase.dbData;
import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.api.Api;
import com.nic.tnsecPollingPersonnel.api.ApiService;
import com.nic.tnsecPollingPersonnel.api.ServerResponse;
import com.nic.tnsecPollingPersonnel.constant.AppConstant;
import com.nic.tnsecPollingPersonnel.databinding.LoginScreenBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;
import com.nic.tnsecPollingPersonnel.utils.UrlGenerator;
import com.nic.tnsecPollingPersonnel.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Kavitha on 03_09_2021.
 */

public class LoginScreen extends AppCompatActivity implements View.OnClickListener, Api.ServerResponseListener {

    private String randString;
    JSONObject jsonObject;
    private PrefManager prefManager;
    public dbData dbData = new dbData(this);
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

public LoginScreenBinding loginScreenBinding;

    Animation stb2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.login_screen);
        loginScreenBinding.setActivity(this);
       /* loginScreenBinding.scrollView.setVerticalScrollBarEnabled(false);
        loginScreenBinding.scrollView.isSmoothScrollingEnabled();*/
//        WindowPreferencesManager windowPreferencesManager = new WindowPreferencesManager(this);
//        windowPreferencesManager.applyEdgeToEdgePreference(getWindow());
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        intializeUI();


    }

    public void intializeUI() {
        prefManager = new PrefManager(this);
        stb2 = AnimationUtils.loadAnimation(this, R.anim.stb2);
        loginScreenBinding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);


        loginScreenBinding.password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    checkLoginScreen();
                }
                return false;
            }
        });

        loginScreenBinding.password.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Poppins-Regular.ttf"));
        loginScreenBinding.tvVersionNumber.setTranslationY(400);
        loginScreenBinding.nicName.setTranslationY(400);
//        loginScreenBinding.btnBuy.setTranslationY(400);

        loginScreenBinding.ivItemOne.setTranslationX(800);
        loginScreenBinding.ivItemTwo.setTranslationX(800);


//        loginScreenBinding.btnBuy.setAlpha(0);

        loginScreenBinding.tvVersionNumber.setAlpha(0);
        loginScreenBinding.nicName.setAlpha(0);

        loginScreenBinding.ivItemOne.setAlpha(0);
        loginScreenBinding.ivItemTwo.setAlpha(0);


//        loginScreenBinding.btnBuy.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();

        loginScreenBinding.tvVersionNumber.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
        loginScreenBinding.nicName.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(100).start();

        loginScreenBinding.ivItemOne.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(200).start();
        loginScreenBinding.ivItemTwo.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();

        loginScreenBinding.ivIlls.startAnimation(stb2);
        randString = Utils.randomChar();



        try {
            String versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
            loginScreenBinding.tvVersionNumber.setText("Version" + " " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    public boolean validate() {
        boolean valid = true;
        String username = loginScreenBinding.username.getText().toString().trim();
        prefManager.setUserName(username);
        String password = loginScreenBinding.password.getText().toString().trim();


        if (username.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the username");
        } else if (password.isEmpty()) {
            valid = false;
            Utils.showAlert(this, "Please enter the password");
        }
        return valid;
    }

    public void checkLoginScreen() {
        loginScreenBinding.username.setText("zpkpmkdtr1"); // loc
        loginScreenBinding.password.setText("test123#$");

        /*loginScreenBinding.username.setText("zpvelgthm7"); //prod
        loginScreenBinding.password.setText("test123#$");*/

        final String username = loginScreenBinding.username.getText().toString().trim();
        final String password = loginScreenBinding.password.getText().toString().trim();
        prefManager.setUserPassword(password);

        if (Utils.isOnline()) {
            if (!validate())
                return;
            else if (prefManager.getUserName().length() > 0 && password.length() > 0) {
                new ApiService(this).makeRequest("LoginScreen", Api.Method.POST, UrlGenerator.getLoginUrl(), loginParams(), "not cache", this);
            } else {
                Utils.showAlert(this, "Please enter your username and password!");
            }
        } else {
            //Utils.showAlert(this, getResources().getString(R.string.no_internet));
            AlertDialog.Builder ab = new AlertDialog.Builder(
                    LoginScreen.this);
            ab.setMessage("Internet Connection is not avaliable..Please Turn ON Network Connection OR Continue With Off-line Mode..");
            ab.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                            startActivity(I);
                        }
                    });
            ab.setNegativeButton("Continue With Off-Line",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            offline_mode(username, password);
                        }
                    });
            ab.show();
        }

    }

    public Map<String, String> loginParams() {
        Map<String, String> params = new HashMap<>();
        params.put(AppConstant.KEY_SERVICE_ID, "login");

        String random = Utils.randomChar();

        params.put(AppConstant.USER_LOGIN_KEY, random);
        Log.d("randchar", "" + random);

        params.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        Log.d("user", "" + loginScreenBinding.username.getText().toString().trim());

        String encryptUserPass = Utils.getSHA512(loginScreenBinding.password.getText().toString().trim());
        String encryptUserPassword = Utils.md5(encryptUserPass);
        prefManager.setEncryptPass(encryptUserPass);
        Log.d("SHA512", "" + encryptUserPass);

        String userPass = encryptUserPass.concat(random);
        Log.d("userpass", "" + userPass);
        String sha256 = Utils.getSHA(userPass);
        Log.d("sha256", "" + sha256);

        params.put(AppConstant.KEY_USER_PASSWORD, sha256);
        params.put(AppConstant.KEY_APP_CODE,"P");
        params.put(AppConstant.KEY_APP_ID,"com.nic.tnsecPollingPersonnel");

        Log.d("user", "" + loginScreenBinding.username.getText().toString().trim());

        Log.d("params", "" + params);
        return params;
    }

    //The method for opening the registration page and another processes or checks for registering


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            JSONObject loginResponse = serverResponse.getJsonResponse();
            String urlType = serverResponse.getApi();

            if ("LoginScreen".equals(urlType)) {
                String status = loginResponse.getString(AppConstant.KEY_STATUS);
                String response = loginResponse.getString(AppConstant.KEY_RESPONSE);
                if (status.equalsIgnoreCase("OK")) {
                    if (response.equals("LOGIN_SUCCESS")) {
                        Log.d("loginResponse", "" + loginResponse);
                        String key =  loginResponse.getString(AppConstant.KEY_USER);
                        String user_data =  loginResponse.getString(AppConstant.USER_DATA);
                        String decryptedKey = Utils.decrypt(prefManager.getEncryptPass(), key);
                        String userDataDecrypt = Utils.decrypt(prefManager.getEncryptPass(), user_data);
                        Log.d("userdatadecry", "" + userDataDecrypt);
                        jsonObject = new JSONObject(userDataDecrypt);
                        prefManager.setDistrictCode(jsonObject.getString(AppConstant.DISTRICT_CODE));
                        prefManager.setBlockCode(jsonObject.getString(AppConstant.BLOCK_CODE));
                        prefManager.setRoZoneId(jsonObject.getString(AppConstant.RO_ZONE_ID));
                        prefManager.setZonalPartyName(jsonObject.getString(AppConstant.ZONAL_PARTY_NAME));

                        prefManager.setUserPassKey(decryptedKey);
                        getActivityTypeList();
                        getActivityList();
                        getPollingStationList();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showHomeScreen();
                            }
                        }, 1000);

                    } else {
                        if (response.equals("LOGIN_FAILED")) {
                            Utils.showAlert(this, "Invalid UserName Or Password");
                        }
                    }
                }

            }
            if ("ActivityTypeList".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertActivityTypeListTask().execute(jsonObject);
                }
                Log.d("ActivityTypeListRes", "" + responseDecryptedBlockKey);
            }
            if ("ActivityList".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertActivityListTask().execute(jsonObject);
                }
                Log.d("ActivityListRes", "" + responseDecryptedBlockKey);
            }
            if ("PollingStationList".equals(urlType) && loginResponse != null) {
                String key = loginResponse.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new InsertPollingStationListTask().execute(jsonObject);
                }
                Log.d("PollingStationList", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getActivityTypeList() {
        try {
            new ApiService(this).makeJSONObjectRequest("ActivityTypeList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), ActivityTypeListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getActivityList() {
        try {
            new ApiService(this).makeJSONObjectRequest("ActivityList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), ActivityListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getPollingStationList() {
        try {
            new ApiService(this).makeJSONObjectRequest("PollingStationList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), PollingStationListJsonParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject PollingStationListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.PollingStationListParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("ActivityList", "" + dataSet);
        return dataSet;
    }
    public JSONObject ActivityListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.ActivityListParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("ActivityList", "" + dataSet);
        return dataSet;
    }
    public JSONObject ActivityTypeListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.ActivityTypeListParams(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("ActivityTypeList", "" + dataSet);
        return dataSet;
    }

    public class InsertActivityTypeListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<ElectionProject> count = dbData.getAll_ActivityType();
            if (count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ElectionProject ListValue = new ElectionProject();
                        try {
                            ListValue.setActivity_type(jsonArray.getJSONObject(i).getString(AppConstant.ACTIVITY_TYPE));
                            ListValue.setActivity_type_desc(jsonArray.getJSONObject(i).getString(AppConstant.ACTIVITY_TYPE_DESC));
                            ListValue.setDisplay_order(jsonArray.getJSONObject(i).getString(AppConstant.DISPLAY_ORDER));
                            dbData.insertActivityType(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }
    public class InsertActivityListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<ElectionProject> count = dbData.getAll_ActivityList();
            if (count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ElectionProject ListValue = new ElectionProject();
                        try {
                            ListValue.setActivity_id(jsonArray.getJSONObject(i).getString(AppConstant.ACTIVITY_ID));
                            ListValue.setActivity_description(jsonArray.getJSONObject(i).getString(AppConstant.ACTIVITY_DESCRIPTION));
                            ListValue.setActivity_by(jsonArray.getJSONObject(i).getString(AppConstant.ACTIVITY_BY));
                            ListValue.setActivity_type(jsonArray.getJSONObject(i).getString(AppConstant.ACTIVITY_TYPE));
                            dbData.insertActivityList(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }
    public class InsertPollingStationListTask extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {
            dbData.open();
            ArrayList<ElectionProject> count = dbData.getAll_PollingStationList();
            if (count.size() <= 0) {
                if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ElectionProject ListValue = new ElectionProject();
                        try {
                            ListValue.setPolling_station_no(jsonArray.getJSONObject(i).getString(AppConstant.LPOLLING_STATION_NO));
                            ListValue.setPolling_booth_name(jsonArray.getJSONObject(i).getString(AppConstant.POLLING_BOOTH_NAME));
                            ListValue.setPolling_booth_id(jsonArray.getJSONObject(i).getString(AppConstant.POLLING_BOOTH_ID));
                            ListValue.setDcode(jsonArray.getJSONObject(i).getString(AppConstant.DISTRICT_CODE));
                            ListValue.setBcode(jsonArray.getJSONObject(i).getString(AppConstant.BLOCK_CODE));
                            ListValue.setPvcode(jsonArray.getJSONObject(i).getString(AppConstant.PV_CODE));
                            ListValue.setPvname(jsonArray.getJSONObject(i).getString(AppConstant.PV_NAME));
                            ListValue.setLlpolling_booth_name(jsonArray.getJSONObject(i).getString(AppConstant.LLPOLLING_BOOTH_NAME));
                            ListValue.setSave_status("No");
                            ListValue.setIsChecked_status("No");
                            ListValue.setUnChecked_status("No");
                            ListValue.setGet_remark_text("");

                            dbData.insertPollingStationList(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
            return null;
        }

    }
    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, "Server Error!");
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        showHomeScreen();
//    }

    public void showHomeScreen() {
        Intent intent = new Intent(LoginScreen.this, Dashboard.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.fleft, R.anim.fhelper);
    }

    public void offline_mode(String name, String pass) {
        String userName = prefManager.getUserName();
        String password = prefManager.getUserPassword();
        if (name.equals(userName) && pass.equals(password)) {
            showHomeScreen();
        } else {
            Utils.showAlert(this, "No data available for offline. Please Turn On Your Network");
        }
    }
}
