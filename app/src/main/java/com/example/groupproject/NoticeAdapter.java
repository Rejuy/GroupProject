package com.example.groupproject;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends
        RecyclerView.Adapter<NoticeAdapter.NoticeHolder> {
    private final ArrayList<String> itemList;
    private final LayoutInflater mInflater;

    class NoticeHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
    {
        public final TextView contentItemView;
        final NoticeAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in
         * the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views
         *                for the RecyclerView.
         */
        public NoticeHolder(View itemView, NoticeAdapter adapter) {
            super(itemView);

            contentItemView = itemView.findViewById(R.id.notice_content);



            // On click

//            likeButton.setOnClickListener(PostListAdapter.this);
//            commentButton.setOnClickListener(PostListAdapter.this);
//            shareButton.setOnClickListener(PostListAdapter.this);
            this.mAdapter = adapter;
//            itemView.setOnClickListener(this);
        }


    }

    public NoticeAdapter(Context context, ArrayList<String> _itemList) {
        mInflater = LayoutInflater.from(context);
        this.itemList = _itemList;
    }
    @NonNull
    @Override
    public NoticeAdapter.NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(
                R.layout.notice_item, parent, false);
        return new NoticeAdapter.NoticeHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.NoticeHolder holder, int position) {
        System.out.println("yyyyyyy!!!!!!");
        String objItem = itemList.get(holder.getAdapterPosition());
        System.out.println(position);
        holder.contentItemView.setText(objItem);
        System.out.println(holder.contentItemView);

    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
