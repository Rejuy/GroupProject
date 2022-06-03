package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.hutool.http.HttpUtil;

public class SelfItemActivity extends AppCompatActivity {
    public static final int NORMAL_REQUEST = 0;
    public static final int TEXT_REQUEST = 1;

    public static final String FOLLOW = "关注";
    public static final String UNFOLLOW = "取消关注";
    public static final String BLOCK = "屏蔽";
    public static final String UNBLOCK = "取消屏蔽";
    public static final String NO_ITEM = "no item";

    private final List<Item> itemList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;

    private Boolean isFollowing = false;
    private Boolean isBlocking = false;

    private static final String url = Constant.backendUrl + Constant.getSelfItemUrl;

    private String userIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_item);

        ImageView homePageButton = (ImageView)findViewById(R.id.homepage_button);
        homePageButton.setOnClickListener(this::backToIndex);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 1);
        int globalUserId = IndexActivity.user_id;

        // Buttons
        Button followButton = (Button) findViewById(R.id.follow_user_button);
        Button blockButton = (Button) findViewById(R.id.block_user_button);
        if (globalUserId == userId)
        {
            followButton.setVisibility(View.INVISIBLE);
            followButton.setEnabled(false);
            blockButton.setVisibility(View.INVISIBLE);
            blockButton.setEnabled(false);
        }
//        String userName = intent.getStringExtra("userName");
        // TODO: Get data of user in user_id
        // TODO: Set user image
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", userId);
        // ------------- Put global user id ?
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(curUrl, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        isFollowing = false;
        if (isFollowing)
        {
            followButton.setText(UNFOLLOW);
            followButton.setEnabled(true);
        }
        else
        {
            followButton.setText(FOLLOW);
            followButton.setEnabled(true);
        }
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing)
                {
                    followButton.setText(FOLLOW);
                    isFollowing = false;
                }
                else
                {
                    followButton.setText(UNFOLLOW);
                    isFollowing = true;
                }
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(curUrl, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
            }
        });

        isBlocking = false;
        if (isBlocking)
        {
            blockButton.setText(UNBLOCK);
            blockButton.setEnabled(true);
        }
        else
        {
            blockButton.setText(BLOCK);
            blockButton.setEnabled(true);
        }
        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBlocking)
                {
                    blockButton.setText(BLOCK);
                    isBlocking = false;
                }
                else
                {
                    blockButton.setText(UNBLOCK);
                    isBlocking = true;
                }
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(curUrl, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
            }
        });

        String userName = "啊哈！";
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
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();

        // result (String) -->> result (json)
        ///////////////////////////////////////////
//                String result = "no item";
        if (resu.equals(NO_ITEM) )
        {
            alertNoItem();
            return;
        }
        JSONArray json_list = (JSONArray) mapType.get("data");
        // Construct temporary data
        itemList.clear();
        for (int i = 0; i < json_list.size(); i++) {
            JSONObject tmp =  (JSONObject) json_list.get(i);
            String curUserName = tmp.get("username").toString();
            String curTitle = tmp.get("title").toString();
            String curContent = tmp.get("content").toString()+"\n";
            String curFollowCondition = ((Boolean)tmp.get("is_followed")?Item.FOLLOW:Item.HAVE_NOT_FOLLOW);
            String curTime = tmp.get("created_time").toString();
            curContent += curTime;
            int curLikesCount = (int)tmp.get("like_count");
            int curCommentsCount = (int)tmp.get("comment_count");
            int curType = (int)tmp.get("type");
            String curFilename = tmp.get("file_name").toString();

            Item curItem = new Item(i, curTitle, curContent, curUserName, curFollowCondition, i,
                    curLikesCount, curCommentsCount, curType, false,curFilename);
            itemList.add(curItem);
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

    public void alertNoItem()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(null);
        builder.setTitle("提示");
        builder.setMessage("没有所查动态！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(SelfItemActivity.this, "请重新查询...", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}