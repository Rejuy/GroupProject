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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    private TextView textViewState;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_item, container, false);
        Intent intent = getActivity().getIntent();
        userId = intent.getIntExtra("userId", 1);
        System.out.println("====== Fragment Item ======\n" + userId + "\n============\n");

        TextView textViewUser = rootView.findViewById(R.id.test);
        String id_ = Integer.toString(userId);
        textViewUser.setText(id_);

        textViewState = rootView.findViewById(R.id.state);

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
        textViewState.setText("all items on!");
        allItemButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
        followItemButton.setBackgroundColor(getResources().getColor(R.color.title_bg));

        allItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Get all items
                textViewState.setText("all items on!");
                allItemButton.setEnabled(false);
                followItemButton.setEnabled(true);
                allItemButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
                followItemButton.setBackgroundColor(getResources().getColor(R.color.title_bg));

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id", userId);
                paramMap.put("search", "");
                paramMap.put("search_type", "");
                paramMap.put("type", new Boolean[]{true, true, true, true});
                paramMap.put("follower", false);
                paramMap.put("sort", "time");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(curUrl, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
//                String result = "no item";
//                if (result == NO_ITEM)
//                {
//                    alertNoItem();
//                    return;
//                }

                // Construct temporary data
                itemList.clear();
                for (int i = 0; i < 10; i++) {
                    String curUserName = "测试用户all" + i;
                    String curTitle = "测试标题all" + i;
                    String curContent = "all测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
                            "内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容" + i + "\n";
                    String curFollowCondition = Item.HAVE_NOT_FOLLOW;
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
                textViewState.setText("follow items on!");
                allItemButton.setEnabled(true);
                allItemButton.setBackgroundColor(getResources().getColor(R.color.title_bg));
                followItemButton.setBackgroundColor(getResources().getColor(R.color.dark_green));
                followItemButton.setEnabled(false);

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id", userId);
                paramMap.put("search", "");
                paramMap.put("search_type", "");
                paramMap.put("type", new Boolean[]{true, true, true, true});
                paramMap.put("follower", true);
                paramMap.put("sort", "time");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(curUrl, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
//                String result = "no item";
//                if (result == NO_ITEM)
//                {
//                    alertNoItem();
//                    return;
//                }

                // Construct temporary data
                itemList.clear();
                for (int i = 0; i < 5; i++) {
                    String curUserName = "测试用户follow" + i;
                    String curTitle = "测试标题follow" + i;
                    String curContent = "follow测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
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
                }
                mAdapter = new PostListAdapter(getActivity(), itemList);
                // Connect the adapter with the recycler view.
                mRecyclerView.setAdapter(mAdapter);
                // Give the recycler view a default layout manager.
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id", userId);
        paramMap.put("search", "");
        paramMap.put("search_type", "");
        paramMap.put("type", new Boolean[]{true, true, true, true});
        paramMap.put("follower", false);
        paramMap.put("sort", "time");
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(curUrl, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
//        String result = "no item";
//        if (result == NO_ITEM)
//        {
//            alertNoItem();
//            return rootView;
//        }
        // Set recycler related variables
        // Put initial data into the word list.
        for (int i = 0; i < 10; i++) {
            String curUserName = "测试用户all" + i;
            String curTitle = "测试标题all" + i;
            String curContent = "all测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
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