package com.example.groupproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

import cn.hutool.http.HttpUtil;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    public static final int NORMAL_REQUEST = 1;

    public static final String USER_NAME_EXISTS = "user_name already exists";
    public static final String USER_EMAIL_EXISTS = "email already exists";
    public static final String REGISTER_SUCCESS = "ok";
    public static final int PASSWORD_NOT_SAME = 0;
    public static final int INVALID_EMAIL = 1;
    public static final int INVALID_NAME = 2;
    public static final int INVALID_PASSWORD = 3;
//    public static final int REGISTER_SUCCESS = 4;
    public static final int SOMETHING_WENT_WRONG = -1;

    public static final String url = Constant.backendUrl + Constant.registerUrl;

    private EditText accountCreateEditText;
    private EditText nameCreateEditText;
    private EditText passwordCreateEditText;
    private EditText passwordConfirmEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        accountCreateEditText = findViewById(R.id.register_edit_email);
        nameCreateEditText = findViewById(R.id.register_edit_name);
        passwordCreateEditText = findViewById(R.id.register_edit_password);
        passwordConfirmEditText = findViewById(R.id.register_confirm_password);

    }

    public void toLogin(View view) {
        Log.d(LOG_TAG, "Back button clicked!");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, NORMAL_REQUEST);
    }

    public void register(View view) {
        Log.d(LOG_TAG, "Register button clicked!");

        // Get user information
        String rawAccount = accountCreateEditText.getText().toString();
        String rawName = nameCreateEditText.getText().toString();
        String rawPassword = passwordCreateEditText.getText().toString();
        String confirmedPassword = passwordConfirmEditText.getText().toString();

        // Check password
        if (!rawPassword.equals(confirmedPassword))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(null);
            builder.setTitle("提示");
            builder.setMessage("两次密码输入不一致！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(RegisterActivity.this, "请重新输入...", Toast.LENGTH_SHORT).show();
                }
            }).show();
            return;
        }

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_name", rawName);
        paramMap.put("password", rawPassword);
        paramMap.put("email", rawAccount);
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        System.out.println(mapType.get("msg").toString());
        String res = (String) mapType.get("msg").toString();

        // result (String) -->> result (json)
        ///////////////////////////////////////////
//        String result = REGISTER_SUCCESS;

        // React to the result of backend
        if (res.equals(REGISTER_SUCCESS))
        {
            // Success: jump to login activity
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(null);
            builder.setTitle("提示");
            builder.setMessage("用户创建成功！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(RegisterActivity.this, "您可以准备登陆", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivityForResult(intent, NORMAL_REQUEST);
                }
            }).show();

        }
        else if (res.equals(USER_NAME_EXISTS))
        {
            System.out.println("????????????");
            // User name exists
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(null);
            builder.setTitle("提示");
            builder.setMessage("用户名已存在！请更换用户名。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(RegisterActivity.this, "请重新输入...", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
        else if (res.equals(USER_EMAIL_EXISTS))
        {
            // User email exists
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(null);
            builder.setTitle("提示");
            builder.setMessage("邮箱已存在！请更换邮箱。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(RegisterActivity.this, "请重新输入...", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
        else
        {
            // Other circumstances
        }
    }
}