package com.example.myapplication.db;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.myapplication.Todo;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncEngine {

    private LocalTodoAccessor localAccessor;
    private RemoteTodoAccessor remoteAccessor;

    // A callback interface so the Engine can talk to the UI when it finishes
    public interface SyncCallback {
        void onSyncComplete();
        void onOfflineMode();
    }

    public SyncEngine(Context context) {
        this.localAccessor = new LocalTodoAccessor(context);
        this.remoteAccessor = new RemoteTodoAccessor();
    }

    public void synchronize(SyncCallback callback) {
        // 1. Create a background thread so we don't freeze the screen
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // 2. Create a handler connected to the main screen to send messages back
        Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // --- THIS CODE RUNS IN THE BACKGROUND ---

            // Try to fetch remote data first to test the connection
            List<Todo> remoteTodos = remoteAccessor.readAllTodos();

            if (remoteTodos == null) {
                // OFFLINE MODE: The server didn't respond (WildFly is off or no internet)
                mainThreadHandler.post(() -> callback.onOfflineMode());
                return; // Stop the sync process and just use local DB
            }

            // Connection successful! Let's check the local database
            List<Todo> localTodos = localAccessor.readAllTodos();

            if (!localTodos.isEmpty()) {
                // RULE 1: Local exists -> Delete all remote, recreate local remotely

                // Delete everything on the server
                for (Todo remoteTodo : remoteTodos) {
                    remoteAccessor.deleteTodo(remoteTodo.getId());
                }

                // Upload all local items to the server
                for (Todo localTodo : localTodos) {
                    // The server will assign it a new ID, but we keep the local ID intact
                    remoteAccessor.createTodo(localTodo);
                }

            } else {
                // RULE 2: No local exists -> Download remote and save to local

                for (Todo remoteTodo : remoteTodos) {
                    // Save to SQLite
                    localAccessor.createTodo(remoteTodo);
                }
            }

            // Sync is finished! Tell the main screen to refresh the list
            mainThreadHandler.post(() -> callback.onSyncComplete());
        });
    }
}