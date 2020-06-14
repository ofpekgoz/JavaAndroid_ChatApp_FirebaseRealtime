package com.omerfpekgoz.chatprojectfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omerfpekgoz.chatprojectfirebase.R;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbarLogin;
    private TextView txtYeniHesap2;
    private EditText txtEmailLogin, txtSifreLogin;
    private Button btnGirisYapLogin, btnYeniHesapLogin;

    private FirebaseAuth auth;
    private FirebaseUser mUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbarLogin = findViewById(R.id.toolbarLogin);
        txtYeniHesap2 = findViewById(R.id.txtYeniHesap2);
        txtEmailLogin = findViewById(R.id.txtEmailLogin);
        txtSifreLogin = findViewById(R.id.txtSifreLogin);
        btnGirisYapLogin = findViewById(R.id.btnGirisYapLogin);
        btnYeniHesapLogin = findViewById(R.id.btnYeniHesapLogin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        toolbarLogin.setTitle("Giriş Yap");
        setSupportActionBar(toolbarLogin);


        btnGirisYapLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        btnYeniHesapLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    public void loginUser() {
        String email = txtEmailLogin.getText().toString();
        String password = txtSifreLogin.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Snackbar.make(btnGirisYapLogin, "Email Giriniz", 3000).show();
            return;

        }
        if (TextUtils.isEmpty(password)) {
            Snackbar.make(btnGirisYapLogin, "Şifre Giriniz", 3000).show();
            return;
        }

        btnGirisYapLogin.setEnabled(false);

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    Toast.makeText(LoginActivity.this, "Giriş Başarılı", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, Main2Activity.class));
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Giriş Başarısız", Toast.LENGTH_LONG).show();
                    btnGirisYapLogin.setEnabled(true);
                }

            }
        });

    }

}
