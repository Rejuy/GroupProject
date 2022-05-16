package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        Intent intent = getIntent();
        String introduction = intent.getStringExtra("introduction");
        TextView tv = findViewById(R.id.introduction);
        tv.setText(introduction);

        ImageView homePageButton = (ImageView)findViewById(R.id.homepage_button);
        homePageButton.setOnClickListener(this::backToIndex);

    }

    public void backToIndex(View view) {
//        Log.d(LOG_TAG, "To register button clicked!");
        System.out.print("image click");
        Intent intent = new Intent(this, IndexActivity.class);
        startActivityForResult(intent, 0);
    }
}