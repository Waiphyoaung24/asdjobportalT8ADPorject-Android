package com.example.myapplication.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.delegates.JobListDelegate;
import com.google.android.material.card.MaterialCardView;

public class JobListItemViewHolder extends RecyclerView.ViewHolder {


    TextView tvJobPosition;
    TextView tvCompanyName;
    TextView tvJobLocation;
    TextView tvJobDesc;
    JobDTO data;
    MaterialCardView cardViewJobList;
    private JobListDelegate mDelegate;
    public JobListItemViewHolder(@NonNull  View itemView, JobListDelegate mDelegate) {
        super(itemView);
        this.mDelegate = mDelegate;

        tvJobPosition = itemView.findViewById(R.id.tv_job_position);
        tvCompanyName = itemView.findViewById(R.id.tv_company_name);
        tvJobLocation = itemView.findViewById(R.id.tv_job_location);
        tvJobDesc = itemView.findViewById(R.id.tv_short_desc);
        cardViewJobList = itemView.findViewById(R.id.card_job_list);

        cardViewJobList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelegate.onClickJobList(data.getId());
            }
        });
    }

    public void bind(JobDTO mData){
        data = mData;
    tvJobPosition.setText(mData.getJobTitle());
    tvJobLocation.setText(mData.getJobPositionURL());
    tvCompanyName.setText(mData.getJobIndustry());
    tvJobDesc.setText(mData.getJobDescription());

    }

}
