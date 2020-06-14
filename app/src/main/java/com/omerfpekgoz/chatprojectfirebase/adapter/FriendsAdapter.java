package com.omerfpekgoz.chatprojectfirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.activity.ChatActivity;
import com.omerfpekgoz.chatprojectfirebase.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.cardViewHolder> {

    private Context mContext;
    private List<String> friendsKeyList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseAuth auth;
    FirebaseUser mUser;

    String mUserId;
    String otherUserId;


    public FriendsAdapter() {
    }

    public FriendsAdapter(Context mContext, List<String> friendsKeyList) {
        this.mContext = mContext;
        this.friendsKeyList = friendsKeyList;

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        mUserId = mUser.getUid();

        friendsKeyList = new ArrayList<>();
    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_friends_layout, parent, false);

        return new cardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final cardViewHolder holder, final int position) {

        databaseReference.child("Kullanicilar").child(friendsKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);


                if (!user.getUserName().equals("null")) {


                    Picasso.with(mContext).load(user.getImage()).into(holder.profileImageFriends);
                    holder.txtKullaniciAdi.setText(user.getUserName());
                    holder.txtDurum.setText(user.getContext());

                    holder.btnMesajGonder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            otherUserId = dataSnapshot.getKey();
                            Intent intent = new Intent(mContext, ChatActivity.class);
                            intent.putExtra("otherUserName", holder.txtKullaniciAdi.getText().toString());
                            intent.putExtra("otherUserId", otherUserId);
                            mContext.startActivity(intent);


                        }
                    });


                    Boolean state = Boolean.parseBoolean(dataSnapshot.child("state").getValue().toString());

                    if (state) {
                        holder.stateİmage.setImageResource(R.drawable.onlineimage);
                    } else {
                        holder.stateİmage.setImageResource(R.drawable.offlineimage);
                    }

                    holder.btnKaldır.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeFriends(mUserId, friendsKeyList.get(position));
                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsKeyList.size();
    }


    public class cardViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImageFriends, stateİmage;
        private TextView txtKullaniciAdi, txtDurum;
        private Button btnKaldır, btnMesajGonder;

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageFriends = itemView.findViewById(R.id.profileImageFriends);
            stateİmage = itemView.findViewById(R.id.stateİmage);
            txtKullaniciAdi = itemView.findViewById(R.id.txtKullaniciAdi);
            txtDurum = itemView.findViewById(R.id.txtDurum);
            btnKaldır = itemView.findViewById(R.id.btnKaldır);
            btnMesajGonder = itemView.findViewById(R.id.btnMesajGonder);


        }
    }

    public void removeFriends(final String mUserId, final String otherUserId) {   //Arkadaş Listesinden Kişiyi Çıkarma
        databaseReference.child("Arkadaslar").child(mUserId).child(otherUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                databaseReference.child("Arkadaslar").child(otherUserId).child(mUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Kişi Arkadaş Listesinden Silindi", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}

