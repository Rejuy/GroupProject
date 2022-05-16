package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class SelfItemActivity extends AppCompatActivity {
    public static final int NORMAL_REQUEST = 0;
    public static final int TEXT_REQUEST = 1;

    private final List<Item> itemList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;

    private String userIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_item);

        ImageView homePageButton = (ImageView)findViewById(R.id.homepage_button);
        homePageButton.setOnClickListener(this::backToIndex);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 1);
        String userName = intent.getStringExtra("userName");
        // TODO: Get data of user in user_id
        // TODO: Set user image
        userIntroduction = "这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介" +
                "这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介" +
                "这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介";
        String showIntroduction = userIntroduction.substring(0, 10) + "...   >>>";
        TextView tvIntroduction = findViewById(R.id.user_introduction);
        tvIntroduction.setText(showIntroduction);
        tvIntroduction.setOnClickListener(this::toIntroduction);

        TextView tvTitle = findViewById(R.id.self_item_title);
        tvTitle.setText(userName + "的个人主页");

        // Set recycler related variables
        // Put initial data into the word list.
        for (int i = 0; i < 15; i++) {
            String curUserName = "测试用户";
            String curTitle = "测试标题" + i;
            String curContent = "测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
                    "内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容" + i + "\n";
            Date d = new Date();
            System.out.println(d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = sdf.format(d);
            curContent += curTime;
            int curLikesCount = i * 100;
            int curCommentsCount = i * 10;
            int curType = Item.TEXT;
            Item curItem = new Item(i, curTitle, curContent, userName, userId,
                    curLikesCount, curCommentsCount, curType, false);
            itemList.add(curItem);

//            mContentList.addLast("动态说了点什么呢？（我是内容） " + i + "\n" + curTime);
        }

        // Create recycler view.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PostListAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Inflate the layout for this fragment
    }

    public void backToIndex(View view) {
//        Log.d(LOG_TAG, "To register button clicked!");
        System.out.print("image click");
        Intent intent = new Intent(this, IndexActivity.class);
        startActivityForResult(intent, NORMAL_REQUEST);
    }

    public void toIntroduction(View view) {
//        Log.d(LOG_TAG, "To register button clicked!");
        System.out.print("image click");
        Intent intent = new Intent(this, IntroductionActivity.class);
        intent.putExtra("introduction", userIntroduction);
        startActivityForResult(intent, NORMAL_REQUEST);
    }
}