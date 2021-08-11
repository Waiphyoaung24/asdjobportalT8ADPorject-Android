package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.CompaniesReviewDTO;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.network.RetrofitClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewReviewFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    List<ReviewDTO> reviewListResponseData;
    List<String> mDataset;
    private TextView Company_Name, UserName,ratingNumber;
    private RatingBar Company_Rating, Rating_By_User;
    private Button submit;
    private EditText Written_Review, job_title;
    private Spinner spin;
    ReviewDTO userReview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_new_review, container, false);

        mDataset = new ArrayList<>();
        userReview = new ReviewDTO();
        //Initialize interface items
        initializeInterface(root);
        spin = (Spinner) root.findViewById(R.id.company_spinner);
        spin.setOnItemSelectedListener(this);

        //Get the list of companies names
        fetchData();

        return root;
    }
    private void fetchData() {

        Call<List<CompaniesReviewDTO>> call = RetrofitClient.getInstance().getResponse().getAllCompanyReviews();
        call.enqueue(new Callback<List<CompaniesReviewDTO>>() {


            @Override
            public void onResponse(Call<List<CompaniesReviewDTO>> call, Response<List<CompaniesReviewDTO>> response) {
                List<CompaniesReviewDTO> eList = response.body();


                for (int i = 0; i < eList.size(); i++) {
                    //Log.e("name", eList.get(i).getCompanyName());
                    mDataset.add(eList.get(i).getCompanyName());
                    //set the spinner
                    ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item,mDataset);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(aa);


                }
                Toast.makeText(getActivity(), String.valueOf(mDataset.size()), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<CompaniesReviewDTO>> call, Throwable t) {
                Toast.makeText(getActivity(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void initializeInterface(View root) {

        Company_Name = root.findViewById(R.id.Company_Name);
        UserName = root.findViewById(R.id.UserName);
        Company_Rating = root.findViewById(R.id.Company_Rating);
        Rating_By_User = root.findViewById(R.id.Rating_By_User);
        ratingNumber = root.findViewById(R.id.ratingNumber);
        submit = root.findViewById(R.id.submit);
        Written_Review = root.findViewById(R.id.Written_Review);
        job_title = root.findViewById(R.id.job_title);
        setupBtns();
    }
    private void setupBtns() {
        Rating_By_User.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser==true){
                    userReview.setReviewstars(ratingBar.getRating());
                    ratingNumber.setText(String.valueOf(ratingBar.getRating()));
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userReview.setReviewDescription(Written_Review.getText().toString());
                userReview.setUserId(reviewListResponseData.get(0).getUserId());
                userReview.setReviewDate(LocalDate.now().toString());
                userReview.setCompanyName(reviewListResponseData.get(0).getCompanyName());
                userReview.setJobTitle(job_title.getText().toString());
                sendData();

            }
        });

    }

    private void sendData(){

        Call<ReviewDTO> call1 =  RetrofitClient.getInstance().getResponse().createReview(userReview);
        call1.enqueue(new Callback<ReviewDTO>() {
            @Override
            public void onResponse(Call<ReviewDTO> call1, Response<ReviewDTO> response) {
                if(response.isSuccessful()){
                    //Log.i(TAG, "post submitted to API." + response.body().toString());
                    Toast.makeText(getActivity(), "Review Submitted", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ReviewDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "An error has occured", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fetchCompanyData(mDataset.get(position));

    }

    private void fetchCompanyData(String companyName) {
        (RetrofitClient.getInstance().getResponse().getReviewsByCompanyName(companyName)).enqueue(new Callback<List<ReviewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewDTO>> call, Response<List<ReviewDTO>> response) {

                reviewListResponseData = response.body();
                //Display Company review info
                Company_Name.setText(reviewListResponseData.get(0).getCompanyName());
                Company_Rating.setRating((float)reviewListResponseData.stream().mapToDouble(x->x.getReviewstars()).average().getAsDouble());
                // This user name data will have to change. Will be the login user name
                UserName.setText(String.valueOf(reviewListResponseData.get(0).getUserId()));
            }

            @Override
            public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}