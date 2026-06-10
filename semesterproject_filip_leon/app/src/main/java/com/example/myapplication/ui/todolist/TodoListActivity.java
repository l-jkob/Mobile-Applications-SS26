package com.example.myapplication.ui.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import android.widget.Toast;
import com.example.myapplication.R;
import com.example.myapplication.Todo;
import com.example.myapplication.db.LocalTodoAccessor;
import com.example.myapplication.db.SyncEngine;
import com.example.myapplication.ui.tododetail.TodoDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TodoListActivity extends Activity {

    private static final int REQUEST_EDIT_TODO = 1;
    private static final int REQUEST_ADD_TODO = 2;

    private ListView todoListView;
    private Button addTodoButton;
    private Button sortByDateButton;
    private Button favoritesFirstButton;

    private ArrayList<Todo> todos;
    private TodoAdapter adapter;

    private int nextTodoId = 4;

    private LocalTodoAccessor localAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        todoListView = findViewById(R.id.todoListView);
        addTodoButton = findViewById(R.id.buttonAddTodo);
        sortByDateButton = findViewById(R.id.buttonSortByDate);
        favoritesFirstButton = findViewById(R.id.buttonFavoritesFirst);

        //createDummyTodos();

        localAccessor = new LocalTodoAccessor(this);
        todos = new ArrayList<>(localAccessor.readAllTodos());

        // Start the Synchronization Engine
        SyncEngine syncEngine = new SyncEngine(this);

        // We pass in the callback to handle what happens when the background thread finishes
        syncEngine.synchronize(new SyncEngine.SyncCallback() {
            @Override
            public void onSyncComplete() {
                Toast.makeText(TodoListActivity.this, "Sync Successful!", Toast.LENGTH_SHORT).show();
                // Refresh the screen with the new database items
                todos.clear();
                todos.addAll(localAccessor.readAllTodos());
                if (adapter != null) adapter.notifyDataSetChanged();
            }

            @Override
            public void onOfflineMode() {
                Toast.makeText(TodoListActivity.this, "Offline Mode", Toast.LENGTH_LONG).show();
                // Still load the local database items even if offline
                todos.clear();
                todos.addAll(localAccessor.readAllTodos());
                if (adapter != null) adapter.notifyDataSetChanged();
            }
        });

        adapter = new TodoAdapter(this, todos);
        todoListView.setAdapter(adapter);

        addTodoButton.setOnClickListener(v -> openDetailForNewTodo());

        sortByDateButton.setOnClickListener(v -> {
            sortByDateThenImportance();
            adapter.notifyDataSetChanged();
        });

        favoritesFirstButton.setOnClickListener(v -> {
            sortByImportanceThenDate();
            adapter.notifyDataSetChanged();
        });

        todoListView.setOnItemClickListener(
                (parent, view, position, id) -> openDetailForExistingTodo(position)
        );
    }

    private void createDummyTodos() {
        todos = new ArrayList<>();
        todos.add(new Todo(1, "MAaS-Projekt abgeben", "Semesterprojekt finalisieren", false, true, "06/04/2026", "1:45 PM"));
        todos.add(new Todo(2, "UML-Diagramm erstellen", "Klassendiagramm für die Abgabe erstellen", false, false, "06/05/2026", "10:00 AM"));
        todos.add(new Todo(3, "Todo-Detailansicht bauen", "DatePicker, TimePicker und Checkboxen einbauen", true, false, "06/06/2026", "3:30 PM"));
    }

    private void openDetailForNewTodo() {
        Intent intent = new Intent(
                TodoListActivity.this,
                TodoDetailActivity.class
        );

        intent.putExtra(TodoDetailActivity.EXTRA_POSITION, -1);
        intent.putExtra(TodoDetailActivity.EXTRA_NAME, "");
        intent.putExtra(TodoDetailActivity.EXTRA_DESCRIPTION, "");
        intent.putExtra(TodoDetailActivity.EXTRA_DUE_DATE, "");
        intent.putExtra(TodoDetailActivity.EXTRA_DUE_TIME, "");
        intent.putExtra(TodoDetailActivity.EXTRA_DONE, false);
        intent.putExtra(TodoDetailActivity.EXTRA_FAVORITE, false);
        intent.putStringArrayListExtra(
                TodoDetailActivity.EXTRA_LINKED_CONTACTS,
                new ArrayList<>()
        );

        startActivityForResult(intent, REQUEST_ADD_TODO);
    }

    private void openDetailForExistingTodo(int position) {
        Todo selectedTodo = todos.get(position);

        Intent intent = new Intent(
                TodoListActivity.this,
                TodoDetailActivity.class
        );

        intent.putExtra(TodoDetailActivity.EXTRA_POSITION, position);
        intent.putExtra(TodoDetailActivity.EXTRA_NAME, selectedTodo.getName());
        intent.putExtra(TodoDetailActivity.EXTRA_DESCRIPTION, selectedTodo.getDescription());
        intent.putExtra(TodoDetailActivity.EXTRA_DUE_DATE, selectedTodo.getDueDate());
        intent.putExtra(TodoDetailActivity.EXTRA_DUE_TIME, selectedTodo.getDueTime());
        intent.putExtra(TodoDetailActivity.EXTRA_DONE, selectedTodo.isDone());
        intent.putExtra(TodoDetailActivity.EXTRA_FAVORITE, selectedTodo.isFavorite());
        intent.putStringArrayListExtra(
                TodoDetailActivity.EXTRA_LINKED_CONTACTS,
                selectedTodo.getLinkedContacts()
        );

        startActivityForResult(intent, REQUEST_EDIT_TODO);
    }

    private void sortByDateThenImportance() {
        Collections.sort(todos, new Comparator<Todo>() {
            @Override
            public int compare(Todo firstTodo, Todo secondTodo) {
                int doneComparison =
                        Boolean.compare(firstTodo.isDone(), secondTodo.isDone());

                if (doneComparison != 0) {
                    return doneComparison;
                }

                int dateComparison =
                        getDateTimeValue(firstTodo).compareTo(getDateTimeValue(secondTodo));

                if (dateComparison != 0) {
                    return dateComparison;
                }

                return Boolean.compare(secondTodo.isFavorite(), firstTodo.isFavorite());
            }
        });
    }

    private void sortByImportanceThenDate() {
        Collections.sort(todos, new Comparator<Todo>() {
            @Override
            public int compare(Todo firstTodo, Todo secondTodo) {
                int doneComparison =
                        Boolean.compare(firstTodo.isDone(), secondTodo.isDone());

                if (doneComparison != 0) {
                    return doneComparison;
                }

                int favoriteComparison =
                        Boolean.compare(secondTodo.isFavorite(), firstTodo.isFavorite());

                if (favoriteComparison != 0) {
                    return favoriteComparison;
                }

                return getDateTimeValue(firstTodo).compareTo(getDateTimeValue(secondTodo));
            }
        });
    }

    private String getDateTimeValue(Todo todo) {
        String date = todo.getDueDate();
        String time = todo.getDueTime();

        if (date == null) {
            date = "";
        }

        if (time == null) {
            time = "";
        }

        return date + " " + time;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            int position = data.getIntExtra(TodoDetailActivity.EXTRA_POSITION, -1);
            boolean shouldDelete =
                    data.getBooleanExtra(TodoDetailActivity.EXTRA_DELETE, false);

            if (requestCode == REQUEST_ADD_TODO) {
                addNewTodo(data, shouldDelete);
            }

            if (requestCode == REQUEST_EDIT_TODO) {
                updateExistingTodo(data, position, shouldDelete);
            }

            adapter.notifyDataSetChanged();
        }
    }

    private void addNewTodo(Intent data, boolean shouldDelete) {
        if (shouldDelete) {
            return;
        }

        Todo newTodo = new Todo(
                0, // ID 0 is fine, SQLite will auto-generate a real ID for us!
                data.getStringExtra(TodoDetailActivity.EXTRA_NAME),
                data.getStringExtra(TodoDetailActivity.EXTRA_DESCRIPTION),
                data.getBooleanExtra(TodoDetailActivity.EXTRA_DONE, false),
                data.getBooleanExtra(TodoDetailActivity.EXTRA_FAVORITE, false),
                data.getStringExtra(TodoDetailActivity.EXTRA_DUE_DATE),
                data.getStringExtra(TodoDetailActivity.EXTRA_DUE_TIME)
        );

        Todo savedTodo = localAccessor.createTodo(newTodo);

        todos.add(savedTodo);

        pushChangesToServer();
    }

    private void updateExistingTodo(Intent data, int position, boolean shouldDelete) {
        if (position < 0 || position >= todos.size()) {
            return;
        }

        Todo todo = todos.get(position);

        if (shouldDelete) {
            localAccessor.deleteTodo(todo.getId());
            todos.remove(position);
            pushChangesToServer();
            return;
        }

        todo.setName(data.getStringExtra(TodoDetailActivity.EXTRA_NAME));
        todo.setDescription(data.getStringExtra(TodoDetailActivity.EXTRA_DESCRIPTION));
        todo.setDueDate(data.getStringExtra(TodoDetailActivity.EXTRA_DUE_DATE));
        todo.setDueTime(data.getStringExtra(TodoDetailActivity.EXTRA_DUE_TIME));
        todo.setDone(data.getBooleanExtra(TodoDetailActivity.EXTRA_DONE, false));
        todo.setFavorite(data.getBooleanExtra(TodoDetailActivity.EXTRA_FAVORITE, false));


        localAccessor.updateTodo(todo);
        pushChangesToServer();
    }

    private void pushChangesToServer() {
        SyncEngine syncEngine = new SyncEngine(this);
        syncEngine.synchronize(new SyncEngine.SyncCallback() {
            @Override
            public void onSyncComplete() {
                // Silently finishes the push in the background!
            }

            @Override
            public void onOfflineMode() {
                // If offline, the local DB still has it safely saved.
            }
        });
    }
}