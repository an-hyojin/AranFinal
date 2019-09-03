package com.example.aran2;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    boolean temp = false;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button addBtn;
    ArrayList<DiaryContent> diaryContentArrayList;
    ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DaysAddActivity.class);
                startActivity(intent);
            }
        });
        diaryContentArrayList = new ArrayList<>();
        final DaysAdapter daysAdapter = new DaysAdapter(getApplicationContext(), diaryContentArrayList);
        recyclerView.setAdapter(daysAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("id");
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot data: dataSnapshot.getChildren()){
//                    DiaryContent diaryContent =data.getValue(DiaryContent.class);
//                    diaryContentArrayList.add(diaryContent);
//                    daysAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final DiaryContent diaryContent =dataSnapshot.getValue(DiaryContent.class);
                diaryContentArrayList.add(0,diaryContent);
                DatabaseReference imageRef = databaseReference.child("imageUri");
                daysAdapter.notifyItemInserted(0);
                imageRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data :dataSnapshot.getChildren()){
                            ArrayList<String> uris = new ArrayList<>();
                            uris.add((String)data.getValue());
                            diaryContent.imageUri = uris;
                            diaryContentArrayList.remove(0);
                            diaryContentArrayList.add(0, diaryContent);
                            daysAdapter.notifyItemChanged(0, null);
                        }
                        daysAdapter.notifyItemChanged(0,null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
