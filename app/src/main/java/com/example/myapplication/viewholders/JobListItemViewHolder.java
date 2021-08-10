package com.example.myapplication.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.JobDTO;

public class JobListItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvTitle;
    TextView tvJobPosition;
    TextView tvJobIndustry;
    TextView tvJobDesc;
    public JobListItemViewHolder(@NonNull  View itemView) {
        super(itemView);
        tvTitle = itemView.findViewById(R.id.tv_job_title);
        tvJobPosition = itemView.findViewById(R.id.tv_job_position);
        tvJobIndustry = itemView.findViewById(R.id.tv_job_industry);
        tvJobDesc = itemView.findViewById(R.id.tv_job_desc);
    }

    public void bind(JobDTO mData){
    tvTitle.setText("Title :"+mData.getJobTitle());
    tvJobPosition.setText("Position : "+mData.getJobPositionURL());
    tvJobIndustry.setText("Industry :" +mData.getJobIndustry());
    tvJobDesc.setText("Descrption :" +mData.getJobDescription());

    }
}
