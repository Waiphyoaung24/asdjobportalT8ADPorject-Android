package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.data.Token;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText usernameText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        usernameText = findViewById(R.id.usernameText);
        passwordText = findViewById(R.id.passwordText);

        loginButton.setOnClickListener(v -> login(usernameText.getText().toString(), passwordText.getText().toString()));
    }

    private void login(String username, String password){
        Call<Token> call = RetrofitClient.getInstance().getResponse().login(username, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                Log.i("token",token.toString());
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("access_token",token.getAccess_token());
                intent.putExtra("refresh_token",token.getRefresh_token());
                intent.putExtra("username",token.getUsername());
                Log.i("intent",intent.getExtras().getString("username"));
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("error: ", t.getMessage());
            }
        });
    }


}