package com.nic.tnsecPollingPersonnel.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.nic.tnsecPollingPersonnel.R;
import com.nic.tnsecPollingPersonnel.Session.PrefManager;
import com.nic.tnsecPollingPersonnel.databinding.PendingAdapterBinding;
import com.nic.tnsecPollingPersonnel.databinding.PollingStaionRecyclerItemBinding;
import com.nic.tnsecPollingPersonnel.pojo.ElectionProject;

import java.util.List;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyViewHolder> {

    private static Activity context;
    private PrefManager prefManager;
    private List<ElectionProject> projectList;
    private LayoutInflater layoutInflater;

    public PendingAdapter(Activity context, List<ElectionProject> projectList) {

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
