package com.example.myapplication.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.JobDetailActivity;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.delegates.BookmarkItemDelegate;

public class BookmarkItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvjobtitle;
    TextView tvjobcompany;
    TextView tvjobdatebookmark;
    LinearLayout llviewJob;
    BookmarkedJobsDTO mData;
    private BookmarkItemDelegate mDelegate;

    public BookmarkItemViewHolder(@NonNull View itemView,BookmarkItemDelegate mDelegate) {
        super(itemView);
        tvjobtitle = itemView.findViewById(R.id.tv_job_title);
        tvjobcompany = itemView.findViewById(R.id.tv_job_company);
        llviewJob = itemView.findViewById(R.id.ll_viewJob);
        tvjobdatebookmark = itemView.findViewById(R.id.tv_job_datebookmark);
        this.mDelegate = mDelegate;
        llviewJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mDelegate.onClickViewJob(mData.getJobId());
            }
        });
    }

    public void bind(BookmarkedJobsDTO mData){
        this.mData =mData;
        tvjobtitle.setText(mData.getJobtitle());
        tvjobcompany.setText(mData.getCompanyname());
        tvjobdatebookmark.setText("Date Bookmarked :" +mData.getBookmarkDate());
    }
}