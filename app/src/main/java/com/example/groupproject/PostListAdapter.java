package com.example.groupproject;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
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

    private static final String likeUrl = Constant.backendUrl + Constant.itemLikeUrl;
    private static final String dislikeUrl = Constant.backendUrl + Constant.itemDislikeUrl;
    private static final String SUCCESS = "ok";
    private static final String FAILURE = "like not found";


    class PostViewHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener {
    {
        public final TextView titleItemView;
        public final TextView contentItemView;
        public final TextView userNameItemView;
        public final TextView likesCountItemView;
        public final TextView commentCountItemView;
        public final TextView followCondition;
        public final ImageView likeButton;
        public final ImageView commentButton;
        public final ImageView shareButton;
        public final ImageView userImage;
        public final ImageView itemImage;
        public final VideoView itemVideo;
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
            followCondition = itemView.findViewById(R.id.follow_condition);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            shareButton = itemView.findViewById(R.id.share_button);
            userImage = itemView.findViewById(R.id.user_image);
            itemImage = itemView.findViewById(R.id.item_image);
            itemVideo = itemView.findViewById(R.id.item_video);

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
        holder.followCondition.setText(curItem.getFollowCondition());
        URL url = null;
        if(curItem.getType() == Item.IMAGE){
            try {
                url = new URL(Constant.backendUrl+"/media/"+curItem.getFileName());
                System.out.println(curItem.getFileName());
                Bitmap bitmap = requestImg(url);
                holder.itemImage.setImageBitmap(bitmap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else if(curItem.getType()==Item.VIDEO){

            try {
                url = new URL(Constant.backendUrl+"/media/"+curItem.getFileName());
//                System.out.println(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            holder.itemVideo.setMediaController(new MediaController(holder.itemView.getContext()));
//            System.out.println(url.toString());
            Uri ano_uri = Uri.parse(url.toString());
            System.out.println(ano_uri);
            holder.itemVideo.setVideoURI(ano_uri);
            holder.itemVideo.start();
//            holder.itemVideo.requestFocus();
        }
        holder.contentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Item curItem = itemList.get(curPosition);
                int item_id = curItem.getItemId();
                To_detail(item_id);
            }
        });
        holder.titleItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Item curItem = itemList.get(curPosition);
                int item_id = curItem.getItemId();
                To_detail(item_id);
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Item curItem = itemList.get(curPosition);
                int item_id = curItem.getItemId();
                To_detail(item_id);
            }
        });
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Item curItem = itemList.get(curPosition);
                if (curItem.getLiked()) {
                    // TODO: Send change to backend
                    HashMap<String, Object> paramMap = new HashMap<>();
                    paramMap.put("item_id", curItem.getItemId());
                    paramMap.put("user_id", curItem.getUserId());
                    ///////////////////////////////////////////
                    ////////// Backend Connection /////////////
                    // String result = HttpUtil.post(likeUrl, paramMap);
                    // result (String) -->> result (json)
                    ///////////////////////////////////////////
                    String result = SUCCESS;
                    if (result.equals(FAILURE))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
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
                    curItem.unlike();
                    itemList.set(position, curItem);
                    holder.likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_unlike));
                    holder.likesCountItemView.setText(String.valueOf(curItem.getLikesCount()));
                }
                else {
                    // TODO: Send change to backend
                    HashMap<String, Object> paramMap = new HashMap<>();
                    paramMap.put("item_id", curItem.getItemId());
                    paramMap.put("user_id", curItem.getUserId());
                    ///////////////////////////////////////////
                    ////////// Backend Connection /////////////
                    // String result = HttpUtil.post(dislikeUrl, paramMap);
                    // result (String) -->> result (json)
                    ///////////////////////////////////////////
                    curItem.like();
                    itemList.set(position, curItem);
                    holder.likeButton.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_like));
                    holder.likesCountItemView.setText(String.valueOf(curItem.getLikesCount()));
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

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curPosition = holder.getAdapterPosition();
                Item curItem = itemList.get(curPosition);
                String shareText = "【标题】" + curItem.getTitle() + "\n" + "【内容】" + curItem.getContent();
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);//分享的文本内容
                sendIntent.setType("text/plain");
                holder.itemView.getContext().startActivity(Intent.createChooser(sendIntent, "分享到"));
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

    public void To_detail(int item_id){
        Context curContext = mInflater.getContext();
        Intent intent = new Intent(curContext, DetailedItemActivity.class);
//                String searchContent = searchEditText.getText().toString();
        intent.putExtra("item_id", item_id);
        curContext.startActivity(intent);
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
