package com.nic.tnsecPollingPersonnel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.activity.Dashboard;
import com.nic.tnsecPollingPersonnel.activity.ViewServerDataScreen;
import com.nic.tnsecPollingPersonnel.databinding.ActivityListAdapterBinding;
import com.nic.tnsecPollingPersonnel.databinding.ActivityListAdapterServerBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import java.util.ArrayList;

public class ActivityListAdapterForServerData extends RecyclerView.Adapter<ActivityListAdapterForServerData.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private ArrayList<ElectionProject> projectList;
    private LayoutInflater layoutInflater;

    public ActivityListAdapterForServerData(Activity context, ArrayList<ElectionProject> projectList) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.projectList = projectList;
    }

    @Override
    public ActivityListAdapterForServerData.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ActivityListAdapterServerBinding activityListAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.activity_list_adapter_server, viewGroup, false);
        return new ActivityListAdapterForServerData.MyViewHolder(activityListAdapterBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ActivityListAdapterServerBinding activityListAdapterBinding;

        public MyViewHolder(ActivityListAdapterServerBinding Binding) {
            super(Binding.getRoot());
            activityListAdapterBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
      holder.activityListAdapterBinding.activityName.setText(projectList.get(position).getActivity_description());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setActivityId(projectList.get(position).getActivity_id());
                prefManager.setActivityName(projectList.get(position).getActivity_description());
                ((ViewServerDataScreen)context).getServerList();
            }
        });

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }


}
