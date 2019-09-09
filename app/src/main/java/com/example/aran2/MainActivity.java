package com.example.aran2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        frameLayout = findViewById(R.id.main_frameLayout);
        //DiaryFragment = new DiaryFragment();
        //FragmentManager fm = getSupportFragmentManager();
        //FragmentTransaction tran == fm.beginTransaction();
        //tran.replace(R.id.main_frameLayout, diaryFragment);
        //tran.commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos){
                    case 0:

                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
