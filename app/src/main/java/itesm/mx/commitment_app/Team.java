package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Team extends AppCompatActivity {
    String new_user;
    String project;
    ListView team_list;

    private ArrayList<String> names;
    private ArrayList<String> ids;

    EditText new_user_field;
    Button submit_new_user_button;

    MyApplication context;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        team_list = (ListView) findViewById(R.id.team_list);
        new_user_field = (EditText) findViewById(R.id.new_user);
        submit_new_user_button = (Button) findViewById(R.id.submit_new_user_button);
        names = new ArrayList<String>();
        ids = new ArrayList<String>();
        context = (MyApplication) getApplicationContext();
        project = context.getProject();
        mDatabase = FirebaseDatabase.getInstance().getReference();



        mDatabase.child("projects").child(project).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TeamModel team = dataSnapshot.getValue(TeamModel.class);
                ids = (ArrayList<String>) team.users;
                names = new ArrayList<String>();
                for (String id : ids) {
                    mDatabase.child("users").child(id).child("name").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = (String) dataSnapshot.getValue();
                            names.add(name);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                TeamList adapter = new TeamList(Team.this, ids, names, project);
                team_list.setAdapter(adapter);
                team_list.invalidateViews();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "An error ocurred while loading the team members",
                        Toast.LENGTH_SHORT).show();
            }
        });

        submit_new_user_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new_user = new_user_field.getText().toString();

                Boolean no_errors = true;

                if (!isValidEmail(new_user)) {
                    new_user_field.setError("Invalid Email");
                    no_errors = false;
                }

                if (no_errors) {
                    mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            HashMap<String, User> users_query = (HashMap<String, User>) dataSnapshot.getValue();
                            String uid = "";
                            String name = "";
                            Boolean exists = false;
                            for (User user: users_query.values()) {
                                if (user.email.equals(new_user)) {
                                    uid = user.id;
                                    name = user.name;
                                    if (ids.contains(uid)) {
                                        exists = true;
                                    }
                                    break;
                                }
                            }
                            if (uid.equals("")) {
                                Toast.makeText(context, "This email does not have an account",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                if (!exists) {
                                    ids.add(uid);
                                    names.add(name);
                                }
                            }
                            mDatabase.child("projects").child(project).child("users").setValue(ids);
                            Toast.makeText(context, "User added to project!",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, "An error ocurred while loading the team members",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_team);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_dashboard) {
                    startActivity(new Intent(Team.this, Dashboard.class));
                }
                if (tabId == R.id.tab_surveys) {
                    startActivity(new Intent(Team.this, Surveys.class));

                }
                if (tabId == R.id.tab_commitments) {
                    startActivity(new Intent(Team.this, Commitments.class));
                }

                if (tabId == R.id.tab_notifications) {
                    startActivity(new Intent(Team.this, Notifications.class));

                }
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

    protected void onRestart(){
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_team);
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
