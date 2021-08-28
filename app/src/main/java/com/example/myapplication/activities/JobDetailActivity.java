package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.data.JobAdminDTO;
import com.example.myapplication.data.ResponseMessage;
import com.example.myapplication.fragments.ListCompanyReviewFragment;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailActivity extends BaseActivity {

    ImageView Bookmark;
    Button SeeReviews;
    Button ApplyViaURL;
    Button ApplyViaHrEmail;
    Button ShareURL;
    long id;
    Intent intent = null;
    Uri uri;
    TextView asd_level;
    TextView job_title;
    TextView job_qualification;
    TextView job_description;
    ImageView ivBack;
    JobAdminDTO jobadminDTO;
    ApplicantDTO applicantDTO;

    String username_,access_token;
    String authorization;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        SharedPreferences storeToken = this.getSharedPreferences("storeToken", Context.MODE_PRIVATE);
        access_token = storeToken.getString("access_token",null);
        username_ = storeToken.getString("username",null);
        authorization = "Bearer "+access_token;

        loadUserProfile();
        bindComponents();

        Intent intent = getIntent();
        id = intent.getLongExtra("jobId",-1);


        getJob();

        Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(access_token!=null) {
                    saveBookmark();
                }else{
                    Toast.makeText(JobDetailActivity.this, "Login First to use this feature", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ApplyViaURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(access_token!=null) {
                    ApplyJobUrl();
                }else{
                    Toast.makeText(JobDetailActivity.this, "Login First to use this feature", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ApplyViaHrEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(access_token!=null) {
                    ApplyViaHrEmail();
                }else{
                    Toast.makeText(JobDetailActivity.this, "Login First to use this feature", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ShareURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(access_token!=null) {
                    ShareURL();
                }else{
                    Toast.makeText(JobDetailActivity.this, "Login First to use this feature", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SeeReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeReviewForAParticularCompany();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        onBackPressed();

            }
        });

    }

    private void bindComponents() {

        Bookmark = findViewById(R.id.iv_bookmark);

        asd_level = findViewById(R.id.asd_level);
        ApplyViaURL = findViewById(R.id.ApplyViaURL);
        job_title = findViewById(R.id.tv_job_title);
        ApplyViaHrEmail = findViewById(R.id.ApplyViaHrEmail);
        job_qualification = findViewById(R.id.job_qualification);
        job_description = findViewById(R.id.job_description);
        ShareURL = findViewById(R.id.ShareURL);
        ivBack = findViewById(R.id.iv_back_detail);
        SeeReviews = findViewById(R.id.SeeReviews);
    }

    private void saveBookmark() {


        Call<BookmarkedJobsDTO> call = RetrofitClient.getInstance().getResponse().saveBookmark(id,authorization);
        call.enqueue(new Callback<BookmarkedJobsDTO>() {
            @Override
            public void onResponse(Call<BookmarkedJobsDTO> call, Response<BookmarkedJobsDTO> response) {
                if (response.isSuccessful()) {
                    Log.e("success", "success");
                }
            }

            @Override
            public void onFailure(Call<BookmarkedJobsDTO> call, Throwable t) {

            }
        });
        Toast.makeText(getApplicationContext(), "Bookmark this job", Toast.LENGTH_SHORT).show();
    }

    private void ApplyJobUrl() {
        Call<ResponseMessage> call = RetrofitClient.getInstance().getResponse().ApplyJobUrl(id,authorization);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                ResponseMessage message = response.body();

                uri = Uri.parse(message.getMessage());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                //String url = response.body();
                //Log.e("message",url);
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void ShareURL() {
        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().ShareURL(id,authorization);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                JobAdminDTO jobadminDTO = response.body();
                String string = getString(R.string.shareurl);

                //need to update using session user's email
                uri = Uri.parse("mailto:"+applicantDTO.getUsername());
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, jobadminDTO.getCompanyname() + " Career Website for application of " + jobadminDTO.getJobTitle());
                intent.putExtra(Intent.EXTRA_TEXT, string + jobadminDTO.getJobPositionURL());

                //if intent contain something

                        //controlling how this intent is handled
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


            }

            @Override
            public void onFailure(Call<JobAdminDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void ApplyViaHrEmail() {
        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().ApplyJobEmail(id,authorization);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                JobAdminDTO jobadminDTO = response.body();
                String string = getString(R.string.emailapplication, jobadminDTO.getJobTitle(), jobadminDTO.getCompanyname(),
                        applicantDTO.getFirstName(),applicantDTO.getLastName(),applicantDTO.getGender(),applicantDTO.getUsername(),applicantDTO.getContactNumber(),
                        applicantDTO.getResumeURl(),applicantDTO.getSelfIntroduction(),applicantDTO.getFirstName(),applicantDTO.getLastName());

                uri = Uri.parse("mailto:" + jobadminDTO.getCompanyemail());
                //Log.e("message", String.valueOf(uri));
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Application of position - " + jobadminDTO.getJobTitle());
                intent.putExtra(Intent.EXTRA_TEXT, string);

                //if intent contain something

                        //controlling how this intent is handled
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


            }

            @Override
            public void onFailure(Call<JobAdminDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void getJob() {

        final ProgressDialog progressDialog = new ProgressDialog(JobDetailActivity.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog

        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().getJob(id);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                progressDialog.dismiss();
                jobadminDTO = response.body();


                if (asd_level != null) {
                    asd_level.setText("Autism Level - " + Integer.toString(jobadminDTO.getAutismLevel()));
                }
                if (job_title != null) {
                    job_title.setText(jobadminDTO.getJobTitle());
                }


                if (job_qualification != null) {
                    job_qualification.setText(jobadminDTO.getJobqualification());
                }

                if (job_description != null) {
                    job_description.setText(jobadminDTO.getJobDescription());
                }


            }

            @Override
            public void onFailure(Call<JobAdminDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    public void seeReviewForAParticularCompany() {
        Intent i = new Intent(JobDetailActivity.this,MainActivity.class);
        i.putExtra("tag",jobadminDTO.getCompanyname());
        startActivity(i);


    }

    public void loadUserProfile() {

        Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authorization, username_);
        call.enqueue(new Callback<ApplicantDTO>() {
            @Override
            public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                 applicantDTO = response.body();
            }
            @Override
            public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

}