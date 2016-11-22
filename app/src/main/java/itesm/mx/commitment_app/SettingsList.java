package itesm.mx.commitment_app;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by luis on 18/11/16.
 */

class SettingsList extends ArrayAdapter<String> {
    private final String [] items;
    private final Activity context;
    private final int [] colors;

    SettingsList(Activity context, String [] items, int [] colors) {
        super(context, R.layout.settings_list_adapter, items);
        this.context = context;
        this.items = items;
        this.colors = colors;
    }

    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView= inflater.inflate(R.layout.settings_list_adapter, null, true);
        TextView itemView = (TextView) rowView.findViewById(R.id.settings_text_item);
        itemView.setText(items[position]);
        if (colors[position] != 0)
            itemView.setTextColor(colors[position]);
        itemView.setTag(position);


        return rowView;
    }
}
