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
import com.google.firebase.auth.FirebaseAuth;
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
public class PersonalDashboard extends Fragment {
    ListView listView;
    ArrayList<Commitment> commitments;
    ArrayList<String> surveys;
    ArrayList<Integer> surveyProgress;
    MyApplication context;
    String project;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth;

    public PersonalDashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        surveys = new ArrayList<String>();
        surveyProgress = new ArrayList<Integer>();
        mAuth = FirebaseAuth.getInstance();
        context = (MyApplication) getActivity().getApplicationContext();
        project = context.getProject();
        commitments = new ArrayList<Commitment>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_dashboard, container, false);
        listView = (ListView)view.findViewById(R.id.dashboard_list);
        final PieChart pieChart = (PieChart) view.findViewById(R.id.pie_chart);

        mDatabase.child("projects").child(project).child("commitments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Commitment commitment = dataSnapshot.getValue(Commitment.class);
                commitments.add(commitment);
                populateLists();
                drawPieChart(pieChart, getOverallScore());
                DashboardList adapter = new DashboardList(getActivity(), surveys, surveyProgress);
                listView.setAdapter(adapter);
                listView.invalidateViews();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Commitment commitment = dataSnapshot.getValue(Commitment.class);
                for (Commitment com : commitments) {
                    if (com.id.equals(commitment.id)) {
                        commitments.remove(com);
                        commitments.add(commitment);
                        break;
                    }
                }
                populateLists();
                drawPieChart(pieChart, getOverallScore());
                DashboardList adapter = new DashboardList(getActivity(), surveys, surveyProgress);
                listView.setAdapter(adapter);
                listView.invalidateViews();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Commitment commitment = dataSnapshot.getValue(Commitment.class);
                commitments.remove(commitment);
                populateLists();
                drawPieChart(pieChart, getOverallScore());
                DashboardList adapter = new DashboardList(getActivity(), surveys, surveyProgress);
                listView.setAdapter(adapter);
                listView.invalidateViews();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DashboardList adapter = new DashboardList(getActivity(), surveys, surveyProgress);
        listView.setAdapter(adapter);
        //Must be at the end!
        return view;
    }


    public void drawPieChart (PieChart pieChart, int myProgress) {
        List <PieEntry> entries = new ArrayList<>();
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
        surveys = new ArrayList<String>();
        surveyProgress = new ArrayList<Integer>();
        for (Commitment commitment : commitments) {
            int count = 0;
            int aggregate = 0;
            for (Survey survey : commitment.surveys.values()) {
                if (survey.rating >= 0 && survey.to.equals(mAuth.getCurrentUser().getUid())) {
                    count++;
                    aggregate += survey.rating;
                }
            }
            if (count == 0) {
                continue;
            }
            int score = (int) (aggregate / count * 20);
            surveyProgress.add(score);
            surveys.add(commitment.name);
        }
    }

    public int getOverallScore() {
        int sum = 0;
        for (int i = 0; i < surveyProgress.size(); i++) {
            sum += surveyProgress.get(i);
        }
        if (surveyProgress.size()==0)
            return 0;
        sum = (int) sum / surveyProgress.size();
        return sum;
    }
}
