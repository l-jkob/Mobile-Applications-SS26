public class TodoContact {

    private int todoId;
    private int contactId;

    public TodoContact(int todoId, int contactId) {
        this.todoId = todoId;
        this.contactId = contactId;
    }

    public int getTodoId() {
        return todoId;
    }

    public int getContactId() {
        return contactId;
    }
}