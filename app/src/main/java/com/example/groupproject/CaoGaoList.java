package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CaoGaoList extends AppCompatActivity {
    private final ArrayList<item_unfinished> itemList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private CaoGaoAdapter mAdapter;
    String total_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cao_gao_list);
        readSDcard();
        if(total_msg!=""){
            HashMap mapType = JSON.parseObject(total_msg,HashMap.class);
//            String tmp_str=mapType.get("save_list").toString();
            System.out.println(mapType.get("save_list").toString());
            JSONArray tmp_list = (JSONArray) mapType.get("save_list");
            int size = tmp_list.size();
            for(int i =0;i<size;i++){
                JSONObject tmp =  (JSONObject) tmp_list.get(i);
                item_unfinished it = new item_unfinished();
                it.setTitle(tmp.get("title").toString());
                it.setContent(tmp.get("content").toString());
                it.setLoc(tmp.get("loc").toString());
                it.setFilename(tmp.get("title").toString());
                it.setTime("");
                it.setType((int)tmp.get("type"));
                itemList.add(it);
            }
        }
        mRecyclerView = this.findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new CaoGaoAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void readSDcard(){
        try {
            // 推断是否存在SD卡
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                // 获取SD卡的文件夹
                String sdDire = getExternalFilesDir(null).getPath();
                File testFile = new File(sdDire,"test.txt");
                FileInputStream inStream = new FileInputStream(testFile);
                InputStreamReader reader = new InputStreamReader(inStream, "GBK");
                BufferedReader bReader =  new BufferedReader(reader);
                StringBuffer stringBuffer = new StringBuffer("");
                String str;
                while ((str = bReader.readLine()) != null) {
                    stringBuffer.append(str);
                }
                total_msg =  stringBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}