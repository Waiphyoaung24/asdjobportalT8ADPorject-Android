package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.data.JobAdminDTO;
import com.example.myapplication.data.ResponseMessage;
import com.example.myapplication.network.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobAdminActivity extends AppCompatActivity {

    Button Bookmark;
    Button SeeReviews;
    Button ApplyViaURL;
    Button ApplyViaHrEmail;
    Button ShareURL;

    long id;
    Intent intent = null;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_admin);
        Intent intent = getIntent();
        id = intent.getLongExtra("jobId",0);
        getJob();

        Bookmark = findViewById(R.id.Bookmark);
        Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBookmark();
            }
        });

        ApplyViaURL = findViewById(R.id.ApplyViaURL);
        ApplyViaURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyJobUrl();
            }
        });

        ApplyViaHrEmail = findViewById(R.id.ApplyViaHrEmail);
        ApplyViaHrEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyViaHrEmail();
            }
        });

        ShareURL = findViewById(R.id.ShareURL);
        ShareURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareURL();
            }
        });
    }

    private void saveBookmark(){
        Call<BookmarkedJobsDTO> call = RetrofitClient.getInstance().getResponse().saveBookmark(id);
        call.enqueue(new Callback<BookmarkedJobsDTO>() {
            @Override
            public void onResponse(Call<BookmarkedJobsDTO> call, Response<BookmarkedJobsDTO> response) {
                if(response.isSuccessful()){
                    Log.e("success","success");
                }
            }

            @Override
            public void onFailure(Call<BookmarkedJobsDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void ApplyJobUrl(){
        Call<ResponseMessage> call = RetrofitClient.getInstance().getResponse().ApplyJobUrl(id);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                ResponseMessage message = response.body();

                uri = Uri.parse(message.getMessage());
                intent = new Intent(Intent.ACTION_VIEW, uri);

                //if intent contain something
                if(intent !=  null){
                    //to check if the device has an app that can handle the requested implicit intent
                    if(intent.resolveActivity(getPackageManager())!=null){
                        //controlling how this intent is handled
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                //String url = response.body();
                //Log.e("message",url);
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void ShareURL(){
        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().ShareURL(id);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                JobAdminDTO jobadminDTO = response.body();
                String string = getString(R.string.shareurl);

                //need to update using session user's email
                uri = Uri.parse("mailto:john@gmail.com");
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, jobadminDTO.getCompanyname()+" Career Website for application of "+jobadminDTO.getJobTitle());
                intent.putExtra(Intent.EXTRA_TEXT,  string+jobadminDTO.getJobPositionURL());

                //if intent contain something
                if(intent !=  null){
                    //to check if the device has an app that can handle the requested implicit intent
                    if(intent.resolveActivity(getPackageManager())!=null){
                        //controlling how this intent is handled
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JobAdminDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void ApplyViaHrEmail(){
        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().ApplyJobEmail(id);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                JobAdminDTO jobadminDTO = response.body();
                String string = getString(R.string.emailapplication, jobadminDTO.getJobTitle(),jobadminDTO.getCompanyname());

                uri = Uri.parse("mailto:"+jobadminDTO.getCompanyemail());
                //Log.e("message", String.valueOf(uri));
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Application of position - " + jobadminDTO.getJobTitle());
                intent.putExtra(Intent.EXTRA_TEXT,  string);

                //if intent contain something
                if(intent !=  null){
                    //to check if the device has an app that can handle the requested implicit intent
                    if(intent.resolveActivity(getPackageManager())!=null){
                        //controlling how this intent is handled
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JobAdminDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void getJob() {
        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().getJob(id);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                JobAdminDTO jobadminDTO = response.body();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TextView job_title = findViewById(R.id.job_title);
                        if(job_title != null){
                            job_title.setText(jobadminDTO.getJobTitle());
                        }
                        TextView job_company = findViewById(R.id.job_company);
                        if(job_company != null){
                            job_company.setText(jobadminDTO.getCompanyname());
                        }
                        TextView job_qualification =  findViewById(R.id.job_qualification);
                        if(job_qualification != null){
                            job_qualification.setText(jobadminDTO.getJobqualification());
                        }
                        TextView job_description = findViewById(R.id.job_description);
                        if(job_description != null){
                            job_description.setText(jobadminDTO.getJobDescription());
                        }
                    }
                }).start();

            }

            @Override
            public void onFailure(Call<JobAdminDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }
}