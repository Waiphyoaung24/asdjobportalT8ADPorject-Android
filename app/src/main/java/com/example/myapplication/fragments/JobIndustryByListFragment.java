package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ListJobAdapter;
import com.example.myapplication.data.JobDTO;
import com.example.myapplication.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobIndustryByListFragment extends Fragment {

    TextView tvCategoryTitle;
    String jodIndustry;
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

        View root = inflater.inflate(R.layout.fragment_category_list_by_job, container, false);
        tvCategoryTitle = root.findViewById(R.id.tv_job_category_title);
        rvListJob = root.findViewById(R.id.rvList_Jobs);
        mAdapter = new ListJobAdapter();
        startLoadingCategoryList();

        return root;
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
}