package com.omerfpekgoz.chatprojectfirebase.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.omerfpekgoz.chatprojectfirebase.R;
import com.omerfpekgoz.chatprojectfirebase.adapter.TabsAdapter;
import com.omerfpekgoz.chatprojectfirebase.model.User;
import com.omerfpekgoz.chatprojectfirebase.utils.RandomName;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    FirebaseAuth auth;
    FirebaseUser mUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    private TabLayout tabsMain;

    View view;

    private TabsAdapter tabsAdapter;
    private ViewPager viewPagerMain;

    private ImageView imgAddPhoto;
    private CircleImageView profileImage;
    private EditText txtKullaniciAdiProfil, txtEmailProfil, txtDurumProfil;
    private Button btnGuncelleProfil;
    String imageUrl;


    public UserProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        init(view);
        getUserInfo();
        return view;
    }

    public void init(View view) {

        txtKullaniciAdiProfil = view.findViewById(R.id.txtKullaniciAdiProfil);
        txtEmailProfil = view.findViewById(R.id.txtEmailProfil);
        txtDurumProfil = view.findViewById(R.id.txtDurumProfil);
        btnGuncelleProfil = view.findViewById(R.id.btnGuncelleProfil);
        profileImage = view.findViewById(R.id.profileImageFriends);
        imgAddPhoto = view.findViewById(R.id.imgAddPhoto);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        imgAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotosGallery();
            }
        });

        btnGuncelleProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

        auth = FirebaseAuth.getInstance();
        mUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Kullanicilar").child(mUser.getUid());

    }

    public void getUserInfo() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                txtKullaniciAdiProfil.setText(user.getUserName());
                txtEmailProfil.setText(user.getEmail());
                txtDurumProfil.setText(user.getContext());
                imageUrl = user.getImage();

                if (!user.getImage().equals("null")) {
                    Picasso.with(getContext()).load(imageUrl).into(profileImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateUserInfo() {
        String userName = txtKullaniciAdiProfil.getText().toString();
        String email = txtEmailProfil.getText().toString();
        String context = txtDurumProfil.getText().toString();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Kullanicilar").child(auth.getUid());

        Map<String, String> kullanici = new HashMap<>();
        kullanici.put("userName", userName);
        kullanici.put("email", email);
        kullanici.put("context", context);

        if (imageUrl.equals("null")) {
            kullanici.put("image", "null");
        } else {
            kullanici.put("image", imageUrl);

        }


        databaseReference.setValue(kullanici).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Bilgiler Güncellendi", Toast.LENGTH_LONG).show();
                    getUserInfo();
                }

            }
        });
    }

    public void openPhotosGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);

    }

    public void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            final Uri filePath = data.getData();
            final StorageReference ref = storageReference.child("KullaniciResimleri").child(RandomName.getSlatString() + ".jpg");
            ref.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Resim Güncellendi", Toast.LENGTH_LONG).show();

                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageUrl = uri.toString();
                                updateUserInfo();
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "Resim Güncelenemedi", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
