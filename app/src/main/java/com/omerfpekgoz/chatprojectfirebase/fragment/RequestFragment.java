package com.omerfpekgoz.chatprojectfirebase.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.activity.Main2Activity;
import com.omerfpekgoz.chatprojectfirebase.adapter.RequestAdapter;
import com.omerfpekgoz.chatprojectfirebase.adapter.UserAdapter;
import com.omerfpekgoz.chatprojectfirebase.model.User;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment {

    private View view;

    private FirebaseAuth auth;
    private FirebaseUser mUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String mUserId;
    private List<String> requestKeyList;

    private RecyclerView recylerRequest;
    private RequestAdapter requestAdapter;
    private UserAdapter userAdapter;

    public RequestFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_request, container, false);
        init(view);
        getAllRequests();
        return view;
    }


    public void init(View view) {

        recylerRequest = view.findViewById(R.id.recylerRequest);

        recylerRequest.setHasFixedSize(true);
        recylerRequest.setLayoutManager(new GridLayoutManager(getContext(), 1));

        requestAdapter = new RequestAdapter();
        userAdapter = new UserAdapter();
        requestAdapter.notifyDataSetChanged();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        mUserId = mUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Istekler");

        requestKeyList = new ArrayList<>();
    }

    public void getAllRequests() {


        databaseReference.child(mUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String checkValue = dataSnapshot.child("value").getValue().toString();

                if (checkValue.equals("Istek Alındı")) {
                    requestKeyList.add(dataSnapshot.getKey());

                    requestAdapter.notifyDataSetChanged();

                }


                requestAdapter = new RequestAdapter(getContext(), requestKeyList);
                recylerRequest.setAdapter(requestAdapter);

            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                requestKeyList.remove(dataSnapshot.getKey());
                requestAdapter.notifyDataSetChanged();

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
