package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {
    private String name = "这里是用户名";;
    private String email = "11111@aks.com";
    private String password = "123456aaa";

    private TextView curName;
    private TextView curEmail;
    private TextView curPassword;

    private ImageView confirmName;
    private ImageView confirmEmail;
    private ImageView confirmPassword;

    private EditText editNewName;
    private EditText editNewEmail;
    private EditText editNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Intent intent = getIntent();
        int userId = intent.getIntExtra("userId", 1);

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
                curName.setText("当前用户名：" + name);
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
                curEmail.setText("当前邮箱：" + email);
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
                curPassword.setText("当前密码：" + password);
//                intent.putExtra("searchContent", searchContent);
//                startActivityForResult(intent, TEXT_REQUEST);
            }
        });
    }
}