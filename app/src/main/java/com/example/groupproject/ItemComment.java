package com.example.groupproject;

public class ItemComment {
    private int id;
    private int item_id;
    private int user_id;
    private String content;
    private String created_time;
    private String user_name;

    public ItemComment(){
        id = 0;
        item_id = 0;
        user_id = 0;
        user_name="lyq";
        content = "lyqtest";
        created_time = "lyqtest";
    }

    public ItemComment(int _id,int _item_id,int new_user_id,String new_content,String new_created_time){
        id=_id;
        item_id = _item_id;
        user_id=new_user_id;
        content=new_content;
        created_time=new_created_time;
    }


    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreate_time(String create_time) {
        this.created_time = create_time;
    }
    public void setUser_name(String name){this.user_name=name;}

    public int getUser_id() {
        return user_id;
    }
    public int getId() {
        return id;
    }
    public int getItem_id() {
        return item_id;
    }

    public String getContent() {
        return content;
    }

    public String getCreate_time() {
        return created_time;
    }
}
