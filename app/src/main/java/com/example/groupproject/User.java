package com.example.groupproject;

public class User {
    private int userId;
    private String userName;
    private String imageUrl;

    public User(int _userId, String _userName, String _imageUrl)
    {
        userId = _userId;
        userName = _userName;
        imageUrl = _imageUrl;
    }

    public int getUserId()
    {
        return userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }
}
