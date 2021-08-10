package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.CategoryJobAdapter;
import com.example.myapplication.delegates.CategoryByJobDelegate;
import com.example.myapplication.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobIndustryByCategoryFragment extends Fragment implements CategoryByJobDelegate  {

    RecyclerView rvCategoryJob;
    private CategoryJobAdapter mAdapter;
    List<String>category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startLoadingCategory();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_by_job_industry, container, false);
    }



    @Override
    public void onStart() {
        super.onStart();
        View view = getView();
        rvCategoryJob = view.findViewById(R.id.rv_category_job);


    }

    private void startLoadingCategory() {
        Call<List<String>> call = RetrofitClient.getInstance().getResponse().getCateoryList();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                category = response.body();
                bindAndshowCategory();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

    }


    private void bindAndshowCategory(){

        mAdapter = new CategoryJobAdapter(category,getContext(),this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        rvCategoryJob.setAdapter(mAdapter);
        rvCategoryJob.setLayoutManager(gridLayoutManager);
    }


    @Override
    public void onClickCategoryJob(String jodIndustry) {
        JobIndustryByListFragment fragment = new JobIndustryByListFragment();
        fragment.setJobId(jodIndustry);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fl_container,fragment);
        trans.commit();
    }
}