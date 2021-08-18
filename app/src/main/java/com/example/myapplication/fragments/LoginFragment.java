package com.example.myapplication.fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.Token;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {

    Button loginButton;
    EditText usernameText, passwordText;
    String username;

    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = view.findViewById(R.id.loginButton);
        usernameText = view.findViewById(R.id.usernameText);
        passwordText = view.findViewById(R.id.passwordText);
        try{
            SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            username = storeToken.getString("username",null);
            if(username!=null)
                Toast.makeText(getActivity(),"you have already Signed in",Toast.LENGTH_SHORT).show();
        } catch(NullPointerException e){
            Log.i("there is no user signed in","");
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameText.getText().toString(), passwordText.getText().toString());
            }
        });
        return view;
    }

    private void login(String username, String password){
        Call<Token> call = RetrofitClient.getInstance().getResponse().login(username, password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                if(token!=null && response.isSuccessful()){
                    Log.i("login success",token.toString());
                    SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeToken.edit();
                    editor.putString("access_token",token.getAccess_token());
                    editor.putString("refresh_token",token.getRefresh_token());
                    editor.putString("username",token.getUsername());
                    editor.apply();
                    Toast.makeText(getActivity(),"login success",Toast.LENGTH_SHORT).show();
                    //TODO return back to main activity;
                } else {
                    Toast.makeText(getActivity(),"login unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("login fail: ", t.getMessage());
                Toast.makeText(getActivity(),"login fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
}