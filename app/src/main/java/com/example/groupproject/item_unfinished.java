package com.example.groupproject;

public class item_unfinished {
    private String title = "unset";
    private String content= "unset";
    private  int type = 0;//默认为纯文本
    private String loc= "unset";
    private String filename= "unset";
    private String create_time = "unset";
    public void setTime(String str){this.create_time=str;}
    public void setTitle(String str){
        this.title=str;
    }
    public void setContent(String str){
        this.content=str;
    }
    public void setType(int type){
        this.type=type;
    }
    public void setLoc(String str){
        this.loc=str;
    }
    public void setFilename(String str){
        this.filename=str;
    }
    public String getCreate_time(){return this.create_time;}
    public String getTitle(){
        return  this.title;
    }
    public String getContent(){
        return this.content;
    }
    public String getLoc(){
        return this.loc;
    }
    public int getType(){
        return this.type;
    }
    public String getFilename(){
        return this.filename;
    }
}
