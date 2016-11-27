package itesm.mx.commitment_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Surveys extends AppCompatActivity {

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;

    private HashMap<String, Commitment> commitments;
    String project;
    MyApplication context;
    private DatabaseReference mDatabase;
    HashMap<String, List<String>> currentSurveys;
    ArrayList<String> surveyMembers;
    ArrayList<String> surveyTitle;
    ArrayList<String> commitmentIds;
    HashMap<String, List<String>> commitmentSurveyIds;
    ArrayList<String> surveyIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        expandableListView = (ExpandableListView) findViewById(R.id.survey_expandable_list);
        context = (MyApplication) getApplicationContext();
        project = context.getProject();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        commitments = new HashMap<String, Commitment>();

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_surveys);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_dashboard) {
                    startActivity(new Intent(Surveys.this, Dashboard.class));
                }

                if (tabId == R.id.tab_commitments) {
                    startActivity(new Intent(Surveys.this, Commitments.class));

                }
                if (tabId == R.id.tab_team) {
                    startActivity(new Intent(Surveys.this, Team.class));

                }
                if (tabId == R.id.tab_notifications) {
                    startActivity(new Intent(Surveys.this, Notifications.class));

                }

            }
        });
//        if (project!=null) {
            mDatabase.child("projects").child(project).child("commitments").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    Log.d("NEW COMMITMENT", commitment.id);
                    commitments.put(commitment.id, commitment);
                    Log.d("COMMITMENT SIZE", Integer.toString(commitments.size()));
                    populateLists();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    commitments.put(commitment.id, commitment);
                    populateLists();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    commitments.remove(commitment.id);
                    populateLists();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



//    }

    public void populateLists() {

        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> users_query = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                surveyTitle = new ArrayList<String>();
                currentSurveys = new HashMap<String, List<String>>();
                commitmentIds = new ArrayList<String>();
                commitmentSurveyIds = new HashMap<String, List<String>>();
                for (Commitment commitment : commitments.values()) {
                    surveyTitle.add(commitment.name);
                    commitmentIds.add(commitment.id);
                    surveyMembers = new ArrayList<String>();
                    surveyIds = new ArrayList<String>();
                    Log.d("Commitment Name: ", commitment.name);
                    for (Survey survey : commitment.surveys.values()) {
                        if ((survey.rating==-1) && survey.from.equals(user.getUid())) {
                            surveyMembers.add(users_query.get(survey.to).get("name"));
                            surveyIds.add(survey.id);
                            Log.d("surveyMember",users_query.get(survey.to).get("name"));
                        }
                    }
                    if(!surveyMembers.isEmpty()) {
                        currentSurveys.put(commitment.name, surveyMembers);
                        commitmentSurveyIds.put(commitment.id, surveyIds);
                    }
                    surveyTitle.retainAll(currentSurveys.keySet());
                    commitmentIds.retainAll(commitmentSurveyIds.keySet());
                }

                expandableListAdapter = new SurveysList(Surveys.this, surveyTitle, currentSurveys, commitmentIds, commitmentSurveyIds, project);
                expandableListView.setAdapter(expandableListAdapter);
                expandableListView.invalidateViews();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), Settings.class));
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_surveys);

    }

}
