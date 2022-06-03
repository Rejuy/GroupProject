package com.example.groupproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

public class CommentAdapter extends
        RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    private final ArrayList<ItemComment> itemList;
    private final LayoutInflater mInflater;

    class CommentHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
    {
        public final ImageView UserImageView;
        public final TextView UsernameItemView;
        public final TextView contentItemView;
        public final TextView createTimeItemView;
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
        }else{
            System.out.println(cur_item.getUser_id());
        }
        if(!cur_item.getContent().equals("lyqtest")){
            holder.contentItemView.setText(cur_item.getContent());
        }else{
            holder.contentItemView.setText("默认");
        }
        holder.createTimeItemView.setText(cur_item.getCreate_time());
        holder.UserImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Object objItem = itemList.get(position);
                ItemComment tmp_item = (ItemComment) objItem;
                System.out.print("image click");
                Context curContext = mInflater.getContext();
                Intent intent = new Intent(curContext, SelfItemActivity.class);
//                String searchContent = searchEditText.getText().toString();
                intent.putExtra("userId", 111);
                intent.putExtra("userName","lyq");
                curContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
