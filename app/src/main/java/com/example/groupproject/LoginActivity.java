package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    public static final int NORMAL_REQUEST = 0;

    public static final String USER_NOT_FOUND = "user not found";
    public static final String WRONG_PASSWORD = "wrong password";
    public static final int WRONG_ACCOUNT = 1;
    public static final int SOMETHING_WENT_WRONG = -1;

    public static final String url = Constant.backendUrl + Constant.loginUrl;

    private EditText accountEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountEditText = findViewById(R.id.login_edit_account);
        passwordEditText = findViewById(R.id.login_edit_password);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    public void toRegister(View view) {
        Log.d(LOG_TAG, "To register button clicked!");
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, NORMAL_REQUEST);
    }

    public void login(View view) {
        Log.d(LOG_TAG, "Login button clicked!");

        // Get user information
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_name", account);
        paramMap.put("password", password);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
        // String result = HttpUtil.post(url, paramMap);
        // result (String) -->> result (json)
        ///////////////////////////////////////////
        String result = "1";

        // React to the result of backend
        if (result == USER_NOT_FOUND)
        {
            // User not found
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(null);
            builder.setTitle("提示");
            builder.setMessage("用户名不存在！请重试。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(LoginActivity.this, "请重新输入...", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
        else if (result == WRONG_PASSWORD)
        {
            // Wrong password
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(null);
            builder.setTitle("提示");
            builder.setMessage("密码错误！请重试。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(LoginActivity.this, "请重新输入...", Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
        else
        {
            // Success: jump to index activity
            Intent intent = new Intent(this, IndexActivity.class);
            int userId = Integer.valueOf(result);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, NORMAL_REQUEST);
        }
    }
}