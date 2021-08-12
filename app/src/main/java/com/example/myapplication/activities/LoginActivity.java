package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.Token;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button loginButton, signUpButton;
    EditText usernameText, passwordText;
    Boolean signed=true;
    ApplicantDTO applicant = new ApplicantDTO();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        signUpButton = findViewById(R.id.signUpButton);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);
        loginButton.setOnClickListener(v -> login(usernameText.getText().toString(), passwordText.getText().toString()));
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_ = usernameText.getText().toString();
                String password_ = passwordText.getText().toString();
                Log.i("username: ", username_);
                signUp(username_,password_);

                }
            });
    }

    private void login(String username, String password){
        Call<Token> call = RetrofitClient.getInstance().getResponse().login(username, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = (Token)response.body();
                SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = storeToken.edit();
                editor.putString("access_token",token.getAccess_token());
                editor.putString("refresh_token",token.getRefresh_token());
                editor.putString("username",token.getUsername());
                editor.commit();
                Log.i("username",storeToken.getString("username","default value"));
                Log.i("token",token.toString());
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                if(token!=null){

                    bundle.putSerializable("Token", token);
                }
                intent.putExtras(bundle);
                Log.i("bundle put success", bundle.getSerializable("Token").toString());
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("error: ", t.getMessage());
            }
        });
    }

    private Boolean signUp(String username1, String password1){
        ApplicantDTO applicant1 = new ApplicantDTO(username1, password1);
        Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().saveApplicant(applicant1);
        call.enqueue(new Callback<ApplicantDTO>() {

            @Override
            public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                if(response.isSuccessful()){
                    Log.i("register success","register success");
                    ApplicantDTO applicant_ = response.body();
                    Toast.makeText(LoginActivity.this, "register success, automatic login and back to home page", Toast.LENGTH_SHORT).show();
                    login(applicant_.getUsername(), applicant_.getPassword());
                }
                signed = response.isSuccessful();
            }
            @Override
            public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                Log.e("register request failure: ", t.getMessage());
                signed = false;
            }
        });
        return signed;
    }
}