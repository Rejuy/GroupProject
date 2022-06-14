package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Dialog;
import androidx.appcompat.app.AlertDialog;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
//        Uri uri = Uri.parse("http://59.66.137.34:8765/media/VID_20220516_134006_pKywsaS.mp4/");
//        Intent ointent = new Intent();
//        ointent.setAction(Intent.ACTION_VIEW);
//
////        Uri content_url = Uri.parse("https://www.baidu.com");
//
//        ointent.setDataAndType(uri,"video/mp4");
//        startActivity(ointent);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log.d(LOG_TAG, "To register button clicked!");
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, NORMAL_REQUEST);
    }

    public void login(View view) {
//        WebView mWebView = (WebView) findViewById(R.id.web);
//        mWebView.setWebChromeClient(new WebChromeClient());
////        mWebView.loadUrl("http://59.66.137.34:8765/media/VID_20220516_134006_pKywsaS.mp4/");
//        mWebView.loadData("<html><body><video><source src=\"http://59.66.137.34:8765/media/VID_20220516_134006_pKywsaS.mp4/\"/></video></body></html>","video/mp4","utf-8");
//
        Log.d(LOG_TAG, "Login button clicked!");


        // Get user information
        String account = accountEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("user_name", account);
        paramMap.put("password", password);
        JSONObject obj = new JSONObject(paramMap);
        ///////////////////////////////////////////
        ////////// Backend Connection /////////////
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        String obj_string = obj.toJSONString();
        String result = HttpUtil.post(url, obj_string);
        HashMap mapType = JSON.parseObject(result,HashMap.class);
        System.out.println(mapType.get("data").toString());
        String res = (String) mapType.get("msg").toString();

//        result (String) -->> result (json)
        ///////////////////////////////////////////
//        String result = "1";

        // React to the result of backend
        if (res.equals(USER_NOT_FOUND) )
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
        else if (res.equals(WRONG_PASSWORD))
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
            JSONObject user_id = (JSONObject)mapType.get("data");
            int userId = (int)user_id.get("id");
            intent.putExtra("userId", userId);
            Constant.userId = userId;
            startActivityForResult(intent, NORMAL_REQUEST);
        }
    }
}