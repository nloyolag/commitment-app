package itesm.mx.commitment_app;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by luis on 8/11/16.
 */

class DashboardList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> title;
    private final ArrayList<Integer> progress;

    DashboardList (Activity context, ArrayList<String> title, ArrayList<Integer> progress) {
        super(context, R.layout.list_adapter, title);
        this.context = context;
        this.title = title;
        this.progress = progress;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.dashboard_list_adapter, null, true);
        TextView titleView = (TextView) rowView.findViewById(R.id.progress_title);
        titleView.setText(title.get(position));

        ProgressBar bar = (ProgressBar) rowView.findViewById(R.id.progressBar);
        bar.setProgress(progress.get(position));

        TextView progressValue = (TextView) rowView.findViewById(R.id.progress_value);
        progressValue.setText(String.valueOf(progress.get(position)));


        return rowView;
    }
} 
