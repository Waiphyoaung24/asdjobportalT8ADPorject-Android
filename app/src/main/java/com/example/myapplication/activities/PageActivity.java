package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

public class PageActivity extends AppCompatActivity {

    Button btn1;
    Button btn2;
    Button btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        initComponents();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageActivity.this,MainActivity.class);
                intent.putExtra("fragment",1);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageActivity.this,MainActivity.class);
                intent.putExtra("fragment",2);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PageActivity.this,MainActivity.class);
                intent.putExtra("fragment",3);
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        btn1 = findViewById(R.id.btn_all_job);
        btn2 = findViewById(R.id.btn_category_job);
        btn3 = findViewById(R.id.btn_search_job);
    }


}