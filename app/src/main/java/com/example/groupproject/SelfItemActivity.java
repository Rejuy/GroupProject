package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    private ImageView userImage;
    private TextView followcount;
    private TextView followedcount;
    private TextView createtime;
    private String creat_time = "";
    int follower_num;
    int follow_num;

    private Boolean isFollowing = false;
    private Boolean isBlocking = false;

    private static final String get_user_url = Constant.backendUrl+Constant.getUserUrl;
    private static final String url = Constant.backendUrl + Constant.getSelfItemUrl;
    private static final String follow_url = Constant.backendUrl+Constant.followUrl;
    private static final String follow_delete_url = Constant.backendUrl+Constant.followDeleteUrl;
    private static final String ban_url = Constant.backendUrl+Constant.banUrl;
    private static final String ban_delete_url = Constant.backendUrl+Constant.banDeleteUrl;
    private String userIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_item);

        ImageView homePageButton = (ImageView)findViewById(R.id.homepage_button);
        homePageButton.setOnClickListener(this::backToIndex);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", -1);
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
        paramMap.put("user_id", globalUserId);
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(get_user_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();
        JSONObject json_list = (JSONObject) mapType.get("data");
        JSONArray follow_list = (JSONArray)json_list.get("src_follower_id");
        JSONArray ban_list = (JSONArray)json_list.get("ban_id");
        isFollowing = false;
        for (int i = 0;i<follow_list.size();++i){
            JSONObject tmp = (JSONObject) follow_list.get(i);
            if(tmp.get("id").toString().equals(Integer.toString(userId))){
                isFollowing = true;
            }
        }
        // ------------- Put global user id ?
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(curUrl, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
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
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("src_user_id", IndexActivity.user_id);
                paramMap.put("dst_user_id",userId);
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                if (isFollowing)
                {
                    followButton.setText(FOLLOW);
                    isFollowing = false;
                    String result = HttpUtil.post(follow_delete_url, obj_string);
                    refresh(isFollowing);
                }
                else
                {
                    followButton.setText(UNFOLLOW);
                    isFollowing = true;
                    String result = HttpUtil.post(follow_url, obj_string);
                    refresh(isFollowing);
                }
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(curUrl, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
            }
        });

        isBlocking = false;
        for (int i = 0;i<ban_list.size();++i){
            JSONObject tmp = (JSONObject) ban_list.get(i);
            if(tmp.get("id").toString().equals(Integer.toString(userId))){
                isBlocking = true;
            }
        }
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
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("src_user_id", IndexActivity.user_id);
                paramMap.put("dst_user_id",userId);
                JSONObject obj = new JSONObject(paramMap);
                String obj_string = obj.toJSONString();
                if (isBlocking)
                {
                    blockButton.setText(BLOCK);
                    isBlocking = false;
                    String result = HttpUtil.post(ban_delete_url, obj_string);
                }
                else
                {
                    blockButton.setText(UNBLOCK);
                    isBlocking = true;
                    String result = HttpUtil.post(ban_url, obj_string);
                }
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(curUrl, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
            }
        });
        userImage = findViewById(R.id.user_image);
        HashMap<String, Object> ano_paramMap = new HashMap<>();
        ano_paramMap.put("user_id", userId);
        JSONObject ano_obj = new JSONObject(ano_paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String ano_obj_string = ano_obj.toJSONString();
        String ano_result = HttpUtil.post(get_user_url, ano_obj_string);
        HashMap ano_mapType = JSON.parseObject(ano_result,HashMap.class);
        String ano_resu = (String) ano_mapType.get("msg").toString();
        JSONObject ano_json_list = (JSONObject) ano_mapType.get("data");

        String userName = ano_json_list.get("user_name").toString();
        userIntroduction = ano_json_list.get("introduction").toString();
        String tmp_image = ano_json_list.get("user_image_name").toString();
        creat_time = ano_json_list.get("account_birth").toString();
        JSONArray follower = (JSONArray) ano_json_list.get("dst_follower_id");
        follower_num = follower.size();
        JSONArray follow = (JSONArray) ano_json_list.get("src_follower_id");
        follow_num = follow.size();
        followcount = findViewById(R.id.follow_count);
        followcount.setText("关注:"+follow_num);
        followedcount = findViewById(R.id.followed_count);
        followedcount.setText("被关注:"+follower_num);
        createtime = findViewById(R.id.creat_time);
        createtime.setText("用户创建于:"+creat_time);
        try {
            URL tmp_url = null;
            tmp_url = new URL(Constant.backendUrl+"/media/"+tmp_image);
            System.out.println(tmp_image);
            Bitmap bitmap = requestImg(tmp_url);
            userImage.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String showIntroduction = "暂无介绍";
        TextView tvIntroduction = findViewById(R.id.user_introduction);
        if (userIntroduction.length() >= 10) {
            showIntroduction = userIntroduction.substring(0, 10) + "...   >>>";
            tvIntroduction.setOnClickListener(this::toIntroduction);
        }
        else if (userIntroduction.length() != 0)
            showIntroduction = userIntroduction;

        tvIntroduction.setText(showIntroduction);


        TextView tvTitle = findViewById(R.id.self_item_title);
        tvTitle.setText(userName + "的个人主页");

        // Set recycler related variables
        // Put initial data into the word list.
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        obj_string = ano_obj.toJSONString();
        result = HttpUtil.post(url, obj_string);
        mapType = JSON.parseObject(result,HashMap.class);
        resu = (String) mapType.get("msg").toString();

        // result (String) -->> result (json)
        ///////////////////////////////////////////
//                String result = "no item";
        if (resu.equals(NO_ITEM) )
        {
            alertNoItem();
            return;
        }
        JSONArray item_list = (JSONArray) mapType.get("data");
        // Construct temporary data
        itemList.clear();
        System.out.println("OUTTTTTTTTTTTTTTTT");
        System.out.println(item_list.size());
        for (int i = 0; i < item_list.size(); i++) {
            System.out.println("INNNNNNNNNNNNN");
            JSONObject tmp =  (JSONObject) item_list.get(i);
            System.out.println(tmp);
            int curItemId = (int)tmp.get("item_id");

            String curUserName = tmp.get("user_name").toString();
            String curTitle = tmp.get("title").toString();
            String curContent = tmp.get("content").toString()+"\n";
            String curFollowCondition = isFollowing?Item.FOLLOW:Item.HAVE_NOT_FOLLOW;
            String curTime = tmp.get("created_time").toString();
            curContent += curTime;
            int curLikesCount = (int)tmp.get("like_count");
            int curCommentsCount = (int)tmp.get("comment_count");
            int curType = (int)tmp.get("type");
            String curFilename = tmp.get("file_name").toString();
            String fakeimage_path = "";
            if(curType == 3){
                fakeimage_path = tmp.get("fake_image").toString();
            }
            Boolean is_liked = (Boolean)tmp.get("is_liked");
            Item curItem = new Item(curItemId, curTitle, curContent, curUserName, curFollowCondition, userId,
                    curLikesCount, curCommentsCount, curType, is_liked,curFilename,fakeimage_path,tmp_image);
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

    public void refresh(Boolean condition){
        for (int i = 0; i < itemList.size(); i++)
        {
            Item curItem = itemList.get(i);
            curItem.setFollowCondition(condition ? Item.FOLLOW : Item.HAVE_NOT_FOLLOW);
            itemList.set(i, curItem);
        }
        mAdapter = new PostListAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Inflate the layout for this fragment
    }
    private Bitmap requestImg(final URL imgUrl)
    {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imgUrl.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}