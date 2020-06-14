package com.omerfpekgoz.chatprojectfirebase.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.adapter.FriendsAdapter;
import com.omerfpekgoz.chatprojectfirebase.adapter.RequestAdapter;
import com.omerfpekgoz.chatprojectfirebase.model.User;

import java.util.ArrayList;
import java.util.List;


public class FriendFragment extends Fragment {

    View view;

    private FirebaseAuth auth;
    private FirebaseUser mUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String mUserId;
    private List<String> friendsKeyList;

    private RecyclerView recylerFriends;
    private FriendsAdapter friendsAdapter;

    public FriendFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        init(view);
        getAllFriends();
        return view;
    }

    public void init(View view) {
        recylerFriends = view.findViewById(R.id.recylerFriends);

        recylerFriends.setHasFixedSize(true);
        recylerFriends.setLayoutManager(new GridLayoutManager(getContext(), 1));

        friendsAdapter = new FriendsAdapter();
        friendsAdapter.notifyDataSetChanged();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        mUserId = mUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Arkadaslar");

        friendsKeyList = new ArrayList<>();
    }

    public void getAllFriends() {

        databaseReference.child(mUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String checkValue = dataSnapshot.child("date").getValue().toString();

                if (!checkValue.equals("") && (friendsKeyList.indexOf(dataSnapshot.getKey()) == -1)) {
                    friendsKeyList.add(dataSnapshot.getKey());
                    friendsAdapter.notifyDataSetChanged();

                }

                friendsAdapter = new FriendsAdapter(getContext(), friendsKeyList);
                recylerFriends.setAdapter(friendsAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                friendsKeyList.remove(dataSnapshot.getKey());
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
