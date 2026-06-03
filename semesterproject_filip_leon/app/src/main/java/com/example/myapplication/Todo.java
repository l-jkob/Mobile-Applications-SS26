public class Todo {

    private int id;
    private String name;
    private String description;
    private boolean done;
    private boolean favorite;
    private String dueDate;
    private String dueTime;

    public Todo(int id, String name, String description, boolean done, boolean favorite, String dueDate, String dueTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.done = done;
        this.favorite = favorite;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return done;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }
}