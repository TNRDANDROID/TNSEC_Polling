package com.nic.tnsecPollingPersonnel.adapter;

import android.app.Activity;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.activity.Dashboard;
import com.nic.tnsecPollingPersonnel.activity.PendingListActivity;
import com.nic.tnsecPollingPersonnel.constant.AppConstant;
import com.nic.tnsecPollingPersonnel.databinding.ActivityTypeAdapterBinding;
import com.nic.tnsecPollingPersonnel.databinding.PendingAdapterBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityTypeAdapter extends RecyclerView.Adapter<ActivityTypeAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private ArrayList<ElectionProject> projectList;
    private LayoutInflater layoutInflater;
    int row_index=-1;

    public ActivityTypeAdapter(Activity context, ArrayList<ElectionProject> projectList) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.projectList = projectList;
    }

    @Override
    public ActivityTypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ActivityTypeAdapterBinding activityTypeAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.activity_type_adapter, viewGroup, false);
        return new ActivityTypeAdapter.MyViewHolder(activityTypeAdapterBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ActivityTypeAdapterBinding activityTypeAdapterBinding;

        public MyViewHolder(ActivityTypeAdapterBinding Binding) {
            super(Binding.getRoot());
            activityTypeAdapterBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
      holder.activityTypeAdapterBinding.activityType.setText(projectList.get(position).getActivity_type_desc());
        holder.activityTypeAdapterBinding.activityType.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.activityTypeAdapterBinding.activityTypeLayout.setBackground(context.getResources().getDrawable(R.drawable.round_button_white_shadow));

      holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              row_index=position;
              prefManager.setActivityType(projectList.get(position).getActivity_type());
              ((Dashboard)context).loadActivityListSpinner(projectList.get(position).getActivity_type());
              notifyDataSetChanged();
          }
      });
        if(row_index==position){
            holder.activityTypeAdapterBinding.activityType.setTextColor(context.getResources().getColor(R.color.white));
            holder.activityTypeAdapterBinding.activityTypeLayout.setBackground(context.getResources().getDrawable(R.drawable.round_colored_button_white_shadow));
        }
        else
        {
            holder.activityTypeAdapterBinding.activityType.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.activityTypeAdapterBinding.activityTypeLayout.setBackground(context.getResources().getDrawable(R.drawable.round_button_white_shadow));

        }

    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }


}
