package itesm.mx.commitment_app;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by luis on 10/11/16.
 */

class UserHomeGrid extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> project_names;
    private final ArrayList<String> project_ids;
    private ArrayList<Integer> image_ids;

    UserHomeGrid(Activity context, ArrayList<String> project_names, ArrayList<String> project_ids, ArrayList<Integer> image_ids) {
        super(context, R.layout.user_home_grid_adapter, project_names);
        this.context = context;
        this.project_names = project_names;
        this.project_ids = project_ids;
        this.image_ids = image_ids;
    }

    public View getView(int position, View view, @NonNull ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.user_home_grid_adapter, null, true);

        TextView txtView = (TextView) rowView.findViewById(R.id.textView_user_home);
        txtView.setText(project_names.get(position));

        TextView idView = (TextView) rowView.findViewById(R.id.textView_id);
        idView.setVisibility(View.GONE);
        idView.setText(project_ids.get(position));

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView_user_home);
        imageView.setImageResource(image_ids.get(position));

        return rowView;
    }
}
