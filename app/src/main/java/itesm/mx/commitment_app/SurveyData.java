package itesm.mx.commitment_app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by luis on 22/11/16.
 */

public class SurveyData {
    public static HashMap<String, List<String>> getData() {
    HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

    List<String> commitment1 = new ArrayList<String>();
    commitment1.add("Member 2");
    commitment1.add("Member 3");
    commitment1.add("Member 4");


    List<String> commitment2 = new ArrayList<String>();
    commitment2.add("Member 2");
    commitment2.add("Member 3");
    commitment2.add("Member 4");


    expandableListDetail.put("Commitment 1: Being punctual", commitment1);
    expandableListDetail.put("Commitment 2: Quality of working", commitment2);
    return expandableListDetail;
}
}