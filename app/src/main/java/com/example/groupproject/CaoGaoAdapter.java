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

import java.util.ArrayList;
import java.util.List;

public class CaoGaoAdapter extends
        RecyclerView.Adapter<CaoGaoAdapter.CaoGaoHolder> {
    private final ArrayList<item_unfinished> itemList;
    private final LayoutInflater mInflater;

    class CaoGaoHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
    {
        public final TextView titleItemView;
        public final TextView contentItemView;
        public final TextView createTimeItemView;
        final CaoGaoAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in
         * the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views
         *                for the RecyclerView.
         */
        public CaoGaoHolder(View itemView, CaoGaoAdapter adapter) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.cg_title);
            contentItemView = itemView.findViewById(R.id.cg_content);
            createTimeItemView = itemView.findViewById(R.id.creat_time);


            // On click

//            likeButton.setOnClickListener(PostListAdapter.this);
//            commentButton.setOnClickListener(PostListAdapter.this);
//            shareButton.setOnClickListener(PostListAdapter.this);
            this.mAdapter = adapter;
//            itemView.setOnClickListener(this);
        }


    }

    public CaoGaoAdapter(Context context, ArrayList<item_unfinished> _itemList) {
        mInflater = LayoutInflater.from(context);
        this.itemList = _itemList;
    }
    @NonNull
    @Override
    public CaoGaoAdapter.CaoGaoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(
                R.layout.caogao_item, parent, false);
        return new CaoGaoAdapter.CaoGaoHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CaoGaoAdapter.CaoGaoHolder holder, int position) {
        System.out.println("yyyyyyy!!!!!!");
        Object objItem = itemList.get(position);
        item_unfinished cur_item = (item_unfinished) objItem;
        if(!cur_item.getTitle().equals("unset")){
            holder.titleItemView.setText(cur_item.getTitle());
        }else{
            holder.titleItemView.setText("默认");
        }
        if(!cur_item.getContent().equals("unset")){
            holder.contentItemView.setText(cur_item.getContent());
        }else{
            holder.contentItemView.setText("默认");
        }
        holder.createTimeItemView.setText(cur_item.getCreate_time());
        holder.titleItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                item_unfinished tmp = itemList.get(curPosition);
                Context curContext = mInflater.getContext();
                Intent intent = new Intent(curContext, itemCreateActivity.class);
                intent.putExtra("title",tmp.getTitle());
                intent.putExtra("content",tmp.getContent());
                intent.putExtra("title",tmp.getTitle());
                intent.putExtra("type",tmp.getType());
                intent.putExtra("filename",tmp.getFilename());
                intent.putExtra("create_time",tmp.getCreate_time());
                intent.putExtra("position",position);
                curContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
