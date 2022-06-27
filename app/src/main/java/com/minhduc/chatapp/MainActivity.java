package com.minhduc.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.minhduc.chatapp.fragment.FragmentChats;
import com.minhduc.chatapp.fragment.FragmentHome;
import com.minhduc.chatapp.fragment.FragmentMenus;
import com.minhduc.chatapp.fragment.FragmentUsers;
import com.minhduc.chatapp.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    String userId;
    CircleImageView profileImage;
    TextView userName;
    public static User user;
    FragmentHome fragmentHome;
    FragmentUsers fragmentUsers;
    FragmentMenus fragmentMenus;
    FragmentChats fragmentChats;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takeControls();
        readInfo();
        fragmentHome = new FragmentHome();
        fragmentChats = new FragmentChats();
        fragmentMenus = new FragmentMenus();
        fragmentUsers = new FragmentUsers();
        openFragment(fragmentHome);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        openFragment(fragmentHome);
                        break;
                    case R.id.chats:
                        openFragment(fragmentChats);
                        break;
                    case R.id.menu:
                        openFragment(fragmentMenus);
                        break;
                    case R.id.users:
                        openFragment(fragmentUsers);
                        break;
                }
                return true;
            }
        });
        updateToken();
    }
    private void takeControls(){
        userName = (TextView)findViewById(R.id.txt_username);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_main);
    }
    private void readInfo(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if(user == null){
                    Toast.makeText(MainActivity.this, "Lỗi k đọc được info user!", Toast.LENGTH_SHORT).show();
                }else{
                    userName.setText(user.getUsername());
                    if(!user.getImageUrl().equals("default")){
                        Glide.with(MainActivity.this).load(user.getImageUrl()).into(profileImage);
                    }else{
                        profileImage.setImageResource(R.drawable.user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setStatus(String status) {
        DatabaseReference sttRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        sttRef.child("status").setValue(status);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatus("offline");
    }
    private void openFragment(final Fragment fragment){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_main,fragment);
        fragmentTransaction.commit();
    }

    //register token for notification

    private void updateToken(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("Token").child(user.getUid());
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("testToken", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        ref.setValue(token);
                    }
                });
    }

}