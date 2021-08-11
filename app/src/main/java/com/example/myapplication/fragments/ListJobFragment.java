package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.activities.JobAdminActivity;
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
        mAdapter = new ListJobAdapter(this);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startloadingJobs();
    }

    private void startloadingJobs() {
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
    private void bind(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvListJob.setLayoutManager(linearLayoutManager);
        rvListJob.setAdapter(mAdapter);
    }

    @Override
    public void onClickJobList(long jobId) {
    Intent intent = new Intent(getActivity(), JobAdminActivity.class);
    intent.putExtra("jobId",jobId);
    startActivity(intent);
    }
}