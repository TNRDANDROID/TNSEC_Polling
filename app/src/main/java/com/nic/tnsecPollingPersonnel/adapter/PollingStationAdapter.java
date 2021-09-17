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
import com.nic.tnsecPollingPersonnel.databinding.PollingStaionRecyclerItemBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import java.util.List;

public class PollingStationAdapter extends RecyclerView.Adapter<PollingStationAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private List<ElectionProject> projectList;
    private LayoutInflater layoutInflater;

    public PollingStationAdapter(Activity context, List<ElectionProject> projectList) {

        this.context = context;
        prefManager = new PrefManager(context);
        this.projectList = projectList;
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

        holder.pollingStaionRecyclerItemBinding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.pollingStaionRecyclerItemBinding.save.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
            }
        });
      /*  holder.viewDataAdapterBinding.name.setText(projectList.get(position).getActivity_id());
        holder.viewDataAdapterBinding.organisation.setText(projectList.get(position).getActivity_type());

        holder.viewDataAdapterBinding.viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }





    @Override
    public int getItemCount() {
        return 5/*projectList.size()*/;
    }


}
