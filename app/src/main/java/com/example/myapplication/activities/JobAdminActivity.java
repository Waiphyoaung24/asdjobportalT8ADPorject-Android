/*
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


    }
}*/
