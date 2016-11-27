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
    private ArrayList<String> users;
    private HashMap<String, Commitment> commitments;
    private HashMap<String, Survey> surveys;
    private HashMap<String, Integer> eval_no;
    private HashMap<String, Integer> aggregate;

    ListView listView;
    private DatabaseReference mProjectDatabase;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mDatabase;
    private MyApplication context;
    private String project;
    PieChart pieChart;

    public TeamDashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        commitments = new HashMap<String, Commitment>();
        surveys = new HashMap<String, Survey>();
        context = (MyApplication) getActivity().getApplicationContext();
        project = context.getProject();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ids = new ArrayList<String>();
        names = new ArrayList<String>();
        scores = new ArrayList<Integer>();
        eval_no = new HashMap<String, Integer>();
        aggregate = new HashMap<String, Integer>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_dashboard, container, false);
        listView = (ListView)view.findViewById(R.id.dashboard_list);
        if (project==null)
            onCreate(savedInstanceState);
        if(project!=null) {
            mDatabase.child("projects").child(project).child("commitments").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    Log.d("NEW COMMITMENT", commitment.id);
                    commitments.put(commitment.id, commitment);
                    Log.d("COMMITMENT SIZE", Integer.toString(commitments.size()));
                    populateLists();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    commitments.put(commitment.id, commitment);
                    populateLists();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    commitments.remove(commitment.id);
                    populateLists();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        //change with actual progress
        int teamProgress = 90;
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
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
        dataSet.setColors(Color.rgb(255,64,129),Color.LTGRAY);
        dataSet.setValueTextSize(0);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        Description description = pieChart.getDescription();
        description.setEnabled(false);
    }

    public void populateLists() {
        final ArrayList<String> names_inner = new ArrayList<String>();
        final ArrayList<Integer> scores_inner = new ArrayList<Integer>();
        final ArrayList<Integer> counts = new ArrayList<Integer>();

        mDatabase.child("projects").child(project).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users = (ArrayList<String>) dataSnapshot.getValue();

                mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, HashMap<String, String>> users_query = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                        for (String user : users) {
                            names_inner.add(users_query.get(user).get("name"));
                            scores_inner.add(0);
                            counts.add(0);
                        }

                        for (Commitment commitment : commitments.values()) {
                            for (Survey survey : commitment.surveys.values()) {
                                if (survey.rating >= 0) {
                                    int index = users.indexOf(survey.to);
                                    scores_inner.set(index, scores_inner.get(index) + survey.rating);
                                    counts.set(index, counts.get(index) + 1);
                                }
                            }
                        }

                        for (int i = 0; i < scores_inner.size(); i++) {
                            if (counts.get(i) == 0) {
                                scores_inner.set(i, 0);
                            } else {
                                scores_inner.set(i, (int) scores_inner.get(i) / counts.get(i) * 20);
                            }
                        }

                        drawPieChart(pieChart, getOverallScore(scores_inner));
                        DashboardList adapter = new DashboardList(getActivity(), names_inner, scores_inner);
                        listView.setAdapter(adapter);
                        listView.invalidateViews();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int getOverallScore(ArrayList<Integer> scores) {
        int total = 0;
        for (int score : scores) {
            total += score;
        }
        total = (int) total / scores.size();
        return total;
    }
}