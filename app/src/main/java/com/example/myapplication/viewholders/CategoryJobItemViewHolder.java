package com.example.myapplication.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.delegates.CategoryByJobDelegate;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryJobItemViewHolder extends RecyclerView.ViewHolder  {

    private CategoryByJobDelegate mDelegate;
    TextView tvJodIndustry;
    TextView tvNumberOfJobs;
    MaterialCardView cardViewCategory;


    String title;
    public CategoryJobItemViewHolder(@NonNull  View itemView,CategoryByJobDelegate mDelegate) {
        super(itemView);
        this.mDelegate = mDelegate;
        tvJodIndustry = itemView.findViewById(R.id.tv_industry);
        tvNumberOfJobs = itemView.findViewById(R.id.tv_number_of_jobs);
        cardViewCategory = itemView.findViewById(R.id.card_detail);


        cardViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mDelegate.onClickCategoryJob(title);
            }
        });
    }
    public void bind(String mData){

        String[] parts = mData.split(",");
         title = parts[0].trim();
        tvJodIndustry.setText(title);
        int num = Integer.valueOf(parts[1]);
        tvNumberOfJobs.setText(num + " jobs");


    }

}
