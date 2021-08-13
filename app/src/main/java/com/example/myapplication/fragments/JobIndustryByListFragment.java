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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ListJobAdapter;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.delegates.CategoryByJobDelegate;
import com.example.myapplication.delegates.JobListDelegate;
import com.example.myapplication.network.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobIndustryByListFragment extends Fragment implements JobListDelegate {

    TextView tvCategoryTitle;
    String jodIndustry;
    RecyclerView rvListJob;
    private ListJobAdapter mAdapter;
    private List<JobDTO> mData;
    ImageView ivBack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category_list_by_job, container, false);
        tvCategoryTitle = root.findViewById(R.id.tv_job_category_title);
        rvListJob = root.findViewById(R.id.rvList_Jobs);
        ivBack = root.findViewById(R.id.iv_back);
        mAdapter = new ListJobAdapter(this);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startLoadingCategoryList();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JobIndustryByCategoryFragment fragment = new JobIndustryByCategoryFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = fm.beginTransaction();
                trans.replace(R.id.fl_container, fragment);
                trans.commit();
            }
        });
    }

    private void startLoadingCategoryList(){

        Call<List<JobDTO>> call = RetrofitClient.getInstance().getResponse().getCateogryByIndustry(jodIndustry);
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
        tvCategoryTitle.setText(jodIndustry);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvListJob.setLayoutManager(linearLayoutManager);
        rvListJob.setAdapter(mAdapter);
    }
    public void setJobId(String jodIndustry){
        this.jodIndustry = jodIndustry;
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