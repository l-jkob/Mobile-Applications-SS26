package com.example.myapplication.ui.todolist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;

import com.example.myapplication.R;

import java.util.ArrayList;

public class TodoListActivity extends Activity {

    private ListView todoListView;
    private ArrayList<String> todos;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        todoListView = findViewById(R.id.todoListView);

        todos = new ArrayList<>();
        todos.add("MAaS-Projekt abgeben");
        todos.add("UML-Diagramm erstellen");
        todos.add("Todo-Detailansicht bauen");

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                todos
        );

        todoListView.setAdapter(adapter);

        todoListView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(
                    TodoListActivity.this,
                    com.example.myapplication.ui.tododetail.TodoDetailActivity.class
            );

            startActivity(intent);
        });
    }
}