package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailedItemActivity extends AppCompatActivity {
    Activity that =this;
    private final ArrayList<ItemComment> itemList = new ArrayList<>();
    private Item curItem;
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
    CommentAdapter mAdapter;


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

        String cur_UserName = "测试用户";
        String cur_Title = "测试标题";
        String cur_Content = "测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
                "内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容" + "\n";
        Date _d = new Date();
        System.out.println(_d);
        SimpleDateFormat _sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String cur_Time = _sdf.format(_d);
        cur_Content += cur_Time;
        int cur_LikesCount = 200;
        int cur_CommentsCount = 30;
        titleItemView.setText(cur_Title);
        contentItemView.setText(cur_Content);
        userNameItemView.setText(cur_UserName);
//        likesCountItemView.setText(cur_LikesCount);
//        commentCountItemView.setText(cur_CommentsCount);

        for (int i = 0; i < 15; i++) {
            String curContent = "测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
                    "内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容" + i + "\n";
            Date d = new Date();
            System.out.println(d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = sdf.format(d);
            ItemComment curItem = new ItemComment(i, curContent, curTime);
            itemList.add(curItem);

//            mContentList.addLast("动态说了点什么呢？（我是内容） " + i + "\n" + curTime);
        }

        mRecyclerView = this.findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new CommentAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (curItem.getLiked()) {
                    curItem.unlike();
                    likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_unlike));
                    likesCountItemView.setText(String.valueOf(curItem.getLikesCount()));
                    // TODO: Send change to backend
                }
                else {
                    curItem.like();
                    likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_like));
                    likesCountItemView.setText(String.valueOf(curItem.getLikesCount()));
                    // TODO: Send change to backend
                }
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
                Intent intent = new Intent(that, SelfItemActivity.class);
//                String searchContent = searchEditText.getText().toString();
                intent.putExtra("userId", curItem.getUserId());
                intent.putExtra("userName", curItem.getUserName());
                startActivity(intent);

            }
        });
    }
}