package com.example.myapplication.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.ChatDetailActivity;
import com.example.myapplication.data.ApplicantDTO;
import com.example.myapplication.data.ChatUsers;
import com.example.myapplication.data.ReviewDTO;
import com.example.myapplication.adapters.ReviewAdapter;
import com.example.myapplication.delegates.ReviewItemDelegate;
import com.example.myapplication.network.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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


public class ListCompanyReviewFragment extends Fragment implements ReviewItemDelegate {

    RecyclerView recyclerView;
    List<ReviewDTO> reviewListResponseData;
    String CompanyName;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference ChatRequestRef;
    String userNameFromFirebase, userIdFromFirebase, Current_State, senderUserID, senderName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        Current_State = "new";
        senderUserID = auth.getCurrentUser().getUid();
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Getting the data from CompanyReviewFragment
        Bundle bundle = getArguments();
        if(bundle!=null){
            CompanyName = bundle.getString("CompanyName");
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_list_company_review, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.rvReviews);
        getReviewListData(); // call a method in which we have implement our GET type web API
        loadUserProfile();
        return root;
    }

    private void getReviewListData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog


        (RetrofitClient.getInstance().getResponse().getReviewsByCompanyName(CompanyName)).enqueue(new Callback<List<ReviewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewDTO>> call, Response<List<ReviewDTO>> response) {
                //Log.d("responseGET", response.body().get(0).getName());
                progressDialog.dismiss(); //dismiss progress dialog
                reviewListResponseData = response.body();
                setDataInRecyclerView();
            }

            @Override
            public void onFailure(Call<List<ReviewDTO>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewListResponseData,this);
        recyclerView.setAdapter(reviewAdapter); // set the Adapter to RecyclerView
    }




    @Override
    public void onTapSendMessage(String userId, String userName) {


        if(!senderName.equals(userName)){

            database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(auth.getCurrentUser()!=null){
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            ChatUsers users = dataSnapshot.getValue(ChatUsers.class);

                            if(users.getUserName().toString().equals(userName) ){
                                userNameFromFirebase = users.getUserName().toString();
                                userIdFromFirebase = dataSnapshot.getKey().toString();

                                Intent intent = new Intent(getActivity(), ChatDetailActivity.class);
                                intent.putExtra("userId",userIdFromFirebase);
                                intent.putExtra("userName",userNameFromFirebase);
                                startActivity(intent);
                                SendChatRequest(userIdFromFirebase);
                            }


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            }); 
        }
        else {
            Toast.makeText(getContext(), "You cannot send message to yourself", Toast.LENGTH_SHORT).show();
        }

    }
    private void SendChatRequest(String id){

        String receiverUserID = id;
        Log.d("TAG", "SendChatRequest: "+receiverUserID);
        ChatRequestRef.child(senderUserID).child(receiverUserID).child("request_type").setValue("pending");
        ChatRequestRef.child(senderUserID).child(receiverUserID).child("sent_by").setValue(senderUserID);
        ChatRequestRef.child(senderUserID).child(receiverUserID).child("received_by").setValue(receiverUserID);
    }

    @Override
    public void onTapReport(Long reviewId,String userName) {
        if(!senderName.equals(userName)) {
            showAlertDialog(reviewId);
        }
        else {
            Toast.makeText(getContext(), "You cannot report to yourself", Toast.LENGTH_SHORT).show();
        }


    }
    private void showAlertDialog(Long reviewId){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Report");
        builder.setMessage("Are you sure you really want to report this user?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Call<Void>call1 = RetrofitClient.getInstance().getResponse().updateReview(reviewId,"Reported");
                call1.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        Toast.makeText(getContext(), "You've reported this user", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });

            }
        });
        builder.show();

    }

    public void loadUserProfile(){
            SharedPreferences storeToken = getActivity().getSharedPreferences("storeToken", Context.MODE_PRIVATE);
           String username_ = storeToken.getString("username",null);
            String access_token = storeToken.getString("access_token",null);

            if(username_ != null&&access_token!=null){
                String authorization = "Bearer "+access_token;
                Log.i("request input", username_);
                Log.i("authorization", authorization);
                Call<ApplicantDTO> call = RetrofitClient.getInstance().getResponse().getApplicant(authorization, username_);
                call.enqueue(new Callback<ApplicantDTO>() {
                    @Override
                    public void onResponse(Call<ApplicantDTO> call, Response<ApplicantDTO> response) {
                        Log.i("status", String.valueOf(response.code()));
                        if (response.isSuccessful()) {
                            ApplicantDTO applicant = (ApplicantDTO)response.body();
                            senderName = applicant.getFirstName() + " " + applicant.getLastName();

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

}