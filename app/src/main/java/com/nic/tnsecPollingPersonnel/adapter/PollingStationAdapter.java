package com.nic.tnsecPollingPersonnel.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnsecPollingPersonnel.DataBase.DBHelper;
import com.nic.tnsecPollingPersonnel.DataBase.dbData;
import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.activity.LoginScreen;
import com.nic.tnsecPollingPersonnel.activity.PollingStationList;
import com.nic.tnsecPollingPersonnel.constant.AppConstant;
import com.nic.tnsecPollingPersonnel.databinding.PollingStaionRecyclerItemBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;
import com.nic.tnsecPollingPersonnel.utils.Utils;

import java.util.ArrayList;

public class PollingStationAdapter extends RecyclerView.Adapter<PollingStationAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private ArrayList<ElectionProject> projectList;
    private LayoutInflater layoutInflater;
    public dbData dbData;
    String activityStatus;
    int row_index=-1;

    public PollingStationAdapter(Activity context, ArrayList<ElectionProject> projectList) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.projectList = projectList;
        dbData=new dbData(context);
    }

    @Override
    public PollingStationAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        PollingStaionRecyclerItemBinding pollingStaionRecyclerItemBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.polling_staion_recycler_item, viewGroup, false);
        return new PollingStationAdapter.MyViewHolder(pollingStaionRecyclerItemBinding);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private PollingStaionRecyclerItemBinding pollingStaionRecyclerItemBinding;

        public MyViewHolder(PollingStaionRecyclerItemBinding Binding) {
            super(Binding.getRoot());
            pollingStaionRecyclerItemBinding = Binding;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        if(projectList.get(position).getSave_status().equalsIgnoreCase("No")){
            holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unchecked));
            holder.pollingStaionRecyclerItemBinding.edit.setVisibility(View.GONE);
            holder.pollingStaionRecyclerItemBinding.yes.setEnabled(true);
            holder.pollingStaionRecyclerItemBinding.no.setEnabled(true);
            holder.pollingStaionRecyclerItemBinding.remark.setEnabled(true);
        }
        else {
            holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
            holder.pollingStaionRecyclerItemBinding.edit.setVisibility(View.VISIBLE);
            holder.pollingStaionRecyclerItemBinding.yes.setEnabled(false);
            holder.pollingStaionRecyclerItemBinding.no.setEnabled(false);
            holder.pollingStaionRecyclerItemBinding.remark.setEnabled(false);
        }

        holder.pollingStaionRecyclerItemBinding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                projectList.get(position).setSave_status("No");
                notifyDataSetChanged();
            }
        });

        if(projectList.get(position).getIsChecked_status().equalsIgnoreCase("No")){
            holder.pollingStaionRecyclerItemBinding.yes.setChecked(false);
        }
        else {
            holder.pollingStaionRecyclerItemBinding.yes.setChecked(true);
        }
        if(projectList.get(position).getUnChecked_status().equalsIgnoreCase("No")){
            holder.pollingStaionRecyclerItemBinding.no.setChecked(false);
        }
        else {
            holder.pollingStaionRecyclerItemBinding.no.setChecked(true);
        }

        if(projectList.get(position).getGet_remark_text().toString().equalsIgnoreCase("")){
            holder.pollingStaionRecyclerItemBinding.remark.setText("");
        }
        else {
            holder.pollingStaionRecyclerItemBinding.remark.setText(projectList.get(position).getGet_remark_text());
        }

        holder.pollingStaionRecyclerItemBinding.pvname.setText(projectList.get(position).getPvname());
        holder.pollingStaionRecyclerItemBinding.pollingStationNo.setText("Polling Station No : "+projectList.get(position).getPolling_station_no());
        holder.pollingStaionRecyclerItemBinding.pollingBoothName.setText(projectList.get(position).getPolling_booth_name());
        if(projectList.get(position).getPolling_booth_name() != null && (holder.pollingStaionRecyclerItemBinding.pollingBoothName.getLineCount()>2 ||  projectList.get(position).getPolling_booth_name().length() > 10))
        {
            addReadMore(projectList.get(position).getPolling_booth_name(), holder.pollingStaionRecyclerItemBinding.pollingBoothName);
        }
        holder.pollingStaionRecyclerItemBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(projectList.get(position).getSave_status().equalsIgnoreCase("No")){
                    if (holder.pollingStaionRecyclerItemBinding.yes.isChecked() ||
                            (holder.pollingStaionRecyclerItemBinding.no.isChecked())) {
                        if (holder.pollingStaionRecyclerItemBinding.yes.isChecked()) {
                            activityStatus = "Y";
                        } else {
                            activityStatus = "N";
                        }
                        if (!holder.pollingStaionRecyclerItemBinding.remark.getText().toString().equalsIgnoreCase("")) {
                            String remark = holder.pollingStaionRecyclerItemBinding.remark.getText().toString();
                            String polling_booth_name = projectList.get(position).getPolling_booth_name();

                            saveDetailsInsertUpdate(holder,position, remark, polling_booth_name);
                        } else {
                            Utils.showAlert(context, "Please enter Remarks");
                        }
                    } else {
                        Utils.showAlert(context, "Please Select Yes or No!");

                    }

                }
            }
        });
        holder.pollingStaionRecyclerItemBinding.yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    holder.pollingStaionRecyclerItemBinding.no.setChecked(false);
                    holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unchecked));
                    ((PollingStationList) context).updateYesStatus(projectList,position);
                }else {
                    ((PollingStationList) context).updateUnCheckStatus(projectList,position);
                }

            }
        });
        holder.pollingStaionRecyclerItemBinding.no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    holder.pollingStaionRecyclerItemBinding.yes.setChecked(false);
                    holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unchecked));
                    ((PollingStationList) context).updateNoStatus(projectList,position);
                }else {
                    ((PollingStationList) context).updateUnCheckStatus(projectList,position);
                }

            }
        });

      /*  holder.pollingStaionRecyclerItemBinding.yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.pollingStaionRecyclerItemBinding.yes.isChecked()){
                    holder.pollingStaionRecyclerItemBinding.yes.setChecked(false);
                    ((PollingStationList) context).updateUnCheckStatus(projectList,position);
                }
//                holder.pollingStaionRecyclerItemBinding.yes.setChecked(true);
                holder.pollingStaionRecyclerItemBinding.no.setChecked(false);
                holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unchecked));
                ((PollingStationList) context).updateYesStatus(projectList,position);
            }
        });
        holder.pollingStaionRecyclerItemBinding.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.pollingStaionRecyclerItemBinding.no.isChecked()){
                    holder.pollingStaionRecyclerItemBinding.no.setChecked(false);
                    ((PollingStationList) context).updateUnCheckStatus(projectList,position);
                }
                holder.pollingStaionRecyclerItemBinding.yes.setChecked(false);
//                holder.pollingStaionRecyclerItemBinding.no.setChecked(true);
                holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_unchecked));
                ((PollingStationList) context).updateNoStatus(projectList,position);

            }
        });*/

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
            if(length > 10)
                ss = new SpannableString(text.substring(0, text.length() > 10 ? 10 : text.length()) + " ...ReadMore");
        } else {
            ss = new SpannableString(text.substring(0, text.length() > 10 ? 10 : text.length()) + " ...ReadMore");
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

    public void saveDetailsInsertUpdate(MyViewHolder holder, int position, String remarks, String polling_booth_name){
        String selection = null;
        String[] selectionArgs = null;
        String ro_zone_id,pvname,polling_booth_id,activity_name,activity_id,activity_type,activity_remark,activity_status,Polling_station_no;
        ro_zone_id=prefManager.getRoZoneId();
        polling_booth_id=projectList.get(position).getPolling_booth_id();
        Polling_station_no=projectList.get(position).getPolling_station_no();
        pvname=projectList.get(position).getPvname();
        activity_id=prefManager.getActivityId();
        activity_name=prefManager.getActivityName();
        activity_type=prefManager.getActivityType();
        activity_remark=remarks;
        activity_status= activityStatus;

        ContentValues values = new ContentValues();
        values.put(AppConstant.RO_ZONE_ID,ro_zone_id);
        values.put(AppConstant.POLLING_BOOTH_ID,polling_booth_id);
        values.put(AppConstant.POLLING_STATION_NO,Polling_station_no);
        values.put(AppConstant.PV_NAME,pvname);
        values.put(AppConstant.ACTIVITY_ID,activity_id);
        values.put(AppConstant.ACTIVITY_DESCRIPTION,activity_name);
        values.put(AppConstant.ACTIVITY_TYPE,activity_type);
        values.put(AppConstant.ACTIVITY_REMARK,activity_remark);
        values.put(AppConstant.ACTIVITY_STATUS,activity_status);
        values.put(AppConstant.POLLING_BOOTH_NAME,polling_booth_name);
        dbData.open();
        if(dbData.getSavedDetailsPolingStationNumber(polling_booth_id,activity_id,activity_type).size()==0) {
            long id = LoginScreen.db.insert(DBHelper.SAVE_DATA, null, values);
            Log.d("Insert_id_structures", String.valueOf(id));
            Log.d("Insert_values", String.valueOf(values));
            holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
            projectList.get(position).setSave_status("Yes");
            projectList.get(position).setGet_remark_text(remarks);
            Utils.showAlert(context,"Inserted Successfully");
        }
        else {
            selection = "polling_booth_id = ? and activity_id = ? and activity_type = ? ";
            selectionArgs = new String[]{polling_booth_id,activity_id,activity_type};
            long id=LoginScreen.db.update(DBHelper.SAVE_DATA, values, selection, selectionArgs);
            holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
            projectList.get(position).setSave_status("Yes");
            projectList.get(position).setGet_remark_text(remarks);
            Utils.showAlert(context,"Updated Successfully");
        }
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return projectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
