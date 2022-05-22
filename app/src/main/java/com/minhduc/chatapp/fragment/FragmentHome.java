package com.minhduc.chatapp.fragment;


import static android.app.Activity.RESULT_OK;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.minhduc.chatapp.AddNewfeedActivity;
import com.minhduc.chatapp.R;
import com.minhduc.chatapp.adapter.NewfeedAdapter;
import com.minhduc.chatapp.model.NewFeed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragmentHome extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton btnAdd;
    private DatabaseReference newfeedRef;
    private FirebaseDatabase firebaseDatabase;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;
    //private User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getAllNewfeed();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewFeed();
            }
        });
        getAllNewfeed();
    }

    private void getAllNewfeed() {
        List<NewFeed> list = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        newfeedRef = firebaseDatabase.getReference("Newfeed");
        //Query query = newfeedRef.orderByChild("time");
        newfeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    NewFeed newFeed = (NewFeed) data.getValue(NewFeed.class);
                    Log.w("testNewfeed",newFeed.getCaption());
                    list.add(newFeed);
                }
                Collections.reverse(list);
                NewfeedAdapter adapter = new NewfeedAdapter(list,getContext());
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("testNewfeed",error.getDetails());
            }
        });


    }

    private void addNewFeed() {
        Intent intent = new Intent(getActivity(), AddNewfeedActivity.class);
        startActivity(intent);
    }

    void initView(View view){
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_home);
        btnAdd = (FloatingActionButton) view.findViewById(R.id.floating_btn_home);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllNewfeed();
    }
}
