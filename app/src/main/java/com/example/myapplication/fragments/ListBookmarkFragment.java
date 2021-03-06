package com.example.myapplication.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.JobDetailActivity;
import com.example.myapplication.adapters.ListBookmarkAdapter;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.delegates.BookmarkItemDelegate;
import com.example.myapplication.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBookmarkFragment extends Fragment implements BookmarkItemDelegate {

    RecyclerView rvListBookmark;
    private ListBookmarkAdapter mAdapter;
    private List<BookmarkedJobsDTO> mData;
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
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable  Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.list_bookmark_fragment,container,false);
        rvListBookmark = root.findViewById(R.id.rvListBookmark);
        mAdapter = new ListBookmarkAdapter(this);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startloadingBookmarks();
    }

    private void startloadingBookmarks() {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog
        Call<List<BookmarkedJobsDTO>> call = RetrofitClient.getInstance().getResponse().listBookmarkJobs(authorization);
        call.enqueue(new Callback<List<BookmarkedJobsDTO>>() {
            @Override
            public void onResponse(Call<List<BookmarkedJobsDTO>> call, Response<List<BookmarkedJobsDTO>> response) {

                progressDialog.dismiss();
                mData = response.body();
                mAdapter.setData(mData);
                bindAndShowAllJobs();
            }

            @Override
            public void onFailure(Call<List<BookmarkedJobsDTO>> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void bindAndShowAllJobs(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvListBookmark.setLayoutManager(linearLayoutManager);
        rvListBookmark.setAdapter(mAdapter);
    }

    public void loadUserProfile() {
        if(username_ != null&&access_token!=null){
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authorization, username_);
        }}

    @Override
    public void onClickViewJob(Long jobId) {
        Intent intent = new Intent(getActivity(), JobDetailActivity.class);
        intent.putExtra("jobId",jobId);
        startActivity(intent);
    }
}