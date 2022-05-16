package com.example.groupproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.FileOutputStream;



public class itemCreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_create);
        test();
        writeSDcard("lalala");
        Spinner type_spin = findViewById(R.id.item_types);
        type_spin.setSelection(0);
        type_spin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){

                }else if(position == 1){

                }else  if(position ==2){

                }else if(position ==3){

                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });
    }

    private void writeSDcard(String str) {
        try {
            // 推断是否存在SD卡
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的文件夹
                String sdDire = getExternalFilesDir(null).getPath();
                File testFile = new File(sdDire,"test.txt");
                FileOutputStream outFileStream = new FileOutputStream(testFile);
                outFileStream.write(str.getBytes());
                outFileStream.close();
                System.out.println("test.txt ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void test(){
        item_unfinished it1 = new item_unfinished();
        String item_str= JSON.toJSONString(it1);
        System.out.println(item_str);
    }
    public class item_unfinished{
        private String title = "unset";
        private String content= "unset";
        private  int type = 0;//默认为纯文本
        private String loc= "unset";
        private String filename= "unset";
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
}
