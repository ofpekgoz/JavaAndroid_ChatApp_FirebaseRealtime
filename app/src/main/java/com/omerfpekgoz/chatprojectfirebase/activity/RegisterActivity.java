package com.omerfpekgoz.chatprojectfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omerfpekgoz.chatprojectfirebase.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Toolbar toolbarRegister;
    private TextView txtYeniHesap;
    private EditText txtKullaniciAdiRegister, txtEmailRegister, txtSifreRegister;
    private Button btnYeniHesapRegister, btnHesabinizleGirisRegister;

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbarRegister = findViewById(R.id.toolbarRegister);
        txtYeniHesap = findViewById(R.id.txtYeniHesap);
        txtKullaniciAdiRegister = findViewById(R.id.txtKullaniciAdiRegister);
        txtEmailRegister = findViewById(R.id.txtEmailRegister);
        txtSifreRegister = findViewById(R.id.txtSifreRegister);
        btnYeniHesapRegister = findViewById(R.id.btnYeniHesapRegister);
        btnHesabinizleGirisRegister = findViewById(R.id.btnHesabinizleGirisRegister);

        auth = FirebaseAuth.getInstance();


        toolbarRegister.setTitle("Hesap Oluştur");
        setSupportActionBar(toolbarRegister);

        btnYeniHesapRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });

        btnHesabinizleGirisRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    public void createNewAccount() {

        final String userName = txtKullaniciAdiRegister.getText().toString();
        final String email = txtEmailRegister.getText().toString();
        final String password = txtSifreRegister.getText().toString();

        if (TextUtils.isEmpty(userName)) {
            Snackbar.make(btnYeniHesapRegister, "Kullanıcı Adı Giriniz", 3000).show();
            return;

        }
        if (TextUtils.isEmpty(email)) {
            Snackbar.make(btnYeniHesapRegister, "Email Giriniz", 3000).show();
            return;

        }
        if (TextUtils.isEmpty(password)) {
            Snackbar.make(btnYeniHesapRegister, "Şifre Giriniz", 3000).show();
            return;
        }


        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    insertUserInfo(userName, email, password);

                    Toast.makeText(RegisterActivity.this, "Hesabınız Başarılı Şekilde Oluşturuldu", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();


                } else {
                    if (task.getException().getMessage().toString().equals("The given password is invalid.")) {
                        Snackbar.make(btnYeniHesapRegister, "Şifre 6 Karakterden Az Olamaz", 3000).show();
                    }

                    Toast.makeText(RegisterActivity.this, "Hesap Oluşturulamadı", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    public void insertUserInfo(String userName, String email, String password) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());

        Map<String, String> kullanici = new HashMap<>();
        kullanici.put("userName", userName);
        kullanici.put("email", email);
        kullanici.put("password", password);
        kullanici.put("image", "null");
        kullanici.put("context", "null");

        databaseReference.setValue(kullanici);
    }
}
