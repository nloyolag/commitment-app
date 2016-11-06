package itesm.mx.commitment_app;

/**
 * Created by nloyola on 29/10/16.
 */
public class User {

    public String id;
    public String name;
    public String username;
    public String email;

    public User() {

    }

    public User(String name, String username, String email) {
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

}
