package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import cn.hutool.http.HttpUtil;

public class AccountActivity extends AppCompatActivity {
    private String name = "这里是用户名";;
    private String email = "11111@aks.com";
    private String password = "123456aaa";
    private String introduction = "xxxx";
    private String url = Constant.backendUrl+Constant.updateUserUrl;
    private String user_url = Constant.backendUrl+Constant.getUserUrl;

    private TextView curName;
    private TextView curEmail;
    private TextView curPassword;
    private TextView curIntroduction;

    private ImageView confirmName;
    private ImageView confirmEmail;
    private ImageView confirmPassword;
    private ImageView confirmIntroduction;

    private EditText editNewName;
    private EditText editNewEmail;
    private EditText editNewPassword;
    private EditText editNewIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 1);

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
        }

        // TODO: Get user information to display
        confirmName = findViewById(R.id.confirm_name);
        editNewName = findViewById(R.id.edit_name);
        curName = findViewById(R.id.center_current_name);
        curName.setText("当前用户名：" + name);
        confirmName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                name = editNewName.getText().toString();
                // TODO: Connect backend
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction","");
                paramMap.put("user_name", name);
                paramMap.put("password", "");
                paramMap.put("email","");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curName.setText("当前用户名：" + name);
                }
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);

            }
        });

        confirmEmail = findViewById(R.id.confirm_email);
        editNewEmail = findViewById(R.id.edit_email);
        curEmail = findViewById(R.id.center_current_email);
        curEmail.setText("当前邮箱：" + email);
        confirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                email = editNewEmail.getText().toString();
                // TODO: Connect backend
                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction","");
                paramMap.put("user_name", "");
                paramMap.put("password", "");
                paramMap.put("email",email);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curEmail.setText("当前邮箱：" + email);
                }

//                paramMap.put("user_name", account);
//                paramMap.put("password", password);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        confirmPassword = findViewById(R.id.confirm_password);
        editNewPassword = findViewById(R.id.edit_password);
        curPassword = findViewById(R.id.center_current_password);
        curPassword.setText("当前密码：" + password);
        confirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                password = editNewPassword.getText().toString();
                // TODO: Connect backend
//                paramMap.put("user_name", account);
//                paramMap.put("password", password);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction","");
                paramMap.put("user_name", "");
                paramMap.put("password",password);
                paramMap.put("email","");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curPassword.setText("当前密码：" + password);
                }
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        confirmIntroduction = findViewById(R.id.confirm_introduction);
        editNewIntroduction = findViewById(R.id.edit_introduction);
        curIntroduction = findViewById(R.id.center_current_introduction);
        curIntroduction.setText("当前介绍：" + introduction);
        confirmIntroduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.print("image click");
//                Intent intent = new Intent(getActivity(), SearchResultActivity.class);
                introduction = editNewIntroduction.getText().toString();
                // TODO: Connect backend
//                paramMap.put("user_name", account);
//                paramMap.put("password", password);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////

                HashMap<String, Object> paramMap = new HashMap<>();
                paramMap.put("user_id",IndexActivity.user_id);
                paramMap.put("user_image","");
                paramMap.put("introduction",introduction);
                paramMap.put("user_name", "");
                paramMap.put("password","");
                paramMap.put("email","");
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                // String result = HttpUtil.post(url, paramMap);
                // result (String) -->> result (json)
                ///////////////////////////////////////////
                JSONObject obj = new JSONObject(paramMap);
                ///////////////////////////////////////////
                ////////// Backend Connection /////////////
                String obj_string = obj.toJSONString();
                String result = HttpUtil.post(url, paramMap);
                HashMap mapType = JSON.parseObject(result,HashMap.class);
                String res = (String) mapType.get("msg").toString();
                if(res.equals("ok")){
                    curIntroduction.setText("当前介绍：" + introduction);
                }
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);
            }
        });
    }
}