package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.Token;
import com.example.myapplication.fragments.ListJobFragment;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {


    Button loginButton;
    EditText usernameText, passwordText;
    String username;
    CircularProgressIndicator indicator;

    FirebaseAuth auth;

    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initcomponents();

        loginButton = findViewById(R.id.loginButton);
        usernameText = findViewById(R.id.usernameText);
        indicator = findViewById(R.id.indicator);
        passwordText = findViewById(R.id.passwordText);
        ivBack = findViewById(R.id.iv_back);
        try{
            SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            username = storeToken.getString("username",null);
            if(username!=null)
                Toast.makeText(getApplicationContext(),"you have already Signed in",Toast.LENGTH_SHORT).show();
        } catch(NullPointerException e){
            Log.i("there is no user signed in","");
        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameText.getText().toString(), passwordText.getText().toString());
                indicator.setVisibility(View.VISIBLE);

            }
        });
    }



    private void initcomponents() {
        auth = FirebaseAuth.getInstance();
    }
    private void login(String username, String password){

        new Thread(new Runnable() {
            @Override
            public void run() {
                auth.signInWithEmailAndPassword(username, "123456")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){


                                }else {

                                }
                            }
                        });
            }
        }).start();
       

        Call<Token> call = RetrofitClient.getInstance().getResponse().login(username, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                if(token!=null && response.isSuccessful()){
                    Log.i("login success",token.toString());
                    SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeToken.edit();
                    editor.putString("access_token",token.getAccess_token());
                    editor.putString("refresh_token",token.getRefresh_token());
                    editor.putString("username",token.getUsername());
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("tag", "list");
                    startActivity(intent);
                    indicator.setVisibility(View.GONE);
                   // Toast.makeText(getApplicationContext(),"Login successfully",Toast.LENGTH_SHORT).show();


                    //TODO return back to main activity;
                } else {
                    Toast.makeText(getApplicationContext(),"Login unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("login fail: ", t.getMessage());
                Toast.makeText(getApplicationContext(),"Login fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}