package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.delegates.CategoryByJobDelegate;
import com.example.myapplication.delegates.JobListDelegate;
import com.example.myapplication.viewholders.JobListItemViewHolder;

import java.util.List;

public class ListJobAdapter extends RecyclerView.Adapter<JobListItemViewHolder> {



    private List<JobDTO> mData;
    private JobListDelegate mDelegate;

    public ListJobAdapter(JobListDelegate mDelegate) {
        this.mDelegate = mDelegate;

    }
    @NonNull
    @Override
    public JobListItemViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job_list,parent,false);
        return new JobListItemViewHolder(view,mDelegate);
    }

    @Override
    public void onBindViewHolder(@NonNull JobListItemViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setData(List<JobDTO> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }
}
