package com.minhduc.chatapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.minhduc.chatapp.ChatActivity;
import com.minhduc.chatapp.R;
import com.minhduc.chatapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<User> userList;
    private List<String> chatSessionId,count;
    private Context context;
    private ViewHolder holder;
    private int position;
    private FirebaseUser firebaseUser;
    private String chatSessionId1 = " ",sender,count1=" ";
    private boolean check;
    private HashMap<String,String> chatSessionIDRes,lastMessage,lastMessageRes;

    public ChatAdapter(List<User> userList, Context context,HashMap<String,String>lastMessageRes) {
        this.userList = userList;
        this.context = context;
        this.lastMessageRes = lastMessageRes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View userView  = layoutInflater.inflate(R.layout.user_chat_row,parent,false);
        chatSessionId = new ArrayList<>();
        chatSessionIDRes = new HashMap<>();
        lastMessage  = new HashMap<>();
        count = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sender = firebaseUser.getUid();
        chatSessionId.clear();
        count.clear();
        for(User user:userList){
            chatSessionId1 = " ";
            count1 = "0";
            check = true;
            String receiver = user.getId();
            DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chats");
            chatRef.addValueEventListener(new ValueEventListener() {                //lay id chatSession giua 2 user
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            HashMap<String, Object> hashMap = (HashMap<String, Object>) data.getValue();
                            String id1 = hashMap.get("idUser1").toString();
                            String id2 = hashMap.get("idUser2").toString();
                            if (id1.equals(receiver) && id2.equals(sender) ||
                                    id1.equals(sender) && id2.equals(receiver)) {
                                chatSessionIDRes.put(receiver, hashMap.get("idSession").toString());
                                chatSessionIDRes.put(receiver + "count", hashMap.get("count").toString());
                                check = false;
                            }
                        }
                        if (check) {
                            chatSessionIDRes.put(receiver," ");
                            chatSessionIDRes.put(receiver + "count","1");
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
        //readLastMess();
        return new ViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.userName.setText(user.getUsername());
       // Log.w("?", lastMessage.get(user.getId()));
        holder.lastMess.setText(lastMessageRes.get(user.getId()));
        Log.w("test7","okzo");
        if(user.getImageUrl().equals("default")){
            holder.profileImage.setImageResource(R.drawable.user);
        }else{
            Glide.with(context).load(user.getImageUrl()).into(holder.profileImage);
        }
        if(user.getStatus().equals("offline")){
            holder.statusImage.setVisibility(View.GONE);
        }else{
            holder.statusImage.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                Log.w("test6",chatSessionId.size()+"");
                intent.putExtra("chatSessionId",chatSessionIDRes.get(user.getId()));
                intent.putExtra("count",chatSessionIDRes.get(user.getId()+"count"));
                intent.putExtra("userid",userList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView userName,lastMess;
        private ImageView profileImage,statusImage;
        public ViewHolder(View itemView){
            super(itemView);
            userName = itemView.findViewById(R.id.user_chat_name_row);
            profileImage = itemView.findViewById(R.id.profile_image_user_chat_row);
            statusImage = itemView.findViewById(R.id.img_status);
            lastMess = itemView.findViewById(R.id.last_mess_chat_row);
        }
    }
    void readLastMess(){
        for(User user:userList){
            String sessId = chatSessionIDRes.get(user.getId());
            Log.w("testChat",sessId);
            DatabaseReference lastMessRef = FirebaseDatabase.getInstance().getReference("Chats")
                    .child(sessId).child("messageList");
            lastMessRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot data:snapshot.getChildren()){
                        HashMap<String,Object> hashMap = (HashMap<String, Object>) data.getValue();
                        String lastMess = hashMap.get("message").toString();
                        lastMessage.put(user.getId(),lastMess);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}
