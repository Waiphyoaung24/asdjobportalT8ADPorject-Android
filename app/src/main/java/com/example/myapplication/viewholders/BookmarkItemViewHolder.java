package com.example.myapplication.viewholders;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.BookmarkedJobsDTO;

public class BookmarkItemViewHolder extends RecyclerView.ViewHolder {

    TextView tvjobtitle;
    TextView tvjobcompany;
    TextView tvjobdatebookmark;

    public BookmarkItemViewHolder(@NonNull View itemView) {
        super(itemView);
        tvjobtitle = itemView.findViewById(R.id.tv_job_title);
        tvjobcompany = itemView.findViewById(R.id.tv_job_company);
        tvjobdatebookmark = itemView.findViewById(R.id.tv_job_datebookmark);
    }

    public void bind(BookmarkedJobsDTO mData){
        tvjobtitle.setText("Title :"+mData.getJobtitle());
        tvjobcompany.setText("Position : "+mData.getCompanyname());
        tvjobdatebookmark.setText("Date Bookmarked :" +mData.getBookmarkDate());
    }
}