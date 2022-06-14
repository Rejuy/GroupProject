package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.hutool.http.HttpUtil;

public class NoticeActivity extends AppCompatActivity {
    private String notice_get_url = Constant.backendUrl+Constant.noticeUrl;
    private ArrayList<String> item_list = new ArrayList<>();
    private RecyclerView mRecyclerView;
    NoticeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id",IndexActivity.user_id);
        JSONObject obj = new JSONObject(paramMap);
        String obj_string = obj.toJSONString();
        String result= HttpUtil.post(notice_get_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        JSONArray json_list = (JSONArray) mapType.get("data");
        System.out.println("??????????????????????????????");
        System.out.println(json_list);
        if(json_list.size()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(null);
            builder.setTitle("提示");
            builder.setMessage("未查询到通知！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                                Toast.makeText(getActivity(), "请重新查询...", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }else{
            for(int i = 0;i<json_list.size();i++){
                item_list.add(json_list.get(i).toString());
            }
            System.out.println(item_list.size());
            mRecyclerView = this.findViewById(R.id.recyclerview);
            // Create an adapter and supply the data to be displayed.
            mAdapter = new NoticeAdapter(this, item_list);
            // Connect the adapter with the recycler view.
            mRecyclerView.setAdapter(mAdapter);
            // Give the recycler view a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }
}