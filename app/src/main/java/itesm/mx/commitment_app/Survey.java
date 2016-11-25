package itesm.mx.commitment_app;

/**
 * Created by nloyola on 24/11/16.
 */
public class Survey {

    public String from;
    public String to;
    public String commitment;
    public int rating;
    public String id;

    public Survey() {

    }

    public Survey(String from, String to, String commitment, int rating) {
        this.from = from;
        this.to = to;
        this.commitment = commitment;
        this.rating = rating;
    }

    public Survey(String from, String to, String commitment, int rating, String id) {
        this.from = from;
        this.to = to;
        this.commitment = commitment;
        this.rating = rating;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
