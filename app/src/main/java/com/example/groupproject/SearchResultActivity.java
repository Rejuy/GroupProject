package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    public static final int NORMAL_REQUEST = 0;

    private EditText searchEditText;
    private ImageView gotoImageButton;

    private Spinner typeSpinner;
    private Spinner sortSpinner;
    private Spinner rangeSpinner;
    private Spinner partSpinner;

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
        TextView textView = findViewById(R.id.test_search);
        textView.setText(message);

        // Get spinner
        tvType = (TextView) this.findViewById(R.id.test_type);
        typeSpinner= (Spinner) this.findViewById(R.id.spinner_type);
        typeSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataType));
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataType[position];
                tvType.setText("类型："+str);
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tvSort = (TextView) this.findViewById(R.id.test_sort);
        sortSpinner= (Spinner) this.findViewById(R.id.spinner_sort);
        sortSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataSort));
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataSort[position];
                tvSort.setText("排序："+str);
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tvRange = (TextView) this.findViewById(R.id.test_range);
        rangeSpinner= (Spinner) this.findViewById(R.id.spinner_range);
        rangeSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataRange));
        rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataRange[position];
                tvRange.setText("范围："+str);
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tvPart = (TextView) this.findViewById(R.id.test_part);
        partSpinner= (Spinner) this.findViewById(R.id.spinner_part);
        partSpinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataPart));
        partSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = dataPart[position];
                tvPart.setText("部分："+str);
                refresh();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Test
        tvTestOutcome = findViewById(R.id.test_outcome);



        // Set recycler related variables
        // Put initial data into the word list.
        for (int i = 0; i < 15; i++) {
            String curUserName = "测试用户" + i;
            String curTitle = "测试标题" + i;
            String curContent = "测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
                    "内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容" + i + "\n";
            String curFollowCondition = Item.FOLLOW;
            Date d = new Date();
            System.out.println(d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = sdf.format(d);
            curContent += curTime;
            int curLikesCount = i * 100;
            int curCommentsCount = i * 10;
            int curType = Item.TEXT;
            Item curItem = new Item(i, curTitle, curContent, curUserName, curFollowCondition, i,
                    curLikesCount, curCommentsCount, curType, false);
            itemList.add(curItem);

//            mContentList.addLast("动态说了点什么呢？（我是内容） " + i + "\n" + curTime);
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
        tvTestOutcome.setText("==========\nSearchWords: "
                                + searchWords
                                + "\nType: "
                                + type
                                + "\nSort: "
                                + sort
                                + "\nRange: "
                                + range
                                + "\nPart: "
                                + part
                                + "==========");

        Boolean[] types = new Boolean[]{false, false, false, false};
        if (type.equals("全部"))
            types[0] = types[1] = types[2] = types[3] = true;
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
        paramMap.put("search_type", part);



        paramMap.put("type", type);
        paramMap.put("follower", follow);
        paramMap.put("sort", sortInfo);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(curUrl, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        itemList.clear();
        for (int i = 0; i < 15; i++) {
            String curUserName = "测试用户asdf" + i;
            String curTitle = "测试标题asdf" + i;
            String curContent = "asdf测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
                    "内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容" + i + "\n";
            String curFollowCondition = Item.FOLLOW;
            Date d = new Date();
            System.out.println(d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = sdf.format(d);
            curContent += curTime;
            int curLikesCount = i * 100;
            int curCommentsCount = i * 10;
            int curType = Item.TEXT;
            Item curItem = new Item(i, curTitle, curContent, curUserName, curFollowCondition, i,
                    curLikesCount, curCommentsCount, curType, false);
            itemList.add(curItem);

//            mContentList.addLast("动态说了点什么呢？（我是内容） " + i + "\n" + curTime);
        }
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PostListAdapter(this, itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}