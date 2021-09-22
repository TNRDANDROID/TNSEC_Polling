package com.nic.tnsecPollingPersonnel.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.VolleyError;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

import com.nic.tnsecPollingPersonnel.DataBase.DBHelper;
import com.nic.tnsecPollingPersonnel.DataBase.dbData;
import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.adapter.CommonAdapter;
import com.nic.tnsecPollingPersonnel.adapter.ViewServerDataListAdapter;
import com.nic.tnsecPollingPersonnel.api.Api;
import com.nic.tnsecPollingPersonnel.api.ServerResponse;
import com.nic.tnsecPollingPersonnel.constant.AppConstant;
import com.nic.tnsecPollingPersonnel.databinding.ViewServerDataScreenBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewServerDataScreen extends AppCompatActivity implements Api.ServerResponseListener {
    public ViewServerDataScreenBinding viewServerDataScreenBinding;
    private ShimmerRecyclerView recyclerView;
    private ViewServerDataListAdapter viewServerDataListAdapter;
    public dbData dbData = new dbData(this);
    private SearchView searchView;
    private PrefManager prefManager;
    String pref_Village;

    private List<ElectionProject> activityType = new ArrayList<>();
    private List<ElectionProject> activityTypeOrdered = new ArrayList<>();
    private List<ElectionProject> activityList = new ArrayList<>();
    private List<ElectionProject> activityListOrdered = new ArrayList<>();

    public static SQLiteDatabase db;
    public static DBHelper dbHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewServerDataScreenBinding = DataBindingUtil.setContentView(this, R.layout.view_server_data_screen);
        viewServerDataScreenBinding.setActivity(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        prefManager = new PrefManager(this);

        setSupportActionBar(viewServerDataScreenBinding.toolbar);
        initRecyclerView();

        viewServerDataScreenBinding.serverDataList.setVisibility(View.GONE);
        viewServerDataScreenBinding.notFoundTv.setVisibility(View.GONE);
        viewServerDataScreenBinding.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    String Activity_type = activityType.get(position).getActivity_type();
                    prefManager.setActivityType(Activity_type);
                    loadActivityListSpinner(Activity_type);
                    viewServerDataScreenBinding.activityList.setSelection(0);
                }else {
                    prefManager.setActivityType("");
                    prefManager.setActivityId("");
                    prefManager.setActivityName("");
                    viewServerDataScreenBinding.activityList.setSelection(0);
                    viewServerDataScreenBinding.activityList.setAdapter(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        viewServerDataScreenBinding.activityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position > 0) {
                    String Activity_id = activityList.get(position).getActivity_id();
                    String Activity_name = activityList.get(position).getActivity_description();
                    prefManager.setActivityId(Activity_id);
                    prefManager.setActivityName(Activity_name);
                    new fetchScheduletask().execute();
                }else {
                    prefManager.setActivityId("");
                    prefManager.setActivityName("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        loadActivityTypeSpinner();
    }


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
        viewServerDataScreenBinding.activityList.setAdapter(new CommonAdapter(this, activityList, "activityList"));
    }

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
        viewServerDataScreenBinding.typeSpinner.setAdapter(new CommonAdapter(this, activityType, "activityTypeList"));
    }

    private void initRecyclerView() {
        recyclerView = viewServerDataScreenBinding.serverDataList;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
    }

    public class fetchScheduletask extends AsyncTask<Void, Void,
            ArrayList<ElectionProject>> {
        @Override
        protected ArrayList<ElectionProject> doInBackground(Void... params) {
            dbData.open();
            ArrayList<ElectionProject> savedAllList = new ArrayList<>();
            savedAllList = dbData.getServerList(prefManager.getActivityType(),prefManager.getActivityId());
            Log.d("savedList_COUNT", String.valueOf(savedAllList.size()));
            return savedAllList;
        }

        @Override
        protected void onPostExecute(ArrayList<ElectionProject> savedList) {
            super.onPostExecute(savedList);

            viewServerDataListAdapter = new ViewServerDataListAdapter(ViewServerDataScreen.this, savedList);
            if (savedList.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                viewServerDataScreenBinding.notFoundTv.setVisibility(View.GONE);
                recyclerView.setAdapter(viewServerDataListAdapter);
                recyclerView.showShimmerAdapter();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadCards();
                    }
                }, 1000);
            } else {
                recyclerView.setVisibility(View.GONE);
                viewServerDataScreenBinding.notFoundTv.setVisibility(View.VISIBLE);
            }
        }


    }

    private void loadCards() {

        recyclerView.hideShimmerAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        ImageView searchIcon=
                searchView.findViewById(androidx.appcompat.R.id.search_button);
        SearchView.SearchAutoComplete searchText=
                searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        ImageView closeIcon=
                searchView.findViewById(androidx.appcompat.R.id.search_close_btn);

        searchText.setHint("Search by Id");
        searchText.setTextColor(getResources().getColor(R.color.white));
        searchText.setHintTextColor(getResources().getColor(R.color.grey2));
        searchIcon.setColorFilter(getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);
        closeIcon.setColorFilter(getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                viewServerDataListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                viewServerDataListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {

    }



    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void onBackPress() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

}
