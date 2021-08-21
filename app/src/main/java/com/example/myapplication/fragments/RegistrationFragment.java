package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.ActivityPreAccount;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.Token;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationFragment extends Fragment {

    Button signUpButton;
    EditText signUpUsernameText, signUpPasswordText;
    Boolean userLogined;
    String username;
    ImageView ivBack;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        signUpButton = view.findViewById(R.id.signUpButton);
        signUpUsernameText = view.findViewById(R.id.signUpUsernameText);
        signUpPasswordText = view.findViewById(R.id.signUpPasswordText);
        ivBack = view.findViewById(R.id.iv_back);
        try{
            SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            username = storeToken.getString("username",null);
            if(username!=null) {
                userLogined = true;
                Toast.makeText(getActivity(), "you have already Signed in", Toast.LENGTH_SHORT).show();
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
                    //do sign
                    signUp(signUpUsernameText.getText().toString(), signUpPasswordText.getText().toString());
                }
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityPreAccount.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void signUp(String username1, String password1){
        ApplicantDTO applicant_ = new ApplicantDTO(username1, password1);
        Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().saveApplicant(applicant_);
        call.enqueue(new Callback<ApplicantDTO>() {
            @Override
            public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                if(response.isSuccessful()){
                    Log.i("register success","register success");
                    Toast.makeText(getActivity(), "register success, automatic login and back to home page", Toast.LENGTH_SHORT).show();
                    login(applicant_.getUsername(), applicant_.getPassword());
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
                    SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = storeToken.edit();
                    editor.putString("access_token",token.getAccess_token());
                    editor.putString("refresh_token",token.getRefresh_token());
                    editor.putString("username",token.getUsername());
                    editor.apply();
                    Toast.makeText(getContext(), "Register success", Toast.LENGTH_SHORT).show();
                    ListJobFragment fragment = new ListJobFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction trans = fm.beginTransaction();
                    trans.replace(R.id.fl_container, fragment,"list");
                    trans.commit();
                    //TODO return back to main activity;
                }
            }
            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.i("login fail: ", t.getMessage());
                Toast.makeText(getActivity(),"login fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}