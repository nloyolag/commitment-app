package itesm.mx.commitment_app;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nloyola on 6/11/16.
 */
public class Commitment {

    public String name;
    public String description;
    public String id;
    public HashMap<String, Survey> surveys;
    public String survey_time;

    public Commitment() {

    }

    public Commitment(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSurveys(HashMap<String, Survey> surveys) {
        this.surveys = surveys;
    }

    public void setSurveyTime(String survey_time) {
        this.survey_time = survey_time;
    }

}
