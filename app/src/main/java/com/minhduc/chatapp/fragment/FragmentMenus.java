package com.minhduc.chatapp.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minhduc.chatapp.MainActivity;
import com.minhduc.chatapp.R;
import com.minhduc.chatapp.StartActivity;

public class FragmentMenus extends Fragment {
    private ImageView imgProfile;
    private TextView txtUsername,txtEmail,txtSignout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu,container,false);
//        list = new ArrayList<>();
//        list.add(new MenuItem(R.drawable.logoutwhite,"Sign out"));
//        AdapterMenu adapterMenu = new AdapterMenu(list,getContext());
//        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view_users);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapterMenu);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        txtSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference sttRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                sttRef.child("status").setValue("offline");
                Intent intent = new Intent(v.getContext(), StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ((Activity)v.getContext()).finish();
                FirebaseAuth.getInstance().signOut();
                v.getContext().startActivity(intent);
            }
        });
    }

    private void initView(View view){
        imgProfile = (ImageView)view.findViewById(R.id.img_profile_fragment_menu);
        txtUsername = (TextView) view.findViewById(R.id.txt_username_fragment_menu);
        txtEmail = (TextView) view.findViewById(R.id.txt_email_fragment_menu);
        txtSignout = (TextView) view.findViewById(R.id.txt_signout);
        txtUsername.setText(MainActivity.user.getUsername());
        txtEmail.setText(MainActivity.user.getEmail());
    }

}
