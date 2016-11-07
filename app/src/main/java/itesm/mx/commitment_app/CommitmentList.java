package itesm.mx.commitment_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nloyola on 6/11/16.
 */
public class CommitmentList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    private final ArrayList<String> descriptions;
    public CommitmentList(Activity context, ArrayList<String> names, ArrayList<String> descriptions) {
        super(context, R.layout.commitment_list_adapter, names);
        this.context = context;
        this.names = names;
        this.descriptions = descriptions;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.commitment_list_adapter, null, true);
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        nameView.setText(names.get(position));

        TextView descriptionView = (TextView) rowView.findViewById(R.id.description);
        descriptionView.setText(descriptions.get(position));

        return rowView;
    }

}
