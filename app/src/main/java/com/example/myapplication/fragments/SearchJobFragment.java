package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ListJobAdapter;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchJobFragment extends Fragment {

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
        mAdapter = new ListJobAdapter();
        rvJobList = root.findViewById(R.id.rvList_Jobs);
        etSearch = root.findViewById(R.id.et_search);
        btnSearch = root.findViewById(R.id.btn_search_job_list);
        mData = new ArrayList<>();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoadingSeachJob();
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void startLoadingSeachJob(){
        Call<List<JobDTO>> call = RetrofitClient.getInstance().getResponse().getJobsByTitleOrDesc(etSearch.getText().toString());
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
}