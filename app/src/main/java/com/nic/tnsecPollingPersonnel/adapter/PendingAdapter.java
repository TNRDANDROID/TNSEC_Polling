package com.nic.tnsecPollingPersonnel.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
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

import com.nic.tnsecPollingPersonnel.DataBase.DBHelper;
import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.activity.PendingListActivity;
import com.nic.tnsecPollingPersonnel.constant.AppConstant;
import com.nic.tnsecPollingPersonnel.databinding.PendingAdapterBinding;
import com.nic.tnsecPollingPersonnel.databinding.PollingStaionRecyclerItemBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.nic.tnsecPollingPersonnel.activity.Dashboard.db;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private ArrayList<ElectionProject> projectList;
    private LayoutInflater layoutInflater;

    public PendingAdapter(Activity context, ArrayList<ElectionProject> projectList) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.projectList = projectList;
    }

    @Override
    public PendingAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        PendingAdapterBinding pendingAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.pending_adapter, viewGroup, false);
        return new PendingAdapter.MyViewHolder(pendingAdapterBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private PendingAdapterBinding pendingAdapterBinding;

        public MyViewHolder(PendingAdapterBinding Binding) {
            super(Binding.getRoot());
            pendingAdapterBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
      holder.pendingAdapterBinding.pvname.setText(projectList.get(position).getPvname());
      holder.pendingAdapterBinding.pollingStationNo.setText("- "+projectList.get(position).getPolling_station_no());
      holder.pendingAdapterBinding.pollingBoothName.setText(projectList.get(position).getPolling_booth_name());
      holder.pendingAdapterBinding.activityName.setText(projectList.get(position).getActivity_description());
      holder.pendingAdapterBinding.remark.setText(projectList.get(position).getActivity_remark());

      if(projectList.get(position).getYes_no().equalsIgnoreCase("Y")){
          holder.pendingAdapterBinding.statusVal.setText("Yes");
          holder.pendingAdapterBinding.statusVal.setBackground(context.getResources().getDrawable(R.drawable.button_background_green));
      }else {
          holder.pendingAdapterBinding.statusVal.setText("No");
          holder.pendingAdapterBinding.statusVal.setBackground(context.getResources().getDrawable(R.drawable.button_background_red));
      }
        if(projectList.get(position).getPolling_booth_name() != null && (holder.pendingAdapterBinding.pollingBoothName.getLineCount()>2 ||  projectList.get(position).getPolling_booth_name().length() > 10))
        {
            addReadMore(projectList.get(position).getPolling_booth_name(), holder.pendingAdapterBinding.pollingBoothName);
        }

        if(projectList.get(position).getActivity_remark() != null && (holder.pendingAdapterBinding.remark.getLineCount()>2 ||  projectList.get(position).getActivity_remark().length() > 10))
        {
            addReadMore(projectList.get(position).getActivity_remark(), holder.pendingAdapterBinding.remark);
        }

      holder.pendingAdapterBinding.go.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              uploadData(position);
          }
      });
      holder.pendingAdapterBinding.delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              db.delete(DBHelper.SAVE_DATA,"polling_booth_id = ? and activity_id = ? and activity_type = ? "
                      ,new String[] {projectList.get(position).getPolling_booth_id()
                              ,projectList.get(position).getActivity_id(),
                              projectList.get(position).getActivity_type()});
              projectList.remove(position);
              notifyItemRemoved(position);
              notifyItemRangeChanged(position,projectList.size());
              ((PendingListActivity) context).loadPendingList();
          }
      });

    }

    private void addReadMore(final String text, final TextView textView) {
        SpannableString ss;
        if(textView.getLineCount() > 2){
            String[] lines = textView.getText().toString().split("\n");
            int length = 0;
            int index = 0;
            for (String line : lines) {
                length = length+ line.length();
                index++;
                if(index==3){
                    break;
                }
            }
            ss = new SpannableString(text.substring(0, text.length() > length ? length : text.length()) + " ...ReadMore");
            if(length > 15)
                ss = new SpannableString(text.substring(0, text.length() > 15 ? 15 : text.length()) + " ...ReadMore");
        } else {
            ss = new SpannableString(text.substring(0, text.length() > 15 ? 15 : text.length()) + " ...ReadMore");
        }
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadLess(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(context.getResources().getColor(R.color.terminated, context.getTheme()));
                } else {
                    ds.setColor(context.getResources().getColor(R.color.terminated));
                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    private void addReadLess(final String text, final TextView textView) {
        SpannableString ss = new SpannableString(text + " ReadLess");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                addReadMore(text, textView);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ds.setColor(context.getResources().getColor(R.color.terminated, context.getTheme()));
                } else {
                    ds.setColor(context.getResources().getColor(R.color.terminated));
                }
            }
        };
        ss.setSpan(clickableSpan, ss.length() - 10, ss.length() , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void  uploadData(int position){
        JSONObject dataSet=new JSONObject();
        try{
            dataSet.put("service_id", AppConstant.ZP_POLLING_BOOTH_SAVE);
            dataSet.put("ro_zone_id", projectList.get(position).getRo_zone_id());
            dataSet.put("polling_booth_id", projectList.get(position).getPolling_booth_id());
            dataSet.put("activity_id", projectList.get(position).getActivity_id());
            dataSet.put("activity_remark", projectList.get(position).getActivity_remark());
            dataSet.put("activity_status", projectList.get(position).getYes_no());

            ((PendingListActivity)context).saveKVVTImagesJsonParams(dataSet,projectList.get(position).getPolling_booth_id()
            ,projectList.get(position).getActivity_id(),projectList.get(position).getActivity_type());
        }catch (JSONException e){

        }

    }


    @Override
    public int getItemCount() {
        return projectList.size();
    }


}
