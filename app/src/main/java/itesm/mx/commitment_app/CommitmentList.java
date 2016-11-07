package itesm.mx.commitment_app;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by nloyola on 6/11/16.
 */
public class CommitmentList extends ArrayAdapter<String> {

    private final Activity context;
    private final String project;
    private final ArrayList<String> ids;
    private final ArrayList<String> names;
    private final ArrayList<String> descriptions;
    DatabaseReference mDatabase;

    public CommitmentList(Activity context, ArrayList<String> ids, ArrayList<String> names, ArrayList<String> descriptions, String project) {
        super(context, R.layout.commitment_list_adapter, names);
        this.context = context;
        this.ids = ids;
        this.names = names;
        this.descriptions = descriptions;
        this.project = project;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(int position, final View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.commitment_list_adapter, null, true);

        TextView idView = (TextView) rowView.findViewById(R.id.id);
        idView.setText(ids.get(position));

        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        nameView.setText(names.get(position));

        TextView descriptionView = (TextView) rowView.findViewById(R.id.description);
        descriptionView.setText(descriptions.get(position));

        Button editButton = (Button) rowView.findViewById(R.id.edit_button);
        Button deleteButton = (Button) rowView.findViewById(R.id.delete_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commitment_intent = new Intent(context, EditCommitment.class);
                commitment_intent.putExtra("id", v.findViewById(R.id.id));
                commitment_intent.putExtra("name", v.findViewById(R.id.name));
                commitment_intent.putExtra("description", v.findViewById(R.id.description));
                context.startActivity(commitment_intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = v.findViewById(R.id.id);
                mDatabase.child("projects").child(project).child("commitments").child(id).removeValue();
            }
        });

        return rowView;
    }

}
