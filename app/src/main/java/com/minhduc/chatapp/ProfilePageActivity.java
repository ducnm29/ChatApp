package com.minhduc.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.minhduc.chatapp.model.User;

public class ProfilePageActivity extends AppCompatActivity {
    private ImageView imgFront,imgProfile;
    private TextView txtUserName,txtToImg,txtToMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        initView();
        setUserInfo();
    }

    private void setUserInfo() {
        Intent intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");
        if(user!=null){
            txtUserName.setText(user.getUsername());
            if(!user.getImageUrl().equals("default")){
                FirebaseStorage storage =FirebaseStorage.getInstance();
                StorageReference imgRef = storage.getReference().child("images").child("profile").child(user.getImageUrl());
                GlideApp.with(getApplicationContext()).load(imgRef).into(imgProfile);
            }
        }
    }

    private void initView() {
        imgFront = (ImageView) findViewById(R.id.img_front_img_profilepage);
        imgProfile = (ImageView) findViewById(R.id.img_profile_profilepage);
        txtUserName = (TextView) findViewById(R.id.user_name_profilepage);
        txtToImg = (TextView) findViewById(R.id.txt_toimg);
        txtToMessage = (TextView) findViewById(R.id.txt_tomessage);
    }
}