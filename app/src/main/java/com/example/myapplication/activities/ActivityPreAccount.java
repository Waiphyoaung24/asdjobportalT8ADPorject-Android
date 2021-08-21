package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.fragments.ListJobFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class ActivityPreAccount extends AppCompatActivity {

    MaterialTextView tvLogin;
    MaterialButton btnSignup;
    MaterialTextView tvSkip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_account);
        tvLogin = findViewById(R.id.tv_login);
        btnSignup = findViewById(R.id.btn_sign_up);
        tvSkip = findViewById(R.id.tv_skip);
        SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
        String accesstoken = storeToken.getString("access_token","");
        if(!accesstoken.equals("")){
            Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityPreAccount.this, MainActivity.class);
            intent.putExtra("tag", "skip");
            startActivity(intent);
        }
        else {
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityPreAccount.this, MainActivity.class);
                    intent.putExtra("tag", "login");
                    startActivity(intent);
                }
            });
            tvSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityPreAccount.this, MainActivity.class);
                    intent.putExtra("tag", "skip");
                    startActivity(intent);
                }
            });
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityPreAccount.this, MainActivity.class);
                    intent.putExtra("tag", "signup");
                    startActivity(intent);
                }
            });
        }

    }
}