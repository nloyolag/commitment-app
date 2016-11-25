package itesm.mx.commitment_app;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


/**
 * A simple {@link Fragment} subclass.
 */

public class TeamDashboard extends Fragment {

    private ArrayList<String> ids;
    private ArrayList<String> names;
    private ArrayList<Integer> scores;
    private HashMap<String, User> users;
    private HashMap<String, Survey> surveys;
    private HashMap<String, Integer> eval_no;
    private HashMap<String, Integer> aggregate;

    ListView listView;
    private DatabaseReference mProjectDatabase;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mDatabase;
    private MyApplication context;
    private String project;

    private Boolean loaded_users;

    public TeamDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        users = new HashMap<String, User>();
        surveys = new HashMap<String, Survey>();
        context = (MyApplication) getActivity().getApplicationContext();
        project = context.getProject();

        loaded_users = false;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mProjectDatabase = mDatabase.child("projects").child(project).child("commitments");
        mUserDatabase = mDatabase.child("users");
        ids = new ArrayList<String>();
        names = new ArrayList<String>();
        scores = new ArrayList<Integer>();
        eval_no = new HashMap<String, Integer>();
        aggregate = new HashMap<String, Integer>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_dashboard, container, false);
        listView = (ListView)view.findViewById(R.id.dashboard_list);

        /*mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ENTRA", "ENTRA");
                HashMap<String, User> users_query = (HashMap<String, User>) dataSnapshot.getValue();
                Iterator it = surveys.entrySet().iterator();
                Log.d("YEAH", users_query.getClass().getName());
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    Log.d("APP", pair.getKey().toString());
                    it.remove();
                }
                for (User user: users_query.values()) {
                    if (!users.containsKey(user.id)) {
                        users.put(user.id, user);
                    }
                }
                loaded_users = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        /*mProjectDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Commitment commitment = dataSnapshot.getValue(Survey.class);
                Log.d("NEW SURVEY", survey.id);
                surveys.put(survey.id, survey);
                Log.d("SURVEY SIZE", Integer.toString(surveys.size()));
                populateLists();
                DashboardList adapter = new DashboardList(getActivity(), names, scores);
                listView.setAdapter(adapter);
                listView.invalidateViews();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Survey survey = dataSnapshot.getValue(Survey.class);
                surveys.put(survey.id, survey);
                populateLists();
                DashboardList adapter = new DashboardList(getActivity(), names, scores);
                listView.setAdapter(adapter);
                listView.invalidateViews();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Survey survey = dataSnapshot.getValue(Survey.class);
                surveys.remove(survey.id);
                populateLists();
                DashboardList adapter = new DashboardList(getActivity(), names, scores);
                listView.setAdapter(adapter);
                listView.invalidateViews();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        //change with actual progress
        int teamProgress = 90;
        PieChart pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        drawPieChart(pieChart, teamProgress);

        DashboardList adapter = new DashboardList(getActivity(), names, scores);
        listView.setAdapter(adapter);

        //Must be at the end!
        return view;
    }

    public void drawPieChart (PieChart pieChart, int myProgress) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(myProgress, 0));
        entries.add(new PieEntry(100-myProgress, 0));
        PieDataSet dataSet = new PieDataSet(entries, "PieChart");

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.setCenterText(""+myProgress);
        pieChart.setCenterTextSize(24);
        dataSet.setColors(getResources().getColor(R.color.colorAccent),Color.LTGRAY);
        dataSet.setValueTextSize(0);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        Description description = pieChart.getDescription();
        description.setEnabled(false);
    }

    public void populateLists() {
        ids = new ArrayList<String>();
        names = new ArrayList<String>();
        scores = new ArrayList<Integer>();
        eval_no = new HashMap<String, Integer>();
        aggregate = new HashMap<String, Integer>();
        Iterator it = surveys.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Survey survey = (Survey) pair.getValue();
            //User user = users.get(survey.to);
            if (!ids.contains(survey.to)) {
                ids.add(survey.to);
                //names.add(user.name);
                scores.add(0);
                eval_no.put(survey.to, 0);
                aggregate.put(survey.to, 0);
            }
            int location = ids.indexOf(survey.to);
            aggregate.put(survey.to, aggregate.get(survey.to) + survey.rating);
            eval_no.put(survey.to, eval_no.get(survey.to) + 1);
            scores.set(location, (int) (20 * aggregate.get(survey.to) / eval_no.get(survey.to)));
            it.remove();
        }
    }
}