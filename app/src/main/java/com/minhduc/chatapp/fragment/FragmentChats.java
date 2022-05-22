package com.minhduc.chatapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhduc.chatapp.R;
import com.minhduc.chatapp.adapter.ChatAdapter;
import com.minhduc.chatapp.model.ChatSession;
import com.minhduc.chatapp.model.Message;
import com.minhduc.chatapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentChats extends Fragment {
    User user;
    List<User> userList,allUserList;
    RecyclerView recyclerView;
    ChatAdapter chatAdapter;
    String currentUser;
    HashMap<String,String> lastMessRes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users,container,false);
        userList = new ArrayList<>();
        allUserList = new ArrayList<>();
        lastMessRes = new HashMap<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        readAllUser();
        readUser();
        refreshUser();
        return view;
    }
    public void readUser(){
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ChatSession chatSession = dataSnapshot.getValue(ChatSession.class);
                    HashMap<String,Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    if(hashMap!= null){
                        Log.w("alo",hashMap.get("idUser1").toString());
                        User user1 = getUserByID(hashMap.get("idUser1").toString());
                        User user2 = getUserByID(hashMap.get("idUser2").toString());
                        if(hashMap.get("idUser1").toString().equals(currentUser)){
                            List<Message> messages = chatSession.getMessageList();
                            if(messages!=null){
                                lastMessRes.put(user2.getId(),messages.get(messages.size()-1).getMessage());
                                userList.add(user2);
                                Log.w("alo",user2.getId()+"haha");
                            }
                        }else if(hashMap.get("idUser2").toString().equals(currentUser)){
                            List<Message> messages = chatSession.getMessageList();
                            if(messages!=null){
                                lastMessRes.put(user1.getId(),messages.get(messages.size()-1).getMessage());
                                userList.add(user1);
                                Log.w("alo",user1.getId()+"hahe");
                            }
                        }
                    }
                }
                chatAdapter = new ChatAdapter(userList,getContext(),lastMessRes);
                recyclerView.setAdapter(chatAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public User getUserByID(String userId){
        user = new User("","","","","","");
        for(User user1:allUserList){
            if(user1.getId().equals(userId)){
                return user1;
            }
        }
        return user;
    }
    private void readAllUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUserList.clear();
                for(DataSnapshot data:snapshot.getChildren()){
                    User user = data.getValue(User.class);
                    assert user!= null;
                    //assert
                    if(firebaseUser!= null&&!user.getId().equals(firebaseUser.getUid())){
                        allUserList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("loi","loi r");
            }
        });
    }
    private void refreshUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                readAllUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        readUser();
    }

}
