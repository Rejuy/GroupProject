package com.example.groupproject;

public class Item {
    private int itemId;
    private String title;
    private String content;
    private String userName;
    private String followCondition;
    private int userId;
    private int likesCount;
    private int commentsCount;
    private Boolean liked;
    private String fileName;

    public static final String FOLLOW = "已关注";
    public static final String HAVE_NOT_FOLLOW = "未关注";
    public static final String MYSELF = "我自己";
    // TODO: user image

    private int type;
    public static final int TEXT = 0;
    public static final int IMAGE = 1;
    public static final int AUDIO = 2;
    public static final int VIDEO = 3;

    public Item(int _itemId,
                String _title,
                String _content,
                String _userName,
                String _followCondition,
                int _userId,
                int _likesCount,
                int _commentsCount,
                int _type,
                Boolean _liked,
                String _filename)
    {
        itemId = _itemId;
        title = _title;
        content = _content;
        userName = _userName;
        followCondition = _followCondition;
        userId = _userId;
        likesCount = _likesCount;
        commentsCount = _commentsCount;
        type = _type;
        liked = _liked;
        fileName=_filename;
    }

    public int getItemId() { return itemId; }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public String getFollowCondition() { return followCondition; }

    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public int getType() {
        return type;
    }

    public int getUserId() { return userId; }

    public Boolean getLiked() { return liked; }

    public String getFileName(){return fileName;}

    public void like() {
        likesCount += 1;
        liked = true;
    }

    public void unlike() {
        likesCount -= 1;
        liked = false;
    }
}
