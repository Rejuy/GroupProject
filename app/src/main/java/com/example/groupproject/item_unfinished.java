package com.example.groupproject;

public class item_unfinished {
    private int id = 0;
    private String title = "unset";
    private String content= "unset";
    private  int type = 0;//默认为纯文本
    private String loc= "unset";
    private String filename= "unset";
    private String create_time = "unset";
    public void setId(int i){id = i;}
    public void setTime(String str){create_time=str;}
    public void setTitle(String str){
        title=str;
    }
    public void setContent(String str){
        content=str;
    }
    public void setType(int puttype){
        type=puttype;
    }
    public void setLoc(String str){
        loc=str;
    }
    public void setFilename(String str){
        filename=str;
    }

    public int getId() {
        return id;
    }

    public String getCreate_time(){return create_time;}
    public String getTitle(){
        return  title;
    }
    public String getContent(){
        return content;
    }
    public String getLoc(){
        return loc;
    }
    public int getType(){
        return type;
    }
    public String getFilename(){
        return filename;
    }
}
