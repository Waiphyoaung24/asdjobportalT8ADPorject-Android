package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.Token;
import com.example.myapplication.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class UserFragment extends Fragment {

    private ApplicantDTO applicant = new ApplicantDTO();
    private Token token = new Token();
    private TextView username, firsName, lastName;

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
        try {
            token = (Token) bundle.getSerializable("Token");
            Log.i("get in fragment", token.toString());

        } catch(Exception e){
            Log.e("bundle error", e.getMessage());
        }

        loadUserProfile();
        username = view.findViewById(R.id.et_username);
        firsName = view.findViewById(R.id.et_firstName);
        lastName = view.findViewById(R.id.et_lastName);
        Log.i("username",applicant.getUsername());
        username.setText(applicant.getUsername());
        firsName.setText(applicant.getFirstName());
        lastName.setText(applicant.getLastName());
        return view;
    }
    public void loadUserProfile(){
        //check token
        if(token.getUsername()!=null){
            String username = token.getUsername();
            String authorization = "Bearer "+token.getAccess_token();
            Log.i("request input", username);
            Log.i("authorization", authorization);
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authorization, username);
            call.enqueue(new Callback<ApplicantDTO>() {
                @Override
                public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                    Log.i("status", String.valueOf(response.code()));
                    if(response.isSuccessful()){
                        Log.i("success","success");
                    }
                    applicant = response.body();
                    Log.i("applicant",applicant.toString());
                }
                @Override
                public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                    Log.e("on failure error: ", t.getMessage());
                }
            });
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
}