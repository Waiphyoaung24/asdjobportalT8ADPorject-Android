package com.example.myapplication.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.network.RetrofitClient;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;


public class UserFragment extends Fragment {
    private EditText username,firsName,lastName,gender,contact,password;
    private ImageView avatar;
    private Button delete, update,logOut;
    String username_,access_token;
    private static final String AVATAR_BASE_URL = "http://10.0.2.2:8080/static/";
    private static final String AVATAR_FILE_NAME = "avatar.png";


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
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        username = view.findViewById(R.id.et_userName);
        password =view.findViewById(R.id.et_password);
        firsName = view.findViewById(R.id.et_firstName);
        lastName = view.findViewById(R.id.et_lastName);
        gender = view.findViewById(R.id.et_gender);
        contact=view.findViewById(R.id.et_contactNumber);
        avatar = view.findViewById(R.id.img_avatar);
        delete = view.findViewById(R.id.btn_delete);
        update = view.findViewById(R.id.btn_updateUserProfile);
        logOut = view.findViewById(R.id.btn_logout);
/*        selectAvatar = view.findViewById(R.id.btn_selectAvatar);*/

        try{
            SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            username_ = storeToken.getString("username",null);
            access_token = storeToken.getString("access_token",null);
            if(username_!=null)
                Toast.makeText(getActivity(),"you have already Signed in",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getActivity(),"there is no user signed in, please lpgin first",Toast.LENGTH_SHORT).show();
        } catch(NullPointerException e){
            Log.i("there is no user signed in","");
        }

        loadUserProfile();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
       /* selectAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showChoosePicDialog();
            }
        });*/
        return view;
    }

    public void loadUserProfile(){
        //check token
        if(username_ != null&&access_token!=null){
            String authorization = "Bearer "+access_token;
            Log.i("request input", username_);
            Log.i("authorization", authorization);
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authorization, username_);
            call.enqueue(new Callback<ApplicantDTO>() {
                @Override
                public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                    Log.i("status", String.valueOf(response.code()));
                    if(response.isSuccessful()){
                        Log.i("get user profile success","success");
                        ApplicantDTO applicant = (ApplicantDTO)response.body();
                        Log.i("applicant",applicant.toString());
                        username.setText(applicant.getUsername());
                        firsName.setText(applicant.getFirstName());
                        lastName.setText(applicant.getLastName());
                        gender.setText(applicant.getGender());
                        contact.setText(applicant.getContactNumber());
                        downLoadAvatar(username_);
                    }
                }
                @Override
                public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                    Log.i("on failure error: ", t.getMessage());
                }
            });
        } else {
            Log.i("need to go back to login", "");

            Toast.makeText(getActivity(),"please login first",Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUser(){
        if (username_ != null && access_token != null) {
            String authorization = "Bearer "+access_token;
            ApplicantDTO applicant = new ApplicantDTO();
            applicant.setUsername(username_);
            applicant.setPassword(password.getText().toString());
            applicant.setContactNumber(contact.getText().toString());
            applicant.setGender(gender.getText().toString());
            applicant.setFirstName(firsName.getText().toString());
            applicant.setLastName(lastName.getText().toString());
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().updateApplicant(authorization, applicant);
            call.enqueue(new Callback<ApplicantDTO>() {
                @Override
                public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                    if (response.isSuccessful()) {
                        Log.i("update success", response.toString());
                        Toast.makeText(getActivity(), "Update success", Toast.LENGTH_SHORT).show();
                        //loadUserProfile();
                        updateAvatar(applicant.getUsername(),authorization);

                    } else {
                        Log.i("response", response.message());
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                    Log.i("update on failure", t.getMessage());
                }
            });
        } else {
            //TODO back to LoginFragment
            Toast.makeText(getActivity(), "please login first", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteUser() {
        if (username_ != null && access_token != null) {
            String authorization = "Bearer " + access_token;
            Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().deleteApplicant(authorization, username_);
            call.enqueue(new Callback<ApplicantDTO>() {
                @Override
                public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "delete user success", Toast.LENGTH_SHORT).show();
                        username.setText("");
                        firsName.setText("");
                        lastName.setText("");
                        gender.setText("");
                        contact.setText("");
                        password.setText("");
                    } else {Toast.makeText(getActivity(), "delete user unsuccessful", Toast.LENGTH_SHORT).show();}
                }
                @Override
                public void onFailure(Call<ApplicantDTO> call, Throwable t) {
                    Log.i("delete user failure",t.getMessage());
                    Toast.makeText(getActivity(), "delete user unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //TODO back to LoginFragment
            Toast.makeText(getActivity(), "please login first", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOut(){
        try{
            SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = storeToken.edit();
            editor.clear().apply();
            Toast.makeText(getActivity(),"you have logged out", Toast.LENGTH_SHORT).show();
            password.setText("");
            username.setText("");
            firsName.setText("");
            lastName.setText("");
            gender.setText("");
            contact.setText("");
        } catch(NullPointerException e){
            Log.i("there is no user to log out","");
        }
    }

    private void downLoadAvatar(String username) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Glide.get(getContext()).clearDiskCache();
            }
        };
        new Thread(runnable).start();
        Glide.get(getContext()).clearMemory();
        String avatarUrl = AVATAR_BASE_URL + File.separator + username+File.separator+AVATAR_FILE_NAME;
        Glide.with(getView())
                .load(avatarUrl)
                .into(avatar);
    }

    private void updateAvatar(String username, String authorization) {
    }



    //show the option for choose take a photo or take a picture
    //check permission
    //select a photo from folder
    //take a photo and save in the folder
 }



