package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DisplayUsersActivity extends AppCompatActivity {
    private final List<User> userList = new LinkedList<>();

    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;

    private TextView title;

    public static final String FOLLOW = "关注列表";
    public static final String FOLLOWED = "被关注列表";
    public static final String BLOCK = "黑名单";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        Intent intent = getIntent();
        String range = intent.getStringExtra("range");
//        userId = intent.getIntExtra("userId", 1);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", IndexActivity.user_id);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(curUrl, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        title = (TextView) findViewById(R.id.display_user_title);
        title.setText(range);

        // Put initial data into the word list.
        for (int i = 0; i < 15; i++) {
            String curUserName = range + "测试用户" + i;
            User curUser = new User(i, curUserName, "asdf");
            userList.add(curUser);
        }

        // Create recycler view.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new UserListAdapter(this, userList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Inflate the layout for this fragment
    }
}