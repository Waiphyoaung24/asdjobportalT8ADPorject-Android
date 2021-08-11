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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ListJobAdapter;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.delegates.JobListDelegate;
import com.example.myapplication.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchJobFragment extends Fragment implements JobListDelegate {

    RecyclerView rvJobList;
    EditText etSearch;
    Button btnSearch;

    private ListJobAdapter mAdapter;
    List<JobDTO>mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_search_job, container, false);
        mAdapter = new ListJobAdapter(this);
        rvJobList = root.findViewById(R.id.rvList_Jobs);
        etSearch = root.findViewById(R.id.et_search);
        btnSearch = root.findViewById(R.id.btn_search_job_list);




        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = etSearch.getText().toString();
                if(!search.isEmpty()){
                    startLoadingSeachJob(search);
                }
                else {
                    Toast.makeText(getActivity(), "Please enter a valid search keyword", Toast.LENGTH_SHORT).show();
                }
            
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void startLoadingSeachJob(String search){
        Call<List<JobDTO>> call = RetrofitClient.getInstance().getResponse().getJobsByTitleOrDesc(search);
        call.enqueue(new Callback<List<JobDTO>>() {
            @Override
            public void onResponse(Call<List<JobDTO>> call, Response<List<JobDTO>> response) {
                mData = response.body();
                mAdapter.setData(mData);
                bindAndShowAllJobs();
            }

            @Override
            public void onFailure(Call<List<JobDTO>> call, Throwable t) {

            }
        });
    }
    private void bindAndShowAllJobs(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvJobList.setLayoutManager(linearLayoutManager);
        rvJobList.setAdapter(mAdapter);
    }

    @Override
    public void onClickJobList(long jobId) {
        JobDetailFragment fragment = new JobDetailFragment();
        fragment.setJobId(jobId);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fl_container, fragment);
        trans.commit();
    }




}