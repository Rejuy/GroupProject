package com.example.groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import com.alibaba.fastjson.*;

public class MainActivity extends AppCompatActivity {
    TextView text;
    int TEXT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Activity that =this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println('e');
        System.out.println('c');
        System.out.println("test");

        // Try github
        System.out.println("testtesttest");
        text = findViewById(R.id.test_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(that,itemCreateActivity.class);
                startActivityForResult(intent,TEXT_REQUEST);
            }
        });
    }


}