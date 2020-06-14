package com.omerfpekgoz.chatprojectfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omerfpekgoz.chatprojectfirebase.R;

public class MainActivity extends AppCompatActivity {

    private static int GECİS_SURESİ = 2000;

    FirebaseUser mUser;
    FirebaseAuth auth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        // mUserId=mUser.getUid();

        //Splash Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
                finish();
            }
        }, GECİS_SURESİ);

        onStart();
    }

    @Override
    protected void onStart() {


        super.onStart();
    }

}

