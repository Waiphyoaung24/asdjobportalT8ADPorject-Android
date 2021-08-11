package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.Token;
import com.example.myapplication.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    private ApplicantDTO applicant = new ApplicantDTO();
    private Token token;
    private Context mContext;
    private EditText username, firsName, lastName;

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

        try {
            token = (Token) bundle.getSerializable("Token");
            Log.i("get in fragment", token.toString());
        } catch(Exception e){
            Log.e("error", e.getMessage());
        }
        applicant = loadUserProfile();
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        username = view.findViewById(R.id.et_username);
        firsName = view.findViewById(R.id.et_firstName);
        lastName = view.findViewById(R.id.et_lastName);
        username.setText(applicant.getUsername());
        firsName.setText(applicant.getFirstName());
        lastName.setText(applicant.getLastName());

        return view;
    }

    public ApplicantDTO loadUserProfile(){
        //check token
        if(token.getUsername()!=null){
            String username = token.getUsername();
            String authrization = "Bearer "+token.getAccess_token();
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authrization, username);
            Log.i("request input", username);
            Log.i("token",token.toString());
            call.enqueue(new Callback<ApplicantDTO>() {
                @Override
                public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                    if(response.isSuccessful()){
                        Log.i("success","success");
                    }
                    applicant = response.body();
                    Log.i("applicant",applicant.toString());
                }
                @Override
                public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                    Log.e("error: ", t.getMessage());
                }
            });
        } else {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        return applicant;
    }

}