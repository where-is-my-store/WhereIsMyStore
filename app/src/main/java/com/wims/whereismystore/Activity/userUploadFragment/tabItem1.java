package com.wims.whereismystore.Activity.userUploadFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wims.whereismystore.Activity.SaleUploadActivity;
import com.wims.whereismystore.Class.Photos;
import com.wims.whereismystore.Class.Post;
import com.wims.whereismystore.Class.SaleListAdapter;
import com.wims.whereismystore.Class.SaleListItem;
import com.wims.whereismystore.R;

import java.util.ArrayList;
import java.util.HashMap;

public class tabItem1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters
    private String userUploadID;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<SaleListItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private DatabaseError databaseError;
    private Photos photo;
    private HashMap<String,Object> post;

    public tabItem1(String userUploadID) {
        this.userUploadID=userUploadID;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_item1, container, false);

        //리사클러 뷰
        recyclerView=view.findViewById(R.id.tab_itemTotal);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        arrayList=new ArrayList<>();

        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference("post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    post= (HashMap<String, Object>) dataSnapshot.getValue();
                    if(post.get("writerPin").toString().equals(userUploadID)) {
                        SaleListItem saleListItem = dataSnapshot.getValue(SaleListItem.class);
                        saleListItem.setPostID(dataSnapshot.getKey());
                        photo = dataSnapshot.child("photo").getValue(Photos.class);
                        saleListItem.setImage(photo.getPhoto_1());
                        arrayList.add(0, saleListItem);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("fragment1",String.valueOf(databaseError.toException()));
            }
        });
        adapter=new SaleListAdapter(arrayList,getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}