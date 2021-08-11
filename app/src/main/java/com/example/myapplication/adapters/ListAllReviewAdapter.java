package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.example.myapplication.R;
import com.example.myapplication.data.CompaniesReviewDTO;

public class ListAllReviewAdapter extends RecyclerView.Adapter<ListAllReviewAdapter.ViewHolder> {

    private List<CompaniesReviewDTO> localDataSet;
    private ItemClickListener clickListener;

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
            clickListener.onItemClick(getAdapterPosition());
        }
    }


    public ListAllReviewAdapter(List<CompaniesReviewDTO> dataSet, ItemClickListener clickListener) {
        localDataSet = dataSet;
        this.clickListener = clickListener;
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
        viewHolder.getTextView().setText(localDataSet.get(position).getCompanyName());
        viewHolder.getRatingBar().setRating((float) localDataSet.get(position).getRatings());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
