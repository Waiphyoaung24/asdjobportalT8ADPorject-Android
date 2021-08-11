package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.delegates.CategoryByJobDelegate;
import com.example.myapplication.viewholders.CategoryJobItemViewHolder;
import com.example.myapplication.viewholders.JobListItemViewHolder;

import java.util.List;

public class CategoryJobAdapter  extends RecyclerView.Adapter<CategoryJobItemViewHolder> {
   private List<String> mData;
   private Context context;

   private CategoryByJobDelegate mDelegate;

    public CategoryJobAdapter(CategoryByJobDelegate mDelegate) {
        this.mDelegate = mDelegate;

    }

    @NonNull
    @Override
    public CategoryJobItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_list,parent,false);
        return new CategoryJobItemViewHolder(view,mDelegate);
    }

    @Override
    public void onBindViewHolder(@NonNull  CategoryJobItemViewHolder holder, int position) {
    holder.bind(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
    public void setData(List<String> mData){
        this.mData = mData;
        notifyDataSetChanged();
    }

}
