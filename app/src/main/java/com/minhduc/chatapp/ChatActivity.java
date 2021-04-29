package com.minhduc.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    CircleImageView profileImage;
    TextView txtUserName;
    ImageView imgBack,btnSend;
    Button btnTest;
    EditText edtMessage;
    Intent intent;
    int count ;
    FirebaseUser user;
    DatabaseReference myref,chatRef;
    String receiver,chatSessionId=" ",imageUrlReceiver,test,sender;
    RecyclerView recyclerView;
    List<Message> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        anhXa();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();                     //lay info user
        chatRef = FirebaseDatabase.getInstance().getReference("Chats");
        String sender  = user.getUid();
        intent = getIntent();
        receiver = intent.getStringExtra("userid");
        Log.w("receiver",receiver);
        myref = FirebaseDatabase.getInstance().getReference("Users").child(receiver);
        String chatSessionId1 = intent.getStringExtra("chatSessionId");
        chatSessionId = intent.getStringExtra("chatSessionId");
//        count = Integer.parseInt(intent.getStringExtra("count"))+1;
//        Log.w("test4",chatSessionId1);
        getChatSessionId(sender);                                                        //lay id phien chat
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User nUser = snapshot.getValue(User.class);
                imageUrlReceiver = nUser.getImageUrl();
                Log.w("receiver1",nUser.getImageUrl());
                txtUserName.setText(nUser.getUsername());
                if(nUser.getImageUrl().equals("default")){
                    profileImage.setImageResource(R.drawable.user);
                }else {
                    Glide.with(ChatActivity.this).load(nUser.getImageUrl()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {                     //event gui tin nhan
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtMessage.getText().toString())){
                    Toast.makeText(ChatActivity.this, "Message is empty!", Toast.LENGTH_SHORT).show();
                }else{
                    sendMessage(edtMessage.getText().toString(),sender,receiver,chatSessionId);
                }
            }
        });
//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showMessage(chatSessionId);
//            }
//        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showMessage(chatSessionId1);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                showMessage(chatSessionId);
            }
        });

    }
    private void anhXa(){
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        edtMessage   = (EditText)findViewById(R.id.edtTypeMess);
        profileImage = (CircleImageView)findViewById(R.id.profile_image);
        txtUserName  = (TextView)findViewById(R.id.txt_username);
        imgBack      = (ImageView)findViewById(R.id.back_button);
        btnSend      = (ImageView)findViewById(R.id.btn_send);
        recyclerView = (RecyclerView)findViewById(R.id.recycleview_message);
//        btnTest = (Button)findViewById(R.id.btn_test);
        messageList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void sendMessage(String message,String idSender,String idReceiver,String chatSessionId){
        if(chatSessionId.equals(" ")){
            List<Message> list = new ArrayList<>();
            list.add(new Message(idSender,idReceiver,message,new SimpleDateFormat("hh:mm:ss-dd/MM/yyyy").format(new Date()),"default"));
            HashMap<String,Object> hashMap = new HashMap<>();
            hashMap.put("idUser1",idSender);
            hashMap.put("idUser2",idReceiver);
            hashMap.put("count",1);
            hashMap.put("messageList",list);
            String id = chatRef.push().getKey();
            hashMap.put("idSession",id);
            chatRef.child(id).setValue(hashMap);
            edtMessage.setText("");
            showMessage(id);
        }else{
            DatabaseReference messRef = chatRef.child(chatSessionId).child("messageList");
            DatabaseReference countRef = chatRef.child(chatSessionId).child("count");
            countRef.setValue(count);
            Message message1 = new Message(idSender,idReceiver,message,new SimpleDateFormat("hh:mm:ss-dd/MM/yyyy").format(new Date()),"default");
            Log.w("res1",chatSessionId);
            messRef.child(count+"").setValue(message1);
            edtMessage.setText("");
        }
    }
    private void getChatSessionId(String sender){
        chatSessionId = " ";
        chatRef.addValueEventListener(new ValueEventListener() {                //lay id chatSession giua 2 user
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = 0;
                try {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                        String id1 = hashMap.get("idUser1").toString();
                        String id2 = hashMap.get("idUser2").toString();
                        if ( id1.equals(receiver) &&id2.equals(sender) ||
                                id1.equals(sender) && id2.equals(receiver)) {
                            chatSessionId = hashMap.get("idSession").toString();
                            Log.w("test1", chatSessionId);
                            count = Integer.parseInt(hashMap.get("count").toString());
                            count++;
                        }
                    }
                }catch (Exception e){
                    Log.w("loir",e.toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("loir","loi");
            }
        });
    }
    private void showMessage(String chatSessionId1){
        test="";
        Log.w("check1","loi r");
        if(chatSessionId1!=null&&!chatSessionId1.equals(" ")){
            DatabaseReference messRef = chatRef.child(chatSessionId1).child("messageList");
            messRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int dem = 0;
                    messageList.clear();
                    for(DataSnapshot data: snapshot.getChildren()){
                        HashMap<String,Object> hashMap = (HashMap<String, Object>)data.getValue();
                        String message = hashMap.get("message").toString();
                        String time    = hashMap.get("time").toString();
                        String idSender = hashMap.get("idSender").toString();
                        String idReceiver = hashMap.get("idReceiver").toString();
                        String imgUrl = hashMap.get("imageUrl").toString();
                        Log.w("check1",message);
                        Message message1 = new Message(idSender,idReceiver,message,time,imgUrl);
                        messageList.add(message1);
                        dem++;
                    }
                    MessageAdater adater = new MessageAdater(messageList,ChatActivity.this,imageUrlReceiver);
                    recyclerView.setAdapter(adater);
                    recyclerView.smoothScrollToPosition(dem);
                    adater.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            Log.w("check2","loi");
        }
    }
    private void setStatus(String status) {               // set status for current user
        DatabaseReference sttRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        sttRef.child("status").setValue(status);
    }

    @Override
    protected void onResume() {                         // set user to online when resume app
        super.onResume();
        setStatus("online");
    }

    @Override
    protected void onPause() {                        // set user to offline when pause app
        super.onPause();
        setStatus("offline");
    }

}