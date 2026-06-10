package de.thb.fbi.msr.maus.einkaufsliste.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Todo implements Serializable {

	private long id;

	private static final long serialVersionUID = -7481912314472891511L;
	private String name;
	private String description;
	private boolean done;
	private boolean favorite;
	private String dueDate;
	private String dueTime;
	private ArrayList<String> linkedContacts;

	public Todo() {};
	public Todo(long id, String name, String description, boolean done, boolean favorite, String dueDate, String dueTime) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.done = done;
		this.favorite = favorite;
		this.dueDate = dueDate;
		this.dueTime = dueTime;
		this.linkedContacts = new ArrayList<>();
	}

	public long getId() { return id; }


	public String getName() { return name; }

	public String getDescription() { return description; }

	public boolean isDone() { return done; }

	public boolean isFavorite() { return favorite; }

	public String getDueDate() { return dueDate; }

	public String getDueTime() { return dueTime; }

	public ArrayList<String> getLinkedContacts() { return linkedContacts; }

	public void setId(long id) { this.id = id; }

	public void setName(String name) { this.name = name; }

	public void setDescription(String description) { this.description = description; }

	public void setDone(boolean done) { this.done = done; }

	public void setFavorite(boolean favorite) { this.favorite = favorite; }

	public void setDueDate(String dueDate) { this.dueDate = dueDate; }

	public void setDueTime(String dueTime) { this.dueTime = dueTime; }

	public void setLinkedContacts(ArrayList<String> linkedContacts) {
		this.linkedContacts = linkedContacts;
	}

	public Todo updateFrom(Todo item){
		this.setName(item.getName());
		this.setDescription(item.getDescription());
		this.setDone(item.isDone());
		this.setFavorite(item.isFavorite());
		this.setDueDate(item.getDueDate());
		this.setDueTime(item.getDueTime());
		return this;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Todo)) {
			return false;
		} else {
			return ((Todo) other).getId() == this.getId();
		}
	}

	@Override
	public String toString() {
		return "{Todo " + this.getId() + " " + this.getName() + "}";
	}
}