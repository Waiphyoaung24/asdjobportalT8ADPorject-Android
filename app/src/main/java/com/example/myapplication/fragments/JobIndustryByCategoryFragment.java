package com.example.myapplication.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.CategoryJobAdapter;
import com.example.myapplication.delegates.CategoryByJobDelegate;
import com.example.myapplication.network.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobIndustryByCategoryFragment extends Fragment implements CategoryByJobDelegate  {

    RecyclerView rvCategoryJob;
    private CategoryJobAdapter mAdapter;
    List<String>category;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_category_by_job_industry, container, false);
        rvCategoryJob = root.findViewById(R.id.rv_category_job);

        mAdapter = new CategoryJobAdapter(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startLoadingCategory();

    }

    private void startLoadingCategory() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        Call<List<String>> call = RetrofitClient.getInstance().getResponse().getCateoryList();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                progressDialog.dismiss();
                category = response.body();
                mAdapter.setData(category);
                bindAndshowCategory();
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

    }


    private void bindAndshowCategory(){


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