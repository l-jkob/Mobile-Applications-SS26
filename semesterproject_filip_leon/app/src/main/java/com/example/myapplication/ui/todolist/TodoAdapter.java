package com.example.myapplication.ui.todolist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.Todo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TodoAdapter extends ArrayAdapter<Todo> {

    public TodoAdapter(Context context, ArrayList<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.todo_list_item,
                    parent,
                    false
            );
        }

        TextView titleTextView = convertView.findViewById(R.id.textTodoTitle);
        TextView dateTextView = convertView.findViewById(R.id.textTodoDate);
        TextView timeTextView = convertView.findViewById(R.id.textTodoTime);

        CheckBox doneCheckBox = convertView.findViewById(R.id.checkTodoDone);
        CheckBox favoriteCheckBox = convertView.findViewById(R.id.checkTodoFavorite);

        doneCheckBox.setOnCheckedChangeListener(null);
        favoriteCheckBox.setOnCheckedChangeListener(null);

        doneCheckBox.setChecked(todo.isDone());
        favoriteCheckBox.setChecked(todo.isFavorite());

        updateTodoView(todo, titleTextView, dateTextView, timeTextView);

        doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setDone(isChecked);
            notifyDataSetChanged();
        });

        favoriteCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setFavorite(isChecked);
            notifyDataSetChanged();
        });

        return convertView;
    }

    private void updateTodoView(
            Todo todo,
            TextView titleTextView,
            TextView dateTextView,
            TextView timeTextView
    ) {
        if (todo.isFavorite()) {
            titleTextView.setTypeface(null, Typeface.BOLD);
            titleTextView.setText("★ " + todo.getName());
        } else {
            titleTextView.setTypeface(null, Typeface.NORMAL);
            titleTextView.setText(todo.getName());
        }

        titleTextView.setPaintFlags(
                titleTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
        );

        titleTextView.setTextColor(Color.BLACK);
        dateTextView.setTextColor(Color.LTGRAY);
        timeTextView.setTextColor(Color.LTGRAY);

        if (todo.isDone()) {
            titleTextView.setPaintFlags(
                    titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );
            titleTextView.setTextColor(Color.GRAY);
        } else if (isOverdue(todo)) {
            titleTextView.setTextColor(Color.RED);
            dateTextView.setTextColor(Color.RED);
            timeTextView.setTextColor(Color.RED);
        }

        dateTextView.setText(todo.getDueDate());
        timeTextView.setText(todo.getDueTime());
    }

    private boolean isOverdue(Todo todo) {
        if (todo.getDueDate() == null || todo.getDueDate().isEmpty()) {
            return false;
        }

        if (todo.getDueTime() == null || todo.getDueTime().isEmpty()) {
            return false;
        }

        try {
            SimpleDateFormat dateTimeFormat =
                    new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.US);

            Date dueDateTime = dateTimeFormat.parse(
                    todo.getDueDate() + " " + todo.getDueTime()
            );

            Date now = new Date();

            return dueDateTime.before(now);

        } catch (ParseException e) {
            return false;
        }
    }
}