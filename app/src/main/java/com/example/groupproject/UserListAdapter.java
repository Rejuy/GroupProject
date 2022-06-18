package com.example.groupproject;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Shows how to implement a simple Adapter for a RecyclerView.
 * Demonstrates how to add a click handler for each item in the ViewHolder.
 */
public class UserListAdapter extends
        RecyclerView.Adapter<UserListAdapter.PostViewHolder>  {
    //implements View.OnClickListener
    private final List<User> userList;
    //    private final LinkedList<String> mContentList;
    private final LayoutInflater mInflater;

    private static final String likeUrl = Constant.backendUrl + Constant.itemLikeUrl;
    private static final String dislikeUrl = Constant.backendUrl + Constant.itemDislikeUrl;
    private static final String SUCCESS = "ok";
    private static final String FAILURE = "like not found";


    class PostViewHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
    {
        public final TextView userNameItemView;
        public final ImageView userImage;
        final UserListAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in
         * the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views
         *                for the RecyclerView.
         */
        public PostViewHolder(View itemView, UserListAdapter adapter) {
            super(itemView);
            userNameItemView = itemView.findViewById(R.id.user_name);
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

    public UserListAdapter(Context context, List<User> _userList) {
        mInflater = LayoutInflater.from(context);
        this.userList = _userList;
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
    public UserListAdapter.PostViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(
                R.layout.postlist_user, parent, false);
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
    public void onBindViewHolder(UserListAdapter.PostViewHolder holder,
                                 int position) {
        // Retrieve the data for that position.
        Object objItem = userList.get(position);
        User curUser = (User) objItem;
//        String mCurrentContent = mContentList.get(position);
        // Add the data to the view holder.
        holder.userNameItemView.setText(curUser.getUserName());
        try {
            URL tmp_url = null;
            tmp_url = new URL(Constant.backendUrl+"/media/"+curUser.getImageUrl());
            Bitmap bitmap = requestImg(tmp_url);
            holder.userImage.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



        holder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                User curUser = userList.get(curPosition);
                System.out.print("image click");
                Context curContext = mInflater.getContext();
                Intent intent = new Intent(curContext, SelfItemActivity.class);
//                String searchContent = searchEditText.getText().toString();
                intent.putExtra("userId", curUser.getUserId());
                intent.putExtra("userName", curUser.getUserName());
                curContext.startActivity(intent);

            }
        });


    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return userList.size();
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
