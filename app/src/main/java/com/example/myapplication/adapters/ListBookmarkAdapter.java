package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.viewholders.BookmarkItemViewHolder;

import java.util.List;

public class ListBookmarkAdapter extends RecyclerView.Adapter<BookmarkItemViewHolder>{





    private List<BookmarkedJobsDTO> mData;

    @NonNull
    @Override
    public BookmarkItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_bookmark_item_view_holder,parent,false);
        return new BookmarkItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkItemViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<BookmarkedJobsDTO> mData){
        this.mData = mData;
    }

}
