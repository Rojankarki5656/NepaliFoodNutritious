package Models;
public class User {
    private int id;
    private String name;
    private String actor;
    private String email;
    private String user_preference;

    public User(int id, String name, String actor,String email, String user_preference) {
        this.id = id;
        this.name = name;
        this.actor = actor;
        this.email = email;
        this.user_preference = user_preference;
    }

	public int getId() { return id; }
    public String getName() { return name; }
    public String getActor() {return actor;}
    public String getUserPreference() {return user_preference;}
    public String getEmail() {return email;}

}
