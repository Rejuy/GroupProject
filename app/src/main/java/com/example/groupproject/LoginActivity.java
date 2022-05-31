package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.HashMap;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    public static final int NORMAL_REQUEST = 0;

    public static final int LOGIN_SUCCESS = 0;
    public static final int WRONG_ACCOUNT = 1;
    public static final int WRONG_PASSWORD = 2;
    public static final int SOMETHING_WENT_WRONG = -1;

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

        // Post information to backend
        // TODO
//        HashMap <String,String> data  = new HashMap <String,String>();
//        data.put("user_name",account);
//        data.put("password",password);
//        BackEndConnection ccc = new BackEndConnection();
//        String url = "http://183.172.174.207:8765/user/login/";
//        String res = ccc.test(url,data);
//        System.out.println("==================");
//        System.out.println(res);
//        System.out.println("==================");
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("picture", FileUtil.file("/mnt/sdcard/DCIM/Camera/VID_20220516_134006.mp4"));
//
//        String result1= HttpUtil.post("http://183.172.174.207:8765/filetest/", paramMap);

        Intent tmp_tent = new Intent(this,CaoGaoList.class);
        startActivityForResult(tmp_tent,NORMAL_REQUEST);
//        int result = LOGIN_SUCCESS;
//        int userId = 1;
//
//        // React to the result of backend
//        if (result == LOGIN_SUCCESS)
//        {
//            // Success: jump to index activity
//            Intent intent = new Intent(this, IndexActivity.class);
//            intent.putExtra("userId", userId);
//            startActivityForResult(intent, NORMAL_REQUEST);
//        }
//        else if (result == SOMETHING_WENT_WRONG)
//        {
//            // Something went wrong
//            // TODO
//        }
//        else
//        {
//            // Failure: notice the user to edit again
//            // TODO
//        }
    }
}