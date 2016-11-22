package itesm.mx.commitment_app;

/**
 * Created by nloyola on 6/11/16.
 */
public class Commitment {

    public String name;
    public String description;
    public String id;

    public Commitment() {

    }

    public Commitment(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

}
