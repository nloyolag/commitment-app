package itesm.mx.commitment_app;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by nloyola on 22/11/16.
 */
public class TeamList extends ArrayAdapter<String> {

    private final Activity context;
    private final String project;
    private final ArrayList<String> ids;
    private final ArrayList<String> names;
    DatabaseReference mDatabase;

    public TeamList(Activity context, ArrayList<String> ids, ArrayList<String> names,
                    String project) {
        super(context, R.layout.team_list_adapter, names);
        this.context = context;
        this.ids = ids;
        this.names = names;
        this.project = project;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View getView(final int position, final View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.team_list_adapter, parent, false);

        TextView idView = (TextView) rowView.findViewById(R.id.id);
        idView.setVisibility(View.GONE);
        idView.setText(ids.get(position));

        TextView nameView = (TextView) rowView.findViewById(R.id.edit_name);
        nameView.setText(names.get(position));

        ImageView deleteButton = (ImageView) rowView.findViewById(R.id.delete_button);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogConf = new AlertDialog.Builder(context);
                dialogConf.setTitle("Deleting "+ names.get(position));
                dialogConf.setMessage("Are you ure you want to delete this member?");
                dialogConf.setIcon(R.drawable.ic_delete);
                dialogConf.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ids.remove(position);
                        names.remove(position);
                        mDatabase.child("projects").child(project).child("users").setValue(ids);
                        Toast.makeText(context,"Succesfully deleted member: " + names.get(position),
                                Toast.LENGTH_SHORT).show();
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
