package com.example.groupproject;

public class Item {
    private int itemId;
    private String title;
    private String content;
    private String userName;
    private int userId;
    private int likesCount;
    private int commentsCount;
    private Boolean liked;
    // TODO: user image

    private int type;
    public static final int TEXT = 1;
    public static final int IMAGE = 2;
    public static final int AUDIO = 3;
    public static final int VIDEO = 4;

    public Item(int _itemId,
                String _title,
                String _content,
                String _userName,
                int _userId,
                int _likesCount,
                int _commentsCount,
                int _type,
                Boolean _liked)
    {
        itemId = _itemId;
        title = _title;
        content = _content;
        userName = _userName;
        userId = _userId;
        likesCount = _likesCount;
        commentsCount = _commentsCount;
        type = _type;
        liked = _liked;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

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

    public void like() {
        likesCount += 1;
        liked = true;
    }

    public void unlike() {
        likesCount -= 1;
        liked = false;
    }
}
