package itesm.mx.commitment_app;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by luis on 9/11/16.
 */

class NewProjectList extends ArrayAdapter<String>{

        private final Activity context;
        private final ArrayList<String> member;

        NewProjectList(Activity context, ArrayList<String> member) {
        super(context, R.layout.list_adapter, member);
        this.context = context;
        this.member = member;
    }


        public View getView(int position, View view, @NonNull ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();


        View rowView= inflater.inflate(R.layout.new_project_list_adapter, null, true);
        TextView memberView = (TextView) rowView.findViewById(R.id.member_invited);
        memberView.setText(member.get(position));

        ImageView deleteButton = (ImageView) rowView.findViewById(R.id.delete_member);
        deleteButton.setImageResource(R.drawable.ic_delete);
            deleteButton.setTag(position);
/*
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT);

            }
        });
*/
        return rowView;
    }


}