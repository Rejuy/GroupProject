package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    public static final int NORMAL_REQUEST = 1;

    public static final int PASSWORD_NOT_SAME = 0;
    public static final int INVALID_EMAIL = 1;
    public static final int INVALID_NAME = 2;
    public static final int INVALID_PASSWORD = 3;
    public static final int REGISTER_SUCCESS = 4;
    public static final int SOMETHING_WENT_WRONG = -1;

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

        // Post information to backend
        // TODO
        int result = REGISTER_SUCCESS;
        if (!rawPassword.equals(confirmedPassword))
        {
            result = PASSWORD_NOT_SAME;
        }

        // React to the result of backend
        if (result == REGISTER_SUCCESS)
        {
            // Success: jump to login activity
            Intent intent = new Intent(this, LoginActivity.class);
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