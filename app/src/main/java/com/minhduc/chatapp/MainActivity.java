package com.minhduc.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profileImage;
    TextView userName;
    FragmentUsers fragmentUsers;
    FragmentMenus fragmentMenus;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        takeControls();
        readInfo();
        fragmentMenus = new FragmentMenus();
        fragmentUsers = new FragmentUsers();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.menu:
                        fragmentTransaction.replace(R.id.frame_main,fragmentMenus);
                        break;
                    case R.id.users:
                        fragmentTransaction.replace(R.id.frame_main,fragmentUsers);
                        break;
                }
                fragmentTransaction.commit();
                return true;
            }
        });
    }
    private void takeControls(){
        userName = (TextView)findViewById(R.id.txt_username);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation_main);
    }
    private void readInfo(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
//                try {
//                    assert user!= null;
//                }catch (Exception e){
//                    Log.w("loiroi","loi ne`");
//                }
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


}