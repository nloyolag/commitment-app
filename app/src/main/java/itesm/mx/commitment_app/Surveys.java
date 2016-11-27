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





//
//    ArrayList<String> surveyId;
//    HashMap<String, Integer> userScores;
//    Collection <HashMap<String, String>> survey_list_done;
//    HashMap <String, HashMap <String,String>> users;
//    String title;



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


//
//        Testing
//        commitment_ids = new ArrayList<String>();
//        survey_ids = new ArrayList<String>();
//        surveyId = new ArrayList<String>();
//        userScores = new HashMap<String, Integer>();
//        survey_list_done = new ArrayList<HashMap<String, String>>();
//        users = new HashMap<String, HashMap<String, String>>();
//        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                users = (HashMap<String,HashMap<String,String>>) dataSnapshot.getValue();
//                mDatabase.child("projects").child(project).child("commitments").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        HashMap<String, HashMap<String, String>> commitments = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
//                        for (HashMap<String, String> com : commitments.values()) {
//                            title = com.get("name");
//                            surveyTitle.add(title);
//                            mDatabase.child("projects").child(project).child("commitments").child(com.get("id")).child("surveys").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    HashMap<String, HashMap<String, String>> surveylist = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
//                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    survey_list_done = surveylist.values();
//                                    for (HashMap<String, String> survey : survey_list_done) {
//                                        ArrayList<String> surveyUsers = new ArrayList<String>();
//                                        if (user.getUid().equals(survey.get("from")))
//                                            surveyUsers.add(users.get(survey.get("to")).get("name"));
//                                        currentSurveys.put(title, surveyUsers );
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//
//        /*
//                            mDatabase.child("users").child(id).child("name").addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    String name = (String) dataSnapshot.getValue();
//                                    userScores.put(name, 10);
//                                    Log.d("Name: ", name);
//                                    Log.d("User Score: ", String.valueOf(userScores.size()));
//                                    if (!userScores.isEmpty())
//                                        CreateExpandableListView(userScores);
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                }
//                            });*/
//                            });
//
//                        }
//                        populateList(surveyTitle, currentSurveys);
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(context, "An error ocurred while creating a survey",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });




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

    public void populateLists() {
/*        currentSurveys = new HashMap<String, List<String>>();
        surveyTitle = new ArrayList<String>();
        surveyMembers = new ArrayList<String>();*/

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
/*                for (String s : surveyTitle) {
                    Log.d("Survey Title: ", s);
                    for (String m : currentSurveys.get(s))
                        Log.d("Member: ", m);
                }*/
                expandableListAdapter = new SurveysList(Surveys.this, surveyTitle, currentSurveys, commitmentIds, commitmentSurveyIds, project);
                expandableListView.setAdapter(expandableListAdapter);
                expandableListView.invalidateViews();




// DashboardList adapter = new DashboardList(getActivity(), names_inner, scores_inner);
//                listView.setAdapter(adapter);
//                listView.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /*public void populateList (ArrayList<String> surveyTitle, HashMap<String, List<String>> currentSurveys) {

        expandableListView = (ExpandableListView) findViewById(R.id.survey_expandable_list);
//        expandableListDetail = SurveyData.getData(currentSurveys);
        expandableListTitle = new ArrayList<String>(surveyTitle);
        expandableListAdapter = new SurveysList(appContext, expandableListTitle, currentSurveys);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        final Button doneButton = (Button) expandableListView.findViewById(R.id.done_button);
*//*        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*//*

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                if (v.getTag()!=null) {
                    HashMap<String, Integer> tag = (HashMap<String, Integer>) v.getTag();
                    String member = (String) tag.keySet().toArray()[0];
                    int result = (int) tag.get(member);
                    Toast.makeText(appContext, member + ": " + result, Toast.LENGTH_SHORT);
                }
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }*/


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
