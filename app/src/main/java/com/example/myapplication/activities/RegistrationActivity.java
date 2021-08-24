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
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.ChatUsers;
import com.example.myapplication.data.Token;
import com.example.myapplication.fragments.ListJobFragment;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends BaseActivity {

    Button signUpButton;
    EditText signUpUsernameText, signUpPasswordText,firstUserText,lastUserText;
    Boolean userLogined;
    String username;
    TextView tvLogin;

    private FirebaseAuth auth;
    FirebaseDatabase database;

    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signUpButton = findViewById(R.id.signUpButton);
        signUpUsernameText = findViewById(R.id.signUpUsernameText);
        tvLogin = findViewById(R.id.tv_login);
        signUpPasswordText = findViewById(R.id.signUpPasswordText);
        firstUserText = findViewById(R.id.et_firstuserName);
        lastUserText = findViewById(R.id.et_lastuserName);
        ivBack = findViewById(R.id.iv_back);


        try{
            SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            username = storeToken.getString("username",null);
            if(username!=null) {
                userLogined = true;
                Toast.makeText(this, "you have already Signed in", Toast.LENGTH_SHORT).show();
                //TODO: go back to main activity
            } else {userLogined = false;}
        } catch(NullPointerException e){
            userLogined = false;
            Log.i("there is no user signed in","");
        }




        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userLogined){
                    //do sign-up

                    auth.createUserWithEmailAndPassword(
                            signUpUsernameText.getText().toString(), "123456"
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                ChatUsers user = new ChatUsers(firstUserText.getText().toString()+" "+lastUserText.getText().toString(),
                                        signUpUsernameText.getText().toString(),
                                       "123456");

                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(user);
                                signUp(signUpUsernameText.getText().toString(), signUpPasswordText.getText().toString(),firstUserText.getText().toString(),lastUserText.getText().toString());

                            }else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    private void signUp(String username1, String password1,String firstName,String lastName){
        ApplicantDTO applicant_ = new ApplicantDTO(username1, password1,firstName,lastName);
        Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().saveApplicant(applicant_);
        call.enqueue(new Callback<ApplicantDTO>() {
            @Override
            public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                if(response.isSuccessful()){
                    Log.i("register success","register success");

                    Toast.makeText(getApplicationContext(), "register success, automatic login and back to home page", Toast.LENGTH_SHORT).show();
                    login(applicant_.getUsername(), applicant_.getPassword());
                    /*auth.signInWithEmailAndPassword(applicant_.getUsername(), applicant_.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(), "LOGGING INTO FIREBASE", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/
                }
            }
            @Override
            public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                Log.i("register request failure: ", t.getMessage());
            }
        });
    }

    private void login(String username, String password){
        Call<Token> call = RetrofitClient.getInstance().getResponse().login(username, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = (Token)response.body();
                if(token!=null && response.isSuccessful()){
                    Log.i("login success",token.toString());
                    SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeToken.edit();
                    editor.putString("access_token",token.getAccess_token());
                    editor.putString("refresh_token",token.getRefresh_token());
                    editor.putString("username",token.getUsername());
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Register success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this,MainActivity.class);
                    intent.putExtra("tag", "list");
                    startActivity(intent);
                    //TODO return back to main activity;
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("login fail: ", t.getMessage());
                Toast.makeText(getApplicationContext(),"login fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}