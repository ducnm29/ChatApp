package com.minhduc.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {
    List<MenuItem> list;
    Context mcontext;

    public AdapterMenu(List<MenuItem> list, Context mcontext) {
        this.list = list;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.menu_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = list.get(position);
        holder.item_image.setImageResource(menuItem.getImageMenu());
        holder.item_name.setText(menuItem.getMenuName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView item_image;
        private TextView item_name;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            item_image = itemView.findViewById(R.id.image_item_menu);
            item_name = itemView.findViewById(R.id.txt_menu_item);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(v.getContext(),StartActivity.class);
            v.getContext().startActivity(intent);
            ((Activity)v.getContext()).finish();
        }
    }
}
