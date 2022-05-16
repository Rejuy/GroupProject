package com.example.groupproject;

import android.content.Intent;
import android.os.Bundle;

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
//        String id_ = Integer.toString(user_id);
//        textViewUser.setText(id_);

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
            }
        });

        // Set recycler related variables
        // Put initial data into the word list.
        for (int i = 0; i < 15; i++) {
            String curUserName = "测试用户" + i;
            String curTitle = "测试标题" + i;
            String curContent = "测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试" +
                    "内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容测试内容" + i + "\n";
            Date d = new Date();
            System.out.println(d);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curTime = sdf.format(d);
            curContent += curTime;
            int curLikesCount = i * 100;
            int curCommentsCount = i * 10;
            int curType = Item.TEXT;
            Item curItem = new Item(i, curTitle, curContent, curUserName, i,
                    curLikesCount, curCommentsCount, curType, false);
            itemList.add(curItem);

//            mContentList.addLast("动态说了点什么呢？（我是内容） " + i + "\n" + curTime);
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

//        mAdapter.setOnItemClickListener(new PostListAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, PostListAdapter.ViewName viewName, int position) {
//
//                switch (v.getId()){
//                    case R.id.like_button:
//                        Item curItem = itemList.get(position);
//                        if (curItem.getLiked()) {
//                            curItem.unlike();
//                            itemList.set(position, curItem);
//                            ImageView curLikeButton = (ImageView) rootView.findViewById(R.id.like_button);
//                            curLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_unlike));
//                        }
//                        else {
//                            curItem.like();
//                            itemList.set(position, curItem);
//                            ImageView curLikeButton = (ImageView) rootView.findViewById(R.id.like_button);
//                            curLikeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
//                        }
//                        break;
//                    case R.id.comment_button:
//                        Toast.makeText(getActivity(),"你点击了删除按钮"+(position+1),Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.share_button:
//                        Toast.makeText(getActivity(),"你点击了删除按钮"+(position+1),Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        Toast.makeText(getActivity(),"你点击了item"+(position+1),Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }});
        return rootView;
    }
}