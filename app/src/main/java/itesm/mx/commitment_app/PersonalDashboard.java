package itesm.mx.commitment_app;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDashboard extends Fragment {
    ListView listView;
    String [] surveys = {"Survey 1", "Survey 2", "Survey 3"};
    int [] surveyProgress = {85, 90, 95};



    public PersonalDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_dashboard, container, false);


        //change with actual progress
        int myProgress = 90;
        PieChart pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        drawPieChart(pieChart, myProgress);




        DashboardList adapter = new DashboardList(getActivity(), surveys, surveyProgress );
        listView = (ListView)view.findViewById(R.id.dashboard_list);
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
}
