package com.example.myapplication.db;

import com.example.myapplication.Todo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteTodoAccessor implements TodoCRUDAccessor {

    // 10.0.2.2 is the Android Emulator's address for your computer's localhost
    // IMPORTANT: Make sure this matches the Application context you set in WildFly!
    private static final String BASE_URL = "http://10.0.2.2:8080/backend-1.0-SNAPSHOT/rest/todos";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client;
    private Gson gson;

    public RemoteTodoAccessor() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    @Override
    public List<Todo> readAllTodos() {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                // Tell Gson we expect a List of Todo objects
                Type listType = new TypeToken<ArrayList<Todo>>(){}.getType();
                return gson.fromJson(jsonResponse, listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Return null if server is unreachable
    }

    @Override
    public Todo createTodo(Todo item) {
        String jsonStr = gson.toJson(item);
        RequestBody body = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return gson.fromJson(jsonResponse, Todo.class); // Server returns the item with its new ID
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deleteTodo(long id) {
        Request request = new Request.Builder()
                .url(BASE_URL + "/" + id)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Todo updateTodo(Todo item) {
        String jsonStr = gson.toJson(item);
        RequestBody body = RequestBody.create(jsonStr, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                return gson.fromJson(jsonResponse, Todo.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Todo readTodo(long id) {
        // The professor's REST interface doesn't actually define a GET for a single item,
        // so we can either return null or fetch all and filter. For this project, returning null is fine
        // because we usually read single items from the local SQLite DB anyway.
        return null;
    }
}