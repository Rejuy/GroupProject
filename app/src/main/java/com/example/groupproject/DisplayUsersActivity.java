package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.hutool.http.HttpUtil;

public class DisplayUsersActivity extends AppCompatActivity {
    private final List<User> userList = new LinkedList<>();

    private RecyclerView mRecyclerView;
    private UserListAdapter mAdapter;

    private TextView title;

    public static final String FOLLOW = "关注列表";
    public static final String FOLLOWED = "被关注列表";
    public static final String BLOCK = "黑名单";
    private static final String get_user_url = Constant.backendUrl+Constant.getUserUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        Intent intent = getIntent();
        String range = intent.getStringExtra("range");
//        userId = intent.getIntExtra("userId", 1);

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", IndexActivity.user_id);
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(get_user_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();
        JSONObject json_list = (JSONObject) mapType.get("data");
        JSONArray follow_list = (JSONArray)json_list.get("src_follower_id");
        JSONArray followed_list = (JSONArray)json_list.get("dst_follower_id");
        JSONArray ban_list = (JSONArray)json_list.get("ban_id");
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(curUrl, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        title = (TextView) findViewById(R.id.display_user_title);
        title.setText(range);

        JSONArray list = null;
        if(range.equals(FOLLOW)){
            list = follow_list;
        }else if(range.equals(FOLLOWED)){
            list = followed_list;
        }else {
            list = ban_list;
        }

        // Put initial data into the word list.
        for (int i = 0; i < list.size(); i++) {
            JSONObject tmp = (JSONObject) list.get(i);
            String curUserName = tmp.get("user_name").toString();
            int user_id = (int)tmp.get("id");
            String user_image = tmp.get("user_image_name").toString();
            User curUser = new User(user_id, curUserName, user_image);
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