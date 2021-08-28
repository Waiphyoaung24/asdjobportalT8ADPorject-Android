package com.example.myapplication.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ListViewedJobsAdapter;
import com.example.myapplication.data.ApplicantDTO;
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
    String username_,access_token;
    ApplicantDTO applicant;
    String authorization;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
        access_token = storeToken.getString("access_token",null);
        authorization = "Bearer "+access_token;

        loadUserProfile();
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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        //Retrofit api call ListViewed Jobs
        Call<List<ViewedJobsDTO>> call = RetrofitClient.getInstance().getResponse().ListViewedJobs(authorization);
        call.enqueue(new Callback<List<ViewedJobsDTO>>() {
            @Override
            public void onResponse(Call<List<ViewedJobsDTO>> call, Response<List<ViewedJobsDTO>> response) {
                progressDialog.dismiss();
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

    public void loadUserProfile() {
        if(username_ != null&&access_token!=null){
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authorization, username_);
        }}
}