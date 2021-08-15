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

import com.example.myapplication.R;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.network.RetrofitClient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class UserFragment extends Fragment {
    private EditText username,firsName,lastName,gender,contact,password;
    private ImageView avatar;
    private Button delete, update,logOut,selectAvatar;
    String username_,access_token;
    private File output;

    public static final int REQUEST_PERMISSION_CAMERA_CODE = 123;
    private static final int CROP_PHOTO = 2;
    private static final int REQUEST_CODE_PICK_IMAGE=3;

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
        selectAvatar = view.findViewById(R.id.btn_selectAvatar);

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
        selectAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChoosePicDialog();
            }
        });
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
                        loadUserAvatar();
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


    //show the option for choose take a photo or take a picture
    //check permission
    //select a photo from folder
    //take a photo and save in the folder

    public void loadUserAvatar() {
        String authorization = "Bearer "+access_token;
        Call<ResponseBody> call = RetrofitClient.getInstance().getResponse().downloadAvatar(authorization,username_);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                File file = new File(Environment.getExternalStorageDirectory()+"/avatar");
                try {
                    InputStream inputStream = response.body().byteStream();
                    FileOutputStream outputStream = new FileOutputStream(file);
                    avatar.setImageURI(Uri.fromFile(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (NullPointerException nulle){
                    nulle.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

             }
        });

    }


    public void uploadAvatar(){

    }


    private void showChoosePicDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("select a picture");
        String[] items = {"take a picture","select a picture"};
        builder.setNegativeButton("取消",null);
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch(which){
                    case 0:
                        Log.e("Dialog","TAKE picture");
                        //choose to take a picture using camera
                        check_permission_and_take_photo();
                        break;
                    case 1:
                        Log.e("Dialog","CHOOSE picture");
                        check_permission_and_choose_Photo();
                        break;
                }
            }
        });
        builder.show();
    }

    public void check_permission_and_take_photo(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.i("PEMISSION","success");
                take_Photos();
        }
        else {
            Log.i("PEMISSION","need apply permission");
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},REQUEST_PERMISSION_CAMERA_CODE);
        }
    }

    public void check_permission_and_choose_Photo(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.i("PEMISSION","approved, choose a picture");
                choose_Photos();
            }
        else {
                Log.i("PEMISSION","please apply the permission");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION_CAMERA_CODE);
            }
    }

    void take_Photos(){
        File file=new File(Environment.getExternalStorageDirectory()+"/"+"avatar");
        if(!file.exists()){
            file.mkdir();
        }
        //use timestamp as file name
        output=new File(file,System.currentTimeMillis()+".jpg");
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Uri imageUri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);
    }

    void choose_Photos(){
        //how to open the folder
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        super.onActivityResult(req, res, data);
        switch (req) {
            case CROP_PHOTO:
/*                if (res==RESULT_OK) {
                    try {
                        Bitmap bit = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(imageUri));
                        img.setImageBitmap(bit);
                    } catch (Exception e) {
                        Toast.makeText(this,"程序崩溃",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Log.i("tag", "失败");
                }*/
                break;

            case REQUEST_CODE_PICK_IMAGE:
                if (res == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        Bitmap bit = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(uri));
                        avatar.setImageBitmap(bit);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("tag", e.getMessage());
                        Toast.makeText(getContext(), "can not select the picture", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.i("", "fail");
                }
                break;

            default:
                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}