package com.example.myapplication.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ListViewedJobsAdapter;
import com.example.myapplication.data.ViewedJobsDTO;
import com.example.myapplication.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewedJobsFragment extends Fragment {

    RecyclerView rvListViewedJobs;
    private ListViewedJobsAdapter mAdapter;
    private List<ViewedJobsDTO> mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        //inflate the layout for this fragment
        View root = inflater.inflate(R.layout.list_view_jobs_fragment,container, false);
        //view
        rvListViewedJobs = root.findViewById(R.id.rvListViewedJobs);
        mAdapter = new ListViewedJobsAdapter();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startloadingViewedJobs();
    }

    private void startloadingViewedJobs() {
        //Retrofit api call ListViewed Jobs
        Call<List<ViewedJobsDTO>> call = RetrofitClient.getInstance().getResponse().ListViewedJobs();
        call.enqueue(new Callback<List<ViewedJobsDTO>>() {
            @Override
            public void onResponse(Call<List<ViewedJobsDTO>> call, Response<List<ViewedJobsDTO>> response) {
                mData = response.body();
                mAdapter.setData(mData);
                bindAndShowAllJobs();
            }

            @Override
            public void onFailure(Call<List<ViewedJobsDTO>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void bindAndShowAllJobs(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvListViewedJobs.setLayoutManager(linearLayoutManager);
        rvListViewedJobs.setAdapter(mAdapter);
    }
}