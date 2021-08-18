package com.example.myapplication.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.ViewedJobsDTO;

public class ViewedJobsItemViewHolder extends RecyclerView.ViewHolder  {

    TextView tvjobtitle1;
    TextView tvjobcompany1;
    TextView tvjobdateapplied;

    public ViewedJobsItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tvjobtitle1 = itemView.findViewById(R.id.tv_job_title1);
        tvjobcompany1 = itemView.findViewById(R.id.tv_job_company1);
        tvjobdateapplied = itemView.findViewById(R.id.tv_job_applied);
    }

    public void bind(ViewedJobsDTO mData){
        tvjobtitle1.setText(mData.getJobtitle());
        tvjobcompany1.setText(mData.getCompanyname());
        tvjobdateapplied.setText("Date Applied :" +mData.getAppliedDate());
    }
}