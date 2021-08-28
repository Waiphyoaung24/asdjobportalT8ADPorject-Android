package com.example.myapplication.fragments;

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
    DatabaseReference pushedPostRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        ChatUsersAdapter adapter = new ChatUsersAdapter(list, getContext());
        binding.chatRecycleView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecycleView.setLayoutManager(layoutManager);


        database.getReference().child("chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot1) {

                database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        if(auth.getCurrentUser()!=null){
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                ChatUsers users = dataSnapshot.getValue(ChatUsers.class);
                                String senderRoom = auth.getUid()+dataSnapshot.getKey().toString();
                                String receiverRoom = dataSnapshot.getKey().toString()+auth.getUid();
                                if(snapshot1.hasChild(senderRoom ) || snapshot1.hasChild(receiverRoom)){
                                    users.setUserId(dataSnapshot.getKey());
                                    if (!users.getUserId().equals(FirebaseAuth.getInstance().getUid())
                                    ){
                                        list.add(users);
                                        for (int i = 0 ; i<list.size(); i++){
                                            // Toast.makeText(getContext(), "listing "+list.get(i).getUserName(), Toast.LENGTH_SHORT).show();
                                            Log.d("TAG", "I've sent and received msg from those : "+list.get(i).getUserName());
                                        }
                                    }
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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