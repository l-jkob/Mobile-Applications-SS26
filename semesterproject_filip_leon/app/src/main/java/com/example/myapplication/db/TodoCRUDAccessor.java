package com.example.myapplication.db;

import com.example.myapplication.Todo;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/todos")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface TodoCRUDAccessor {

    @GET
    List<Todo> readAllTodos();

    @POST
    Todo createTodo(Todo item);

    @DELETE
    @Path("/{todoId}")
    boolean deleteTodo(@PathParam("todoId") long todoId);

    @PUT
    Todo updateTodo(Todo item);

    Todo readTodo(long id);
}