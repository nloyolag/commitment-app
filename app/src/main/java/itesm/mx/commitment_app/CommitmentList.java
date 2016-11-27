package itesm.mx.commitment_app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nloyola on 6/11/16.
 */
public class CommitmentList extends ArrayAdapter<String> {

    private final Activity context;
    private final String project;
    private final ArrayList<String> ids;
    private final ArrayList<String> names;
    private final ArrayList<String> descriptions;
    private final ArrayList<String> survey_dates;
    DatabaseReference mDatabase;

    public CommitmentList(Activity context, ArrayList<String> ids, ArrayList<String> names, ArrayList<String> descriptions, ArrayList<String> survey_dates, String project) {
        super(context, R.layout.commitment_list_adapter, names);
        this.context = context;
        this.ids = ids;
        this.names = names;
        this.descriptions = descriptions;
        this.survey_dates = survey_dates;
        this.project = project;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public View getView(final int position, final View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.commitment_list_adapter, parent, false);

        TextView idView = (TextView) rowView.findViewById(R.id.id);
        idView.setVisibility(View.GONE);
        idView.setText(ids.get(position));

        TextView nameView = (TextView) rowView.findViewById(R.id.edit_name);
        nameView.setText(names.get(position));

        TextView descriptionView = (TextView) rowView.findViewById(R.id.description);
        descriptionView.setText(descriptions.get(position));

        ImageButton editButton = (ImageButton) rowView.findViewById(R.id.edit_button);
        ImageButton deleteButton = (ImageButton) rowView.findViewById(R.id.delete_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("projects").child(project).child("commitments").child(ids.get(position)).child("surveys").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, Survey> surveys = (HashMap<String, Survey>) dataSnapshot.getValue();
                            Intent commitment_intent = new Intent(context, EditCommitment.class);
                            commitment_intent.putExtra("id", ids.get(position));
                            commitment_intent.putExtra("name", names.get(position));
                            commitment_intent.putExtra("description", descriptions.get(position));
                            commitment_intent.putExtra("survey_time", survey_dates.get(position));
                            commitment_intent.putExtra("surveys", surveys);
                            context.startActivity(commitment_intent);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogConf = new AlertDialog.Builder(context);
                dialogConf.setTitle("Deleting Commitment");
                dialogConf.setMessage("Are you ure you want to delete this commitment?");
                dialogConf.setIcon(R.drawable.ic_delete);
                dialogConf.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String idString = ids.get(position);
                        mDatabase.child("projects").child(project).child("commitments").child(idString).removeValue();
                    }
                });
                dialogConf.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog myDialog = dialogConf.create();
                myDialog.show();
            }
        });
        return rowView;
    }
}
