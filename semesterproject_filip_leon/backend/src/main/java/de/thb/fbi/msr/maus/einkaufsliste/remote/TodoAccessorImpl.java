package de.thb.fbi.msr.maus.einkaufsliste.remote;

import de.thb.fbi.msr.maus.einkaufsliste.model.Todo;
import de.thb.fbi.msr.maus.einkaufsliste.model.TodoCRUDAccessor;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/todos")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class TodoAccessorImpl implements TodoCRUDAccessor {

	// The server's in-memory "database"
	private static final List<Todo> sTodoList = new ArrayList<>();
	private static long idCount = 0;

	@Override
	public List<Todo> readAllTodos() {
		return sTodoList;
	}

	@Override
	public Todo createTodo(Todo item) {
		item.setId(idCount++);
		sTodoList.add(item);
		return item;
	}

	@Override
	public boolean deleteTodo(long id) {
		return sTodoList.removeIf(todo -> todo.getId() == id);
	}

	@Override
	public Todo updateTodo(Todo item) {
		for (int i = 0; i < sTodoList.size(); i++) {
			if (sTodoList.get(i).getId() == item.getId()) {
				sTodoList.get(i).updateFrom(item);
				return sTodoList.get(i);
			}
		}
		return null;
	}

	@Override
	public Todo readTodo(long id) {
		for (Todo todo : sTodoList) {
			if (todo.getId() == id) return todo;
		}
		return null;
	}
}