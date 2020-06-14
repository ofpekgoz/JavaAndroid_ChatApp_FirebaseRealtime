package com.omerfpekgoz.chatprojectfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omerfpekgoz.chatprojectfirebase.R;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView imageChat;
    private TextView txtBaslik;
    private Button btnGirisYap, btnYeniHesap;

    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        imageChat = findViewById(R.id.imageChat);
        txtBaslik = findViewById(R.id.txtBaslik);
        btnGirisYap = findViewById(R.id.btnGirisYap);
        btnYeniHesap = findViewById(R.id.btnYeniHesap);

        init();


        btnGirisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnYeniHesap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    public void init() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

}
