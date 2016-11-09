package itesm.mx.commitment_app;

import android.app.Activity;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by luis on 6/11/16.
 */

class NotificationList extends ArrayAdapter<String> {

    private final Activity context;
    private final Integer[] imageId;
    private final String[] title;
    private final String[] text;

    NotificationList (Activity context, Integer[] imageId, String[] title, String[] text) {
        super(context, R.layout.list_adapter, title);
        this.context = context;
        this.imageId = imageId;
        this.title = title;
        this.text = text;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.list_adapter, null, true);
        TextView titleView = (TextView) rowView.findViewById(R.id.notification_title);
        titleView.setText(title[position]);

        TextView txtView = (TextView) rowView.findViewById(R.id.notification_txt);
        txtView.setText(text[position]);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.notification_img);
        imageView.setImageResource(imageId[position]);

        return rowView;
    }
}