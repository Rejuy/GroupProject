package com.example.groupproject;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

/**
 * Shows how to implement a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class PostListAdapter extends
        RecyclerView.Adapter<PostListAdapter.PostViewHolder>  {
//implements View.OnClickListener
    private final List<Item> itemList;
//    private final LinkedList<String> mContentList;
    private final LayoutInflater mInflater;

    class PostViewHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
    {
        public final TextView titleItemView;
        public final TextView contentItemView;
        public final TextView userNameItemView;
        public final TextView likesCountItemView;
        public final TextView commentCountItemView;
        public final ImageView likeButton;
        public final ImageView commentButton;
        public final ImageView shareButton;
        public final ImageView userImage;
        final PostListAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in
         * the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views
         *                for the RecyclerView.
         */
        public PostViewHolder(View itemView, PostListAdapter adapter) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.post_title);
            contentItemView = itemView.findViewById(R.id.post_content);
            userNameItemView = itemView.findViewById(R.id.user_name);
            likesCountItemView = itemView.findViewById(R.id.like_count_textview);
            commentCountItemView = itemView.findViewById(R.id.comment_count_textview);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            shareButton = itemView.findViewById(R.id.share_button);
            userImage = itemView.findViewById(R.id.user_image);

            // On click

//            likeButton.setOnClickListener(PostListAdapter.this);
//            commentButton.setOnClickListener(PostListAdapter.this);
//            shareButton.setOnClickListener(PostListAdapter.this);
            this.mAdapter = adapter;
//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View view) {
//            // Get the position of the item that was clicked.
//            int mPosition = getLayoutPosition();
//
//            // Use that to access the affected item in mWordList.
//            String element = mWordList.get(mPosition);
//            // Change the word in the mWordList.
//
//            mWordList.set(mPosition, "Clicked! " + element);
//            // Notify the adapter, that the data has changed so it can
//            // update the RecyclerView to display the data.
//            mAdapter.notifyDataSetChanged();
//        }
    }

    public PostListAdapter(Context context, List<Item> _itemList) {
        mInflater = LayoutInflater.from(context);
        this.itemList = _itemList;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to
     * represent an item.
     *
     * This new ViewHolder should be constructed with a new View that can
     * represent the items of the given type. You can either create a new View
     * manually or inflate it from an XML layout file.
     *
     * The new ViewHolder will be used to display items of the adapter using
     * onBindViewHolder(ViewHolder, int, List). Since it will be reused to
     * display different items in the data set, it is a good idea to cache
     * references to sub views of the View to avoid unnecessary findViewById()
     * calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after
     *                 it is bound to an adapter position.
     * @param viewType The view type of the new View. @return A new ViewHolder
     *                 that holds a View of the given view type.
     */
    @Override
    public PostListAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(
                R.layout.postlist_item, parent, false);
        return new PostViewHolder(mItemView, this);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the ViewHolder.itemView to
     * reflect the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent
     *                 the contents of the item at the given position in the
     *                 data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(PostListAdapter.PostViewHolder holder,
                                 int position) {
        // Retrieve the data for that position.
        Object objItem = itemList.get(position);
        Item curItem = (Item) objItem;
//        String mCurrentContent = mContentList.get(position);
        // Add the data to the view holder.
        holder.titleItemView.setText(curItem.getTitle());
        holder.contentItemView.setText(curItem.getContent());
        holder.userNameItemView.setText(curItem.getUserName());
        holder.commentCountItemView.setText(String.valueOf(curItem.getCommentsCount()));
        holder.likesCountItemView.setText(String.valueOf(curItem.getLikesCount()));

        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Item curItem = itemList.get(curPosition);
                if (curItem.getLiked()) {
                    curItem.unlike();
                    itemList.set(position, curItem);
                    holder.likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_unlike));
                    holder.likesCountItemView.setText(String.valueOf(curItem.getLikesCount()));
                    // TODO: Send change to backend
                }
                else {
                    curItem.like();
                    itemList.set(position, curItem);
                    holder.likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_like));
                    holder.likesCountItemView.setText(String.valueOf(curItem.getLikesCount()));
                    // TODO: Send change to backend
                }
            }
        });

        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Item curItem = itemList.get(curPosition);
                System.out.print("image click");
                Context curContext = mInflater.getContext();
                Intent intent = new Intent(curContext, SelfItemActivity.class);
//                String searchContent = searchEditText.getText().toString();
                intent.putExtra("userId", curItem.getUserId());
                intent.putExtra("userName", curItem.getUserName());
                curContext.startActivity(intent);

            }
        });

//        holder.contentItemView.setText(mCurrentContent);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }



//    public enum  ViewName {
//        LIKE,
//        COMMENT,
//        SHARE,
//        WHOLE
//    }
//
//    //自定义一个回调接口来实现Click和LongClick事件
//    public interface OnItemClickListener  {
//        void onItemClick(View v, ViewName viewName, int position);
//    }
//
//    private OnItemClickListener mOnItemClickListener;//声明自定义的接口
//
//    //定义方法并暴露给外面的调用者
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.mOnItemClickListener = listener;
//    }
//
//    @Override
//    public void onClick(View v) {
//        int position = (int) v.getTag(); //getTag()获取数据
//        if (mOnItemClickListener != null) {
//            switch (v.getId()){
//                case R.id.like_button://
//                    mOnItemClickListener.onItemClick(v, ViewName.LIKE, position);
//                    break;
//                case R.id.comment_button://
//                    mOnItemClickListener.onItemClick(v, ViewName.COMMENT, position);
//                    break;
//                case R.id.share_button://
//                    mOnItemClickListener.onItemClick(v, ViewName.SHARE, position);
//                    break;
//                default:
//                    mOnItemClickListener.onItemClick(v, ViewName.WHOLE, position);
//                    break;
//            }
//        }
//    }
}
