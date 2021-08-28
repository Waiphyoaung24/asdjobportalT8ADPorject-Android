package com.example.myapplication.fragments;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ListAllJobReviewAdapter;
import com.example.myapplication.adapters.ListAllReviewAdapter;
import com.example.myapplication.data.CompaniesReviewDTO;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.network.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyReviewFragment extends Fragment implements ListAllReviewAdapter.ItemClickListener, AdapterView.OnItemSelectedListener
        , ListAllJobReviewAdapter.ItemClickListener {

    protected RecyclerView mRecyclerView;
    protected ListAllReviewAdapter mAdapter;
    protected ListAllJobReviewAdapter mJobAdapter;
    TextView textView;
    List<CompaniesReviewDTO> mDataset ;
    Spinner spin;
    String[] viewType = { "Company","Job"};
    List<ReviewDTO> reviewListResponseData;
    private String currentView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mDataset = new ArrayList<>();
        //reviewListResponseData = new ArrayList<>();
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_company_review, container, false);
        spin = (Spinner) root.findViewById(R.id.spinner);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);



        return root;
    }

    private void fetchData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        Call<List<CompaniesReviewDTO>> call = RetrofitClient.getInstance().getResponse().getAllCompanyReviews();
        call.enqueue(new Callback<List<CompaniesReviewDTO>>() {


            @Override
            public void onResponse(Call<List<CompaniesReviewDTO>> call, Response<List<CompaniesReviewDTO>> response) {
                progressDialog.dismiss();
                List<CompaniesReviewDTO> eList = response.body();


                for (int i = 0; i < eList.size(); i++) {
                    //Log.e("name", eList.get(i).getCompanyName());
                    mDataset.add(eList.get(i));
                }
                Toast.makeText(getActivity(), String.valueOf(mDataset.size()), Toast.LENGTH_SHORT).show();

                // textView.setText(mDataset.get(0));
                mRecyclerView.removeAllViews();
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mAdapter = new ListAllReviewAdapter(mDataset,CompanyReviewFragment.this);
                mRecyclerView.setAdapter(mAdapter);


            }

            @Override
            public void onFailure(Call<List<CompaniesReviewDTO>> call, Throwable t) {
                Toast.makeText(getActivity(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){
        //fetchData();
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,viewType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(viewType[position].equals("Job")){
            reviewListResponseData = new ArrayList<>();
            currentView = "Job";
            fetchReviewData();
        }
        if(viewType[position].equals("Company")){
            mDataset = new ArrayList<>();
            currentView = "Company";
            fetchData();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position, String jobTitle) {

        if(currentView.equals("Company")){
            Bundle arguments = new Bundle();
            arguments.putString("CompanyName",mDataset.get(position).getCompanyName());
            ListCompanyReviewFragment fragment = new ListCompanyReviewFragment();
            fragment.setArguments(arguments);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.fl_container,fragment,"tag");
            trans.addToBackStack("tag");
            trans.commit();
        }

        if(currentView.equals("Job")){
            Bundle arguments = new Bundle();
            //need to get the data from the recycleview
            arguments.putString("JobName",jobTitle);
            ListJobReviewFragment fragment = new ListJobReviewFragment();
            fragment.setArguments(arguments);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.fl_container,fragment,"tagJob");
            trans.addToBackStack("tagJob");
            trans.commit();
        }

    }

    private void fetchReviewData() {

        (RetrofitClient.getInstance().getResponse().getAllReviews()).enqueue(new Callback<List<ReviewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewDTO>> call, Response<List<ReviewDTO>> response) {
                //Log.d("responseGET", response.body().get(0).getName());
                reviewListResponseData = response.body();
                mRecyclerView.removeAllViews();
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                mJobAdapter = new ListAllJobReviewAdapter(reviewListResponseData,CompanyReviewFragment.this);
                //mRecyclerView.swapAdapter(mJobAdapter,true);
                mRecyclerView.setAdapter(mJobAdapter);

            }

            @Override
            public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        if(currentView.equals("Company")){
            Bundle arguments = new Bundle();
            arguments.putString("CompanyName",mDataset.get(position).getCompanyName());
            ListCompanyReviewFragment fragment = new ListCompanyReviewFragment();
            fragment.setArguments(arguments);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.fl_container,fragment,"tag");
            trans.addToBackStack("tag");
            trans.commit();
        }

        if(currentView.equals("Job")){
            Bundle arguments = new Bundle();
            //need to get the data from the recycleview
            // arguments.putString("JobName",jobTitle);
            ListJobReviewFragment fragment = new ListJobReviewFragment();
            fragment.setArguments(arguments);
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction trans = fm.beginTransaction();
            trans.replace(R.id.fl_container,fragment,"tagJob");
            trans.addToBackStack("tagJob");
            trans.commit();
        }

    }
}