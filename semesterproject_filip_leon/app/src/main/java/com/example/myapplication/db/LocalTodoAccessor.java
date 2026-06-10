package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.Todo;

import java.util.ArrayList;
import java.util.List;

public class LocalTodoAccessor implements TodoCRUDAccessor {

    private TodoDbHelper dbHelper;

    public LocalTodoAccessor(Context context) {
        dbHelper = new TodoDbHelper(context);
    }

    // Helper method to convert a database row back into a Todo Java object
    private Todo cursorToTodo(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_NAME));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_DESCRIPTION));
        boolean done = cursor.getInt(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_DONE)) == 1;
        boolean favorite = cursor.getInt(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_FAVORITE)) == 1;
        String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_DUE_DATE));
        String dueTime = cursor.getString(cursor.getColumnIndexOrThrow(TodoDbHelper.COLUMN_DUE_TIME));

        return new Todo(id, name, description, done, favorite, dueDate, dueTime);
    }

    @Override
    public Todo createTodo(Todo item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoDbHelper.COLUMN_NAME, item.getName());
        values.put(TodoDbHelper.COLUMN_DESCRIPTION, item.getDescription());
        values.put(TodoDbHelper.COLUMN_DONE, item.isDone() ? 1 : 0);
        values.put(TodoDbHelper.COLUMN_FAVORITE, item.isFavorite() ? 1 : 0);
        values.put(TodoDbHelper.COLUMN_DUE_DATE, item.getDueDate());
        values.put(TodoDbHelper.COLUMN_DUE_TIME, item.getDueTime());

        long insertId = db.insert(TodoDbHelper.TABLE_TODOS, null, values);
        item.setId(insertId); // Assign the new database ID to the object
        db.close();
        return item;
    }

    @Override
    public Todo readTodo(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TodoDbHelper.TABLE_TODOS,
                null, TodoDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Todo todo = null;
        if (cursor != null && cursor.moveToFirst()) {
            todo = cursorToTodo(cursor);
            cursor.close();
        }
        db.close();
        return todo;
    }

    @Override
    public List<Todo> readAllTodos() {
        List<Todo> todos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TodoDbHelper.TABLE_TODOS,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                todos.add(cursorToTodo(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return todos;
    }

    @Override
    public Todo updateTodo(Todo item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TodoDbHelper.COLUMN_NAME, item.getName());
        values.put(TodoDbHelper.COLUMN_DESCRIPTION, item.getDescription());
        values.put(TodoDbHelper.COLUMN_DONE, item.isDone() ? 1 : 0);
        values.put(TodoDbHelper.COLUMN_FAVORITE, item.isFavorite() ? 1 : 0);
        values.put(TodoDbHelper.COLUMN_DUE_DATE, item.getDueDate());
        values.put(TodoDbHelper.COLUMN_DUE_TIME, item.getDueTime());

        int rowsAffected = db.update(TodoDbHelper.TABLE_TODOS, values,
                TodoDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();

        // Return the item if the database updated successfully, otherwise return null
        if (rowsAffected > 0) {
            return item;
        } else {
            return null;
        }
    }

    @Override
    public boolean deleteTodo(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(TodoDbHelper.TABLE_TODOS,
                TodoDbHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }
}