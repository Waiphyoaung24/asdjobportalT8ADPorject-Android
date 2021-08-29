package com.example.myapplication.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ChatUsersAdapter;
import com.example.myapplication.data.ChatUsers;
import com.example.myapplication.data.MessageModel;
import com.example.myapplication.databinding.FragmentChatsBinding;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {




    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<ChatUsers> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference ChatRequestRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        ChatRequestRef = FirebaseDatabase.getInstance().getReference().child("Chat Requests");
        ChatUsersAdapter adapter = new ChatUsersAdapter(list, getContext());
        binding.chatRecycleView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecycleView.setLayoutManager(layoutManager);



        ChatRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {
                        list.clear();
                        for (DataSnapshot dataSnapshot: snapshot1.getChildren()){

                            ChatUsers users = dataSnapshot.getValue(ChatUsers.class);
                            //String senderStatus = String.valueOf(snapshot.child(dataSnapshot.getKey()).child(auth.getCurrentUser().getUid()).child("request_type").getValue());
                            String sendStatus = String.valueOf(snapshot.child(auth.getUid()).child(dataSnapshot.getKey()).child("request_type").getValue());
                            String receiveStatus = String.valueOf(snapshot.child(dataSnapshot.getKey()).child(auth.getUid()).child("request_type").getValue());
                            String senderId = String.valueOf(snapshot.child(auth.getUid()).child(dataSnapshot.getKey()).child("sent_by").getValue());
                            String receiverId = String.valueOf(snapshot.child(dataSnapshot.getKey()).child(auth.getUid()).child("received_by").getValue());
                            Log.d("TAG", "onDataChange: "+receiveStatus);
                            Log.d("tag", "receive id "+ receiverId);
                            Log.d("tag", "auth is "+auth.getUid());



                            if (auth.getUid().equals(senderId) && getContext() != null && sendStatus == "pending"){
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Notification")
                                        .setMessage("You sent message request to "+users.getUserName()).show();
                            }
                            if (auth.getUid().equals(receiverId) && getContext() != null && receiveStatus.equals("pending")){
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Message Request")
                                        .setMessage(users.getUserName()+" wants to contact you")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                ChatRequestRef.child(dataSnapshot.getKey()).child(auth.getCurrentUser().getUid())
                                                        .child("request_type").setValue("approved");
                                                /*ChatRequestRef.child(auth.getCurrentUser().getUid()).child(dataSnapshot.getKey())
                                                        .child("request_type").setValue("approved");*/

                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                snapshot.child(dataSnapshot.getKey()).child(auth.getCurrentUser().getUid()).getRef().removeValue();
                                                snapshot.child(auth.getCurrentUser().getUid()).child(dataSnapshot.getKey()).getRef().removeValue();
                                                database.getReference().child("chats").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot2) {
                                                        String senderRoom = auth.getUid()+dataSnapshot.getKey();
                                                        String receiverroom = dataSnapshot.getKey()+auth.getUid();
                                                        snapshot2.child(senderRoom).getRef().removeValue();
                                                        snapshot2.child(receiverroom).getRef().removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                                                    }
                                                });
                                            }
                                        }).show();
                                Log.d("tag", "receive id "+receiverId);
                            }

                            if ( sendStatus.equals("approved") || receiveStatus.equals("approved")){
                                users.setUserId(dataSnapshot.getKey());
                                if (users.getUserName() != auth.getCurrentUser().getUid()){
                                    list.add(users);
                                }
                            }



                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}