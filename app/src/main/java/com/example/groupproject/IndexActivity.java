package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class IndexActivity extends AppCompatActivity {
    public static int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        Intent intent = getIntent();
//        user_id = intent.getIntExtra("userId", -1);
        user_id = Constant.userId;
        System.out.println("Index?????????????");
        System.out.println(user_id);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_item));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_self_item));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_label_self_center));

        final ViewPager viewPager = findViewById(R.id.index_pager);
        final TotalPagerAdapter adapter = new TotalPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
//                        System.out.println("===MainOnTabSelected===");
//                        System.out.println(tab.getPosition());
//                        System.out.println("============");
                        viewPager.setCurrentItem(tab.getPosition());
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