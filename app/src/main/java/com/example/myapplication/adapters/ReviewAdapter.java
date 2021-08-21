package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.myapplication.R;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.fragments.ListCompanyReviewFragment;

public class ReviewAdapter extends
        RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    //ListCompanyReviewFragment context;
    List<ReviewDTO> reviewListResponseData;
    private ItemClickListener clickListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public ReviewAdapter(List<ReviewDTO> reviewListResponseData, ItemClickListener clickListener) {
        this.reviewListResponseData = reviewListResponseData;
        //this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, null);
        ReviewViewHolder reviewViewHolder = new ReviewViewHolder(view, clickListener);
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
        // implement setONCLickListtener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with user name
                Toast.makeText(view.getContext(), reviewListResponseData.get(position).getReviewDescription(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return reviewListResponseData.size(); // size of the list items
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // init the item view's
        TextView reviewDescription,reviewJob, reviewCompany,reviewUser, reviewDate;
        RatingBar reviewStar;
        ItemClickListener clickListener;

        public ReviewViewHolder(View itemView, ItemClickListener clickListener) {
            super(itemView);
            // get the reference of item view's
            reviewDate = (TextView) itemView.findViewById(R.id.tv_review_date);
            reviewUser = (TextView) itemView.findViewById(R.id.tv_profile_name);
           /* reviewCompany = (TextView) itemView.findViewById(R.id.ReviewCompany);*/
            reviewJob = (TextView) itemView.findViewById(R.id.tv_job_title);
            reviewDescription = (TextView) itemView.findViewById(R.id.tv_desc);
            reviewStar = (RatingBar) itemView.findViewById(R.id.ratingbar_each);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
