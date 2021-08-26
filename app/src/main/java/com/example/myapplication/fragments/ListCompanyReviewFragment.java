package com.example.myapplication.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.ChatDetailActivity;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.adapters.ReviewAdapter;
import com.example.myapplication.delegates.ReviewItemDelegate;
import com.example.myapplication.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ListCompanyReviewFragment extends Fragment implements ReviewItemDelegate {

    RecyclerView recyclerView;
    List<ReviewDTO> reviewListResponseData;
    String CompanyName;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting the data from CompanyReviewFragment
        Bundle bundle = getArguments();
        if(bundle!=null){
            CompanyName = bundle.getString("CompanyName");
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_company_review, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.rvReviews);
        getReviewListData(); // call a method in which we have implement our GET type web API

        return root;
    }

    private void getReviewListData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog


        (RetrofitClient.getInstance().getResponse().getReviewsByCompanyName(CompanyName)).enqueue(new Callback<List<ReviewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewDTO>> call, Response<List<ReviewDTO>> response) {
                //Log.d("responseGET", response.body().get(0).getName());
                progressDialog.dismiss(); //dismiss progress dialog
                reviewListResponseData = response.body();
                setDataInRecyclerView();
            }

            @Override
            public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewListResponseData,this);
        recyclerView.setAdapter(reviewAdapter); // set the Adapter to RecyclerView
    }




    @Override
    public void onTapSendMessage(String userId, String userName) {
        Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("userName",userName);
        startActivity(intent);
    }

    @Override
    public void onTapReport(Long reviewId) {

        Log.e("reviewId",String.valueOf(reviewId));
        Call<Void>call1 = RetrofitClient.getInstance().getResponse().updateReview(reviewId,"Reported");
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

}