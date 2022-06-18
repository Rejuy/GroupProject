package com.example.groupproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.hutool.http.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragmentItem#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragmentItem extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final int TEXT_REQUEST = 1;

    public static final int ALL_ITEM_STATE = 0;
    public static final int FOLLOW_ITEM_STATE = 1;

    public static final String url = Constant.backendUrl + Constant.getItemUrl;
    public static final String NO_ITEM = "no item";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText searchEditText;
    private ImageView gotoImageButton;
    private Button allItemButton;
    private Button followItemButton;
    private int state = 0;
    private boolean isFirstLoading = true;
    private TextView textViewState;
    private FloatingActionButton fab;

    private final List<Item> itemList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private PostListAdapter mAdapter;

    private int userId;

    public TabFragmentItem() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragmentItem.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragmentItem newInstance(String param1, String param2) {
        TabFragmentItem fragment = new TabFragmentItem();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (!isFirstLoading){
            refresh();
        }else{
            isFirstLoading = false;
        }
    }
    private void refresh(){
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
        System.out.println(resu);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
//                String result = "no item";
        if (resu.equals(NO_ITEM) )
        {
            alertNoItem();
            itemList.clear();
            mRecyclerView = getActivity().findViewById(R.id.recyclerview);
            // Create an adapter and supply the data to be displayed.
            mAdapter = new PostListAdapter(getActivity(), itemList);
            // Connect the adapter with the recycler view.
            mRecyclerView.setAdapter(mAdapter);
            // Give the recycler view a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return;
        }
        JSONArray json_list = (JSONArray) mapType.get("data");

        System.out.println(json_list);
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
        mRecyclerView = getActivity().findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PostListAdapter(getActivity(), itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_item, container, false);
        Intent intent = getActivity().getIntent();
        userId = intent.getIntExtra("userId", 1);
        System.out.println("====== Fragment Item ======\n" + userId + "\n============\n");
        String id_ = Integer.toString(userId);
        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), itemCreateActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        searchEditText = rootView.findViewById(R.id.index_edit_search);

        // Set image button
        gotoImageButton = (ImageView) rootView.findViewById(R.id.goto_button);
        gotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                String searchContent = searchEditText.getText().toString();
                intent.putExtra("searchContent", searchContent);
                intent.putExtra("userId", userId);
                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        // Set item button
        allItemButton = (Button) rootView.findViewById(R.id.item_all_button);
        followItemButton = (Button) rootView.findViewById(R.id.item_follow_button);


        allItemButton.setEnabled(false);
        followItemButton.setEnabled(true);
        allItemButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
        followItemButton.setBackgroundColor(getResources().getColor(R.color.title_bg));

        allItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Get all items
                allItemButton.setEnabled(false);
                followItemButton.setEnabled(true);
                allItemButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
                followItemButton.setBackgroundColor(getResources().getColor(R.color.title_bg));

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id", IndexActivity.user_id);
                paramMap.put("search", "");
                paramMap.put("search_type", "");
                paramMap.put("type", new Boolean[]{true, true, true, true});
                paramMap.put("follower", false);
                paramMap.put("sort", "time");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
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
                mAdapter = new PostListAdapter(getActivity(), itemList);
                // Connect the adapter with the recycler view.
                mRecyclerView.setAdapter(mAdapter);
                // Give the recycler view a default layout manager.
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

        followItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Get follow items
                allItemButton.setEnabled(true);
                allItemButton.setBackgroundColor(getResources().getColor(R.color.title_bg));
                followItemButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
                followItemButton.setEnabled(false);

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id", IndexActivity.user_id);
                paramMap.put("search", "");
                paramMap.put("search_type", "");
                paramMap.put("type", new Boolean[]{true, true, true, true});
                paramMap.put("follower", true);
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

                    }else {
                        curUserImage = tmp.get("user_image_name").toString();
                    }
                    Boolean is_liked = (Boolean)tmp.get("is_liked");
                    Item curItem = new Item(curItemId, curTitle, curContent, curUserName, curFollowCondition, curUserId,
                            curLikesCount, curCommentsCount, curType, is_liked,curFilename,fakeimage_path,curUserImage);
                    itemList.add(curItem);
                }
                mAdapter = new PostListAdapter(getActivity(), itemList);
                // Connect the adapter with the recycler view.
                mRecyclerView.setAdapter(mAdapter);
                // Give the recycler view a default layout manager.
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

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
        System.out.println(resu);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
//                String result = "no item";
        if (resu.equals(NO_ITEM) )
        {
            alertNoItem();
            return rootView;
        }
        JSONArray json_list = (JSONArray) mapType.get("data");

        System.out.println(json_list);
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
        mRecyclerView = rootView.findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PostListAdapter(getActivity(), itemList);
        // Connect the adapter with the recycler view.
        mRecyclerView.setAdapter(mAdapter);
        // Give the recycler view a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // Inflate the layout for this fragment

        return rootView;
    }

    public void alertNoItem()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(null);
        builder.setTitle("提示");
        builder.setMessage("没有所查动态！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "请重新查询...", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }
}