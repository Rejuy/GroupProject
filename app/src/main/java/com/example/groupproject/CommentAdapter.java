package com.example.groupproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.hutool.http.HttpUtil;

public class CommentAdapter extends
        RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private final ArrayList<ItemComment> itemList;
    private final LayoutInflater mInflater;
    private static String user_get_url = Constant.backendUrl+Constant.getUserUrl;
    private static String comment_delete_url = Constant.backendUrl+Constant.commentDeleteUrl;
    class CommentHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
    {
        public final ImageView UserImageView;
        public final TextView UsernameItemView;
        public final TextView contentItemView;
        public final TextView createTimeItemView;
        public final Button delete_button;
        final CommentAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in
         * the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views
         *                for the RecyclerView.
         */
        public CommentHolder(View itemView, CommentAdapter adapter) {
            super(itemView);
            UserImageView = itemView.findViewById(R.id.user_image);
            UsernameItemView = itemView.findViewById(R.id.user_name);
            contentItemView = itemView.findViewById(R.id.comment_content);
            createTimeItemView = itemView.findViewById(R.id.comment_time);
            delete_button=itemView.findViewById(R.id.delete_button);


            // On click

//            likeButton.setOnClickListener(PostListAdapter.this);
//            commentButton.setOnClickListener(PostListAdapter.this);
//            shareButton.setOnClickListener(PostListAdapter.this);
            this.mAdapter = adapter;
//            itemView.setOnClickListener(this);
        }


    }

    public CommentAdapter(Context context, ArrayList<ItemComment> _itemList) {
        mInflater = LayoutInflater.from(context);
        this.itemList = _itemList;
    }
    @NonNull
    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(
                R.layout.comment_item, parent, false);
        return new CommentAdapter.CommentHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentHolder holder, int position) {
        System.out.println("yeeeyyyyyy!!!!!!");
        Object objItem = itemList.get(position);
        ItemComment cur_item = (ItemComment) objItem;
        System.out.println(JSON.toJSONString(cur_item));
        if(cur_item.getUser_id()!=0){
            System.out.println(cur_item.getUser_id());
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("user_id", cur_item.getUser_id());
            JSONObject obj = new JSONObject(paramMap);
            ///////////////////////////////////////////
            ////////// Backend Connection /////////////
            String obj_string = obj.toJSONString();
            String result = HttpUtil.post(user_get_url, obj_string);
            HashMap mapType = JSON.parseObject(result,HashMap.class);
            JSONObject json_list = (JSONObject) mapType.get("data");
            holder.UsernameItemView.setText(json_list.get("user_name").toString());
        }else{
            System.out.println(cur_item.getUser_id());
        }
        if(!cur_item.getContent().equals("lyqtest")){
            holder.contentItemView.setText(cur_item.getContent());
        }else{
            holder.contentItemView.setText("默认");
        }
        holder.createTimeItemView.setText(cur_item.getCreate_time());
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.remove(position);
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id", IndexActivity.user_id);
                paramMap.put("item_id",cur_item.getItem_id());
                paramMap.put("comment_id",cur_item.getId());
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                String result = HttpUtil.post(comment_delete_url, obj_string);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        holder.UserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object objItem = itemList.get(position);
                ItemComment tmp_item = (ItemComment) objItem;
                System.out.print("image click");
                Context curContext = mInflater.getContext();
                Intent intent = new Intent(curContext, SelfItemActivity.class);
//                String searchContent = searchEditText.getText().toString();
                intent.putExtra("userId", tmp_item.getUser_id());
                curContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
