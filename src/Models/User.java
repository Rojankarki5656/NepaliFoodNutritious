package Models;
public class User {
    private int id;
    private String name;
    private String actor;

    public User(int id, String name, String actor) {
        this.id = id;
        this.name = name;
        this.actor = actor;
    }

	public int getId() { return id; }
    public String getName() { return name; }
    public String getActor() {return actor;}

}
