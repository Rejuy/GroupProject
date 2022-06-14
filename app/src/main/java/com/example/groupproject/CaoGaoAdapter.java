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
        public final Button deleteButton;
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
            deleteButton = itemView.findViewById(R.id.delete_button);


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
        System.out.println(JSON.toJSONString(cur_item));
        if(!cur_item.getTitle().equals("unset")){
            holder.titleItemView.setText("标题:"+cur_item.getTitle());
        }else{
            holder.titleItemView.setText("标题:"+"未编辑");
        }
        if(!cur_item.getContent().equals("unset")){
            holder.contentItemView.setText("内容:"+cur_item.getContent());
        }else{
            holder.contentItemView.setText("内容:"+"未编辑");
        }if(!cur_item.getCreate_time().equals("unset")){
            holder.createTimeItemView.setText("上次编辑于:"+cur_item.getCreate_time());
        }else{
            holder.createTimeItemView.setText("上次编辑于:");
        }
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemList.remove(position);
                item_unfinished_list save_list = new item_unfinished_list();
                save_list.setlist(itemList);
                String str = JSON.toJSONString(save_list);
                writeSDcard(str);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
        holder.titleItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int curPosition = holder.getAdapterPosition();
//                item_unfinished tmp = itemList.get(curPosition);
                Context curContext = mInflater.getContext();
                Intent intent = new Intent(curContext, itemCreateActivity.class);
                intent.putExtra("content",cur_item.getContent());
                intent.putExtra("loc",cur_item.getLoc());
                intent.putExtra("title",cur_item.getTitle());
                intent.putExtra("type",cur_item.getType());
                intent.putExtra("filename",cur_item.getFilename());
                intent.putExtra("create_time",cur_item.getCreate_time());
                intent.putExtra("code",0);
                intent.putExtra("position",position);
                curContext.startActivity(intent);
            }
        });
    }

    private void writeSDcard(String str) {
        try {
            // 推断是否存在SD卡
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的文件夹
                Context curContext = mInflater.getContext();
                String sdDire = curContext.getExternalFilesDir(null).getPath();
                File testFile = new File(sdDire,Constant.userId+".txt");
                FileOutputStream outFileStream = new FileOutputStream(testFile);
                outFileStream.write(str.getBytes());
                outFileStream.close();
                System.out.println("test.txt ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public class item_unfinished_list{
        public ArrayList<item_unfinished> save_list = new ArrayList<>();
        public void setlist(ArrayList<item_unfinished> it_list){
            save_list = it_list;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
