package com.minhduc.chatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdater extends RecyclerView.Adapter<MessageAdater.ViewHolder> {

    private List<Message> messageList;
    private Context mcontext;
    private FirebaseUser fuser;
    private String imgUrl;
    public  static final int MSS_RIGHT=0;
    public  static final int MSS_LEFT=1;
    public MessageAdater(List<Message> list,Context mcontext,String imgUrl){
        this.messageList = list;
        this.mcontext = mcontext;
        this.imgUrl = imgUrl;
    }
        @NonNull
        @Override
        public MessageAdater.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSS_RIGHT) {
            View messView = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdater.ViewHolder(messView);
        }else{
            View messView = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdater.ViewHolder(messView);
        }
    }
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Message message = messageList.get(position);
            holder.mess.setText(message.getMessage());
            if(imgUrl.equals("default")){
                holder.profileImage.setImageResource(R.drawable.user);
            }else{
                Glide.with(mcontext).load(imgUrl).into(holder.profileImage);
            }

        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView mess;
            private CircleImageView profileImage;
            public ViewHolder(View itemView){
                super(itemView);
                mess =(TextView) itemView.findViewById(R.id.txt_message);
                profileImage = (CircleImageView) itemView.findViewById(R.id.profile_image_chat_item);
            }
        }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(messageList.get(position).getIdSender().equals(fuser.getUid())){
            return MSS_RIGHT;
        }else{
            return MSS_LEFT;
        }
    }
}
