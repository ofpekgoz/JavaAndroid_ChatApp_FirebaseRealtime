package com.omerfpekgoz.chatprojectfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.LoginFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.adapter.TabsAdapter;
import com.omerfpekgoz.chatprojectfirebase.adapter.UserAdapter;

public class Main2Activity extends AppCompatActivity {

    private Toolbar toolbaraction;
    private TabLayout tabsMain;
    private ViewPager viewPagerMain;

    private TabsAdapter tabsAdapter;


    private FirebaseAuth auth;
    private FirebaseUser mUser;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String mUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbaraction = findViewById(R.id.toolbaraction);
        tabsMain = findViewById(R.id.tabsMain);
        viewPagerMain = findViewById(R.id.viewPagerMain);

        toolbaraction.setTitle("Chat App");
        setSupportActionBar(toolbaraction);

        tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        viewPagerMain.setAdapter(tabsAdapter);

        tabsMain.setupWithViewPager(viewPagerMain);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menuCikisYap) {
            auth.signOut();
            changeExitState();
            Toast.makeText(Main2Activity.this, "Çıkış Yapıldı", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Main2Activity.this, LoginActivity.class));
            finish();
        }
        if (item.getItemId() == R.id.menuTumKullanici) {
            startActivity(new Intent(Main2Activity.this, AllUsersActivity.class));

        }
        super.onOptionsItemSelected(item);

        return true;
    }

    @Override
    protected void onStart() {
        if (mUser == null) {

            startActivity(new Intent(Main2Activity.this, WelcomeActivity.class));
            finish();
        } else {
            mUserId = mUser.getUid();
            changeLoginState();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mUser != null) {
            changeExitState();

        }

    }

    public void changeLoginState() {
        databaseReference.child("Kullanicilar").child(mUserId).child("state").setValue("true");
    }

    public void changeExitState() {
        databaseReference.child("Kullanicilar").child(mUserId).child("state").setValue("false");
    }

}
