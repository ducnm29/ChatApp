package com.minhduc.chatapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.minhduc.chatapp.GlideApp;
import com.minhduc.chatapp.R;
import com.minhduc.chatapp.model.NewFeed;
import java.util.List;

public class NewfeedAdapter extends RecyclerView.Adapter<NewfeedAdapter.NewfeedHolder> {
    private List<NewFeed> listNewfeed;
    private Context context;

    public  NewfeedAdapter(List<NewFeed> listNewfeed, Context context){
        this.listNewfeed = listNewfeed;
        this.context = context;
    }
    @NonNull
    @Override
    public NewfeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newfeed_item,parent,false);
        return new NewfeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewfeedHolder holder, int position) {
        NewFeed newFeed = listNewfeed.get(position);
        if(newFeed != null){
            holder.txtUsername.setText(newFeed.getUser().getUsername());
            holder.txtCaption.setText(newFeed.getCaption());
            Log.w("testrecycle",newFeed.getCaption());
            if(!newFeed.getImgLink().equals("")){
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imgRef = storage.getReference().child("images/newfeed/"+newFeed.getImgLink());
                GlideApp.with(context).load(imgRef).into(holder.imgNewfeed);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listNewfeed.size();
    }

    class NewfeedHolder extends RecyclerView.ViewHolder{
        private ImageView imgProfile,imgNewfeed;
        private TextView txtUsername,txtCaption;
        public NewfeedHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.profile_image_nf_item);
            imgNewfeed = (ImageView) itemView.findViewById(R.id.img_nf_item);
            txtUsername = (TextView) itemView.findViewById(R.id.txt_username_nf_item);
            txtCaption = (TextView) itemView.findViewById(R.id.txt_caption_nf_item);
        }
    }
}
