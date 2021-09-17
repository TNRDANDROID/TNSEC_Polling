package com.nic.tnsecPollingPersonnel.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import java.util.List;

/**
 * Created by shanmugapriyan on 25/05/16.
 */
public class CommonAdapter extends BaseAdapter {
    private List<ElectionProject> electionProjects;
    private Context mContext;
    private String type;


    public CommonAdapter(Context mContext, List<ElectionProject> electionProjects, String type) {
        this.electionProjects = electionProjects;
        this.mContext = mContext;
        this.type = type;
    }


    public int getCount() {
        return electionProjects.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.spinner_drop_down_item, parent, false);
//        TextView tv_type = (TextView) view.findViewById(R.id.tv_spinner_item);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);
        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);
        ElectionProject electionProject = electionProjects.get(position);

        if (type.equalsIgnoreCase("activityTypeList")) {
            tv_type.setText(electionProject.getActivity_type_desc());
        }
        else if (type.equalsIgnoreCase("activityList")){
            tv_type.setText(electionProject.getActivity_description());
        }
        return view;
    }
}
