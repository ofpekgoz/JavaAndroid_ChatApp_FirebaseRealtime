package com.omerfpekgoz.chatprojectfirebase.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.adapter.MessageAdapter;
import com.omerfpekgoz.chatprojectfirebase.model.Message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ImageView imageBack;
    private TextView txtKullaniciAdi;
    private RecyclerView recylerChat;
    private EditText txtMesaj;
    private FloatingActionButton fabButton;

    private String userName;
    private String mUserId;
    private String otherUserId;
    private String date;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseUser mUser;
    private FirebaseAuth auth;

    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private List<String> messageKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        imageBack = findViewById(R.id.imageBack);
        txtKullaniciAdi = findViewById(R.id.txtKullaniciAdi);
        recylerChat = findViewById(R.id.recylerChat);
        txtMesaj = findViewById(R.id.txtMesaj);
        fabButton = findViewById(R.id.fabButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        mUserId = mUser.getUid();

        messageKeyList = new ArrayList<>();

        recylerChat.setHasFixedSize(true);
        recylerChat.setLayoutManager(new LinearLayoutManager(ChatActivity.this));


        date = getDate();

        String otherUserName = getIntent().getExtras().getString("otherUserName");
        final String otherUserId = getIntent().getExtras().getString("otherUserId");


        txtKullaniciAdi.setText(otherUserName);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, Main2Activity.class));
                finish();
            }
        });


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = txtMesaj.getText().toString();
                messageText = txtMesaj.getText().toString();
                sendMessage(mUserId, otherUserId, "text", date, false, messageText);
                txtMesaj.setText("");
                Log.e("otherUserId", otherUserId);
                Log.e("mUserId", mUserId);

            }

        });

        showMessage();

    }


    public void sendMessage(final String mUserId, final String otherUserId, String messageType, String date, Boolean seen, String messageText) {


        final String messageId = databaseReference.child("Mesajlar").child(mUserId).child(otherUserId).push().getKey().toString();

        final Map messageMap = new HashMap();
        messageMap.put("messageType", messageType);
        messageMap.put("seen", seen);
        messageMap.put("date", date);
        messageMap.put("messageText", messageText);
        messageMap.put("from", mUserId);


        databaseReference.child("Mesajlar").child(mUserId).child(otherUserId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child("Mesajlar").child(otherUserId).child(mUserId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
            }
        });

    }

    public void showMessage() {
        String otherUserId = getIntent().getExtras().getString("otherUserId");

        databaseReference.child("Mesajlar").child(mUserId).child(otherUserId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageList = new ArrayList<>();
                Message message = dataSnapshot.getValue(Message.class);

                messageList.add(message);
                messageKeyList.add(dataSnapshot.getKey());


                messageAdapter = new MessageAdapter(ChatActivity.this, messageKeyList, messageList);
                recylerChat.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageAdapter = new MessageAdapter(ChatActivity.this, messageKeyList, messageList);
                recylerChat.setAdapter(messageAdapter);
                messageAdapter.notifyDataSetChanged();
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

    public String getDate() {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date today = Calendar.getInstance().getTime();
        final String date = df.format(today);
        return date;
    }
}
