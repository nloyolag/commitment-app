package itesm.mx.commitment_app;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

/**
 * Created by luis on 19/11/16.
 */

public class SurveysList extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private List<String> commitmentId;
    private HashMap<String, List<String>> surveyId;
    private String project;
    DatabaseReference mDatabase;


    public SurveysList(Context context, List<String> expandableListTitle,
                       HashMap<String, List<String>> expandableListDetail, List<String> commitmentId,
                       HashMap<String, List<String>> surveyId, String project) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.commitmentId = commitmentId;
        this.surveyId = surveyId;
        this.project = project;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    public Object getSurveyId(int listPosition, int expandedListPosition) {
        return this.surveyId.get(this.commitmentId.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition,
                             final boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        final String memberIdText = (String) getSurveyId(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.surveys_list_submenu, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.survey_member);
        expandedListTextView.setText(expandedListText);

        TextView memberIdView = (TextView) convertView.findViewById(R.id.member_id);
        memberIdView.setText(memberIdText);

        final RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        Button done = (Button) convertView.findViewById(R.id.done_button);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingBar.getRating()>0) {
                    Log.d("Result: ", expandedListText + ": " + (int)ratingBar.getRating());
                    mDatabase.child("projects").child(project).child("commitments").
                            child((String)getCommitmentId(listPosition)).child("surveys").
                            child(memberIdText).child("rating").setValue((int)ratingBar.getRating());
                    notifyDataSetChanged();
                }
                else
                    Toast.makeText(context, "The rating must be between 1 and 5 stars",
                            Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    public Object getCommitmentId(int listPosition) {
        return this.commitmentId.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.surveys_list_header, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.survey_header);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}