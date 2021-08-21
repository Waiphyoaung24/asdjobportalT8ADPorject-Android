package com.example.myapplication.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.fragments.ChatsFragment;
import com.example.myapplication.fragments.GroupsFragment;

public class ChatFragmentsAdapter extends FragmentPagerAdapter {
    public ChatFragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 : return  new ChatsFragment();
            case 1 : return  new GroupsFragment();
            default : return new ChatsFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position == 0){
            title = "CHATS";
        }
        if(position == 1){
            title = "GROUPS";
        }
        return title;
    }
}
