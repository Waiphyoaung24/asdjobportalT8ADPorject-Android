package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.ViewedJobsDTO;
import com.example.myapplication.viewholders.ViewedJobsItemViewHolder;

import java.util.List;

public class ListViewedJobsAdapter extends RecyclerView.Adapter<ViewedJobsItemViewHolder>{

    private List<ViewedJobsDTO> mdata1;

    @NonNull
    @Override
    public ViewedJobsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_viewed_jobs_item_view_holder,parent,false);
        return new ViewedJobsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewedJobsItemViewHolder holder, int position) {
        holder.bind(mdata1.get(position));
    }

    @Override
    public int getItemCount() {
        if(mdata1!=null) {
            return mdata1.size();
        }else {
            return 0;
        }
    }

    public void setData(List<ViewedJobsDTO> mData){
        this.mdata1 = mData;
    }
}
