package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.ReviewDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ListAllJobReviewAdapter extends RecyclerView.Adapter<ListAllJobReviewAdapter.ViewHolder> {

    //private List<ReviewDTO> localDataSet;
    private ItemClickListener clickListener;
    private Map<String,List<ReviewDTO>> jobToReviewDTO;
    static List<String> jobTitle = new ArrayList<>();

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView textView;
        private final RatingBar ratingBar;
        ItemClickListener clickListener;


        public ViewHolder(View view, ItemClickListener clickListener) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textView);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            this.clickListener = clickListener;
            //itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        public TextView getTextView() {
            return textView;
        }
        public RatingBar getRatingBar() {return ratingBar;}

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(),jobTitle.get(getAdapterPosition()));

        }
    }


    public ListAllJobReviewAdapter(List<ReviewDTO> dataSet, ItemClickListener clickListener ) {
        processDataset(dataSet);

        //localDataSet = dataSet;
        this.clickListener = clickListener;
    }

    private void processDataset(List<ReviewDTO> dataSet) {
        jobToReviewDTO = dataSet.stream().collect(Collectors.groupingBy(ReviewDTO::getJobTitle));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view, clickListener);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Set<String> job = jobToReviewDTO.keySet();
        jobTitle = job.stream().collect(Collectors.toList());

        List<Float> jobRatings = new ArrayList<>();

        jobToReviewDTO.forEach((j,reviews)->
        {
            jobRatings.add((float) reviews.stream().mapToDouble(x->x.getReviewstars()).average().getAsDouble());
        });



        viewHolder.getTextView().setText(jobTitle.get(position));
        viewHolder.getRatingBar().setRating(jobRatings.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return jobToReviewDTO.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position, String jobTitle);
    }

}
