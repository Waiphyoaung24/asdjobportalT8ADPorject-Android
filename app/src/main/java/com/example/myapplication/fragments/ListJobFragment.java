package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.R;

import com.example.myapplication.adapters.ListJobAdapter;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.delegates.JobListDelegate;
import com.example.myapplication.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListJobFragment extends Fragment implements JobListDelegate {

    RecyclerView rvListJob;
    private ListJobAdapter mAdapter;
    private List<JobDTO> mData;
    boolean flag = false;


    String name;
    float rating;
    int lvl;
    ImageView ivFilter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_job, container, false);
        rvListJob = root.findViewById(R.id.rvList_Jobs);
        ivFilter = getActivity().findViewById(R.id.iv_filter);
        mAdapter = new ListJobAdapter(this);
        ivFilter.setVisibility(View.VISIBLE);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterSearchFragment fragment = new FilterSearchFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.fl_container, fragment);
                trans.commit();


            }
        });
        startLoadingData();

    }

    private void startLoadingData() {
        if (flag) {

            Call<List<JobDTO>> call = RetrofitClient.getInstance().getResponse().filterJobs(name, rating, lvl);
            call.enqueue(new Callback<List<JobDTO>>() {
                @Override
                public void onResponse(Call<List<JobDTO>> call, Response<List<JobDTO>> response) {
                    mData = response.body();
                    mAdapter.setData(mData);
                    bind();
                }

                @Override
                public void onFailure(Call<List<JobDTO>> call, Throwable t) {
                    Log.e("eror", t.getMessage());
                }
            });
        } else {
            Call<List<JobDTO>> call = RetrofitClient.getInstance().getResponse().getAllJobs();
            call.enqueue(new Callback<List<JobDTO>>() {
                @Override
                public void onResponse(Call<List<JobDTO>> call, Response<List<JobDTO>> response) {
                    mData = response.body();
                    mAdapter.setData(mData);
                    bind();

                }

                @Override
                public void onFailure(Call<List<JobDTO>> call, Throwable t) {
                    Log.e("error", "error");
                }
            });
        }
    }

    private void bind() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvListJob.setLayoutManager(linearLayoutManager);
        rvListJob.setAdapter(mAdapter);
    }

    @Override
    public void onClickJobList(long jobId) {
        JobDetailFragment fragment = new JobDetailFragment();
        fragment.setJobId(jobId);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fl_container,fragment);
        trans.commit();
    }

    public void sendData(String name, float rating, int lvl, boolean flag) {

        this.name = name;
        this.rating = rating;
        this.lvl = lvl;
        this.flag = flag;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ivFilter.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ivFilter.setVisibility(View.VISIBLE);
    }
}