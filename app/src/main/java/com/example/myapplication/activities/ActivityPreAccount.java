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
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.ChatUsers;
import com.example.myapplication.fragments.ListJobFragment;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPreAccount extends BaseActivity {

    MaterialTextView tvLogin;
    MaterialButton btnSignup;
    MaterialTextView tvSkip;
    MaterialTextView btnFetch;
    FirebaseAuth auth;
    DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_account);
        tvLogin = findViewById(R.id.tv_login);
        btnSignup = findViewById(R.id.btn_sign_up);
        tvSkip = findViewById(R.id.tv_skip);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        SharedPreferences storeToken = getSharedPreferences("storeToken", Context.MODE_PRIVATE);
        String accesstoken = storeToken.getString("access_token","");
        if(!accesstoken.equals("")){
            Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ActivityPreAccount.this, MainActivity.class);
            intent.putExtra("tag", "list");
            startActivity(intent);
        }
        else {
            tvLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityPreAccount.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
            tvSkip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityPreAccount.this, MainActivity.class);
                    intent.putExtra("tag", "list");
                    startActivity(intent);
                }
            });
            btnSignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityPreAccount.this, RegistrationActivity.class);
                    startActivity(intent);
                }
            });

            //fetchData();

        }

    }

    /*private void fetchData() {
        Call<List<ApplicantDTO>> users = RetrofitClient.getInstance().getResponse().getAllApplicant();
        users.enqueue(new Callback<List<ApplicantDTO>>() {
            @Override
            public void onResponse(Call<List<ApplicantDTO>> call, Response<List<ApplicantDTO>> response) {
                List<ApplicantDTO> uList = response.body();

                //Toast.makeText(ActivityPreAccount.this, String.valueOf(uList.size()), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < uList.size(); i++) {
                    String emailFromMYSQL = uList.get(i).getUsername();
                    //String passwordFromMYSQL = uList.get(i).getLastName();
                    String nameFromMYSQL = uList.get(i).getFirstName() + " " + uList.get(i).getLastName();
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                String emailFromFireBase = snapshot1.child("mail").getValue().toString();
                                if (emailFromFireBase != emailFromMYSQL){
//

                                    auth.createUserWithEmailAndPassword(
                                            emailFromMYSQL, "123456"
                                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            if(task.isSuccessful()){
                                                ChatUsers user = new ChatUsers(nameFromMYSQL,
                                                        emailFromMYSQL,
                                                        "123456");
                                                String id = task.getResult().getUser().getUid();
                                                userRef.child(id).setValue(user);

                                            }
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<ApplicantDTO>> call, Throwable t) {

            }
        });*/
    //}
}