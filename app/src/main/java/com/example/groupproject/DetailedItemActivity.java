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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
    public TextView likename;
    public ImageView likeButton;
    public ImageView commentButton;
    public ImageView shareButton;
    public ImageView userImage;
    public ImageView itemImage;
    private RecyclerView mRecyclerView;
    private EditText new_comment;
    private Button send_comment;
    public  Button startButton;
    public  Button stopButton;
    boolean like = false;
    String like_name_list="";
    String video_str;
    CommentAdapter mAdapter;
    int cur_like_count = 0;
    String cur_filename = "";


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
        itemImage = this.findViewById(R.id.item_image);
        startButton = this.findViewById(R.id.play_button);
        stopButton = this.findViewById(R.id.stop_button);
        likename = this.findViewById(R.id.like_name);

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
        String curUserImage = "";
        if(json_list.getString("user_image_name")==null){

        }else{
            curUserImage= json_list.get("user_image_name").toString();
            try {
                URL tmp_url = null;
                tmp_url = new URL(Constant.backendUrl+"/media/"+curUserImage);
                System.out.println(curUserImage);
                Bitmap bitmap = requestImg(tmp_url);
                userImage.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        int cur_type = (int)json_list.get("type");
        cur_filename = json_list.get("file_name").toString();
        String fake_name = json_list.get("fake_image").toString();
        URL url = null;
        if(cur_type == Item.IMAGE){
            try {
                url = new URL(Constant.backendUrl+"/media/"+cur_filename);
                System.out.println(cur_filename);
                Bitmap bitmap = requestImg(url);
                itemImage.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else if(cur_type==Item.VIDEO){
            try {
                url = new URL(Constant.backendUrl+"/media/"+fake_name);
                Bitmap bitmap = requestImg(url);
                itemImage.setImageBitmap(bitmap);
//                System.out.println(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            my_video_get tmp =new my_video_get();
            tmp.run();
        }else if(cur_type==Item.AUDIO){
            MediaPlayer mmediaplayer = new MediaPlayer();
            System.out.println(Constant.backendUrl+"/media/"+cur_filename);
            try {
                mmediaplayer.setDataSource(Constant.backendUrl+"/media/"+cur_filename);
                System.out.println(Constant.backendUrl+"/media/"+cur_filename);
                mmediaplayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.VISIBLE);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!mmediaplayer.isPlaying()) {
                        mmediaplayer.start();
                    }
                }
            });
            stopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mmediaplayer.isPlaying()) {
                        mmediaplayer.stop();
                        mmediaplayer.release();
                    }
                }
            });
        }
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
        likename.setText(like_name_list);
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
    class my_video_get implements Runnable{
        @Override
        public void run(){
            video_get();
        }
    }
    private void video_get(){
        String sdDire = that.getExternalFilesDir(null).getPath();
        File testFile = new File(sdDire,Constant.userId+cur_filename+".mp4");
        try {
            if(testFile.exists()){
                testFile.delete();
            }
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpUtil.downloadFile(Constant.backendUrl+"/media/"+cur_filename,testFile);
        Toast.makeText(that, "下载成功!!!!!!!!!!",
                Toast.LENGTH_SHORT).show();
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    playVideo(Constant.backendUrl+"/media/"+curItem.getFileName());
                video_str = that.getExternalFilesDir(null).getPath()+"/"+Constant.userId+cur_filename+".mp4";
                my_video_thread tmp =new my_video_thread();
                tmp.run();
                System.out.println(video_str);
            }
        });
    }
    class my_video_thread implements Runnable{
        @Override
        public void run(){
            playVideo(video_str);
        }
    }
    private void playVideo(String uristr){
        Uri uri = Uri.parse(uristr);
        Intent ointent = new Intent();
        ointent.setAction(Intent.ACTION_VIEW);
        ointent.setDataAndType(uri,"video/mp4");
        startActivity(ointent);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
        String curUserImage = "";
        if(json_list.getString("user_image_name")==null){

        }else{
            curUserImage= json_list.get("user_image_name").toString();
            try {
                URL tmp_url = null;
                tmp_url = new URL(Constant.backendUrl+"/media/"+curUserImage);
                System.out.println(curUserImage);
                Bitmap bitmap = requestImg(tmp_url);
                userImage.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

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