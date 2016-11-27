package itesm.mx.commitment_app;

import android.util.Log;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by luis on 22/11/16.
 */

public class SurveyData {

    public static HashMap<String, List<String>> getData(HashMap<String, HashMap<String, Object>> surveys) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        ArrayList<String> ids = new ArrayList<String>(surveys.keySet());
        for (String val : ids) {
            HashMap<String, Object> survey = surveys.get(val);
            ArrayList<String> users = new ArrayList<String>();
            users.add((String)survey.get("to"));
            expandableListDetail.put(val, users);
        }
        return expandableListDetail;
    }

}