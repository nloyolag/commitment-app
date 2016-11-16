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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Commitments extends AppCompatActivity {

    private ListView commitment_list;
    private CommitmentList adapter;
    private ArrayList<String> names;
    private ArrayList<String> descriptions;
    private ArrayList<String> ids;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    private MyApplication context;
    private String project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commitments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        commitment_list = (ListView) findViewById(R.id.commitment_list_adapter);
        context = (MyApplication) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        project = context.getProject();
        names = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        ids = new ArrayList<String>();

        mDatabase.child("projects").child(project).child("commitments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Commitment commitment = dataSnapshot.getValue(Commitment.class);
                if (!ids.contains(commitment.id)) {
                    ids.add(commitment.id);
                    names.add(commitment.name);
                    descriptions.add(commitment.description);
                }
                adapter = new CommitmentList(Commitments.this, ids, names, descriptions, project);
                commitment_list.setAdapter(adapter);
                commitment_list.invalidateViews();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Commitment commitment = dataSnapshot.getValue(Commitment.class);
                int location = ids.indexOf(commitment.id);
                names.set(location, commitment.name);
                descriptions.set(location, commitment.description);
                adapter = new CommitmentList(Commitments.this, ids, names, descriptions, project);
                commitment_list.setAdapter(adapter);
                commitment_list.invalidateViews();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Commitment commitment = dataSnapshot.getValue(Commitment.class);
                int location = ids.indexOf(commitment.id);
                ids.remove(location);
                names.remove(location);
                descriptions.remove(location);
                adapter = new CommitmentList(Commitments.this, ids, names, descriptions, project);
                commitment_list.setAdapter(adapter);
                commitment_list.invalidateViews();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "An error ocurred while loading the commitments",
                        Toast.LENGTH_SHORT).show();
            }
        });

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_commitments);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_dashboard) {
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                }
                if (tabId == R.id.tab_surveys) {
                    startActivity(new Intent(getApplicationContext(), Surveys.class));
                }
                if (tabId == R.id.tab_commitments) {
                }
                if (tabId == R.id.tab_team) {
                    startActivity(new Intent(getApplicationContext(), Team.class));

                }
                if (tabId == R.id.tab_notifications) {
                    startActivity(new Intent(getApplicationContext(), Notifications.class));

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
                startActivity(new Intent(getApplicationContext(), UserSettings.class));
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void onRestart(){
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_commitments);
    }
}
