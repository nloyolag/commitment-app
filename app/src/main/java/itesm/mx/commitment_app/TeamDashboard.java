package itesm.mx.commitment_app;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class TeamDashboard extends Fragment {
    String [] members = {"Member 2", "Member 3", "Member 4"};
    int [] memberProgress = {90, 80, 95};

    private ArrayList<String> names;
    private ArrayList<Integer> scores;
    private ArrayList<String> ids;

    ListView listView;
    private DatabaseReference mDatabase;
    private MyApplication context;
    private String project;

    public TeamDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = (MyApplication) getActivity().getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        project = context.getProject();

        mDatabase.child("projects").child(project).child("evaluations").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_dashboard, container, false);

        //change with actual progress
        int teamProgress = 90;
        PieChart pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        drawPieChart(pieChart, teamProgress);


        DashboardList adapter = new DashboardList(getActivity(), members, memberProgress );
        listView = (ListView)view.findViewById(R.id.dashboard_list);
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
}