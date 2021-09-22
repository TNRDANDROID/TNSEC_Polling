package com.nic.tnsecPollingPersonnel.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.databinding.ViewServerDataAdapterBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import java.util.ArrayList;
import java.util.List;

public class ViewServerDataListAdapter extends RecyclerView.Adapter<ViewServerDataListAdapter.MyViewHolder> implements Filterable {
    private List<ElectionProject> serverDataListValues;
    private List<ElectionProject> serverDataListValuesFiltered;
    private String letter;
    private Context context;


    private LayoutInflater layoutInflater;

    public ViewServerDataListAdapter(Context context, List<ElectionProject> serverDataListValues) {
        this.context = context;
        this.serverDataListValues = serverDataListValues;
        this.serverDataListValuesFiltered = serverDataListValues;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ViewServerDataAdapterBinding viewServerDataAdapterBinding;

        public MyViewHolder(ViewServerDataAdapterBinding Binding) {
            super(Binding.getRoot());
            viewServerDataAdapterBinding = Binding;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(viewGroup.getContext());
        }
        ViewServerDataAdapterBinding viewServerDataAdapterBinding =
                DataBindingUtil.inflate(layoutInflater, R.layout.view_server_data_adapter, viewGroup, false);
        return new MyViewHolder(viewServerDataAdapterBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.viewServerDataAdapterBinding.pvname.setText(serverDataListValuesFiltered.get(position).getPvname());
        holder.viewServerDataAdapterBinding.pollingStationNo.setText("Polling Station No : "+serverDataListValuesFiltered.get(position).getLbpolling_station_no());
        holder.viewServerDataAdapterBinding.pollingBoothName.setText(serverDataListValuesFiltered.get(position).getPolling_booth_name());
        holder.viewServerDataAdapterBinding.activityName.setText(serverDataListValuesFiltered.get(position).getActivity_name());
        holder.viewServerDataAdapterBinding.remark.setText(serverDataListValuesFiltered.get(position).getActivity_remark());
        if(serverDataListValuesFiltered.get(position).getActivity_status().equalsIgnoreCase("Y")){
            holder.viewServerDataAdapterBinding.statusVal.setText("Yes");
            holder.viewServerDataAdapterBinding.statusVal.setTextColor(context.getResources().getColor(R.color.Green3));
        }else {
            holder.viewServerDataAdapterBinding.statusVal.setText("No");
            holder.viewServerDataAdapterBinding.statusVal.setTextColor(context.getResources().getColor(R.color.Red3));
        }

        if(serverDataListValuesFiltered.get(position).getPolling_booth_name() != null && (holder.viewServerDataAdapterBinding.pollingBoothName.getLineCount()>2 ||  serverDataListValuesFiltered.get(position).getPolling_booth_name().length() > 10))
        {
            addReadMore(serverDataListValuesFiltered.get(position).getPolling_booth_name(), holder.viewServerDataAdapterBinding.pollingBoothName);
        }

        if(serverDataListValuesFiltered.get(position).getActivity_remark() != null && (holder.viewServerDataAdapterBinding.remark.getLineCount()>2 ||  serverDataListValuesFiltered.get(position).getActivity_remark().length() > 10))
        {
            addReadMore(serverDataListValuesFiltered.get(position).getActivity_remark(), holder.viewServerDataAdapterBinding.remark);
        }

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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    serverDataListValuesFiltered = serverDataListValues;
                } else {
                    List<ElectionProject> filteredList = new ArrayList<>();
                    for (ElectionProject row : serverDataListValues) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLbpolling_station_no().toLowerCase().contains(charString.toLowerCase()) || row.getLbpolling_station_no().contains(charString.toUpperCase())) {
                            filteredList.add(row);
                        }
                    }

                    serverDataListValuesFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = serverDataListValuesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                serverDataListValuesFiltered = (ArrayList<ElectionProject>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



    @Override
    public int getItemCount() {
        return serverDataListValuesFiltered == null ? 0 : serverDataListValuesFiltered.size();
    }
}
