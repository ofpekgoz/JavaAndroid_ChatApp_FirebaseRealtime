package com.omerfpekgoz.chatprojectfirebase.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.cardViewHolder> {

    private Context mContext;
    private List<String> userKeyList;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, referenceIstek;

    FirebaseAuth auth;
    FirebaseUser mUser;

    private String otherUserId;
    private String mUserId;
    private String checkValue = "";

    private FragmentManager fragmentManager;


    public UserAdapter() {
    }

    public UserAdapter(Context mContext, List<String> userKeyList) {
        this.mContext = mContext;
        this.userKeyList = userKeyList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        referenceIstek = firebaseDatabase.getReference().child("Istekler");
        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();

        mUserId = mUser.getUid();


    }

    @NonNull
    @Override
    public cardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_layout, parent, false);
        return new cardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final cardViewHolder holder, int position) {


        databaseReference.child("Kullanicilar").child(userKeyList.get(position).toString()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {


                User user = dataSnapshot.getValue(User.class);
                if (!user.getUserName().equals("null")) {
                    Picasso.with(mContext).load(user.getImage()).into(holder.profileImage);
                    holder.txtKullaniciAdi.setText(user.getUserName());
                    holder.txtDurum.setText(user.getContext());


                    Boolean state = Boolean.parseBoolean(dataSnapshot.child("state").getValue().toString());

                    if (state) {
                        holder.stateİmage.setImageResource(R.drawable.onlineimage);
                    } else {
                        holder.stateİmage.setImageResource(R.drawable.offlineimage);
                    }


                    otherUserId = dataSnapshot.getKey();
                    checkRequest(otherUserId, mUserId, holder.btnArkadasEkle);


                    holder.btnArkadasEkle.setOnClickListener(new View.OnClickListener() {


                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {


                            otherUserId = dataSnapshot.getKey();
                            checkRequestClick(mUserId, otherUserId, holder.btnArkadasEkle);


                            if (!checkValue.equals("")) {
                                cancelRequest(otherUserId, mUserId);
                                holder.btnArkadasEkle.setText("ARKADAŞ EKLE");
                                holder.btnArkadasEkle.setBackgroundColor(mContext.getColor(R.color.btnarkadasekle));


                            } else {
                                addToFriend(otherUserId, mUserId);
                                holder.btnArkadasEkle.setText("İSTEK GÖNDERİLDİ");
                                holder.btnArkadasEkle.setBackgroundColor(mContext.getColor(R.color.colorPrimary));

                            }

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
        return userKeyList.size();
    }


    public class cardViewHolder extends RecyclerView.ViewHolder {
        private CardView cardUser;
        private CircleImageView profileImage, stateİmage;
        private TextView txtKullaniciAdi, txtDurum;
        private Button btnArkadasEkle;

        public cardViewHolder(@NonNull View itemView) {
            super(itemView);

            cardUser = itemView.findViewById(R.id.cardUser);
            profileImage = itemView.findViewById(R.id.profileImageFriends);
            stateİmage = itemView.findViewById(R.id.stateİmage);
            txtKullaniciAdi = itemView.findViewById(R.id.txtKullaniciAdi);
            txtDurum = itemView.findViewById(R.id.txtDurum);
            btnArkadasEkle = itemView.findViewById(R.id.btnArkadasEkle);

        }
    }

    public void addToFriend(final String otherUserId, final String mUserId) {
        referenceIstek.child(mUserId).child(otherUserId).child("value").setValue("Istek Gönderildi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    referenceIstek.child(otherUserId).child(mUserId).child("value").setValue("Istek Alındı").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                checkValue = "Istek Alındı";
                                Toast.makeText(mContext, "İstek Gönderildi", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(mContext, "İstek Gönderilemedi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(mContext, "İstek Gönderilemedi-Sorun Var", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void checkRequest(final String otherUserId, final String mUserId, final Button button) {   //İstek Kontrol İlk Başta


        referenceIstek.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mUserId)) {
                    checkValue = dataSnapshot.child(mUserId).child("value").getValue().toString();
                    button.setText("İSTEK GÖNDERİLDİ");
                    button.setBackgroundColor(mContext.getColor(R.color.colorPrimary));


                } else {

                    button.setText("ARKADAŞ EKLE");
                    button.setBackgroundColor(mContext.getColor(R.color.btnarkadasekle));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        isFollowingMUser(mUserId, otherUserId, button);
        isFollowingOtherUser(mUserId, otherUserId, button);


    }

    public void checkRequestClick(final String otherUserId, final String mUserId, final Button button) {   //Arkadaş ekle butona basıldığında kontrol
        referenceIstek.child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mUserId)) {
                    checkValue = dataSnapshot.child(mUserId).child("value").getValue().toString();


                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cancelRequest(final String otherUserId, final String mUserId) {

        referenceIstek.child(otherUserId).child(mUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                referenceIstek.child(mUserId).child(otherUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        checkValue = "";
                        Toast.makeText(mContext, "İstek İptal Edildi", Toast.LENGTH_SHORT).show();
                        new UserAdapter().notifyDataSetChanged();

                    }
                });
            }
        });

    }

    public void isFollowingMUser(String mUserId, final String otherUserId, final Button button) {   //mUser için Takip Ediliyor mu Kontrol
        databaseReference.child("Arkadaslar").child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(otherUserId)) {

                    button.setText("TAKİP EDİLİYOR");
                    button.setBackgroundColor(mContext.getColor(R.color.btntakipediliyor));
                    button.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void isFollowingOtherUser(final String mUserId, final String otherUserId, final Button button) {  //OtherUser için takip ediliyor mu Kontrol
        databaseReference.child("Arkadaslar").child(otherUserId).addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mUserId)) {

                    button.setText("TAKİP EDİLİYOR");
                    button.setBackgroundColor(mContext.getColor(R.color.btntakipediliyor));
                    button.setEnabled(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}


