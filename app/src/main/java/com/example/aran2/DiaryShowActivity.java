package com.example.aran2;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DiaryShowActivity extends AppCompatActivity {
    boolean temp = false;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button addBtn;
    DaysAdapter daysAdapter;
    ArrayList<DiaryContent> diaryContentArrayList = new ArrayList<>();
    final int ADD_CODE = 11;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("id");
    TextView txtinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_main);
        recyclerView = findViewById(R.id.diaryMain_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Button cardGame = findViewById(R.id.Main_startCard);
        cardGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CardActivity.class);
                startActivity(intent);
            }
        });
        addBtn = findViewById(R.id.diaryMain_addBtn);

        ConnectivityManager connectivityManager = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        txtinfo = findViewById(R.id.diaryMain_networkTxt);
        if(info==null){
            txtinfo.setVisibility(View.VISIBLE);
            txtinfo.setText("인터넷에 연결되지 않았습니다!");
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OneButtonDialog oneButtonDialog = new OneButtonDialog(DiaryShowActivity.this, "인터넷에 연결되지 않았습니다!", "인터넷 연결을 확인해 주세요", "확인");
                    oneButtonDialog.show();
                }
            });
        }else {
            txtinfo.setVisibility(View.VISIBLE);
            txtinfo.setText("로딩중 . . .");
            daysAdapter = new DaysAdapter(getApplicationContext(), diaryContentArrayList);
            recyclerView.setAdapter(daysAdapter);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DaysAddActivity.class);
                    intent.putExtra("isModi", false);
                    startActivityForResult(intent, ADD_CODE);
                    overridePendingTransition(R.anim.slide_left_show,R.anim.slide_left_remove);

                }
            });
        }
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DiaryContent diaryContent = dataSnapshot.getValue(DiaryContent.class);
                diaryContentArrayList.add(0, diaryContent);
                daysAdapter.notifyDataSetChanged();
                DiaryisEmpty();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DiaryContent diaryContent = dataSnapshot.getValue(DiaryContent.class);
                int order = diaryContentArrayList.indexOf(diaryContent);
                diaryContentArrayList.remove(order);
                diaryContentArrayList.add(order, diaryContent);
                daysAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                DiaryisEmpty();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void DiaryisEmpty(){
        if(diaryContentArrayList.size()==0){
            txtinfo.setText("일기가 없습니다! 추가하기 버튼을 눌러 기록해주세요");
            txtinfo.setVisibility(View.VISIBLE);
        }else{
            txtinfo.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ADD_CODE:
//                    diaryContentArrayList.clear();
//                    databaseReference.addChildEventListener(new ChildEventListener() {
//                     @Override
//                     public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                         DiaryContent diaryContent = dataSnapshot.getValue(DiaryContent.class);
//                         diaryContentArrayList.add(0, diaryContent);
//                         daysAdapter.notifyDataSetChanged();
//                         DiaryisEmpty();
//                     }
//
//                     @Override
//                     public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                     }
//
//                     @Override
//                     public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                     }
//
//                     @Override
//                     public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                     }
//
//                     @Override
//                     public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                     }
//                 });
//                    firebaseDatabase = FirebaseDatabase.getInstance();
//                    final String id = data.getStringExtra("id");
//                    databaseReference = firebaseDatabase.getReference("id");
//
//                    databaseReference.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            diaryContentArrayList.clear();
//                            for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                DiaryContent diaryContent = data.getValue(DiaryContent.class);
//                                diaryContentArrayList.add(0, diaryContent);
//
//                            }
//                            daysAdapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
            }
//
        }
    }
}
