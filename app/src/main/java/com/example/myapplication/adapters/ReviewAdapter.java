package com.example.myapplication.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.myapplication.R;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.delegates.ReviewItemDelegate;
import com.example.myapplication.fragments.ListCompanyReviewFragment;
import com.google.android.material.button.MaterialButton;

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    //ListCompanyReviewFragment context;
    List<ReviewDTO> reviewListResponseData;

    private ReviewItemDelegate mDelegate;




    public ReviewAdapter(List<ReviewDTO> reviewListResponseData,ReviewItemDelegate mDelegate) {
        this.reviewListResponseData = reviewListResponseData;
        //this.context = context;
        this.mDelegate = mDelegate;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, null);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view,mDelegate);
        return reviewViewHolder;
    }
    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {
        // set the data

        holder.reviewDescription.setText( reviewListResponseData.get(position).getReviewDescription());
       // holder.reviewCompany.setText(reviewListResponseData.get(position).getCompanyName());
        holder.reviewJob.setText( reviewListResponseData.get(position).getJobTitle());
        holder.reviewStar.setRating((float) reviewListResponseData.get(position).getReviewstars());
        holder.reviewUser.setText( String.valueOf(reviewListResponseData.get(position).getApplicantName()));
        holder.reviewDate.setText( String.valueOf(reviewListResponseData.get(position).getReviewDate()));




        holder.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDelegate.onTapReport(reviewListResponseData.get(position).getReviewId(),reviewListResponseData.get(position).getApplicantName());
            }
        });
        holder.btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDelegate.onTapSendMessage(String.valueOf(reviewListResponseData.get(position).getUserId()),reviewListResponseData.get(position).getApplicantName());
            }
        });

    }

    @Override
    public int getItemCount() {
        if(reviewListResponseData!= null){
            return reviewListResponseData.size();
        }
        else {
            return  0;
        }

     // size of the list items
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView reviewDescription,reviewJob, reviewCompany,reviewUser, reviewDate;
        RatingBar reviewStar;

        ImageView btnSendMessage;
        ImageView btnReport;
        ReviewItemDelegate mDelegate;

        public ReviewViewHolder(View itemView,ReviewItemDelegate mDelegate) {
            super(itemView);
            // get the reference of item view's
            this.mDelegate = mDelegate;
            reviewDate = (TextView) itemView.findViewById(R.id.tv_review_date);
            reviewUser = (TextView) itemView.findViewById(R.id.tv_profile_name);
           /* reviewCompany = (TextView) itemView.findViewById(R.id.ReviewCompany);*/
            reviewJob = (TextView) itemView.findViewById(R.id.tv_job_title);
            reviewDescription = (TextView) itemView.findViewById(R.id.tv_desc);
            reviewStar = (RatingBar) itemView.findViewById(R.id.ratingbar_each);
            btnSendMessage = itemView.findViewById(R.id.iv_send_message);
            btnReport = itemView.findViewById(R.id.iv_report);

        }


    }


}
