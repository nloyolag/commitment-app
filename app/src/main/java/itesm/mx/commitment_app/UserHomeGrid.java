package itesm.mx.commitment_app;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by luis on 10/11/16.
 */;
class UserHomeGrid extends ArrayAdapter<String> {
    private final Activity context;
    private final String [] projectname;
    private int [] imageId;

    UserHomeGrid(Activity context, String [] projectname, int [] imageId) {
        super(context, R.layout.user_home_grid_adapter, projectname);
        this.context = context;
        this.projectname = projectname;
        this.imageId = imageId;
    }

    public View getView(int position, View view, @NonNull ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.user_home_grid_adapter, null, true);

        TextView txtView = (TextView) rowView.findViewById(R.id.textView_user_home);
        txtView.setText(projectname[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView_user_home);
        imageView.setImageResource(imageId[position]);


        return rowView;
    }
}
