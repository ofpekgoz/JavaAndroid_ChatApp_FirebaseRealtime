package com.omerfpekgoz.chatprojectfirebase.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.adapter.UserAdapter;
import com.omerfpekgoz.chatprojectfirebase.model.User;

import java.util.ArrayList;
import java.util.List;

public class AllUsersActivity extends AppCompatActivity {

    private Toolbar toolbarAllUsers;
    private RecyclerView recylerUsers;
    private UserAdapter userAdapter;

    private Animation animaSlideBottom, animScale;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth auth;
    FirebaseUser mUser;
    private String mUserId;

    List<String> userKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        init();
        getAllUsers();
    }

    public void init() {

        toolbarAllUsers = findViewById(R.id.toolbarRequest);
        recylerUsers = findViewById(R.id.recylerRequest);

        toolbarAllUsers.setTitle("Tüm Kullanıcılar");
        setSupportActionBar(toolbarAllUsers);

        recylerUsers.setHasFixedSize(true);
        recylerUsers.setLayoutManager(new GridLayoutManager(this, 2));


        userKeyList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        mUserId = mUser.getUid();

        changeLoginState();
        animation();
    }

    public void getAllUsers() {
        databaseReference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                databaseReference.child("Kullanicilar").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (!user.getUserName().equals("null") && !dataSnapshot.getKey().equals(mUser.getUid())) {
                            if (userKeyList.indexOf(dataSnapshot.getKey()) == -1) {
                                userKeyList.add(dataSnapshot.getKey());
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                userAdapter = new UserAdapter(getApplicationContext(), userKeyList);
                recylerUsers.setAdapter(userAdapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void animation() {
        animaSlideBottom = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_bottom);
        animScale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_animation);

        recylerUsers.setAnimation(animScale);
    }

    public void changeLoginState() {
        databaseReference.child("Kullanicilar").child(mUserId).child("state").setValue("true");
    }
}
