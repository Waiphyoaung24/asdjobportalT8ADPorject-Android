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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    Button loginButton;
    EditText usernameText, passwordText;
    String username;

    FirebaseAuth auth;

    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initcomponents();

        loginButton = findViewById(R.id.loginButton);
        usernameText = findViewById(R.id.usernameText);
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

            }
        });
    }

    private void initcomponents() {
        auth = FirebaseAuth.getInstance();
    }
    private void login(String username, String password){

        auth.signInWithEmailAndPassword(username, "123456")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "LOGGING IN TO FIREBASE", Toast.LENGTH_SHORT).show();
                        }else {

                        }
                    }
                });

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
                    Toast.makeText(getApplicationContext(),"login success",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("tag", "list");
                    startActivity(intent);


                    //TODO return back to main activity;
                } else {
                    Toast.makeText(getApplicationContext(),"login unsuccessful",Toast.LENGTH_SHORT).show();
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