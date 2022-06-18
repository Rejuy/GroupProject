package com.example.groupproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.hutool.http.HttpUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TabFragmentSelfCenter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TabFragmentSelfCenter extends Fragment {
    public static final int NORMAL_REQUEST = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String user_url = Constant.backendUrl+Constant.getUserUrl;
    private String name = "这里是用户名";;
    private String email = "11111@aks.com";
    private String password = "123456aaa";
    private String introduction = "xxxx";
    private String tmp_filepath = "";
    private String tmp_image = "";
    private String creat_time = "";
    int follower_num;
    int follow_num;

    private TextView noticeButton;
    private TextView draftButton;
    private TextView accountButton;
    private TextView followButton;
    private TextView followedButton;
    private TextView blockButton;
    private TextView followcount;
    private TextView followedcount;
    private TextView createtime;
    private int userId;



    public TabFragmentSelfCenter() {
        // Required empty public constructor
    }
    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TabFragmentSelfCenter.
     */
    // TODO: Rename and change types and number of parameters
    public static TabFragmentSelfCenter newInstance(String param1, String param2) {
        TabFragmentSelfCenter fragment = new TabFragmentSelfCenter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void refresh(){
        ImageView portraitButton = (ImageView) getActivity().findViewById(R.id.user_image);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id",IndexActivity.user_id);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(url, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(user_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();
        if(resu.equals("ok")){
            JSONObject user = (JSONObject)mapType.get("data");
            name = (String) user.get("user_name");
            email = (String) user.get("email");
            password = (String) user.get("password");
            introduction = (String) user.get("introduction");
            tmp_image = user.get("user_image_name").toString();
            creat_time = user.get("account_birth").toString();
            JSONArray follower = (JSONArray) user.get("dst_follower_id");
            follower_num = follower.size();
            JSONArray follow = (JSONArray) user.get("src_follower_id");
            follow_num = follow.size();
        }
        followcount = getActivity().findViewById(R.id.follow_count);
        followcount.setText("关注:"+follow_num);
        followedcount = getActivity().findViewById(R.id.followed_count);
        followedcount.setText("被关注:"+follower_num);
        createtime = getActivity().findViewById(R.id.creat_time);
        createtime.setText("用户创建于:"+creat_time);
        String userIntroduction = introduction;
        TextView tvIntroduction = getActivity().findViewById(R.id.center_introduction);
        tvIntroduction.setText(userIntroduction);

        try {
            URL tmp_url = null;
            tmp_url = new URL(Constant.backendUrl+"/media/"+tmp_image);
            System.out.println(tmp_image);
            Bitmap bitmap = requestImg(tmp_url);
            portraitButton.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
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
        View rootView = inflater.inflate(R.layout.fragment_tab_self_center, container, false);
        Intent intent = getActivity().getIntent();
        userId = IndexActivity.user_id;
        System.out.println("?????????????");
        System.out.println(userId);
        // Inflate the layout for this fragment
        accountButton = (TextView) rootView.findViewById(R.id.account_button);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                System.out.print("image click");
                Intent intent = new Intent(getActivity(), AccountActivity.class);
//                String searchContent = searchEditText.getText().toString();
                intent.putExtra("userId", userId);
                startActivityForResult(intent, NORMAL_REQUEST);
            }
        });
        draftButton = (TextView) rootView.findViewById(R.id.draft_button);
        draftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CaoGaoList.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        noticeButton = (TextView) rootView.findViewById(R.id.notice_button);
        noticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        followButton = (TextView) rootView.findViewById(R.id.follow_button);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDisplayUser(DisplayUsersActivity.FOLLOW);
            }
        });

        followedButton = (TextView) rootView.findViewById(R.id.followed_button);
        followedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDisplayUser(DisplayUsersActivity.FOLLOWED);
            }
        });

        blockButton = (TextView) rootView.findViewById(R.id.block_button);
        blockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDisplayUser(DisplayUsersActivity.BLOCK);
            }
        });

        ImageView portraitButton = (ImageView) rootView.findViewById(R.id.user_image);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_id",IndexActivity.user_id);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(url, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(user_url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        String resu = (String) mapType.get("msg").toString();
        if(resu.equals("ok")){
            JSONObject user = (JSONObject)mapType.get("data");
            name = (String) user.get("user_name");
            email = (String) user.get("email");
            password = (String) user.get("password");
            introduction = (String) user.get("introduction");
            tmp_image = user.get("user_image_name").toString();
            creat_time = user.get("account_birth").toString();
            JSONArray follower = (JSONArray) user.get("dst_follower_id");
            follower_num = follower.size();
            JSONArray follow = (JSONArray) user.get("src_follower_id");
            follow_num = follow.size();
        }
        followcount = rootView.findViewById(R.id.follow_count);
        followcount.setText("关注:"+follow_num);
        followedcount = rootView.findViewById(R.id.followed_count);
        followedcount.setText("被关注:"+follower_num);
        createtime = rootView.findViewById(R.id.creat_time);
        createtime.setText("用户创建于:"+creat_time);

        try {
            URL tmp_url = null;
            tmp_url = new URL(Constant.backendUrl+"/media/"+tmp_image);
            System.out.println(tmp_image);
            Bitmap bitmap = requestImg(tmp_url);
            portraitButton.setImageBitmap(bitmap);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        portraitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
                Intent intent = new Intent(getActivity(), SelfItemActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("userName", "asdf");
                startActivity(intent);
            }
        });

        String userIntroduction = introduction;
        TextView tvIntroduction = rootView.findViewById(R.id.center_introduction);
        tvIntroduction.setText(userIntroduction);


        return rootView;
    }

    public void toDisplayUser(String range)
    {
        Intent intent = new Intent(getActivity(), DisplayUsersActivity.class);
//                String searchContent = searchEditText.getText().toString();
        intent.putExtra("range", range);
        startActivityForResult(intent, NORMAL_REQUEST);
    }
    private Bitmap requestImg(final URL imgUrl)
    {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(imgUrl.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }
}