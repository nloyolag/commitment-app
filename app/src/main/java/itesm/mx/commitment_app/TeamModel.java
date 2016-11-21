package itesm.mx.commitment_app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nloyola on 12/11/16.
 */
public class TeamModel {

    public String name;
    public String id;
    public List<String> users;

    public TeamModel() {

    }

    public TeamModel(String name, String id, List<String> users) {
        this.name = name;
        this.id = id;
        this.users = users;
    }

    public void setId(String id) {
        this.id = id;
    }

}
