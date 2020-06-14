package com.omerfpekgoz.chatprojectfirebase.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.omerfpekgoz.chatprojectfirebase.fragment.FriendFragment;
import com.omerfpekgoz.chatprojectfirebase.fragment.RequestFragment;
import com.omerfpekgoz.chatprojectfirebase.fragment.UserProfileFragment;

public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                return userProfileFragment;

            case 1:
                RequestFragment requestFragment = new RequestFragment();
                return requestFragment;
            case 2:
                FriendFragment friendFragment = new FriendFragment();
                return friendFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PROFİL";
            case 1:
                return "İSTEKLER";
            case 2:
                return "ARKADAŞLAR";
            default:
                return null;
        }


    }
}
