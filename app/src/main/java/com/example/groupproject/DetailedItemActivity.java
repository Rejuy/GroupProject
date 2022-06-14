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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.hutool.http.HttpUtil;

public class DetailedItemActivity extends AppCompatActivity {
    Activity that =this;
    private final ArrayList<ItemComment> itemList = new ArrayList<>();
    private int itemId;
    private String item_get_url = Constant.backendUrl+Constant.getItemUrl;
    private String comment_get_url = Constant.backendUrl+Constant.getItemCommentUrl;
    private String comment_send_url = Constant.backendUrl+Constant.commentSendUrl;
    private String comment_delete_url = Constant.backendUrl+Constant.commentDeleteUrl;
    private static final String likeUrl = Constant.backendUrl + Constant.itemLikeUrl;
    private static final String dislikeUrl = Constant.backendUrl + Constant.itemDislikeUrl;
    private static final String FAILURE = "like not found";
    public TextView titleItemView;
    public TextView contentItemView;
    public TextView userNameItemView;
    public TextView likesCountItemView;
    public TextView commentCountItemView;
    public ImageView likeButton;
    public ImageView commentButton;
    public ImageView shareButton;
    public ImageView userImage;
    private RecyclerView mRecyclerView;
    private EditText new_comment;
    private Button send_comment;
    boolean like = false;
    String like_name_list="";
    CommentAdapter mAdapter;
    int cur_like_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_item);
        titleItemView = this.findViewById(R.id.post_title);
        contentItemView = this.findViewById(R.id.post_content);
        userNameItemView = this.findViewById(R.id.user_name);
        likesCountItemView = this.findViewById(R.id.like_count_textview);
        commentCountItemView = this.findViewById(R.id.comment_count_textview);
        likeButton = this.findViewById(R.id.like_button);
        commentButton = this.findViewById(R.id.comment_button);
        shareButton = this.findViewById(R.id.share_button);
        userImage = this.findViewById(R.id.user_image);
        new_comment = this.findViewById(R.id.send_content);
        send_comment = this.findViewById(R.id.comment_send);

        Intent intent = this.getIntent();
        itemId = intent.getIntExtra("item_id",-1);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id",itemId);
        JSONObject obj = new JSONObject(paramMap);
        String obj_string = obj.toJSONString();
        String result= HttpUtil.post(comment_get_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();
        JSONObject json_list = (JSONObject) mapType.get("data");
        int cur_id = (int)json_list.get("user_id");
        String cur_UserName = json_list.get("user_name").toString();
        String cur_Title = json_list.get("title").toString();
        String cur_Content = json_list.get("content").toString()+'\n';
        String cur_time = json_list.get("created_time").toString();
        cur_Content = cur_Content+cur_time;
        String tmp_content = cur_Content;
        cur_like_count = (int)json_list.get("like_count");
        int cur_CommentsCount = (int)json_list.get("comment_count");
        JSONArray comment_list = (JSONArray) json_list.get("comments");
        JSONArray likeuser_list = (JSONArray) json_list.get("like_user");

        titleItemView.setText(cur_Title);
        contentItemView.setText(cur_Content);
        userNameItemView.setText(cur_UserName);
        likesCountItemView.setText(String.valueOf(cur_like_count));
        commentCountItemView.setText(String.valueOf(cur_CommentsCount));
        for(int i =0;i<likeuser_list.size();i++){
            JSONObject tmp = (JSONObject) likeuser_list.get(i);
            String tmp_name = tmp.get("user_name").toString();
            like_name_list+=tmp_name;
            if(i !=0){
                like_name_list+=",";
            }
            int tmp_user_id = (int)tmp.get("user_id");
            if(tmp_user_id==IndexActivity.user_id){
                like = true;
                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
            }
        }
        for (int i = 0; i < comment_list.size(); i++) {
            JSONObject tmp = (JSONObject) comment_list.get(i);
            String curContent = tmp.get("content").toString();
            int id = (int)tmp.get("id");
            int item_id = (int)tmp.get("item_id");
            int user_id = (int)tmp.get("user_id");
            String curTime = tmp.get("created_time").toString();
            ItemComment curItem = new ItemComment(id,item_id,user_id, curContent, curTime);
            itemList.add(curItem);
        }

        mRecyclerView = this.findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new CommentAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareText = "【标题】" + cur_Title + "\n" + "【内容】" + tmp_content;
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);//分享的文本内容
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "分享到"));
            }
        });
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("item_id", itemId);
                paramMap.put("user_id", IndexActivity.user_id);
                if (!like) {

                    JSONObject obj = new JSONObject(paramMap);
                    ///////////////////////////////////////////
                    ////////// Backend Connection /////////////
                    String obj_string = obj.toJSONString();
                    String result = HttpUtil.post(likeUrl, obj_string);
                    HashMap mapType = JSON.parseObject(result,HashMap.class);
                    String resu = (String) mapType.get("msg").toString();
                    if (resu.equals(FAILURE))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(that);
                        builder.setIcon(null);
                        builder.setTitle("提示");
                        builder.setMessage("未查询到点赞信息！");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(getActivity(), "请重新查询...", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                        return;
                    }
                    like = true;
                    cur_like_count+=1;
                    likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_like));
                    likesCountItemView.setText(String.valueOf(cur_like_count));
                }
                else {
                    JSONObject obj = new JSONObject(paramMap);
                    ///////////////////////////////////////////
                    ////////// Backend Connection /////////////
                    String obj_string = obj.toJSONString();
                    String result = HttpUtil.post(dislikeUrl, obj_string);
                    HashMap mapType = JSON.parseObject(result,HashMap.class);
                    String resu = (String) mapType.get("msg").toString();
                    like = false;
                    cur_like_count-=1;
                    likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_unlike));
                    likesCountItemView.setText(String.valueOf(cur_like_count));
                }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
                Intent intent = new Intent(that, SelfItemActivity.class);
//                String searchContent = searchEditText.getText().toString();
                intent.putExtra("userId", cur_id);
                startActivity(intent);

            }
        });
        send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("comment send!!!!!!!!!!!!!!!!!!!!!!!!!");
                String to_send = new_comment.getText().toString();
                if(to_send.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(that);
                    builder.setIcon(null);
                    builder.setTitle("提示");
                    builder.setMessage("不能发送空评论！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(getActivity(), "请重新查询...", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
                }else{
                    HashMap<String, Object> paramMap = new HashMap<>();
                    paramMap.put("item_id", itemId);
                    paramMap.put("user_id", IndexActivity.user_id);
                    paramMap.put("content",to_send);
                    JSONObject obj = new JSONObject(paramMap);
                    ///////////////////////////////////////////
                    ////////// Backend Connection /////////////
                    String obj_string = obj.toJSONString();
                    String result = HttpUtil.post(comment_send_url, obj_string);
                    AlertDialog.Builder builder = new AlertDialog.Builder(that);
                    builder.setIcon(null);
                    builder.setTitle("提示");
                    builder.setMessage("评论成功！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            refresh();
//                                Toast.makeText(getActivity(), "请重新查询...", Toast.LENGTH_SHORT).show();
                        }
                    }).show();


                }

            }
        });
    }
    public void refresh(){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id",itemId);
        JSONObject obj = new JSONObject(paramMap);
        String obj_string = obj.toJSONString();
        String result= HttpUtil.post(comment_get_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();
        JSONObject json_list = (JSONObject) mapType.get("data");
        String cur_UserName = json_list.get("user_name").toString();
        String cur_Title = json_list.get("title").toString();
        String cur_Content = json_list.get("content").toString()+'\n';
        String cur_time = json_list.get("created_time").toString();
        cur_Content = cur_Content+cur_time;
        cur_like_count = (int)json_list.get("like_count");
        int cur_CommentsCount = (int)json_list.get("comment_count");
        JSONArray comment_list = (JSONArray) json_list.get("comments");
        JSONArray likeuser_list = (JSONArray) json_list.get("like_user");

        titleItemView.setText(cur_Title);
        contentItemView.setText(cur_Content);
        userNameItemView.setText(cur_UserName);
        likesCountItemView.setText(String.valueOf(cur_like_count));
        commentCountItemView.setText(String.valueOf(cur_CommentsCount));
        new_comment.setText("");
        like_name_list="";
        for(int i =0;i<likeuser_list.size();i++){
            JSONObject tmp = (JSONObject) likeuser_list.get(i);
            String tmp_name = tmp.get("user_name").toString();
            like_name_list+=tmp_name;
            if(i !=0){
                like_name_list+=",";
            }
            int tmp_user_id = (int)tmp.get("user_id");
            if(tmp_user_id==IndexActivity.user_id){
                like = true;
            }
        }
        itemList.clear();
        for (int i = 0; i < comment_list.size(); i++) {
            JSONObject tmp = (JSONObject) comment_list.get(i);
            String curContent = tmp.get("content").toString();
            int id = (int)tmp.get("id");
            int item_id = (int)tmp.get("item_id");
            int user_id = (int)tmp.get("user_id");
            String curTime = tmp.get("created_time").toString();
            ItemComment curItem = new ItemComment(id,item_id,user_id, curContent, curTime);
            itemList.add(curItem);
        }

        mRecyclerView = this.findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new CommentAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}