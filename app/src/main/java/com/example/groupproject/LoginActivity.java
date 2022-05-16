package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
        int result = LOGIN_SUCCESS;
        int userId = 1;

        // React to the result of backend
        if (result == LOGIN_SUCCESS)
        {
            // Success: jump to index activity
            Intent intent = new Intent(this, IndexActivity.class);
            intent.putExtra("userId", userId);
            startActivityForResult(intent, NORMAL_REQUEST);
        }
        else if (result == SOMETHING_WENT_WRONG)
        {
            // Something went wrong
            // TODO
        }
        else
        {
            // Failure: notice the user to edit again
            // TODO
        }
    }
}