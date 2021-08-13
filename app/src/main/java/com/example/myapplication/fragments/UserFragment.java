package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.Token;
import com.example.myapplication.network.RetrofitClient;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserFragment extends Fragment {
    private EditText username,firsName,lastName,gender,contact;
    private ImageView avatar;
    String username_,access_token;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        username = view.findViewById(R.id.et_userName);
        firsName = view.findViewById(R.id.et_firstName);
        lastName = view.findViewById(R.id.et_lastName);
        gender = view.findViewById(R.id.et_gender);
        contact=view.findViewById(R.id.et_contactNumber);
        avatar = view.findViewById(R.id.img_avatar);

        try{
            SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            username_ = storeToken.getString("username",null);
            access_token = storeToken.getString("access_token",null);
            if(username_!=null)
                Toast.makeText(getActivity(),"you have already Signed in",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(),"there is no user signed in",Toast.LENGTH_SHORT).show();
        } catch(NullPointerException e){
            Log.i("there is no user signed in","");
        }

        loadUserProfile();
        return view;
    }
    public void loadUserProfile(){
        //check token
        if(access_token!=null){
            String authorization = "Bearer "+access_token;
            Log.i("request input", username_);
            Log.i("authorization", authorization);
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authorization, username_);
            call.enqueue(new Callback<ApplicantDTO>() {
                @Override
                public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                    Log.i("status", String.valueOf(response.code()));
                    if(response.isSuccessful()){
                        Log.i("success","success");
                        ApplicantDTO applicant = (ApplicantDTO)response.body();
                        Log.i("applicant",applicant.toString());
                        username.setText(applicant.getUsername());
                        firsName.setText(applicant.getFirstName());
                        lastName.setText(applicant.getLastName());
                        gender.setText(applicant.getGender());
                        contact.setText(applicant.getContactNumber());
                    }
                }
                @Override
                public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                    Log.i("on failure error: ", t.getMessage());
                }
            });
        } else {
            Log.i("need to go back to login", "");
            //TODO back to LoginFragment
            Toast.makeText(getActivity(),"please login first",Toast.LENGTH_SHORT).show();
        }
    }
}