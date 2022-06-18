package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.hutool.http.HttpUtil;

public class SearchResultActivity extends AppCompatActivity {
    public static final int NORMAL_REQUEST = 0;

    private EditText searchEditText;
    private ImageView gotoImageButton;
    HashMap<String,String> cn_to_en_part = new HashMap<>();
    private Spinner typeSpinner;
    private Spinner sortSpinner;
    private Spinner rangeSpinner;
    private Spinner partSpinner;
    public static final String url = Constant.backendUrl + Constant.getItemUrl;
    public static final String NO_ITEM = "no item";

    String[] dataType = new String[]{"综合", "文字", "图片", "音频", "视频"};
    String[] dataSort = new String[]{"默认", "点赞", "评论", "时间"};
    String[] dataRange = new String[]{"全部", "关注"};
    String[] dataPart = new String[]{"标题", "内容", "用户"};

    private final List<Item> itemList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;

    private TextView tvType;
    private TextView tvSort;
    private TextView tvRange;
    private TextView tvPart;
    private TextView tvTestOutcome;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        cn_to_en_part.put("标题","title");
        cn_to_en_part.put("内容","content");
        cn_to_en_part.put("用户","user");

        ImageView back_image_button = (ImageView)findViewById(R.id.back_button);
        back_image_button.setOnClickListener(this::backToIndex);

        searchEditText = findViewById(R.id.search_edit_search);
        // Set image button
        gotoImageButton = (ImageView) findViewById(R.id.search_goto_button);
        gotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        Intent intent = getIntent();
        String message = intent.getStringExtra("searchContent");
        userId = intent.getIntExtra("userId", 1);
        // Put that message into the text_message TextView.
        typeSpinner= (Spinner) this.findViewById(R.id.spinner_type);
        typeSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataType));
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataType[position];
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sortSpinner= (Spinner) this.findViewById(R.id.spinner_sort);
        sortSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataSort));
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataSort[position];
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        rangeSpinner= (Spinner) this.findViewById(R.id.spinner_range);
        rangeSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataRange));
        rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataRange[position];
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        partSpinner= (Spinner) this.findViewById(R.id.spinner_part);
        partSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataPart));
        partSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataPart[position];
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Test



        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", IndexActivity.user_id);
        paramMap.put("search", "");
        paramMap.put("search_type", "");
        paramMap.put("type", new Boolean[]{true, true, true, true});
        paramMap.put("follower", false);
        paramMap.put("sort", "time");

        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();

        // result (String) -->> result (json)
        ///////////////////////////////////////////
//                String result = "no item";
        if (resu.equals(NO_ITEM) )
        {
            alertNoItem();
            return;
        }
        JSONArray json_list = (JSONArray) mapType.get("data");
        // Construct temporary data
        itemList.clear();
        for (int i = 0; i < json_list.size(); i++) {
            JSONObject tmp =  (JSONObject) json_list.get(i);
            int curUserId = (int)tmp.get("user_id");
            int curItemId = (int)tmp.get("item_id");
            System.out.println("item_list");
            System.out.println(curUserId);
            String curUserName = tmp.get("user_name").toString();
            String curTitle = tmp.get("title").toString();
            String curContent = tmp.get("content").toString()+"\n";
            String curFollowCondition = ((Boolean)tmp.get("is_followed")?Item.FOLLOW:Item.HAVE_NOT_FOLLOW);
            String curTime = tmp.get("created_time").toString();
            curContent += curTime;
            int curLikesCount = (int)tmp.get("like_count");
            int curCommentsCount = (int)tmp.get("comment_count");
            int curType = (int)tmp.get("type");
            String curFilename = tmp.get("file_name").toString();
            String fakeimage_path = "";
            if(curType == 3){
                fakeimage_path = tmp.get("fake_image").toString();
            }
            String curUserImage = "";
            if(tmp.getString("user_image_name")==null){

            }else{
                curUserImage= tmp.get("user_image_name").toString();
            }
            Boolean is_liked = (Boolean)tmp.get("is_liked");
            Item curItem = new Item(curItemId, curTitle, curContent, curUserName, curFollowCondition, curUserId,
                    curLikesCount, curCommentsCount, curType, is_liked,curFilename,fakeimage_path,curUserImage);
            itemList.add(curItem);
        }

        // Create recycler view.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PostListAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Inflate the layout for this fragment
    }

    public void backToIndex(View view) {
//        Log.d(LOG_TAG, "To register button clicked!");
        System.out.print("image click");
        Intent intent = new Intent(this, IndexActivity.class);
        startActivityForResult(intent, NORMAL_REQUEST);
    }

    public void refresh() {
        // Get current condition
        String searchWords = searchEditText.getText().toString();
        String type = typeSpinner.getSelectedItem().toString();
        String sort = sortSpinner.getSelectedItem().toString();
        String range = rangeSpinner.getSelectedItem().toString();
        String part = partSpinner.getSelectedItem().toString();


        Boolean[] types = new Boolean[]{false, false, false, false};
        if (type.equals("综合")) {
            System.out.println("!!!!!!!!!!!!!!");
            types[0] = true;
            types[1] = true;
            types[2] = true;
            types[3] = true;
        }
        else if (type.equals("文字"))
            types[0] = true;
        else if (type.equals("图片"))
            types[1] = true;
        else if (type.equals("音频"))
            types[2] = true;
        else if (type.equals("视频"))
            types[3] = true;

        Boolean follow = false;
        if (range.equals("关注"))
            follow = true;

        String sortInfo = "";
        if (sort.equals("点赞"))
            sortInfo = "like";
        else if (sort.equals("评论"))
            sortInfo = "comment";
        else
            sortInfo = "time";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", userId);
        paramMap.put("search", searchWords);
        paramMap.put("search_type", cn_to_en_part.get(part));



        paramMap.put("type", types);
        paramMap.put("follower", follow);
        paramMap.put("sort", sortInfo);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(curUrl, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();

        // result (String) -->> result (json)
        ///////////////////////////////////////////
//                String result = "no item";
        if (resu.equals(NO_ITEM) )
        {
            alertNoItem();
            return;
        }
        JSONArray json_list = (JSONArray) mapType.get("data");
        // Construct temporary data
        itemList.clear();
        for (int i = 0; i < json_list.size(); i++) {
            JSONObject tmp =  (JSONObject) json_list.get(i);
            int curUserId = (int)tmp.get("user_id");
            int curItemId = (int)tmp.get("item_id");
            System.out.println("item_list");
            System.out.println(curUserId);
            String curUserName = tmp.get("user_name").toString();
            String curTitle = tmp.get("title").toString();
            String curContent = tmp.get("content").toString()+"\n";
            String curFollowCondition = ((Boolean)tmp.get("is_followed")?Item.FOLLOW:Item.HAVE_NOT_FOLLOW);
            String curTime = tmp.get("created_time").toString();

            curContent += curTime;
            int curLikesCount = (int)tmp.get("like_count");
            int curCommentsCount = (int)tmp.get("comment_count");
            int curType = (int)tmp.get("type");
            String curFilename = tmp.get("file_name").toString();
            String fakeimage_path = "";
            if(curType == 3){
                fakeimage_path = tmp.get("fake_image").toString();
            }
            String curUserImage = "";
            if(tmp.getString("user_image_name")==null){

            }else{
                curUserImage= tmp.get("user_image_name").toString();
            }
            Boolean is_liked = (Boolean)tmp.get("is_liked");
            Item curItem = new Item(curItemId, curTitle, curContent, curUserName, curFollowCondition, curUserId,
                    curLikesCount, curCommentsCount, curType, is_liked,curFilename,fakeimage_path,curUserImage);
            itemList.add(curItem);
        }
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PostListAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void alertNoItem()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Activity that = this;
        builder.setIcon(null);
        builder.setTitle("提示");
        builder.setMessage("没有所查动态！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(that, "请重新查询...", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}

