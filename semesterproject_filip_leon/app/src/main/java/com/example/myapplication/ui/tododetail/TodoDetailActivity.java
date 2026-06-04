package com.example.myapplication.ui.tododetail;

import android.app.Activity;
import android.os.Bundle;

import com.example.myapplication.R;

public class TodoDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_detail);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}