package com.example.groupproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

    private TextView noticeButton;
    private TextView draftButton;
    private TextView accountButton;
    private TextView followButton;
    private TextView followedButton;
    private TextView blockButton;
    private int userId;



    public TabFragmentSelfCenter() {
        // Required empty public constructor
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

        String userIntroduction = userId + "这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介" +
                "这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介" +
                "这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介这里是用户简介";
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
}