package com.example.myapplication.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.BookmarkedJobsDTO;
import com.example.myapplication.data.JobAdminDTO;
import com.example.myapplication.data.ResponseMessage;
import com.example.myapplication.network.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetailFragment extends Fragment {

    Button Bookmark;
    Button SeeReviews;
    Button ApplyViaURL;
    Button ApplyViaHrEmail;
    Button ShareURL;
    long id;
    Intent intent = null;
    Uri uri;
    TextView asd_level;
    TextView job_company;
    TextView job_title;
    TextView job_qualification;
    TextView job_description;
    ImageView ivBack;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_job_detail, container, false);

        Bookmark = root.findViewById(R.id.Bookmark);

        asd_level = root.findViewById(R.id.asd_level);
        ApplyViaURL = root.findViewById(R.id.ApplyViaURL);
         job_title = root.findViewById(R.id.job_title);
         job_company = root.findViewById(R.id.job_company);
        ApplyViaHrEmail = root.findViewById(R.id.ApplyViaHrEmail);
         job_qualification = root.findViewById(R.id.job_qualification);
         job_description = root.findViewById(R.id.job_description);
        ShareURL = root.findViewById(R.id.ShareURL);
        ivBack = root.findViewById(R.id.iv_back_detail);
        SeeReviews = root.findViewById(R.id.SeeReviews);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getJob();
        Bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBookmark();
            }
        });
        ApplyViaURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyJobUrl();
            }
        });
        ApplyViaHrEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyViaHrEmail();
            }
        });
        ShareURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareURL();
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

                }
        });
    }


    private void saveBookmark() {
        Call<BookmarkedJobsDTO> call = RetrofitClient.getInstance().getResponse().saveBookmark(id);
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
        Toast.makeText(getActivity().getApplicationContext(), "Bookmark this job", Toast.LENGTH_SHORT).show();
    }

    private void ApplyJobUrl() {
        Call<ResponseMessage> call = RetrofitClient.getInstance().getResponse().ApplyJobUrl(id);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                ResponseMessage message = response.body();

                uri = Uri.parse(message.getMessage());
                intent = new Intent(Intent.ACTION_VIEW, uri);

                //if intent contain something
                if (intent != null) {
                    //to check if the device has an app that can handle the requested implicit intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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

    private void ShareURL() {
        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().ShareURL(id);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                JobAdminDTO jobadminDTO = response.body();
                String string = getString(R.string.shareurl);

                //need to update using session user's email
                uri = Uri.parse("mailto:john@gmail.com");
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, jobadminDTO.getCompanyname() + " Career Website for application of " + jobadminDTO.getJobTitle());
                intent.putExtra(Intent.EXTRA_TEXT, string + jobadminDTO.getJobPositionURL());

                //if intent contain something
                if (intent != null) {
                    //to check if the device has an app that can handle the requested implicit intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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

    private void ApplyViaHrEmail() {
        Call<JobAdminDTO> call = RetrofitClient.getInstance().getResponse().ApplyJobEmail(id);
        call.enqueue(new Callback<JobAdminDTO>() {
            @Override
            public void onResponse(Call<JobAdminDTO> call, Response<JobAdminDTO> response) {
                JobAdminDTO jobadminDTO = response.body();
                String string = getString(R.string.emailapplication, jobadminDTO.getJobTitle(), jobadminDTO.getCompanyname());

                uri = Uri.parse("mailto:" + jobadminDTO.getCompanyemail());
                //Log.e("message", String.valueOf(uri));
                intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Application of position - " + jobadminDTO.getJobTitle());
                intent.putExtra(Intent.EXTRA_TEXT, string);

                //if intent contain something
                if (intent != null) {
                    //to check if the device has an app that can handle the requested implicit intent
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
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

                        if(asd_level != null){
                            asd_level.setText(Integer.toString(jobadminDTO.getAutismLevel()));
                        }
                        if (job_title != null) {
                            job_title.setText(jobadminDTO.getJobTitle());
                        }

                        if (job_company != null) {
                            job_company.setText(jobadminDTO.getCompanyname());
                        }

                        if (job_qualification != null) {
                            job_qualification.setText(jobadminDTO.getJobqualification());
                        }

                        if (job_description != null) {
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
    public void seeReviewForAParticularCompany(){
        Bundle arguments = new Bundle();
        arguments.putString("CompanyName",job_company.getText().toString());
        ListCompanyReviewFragment fragment = new ListCompanyReviewFragment();
        fragment.setArguments(arguments);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = fm.beginTransaction();
        trans.replace(R.id.fl_container,fragment,"tag");
        trans.addToBackStack("tag");
        trans.commit();
    }

    public void setJobId(Long jobId) {
        this.id = jobId;
    }
}