package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private ArrayList<Commitment> commitmentsList;
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

        context = (MyApplication) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        project = context.getProject();
        commitmentsList = new ArrayList<Commitment>();
        names = new ArrayList<String>();
        descriptions = new ArrayList<String>();
        ids = new ArrayList<String>();

        mDatabase.child("projects").child(project).child("commitments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                HashMap<String, Object> commitments = (HashMap<String, Object>) snapshot.getValue();
                for (Object commitmentVal : commitments.values()) {
                    HashMap<String, Object> commitmentMap = (HashMap<String, Object>) commitmentVal;
                    String id = (String) commitmentMap.remove("id");
                    String name = (String) commitmentMap.remove("name");
                    String description = (String) commitmentMap.remove("description");
                    Commitment commitment = new Commitment(name, description);
                    commitment.setId(id);
                    if (!commitmentsList.contains(commitment)) {
                        commitmentsList.add(commitment);
                    }
                }
                populateListView();
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

    public void onRestart(){
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_commitments);
    }




    private void populateListView() {
        for (Commitment commitment : commitmentsList) {
            if (!ids.contains(commitment.id)) {
                ids.add(commitment.id);
            }
            if (!names.contains(commitment.name)) {
                names.add(commitment.name);
            }
            if (!descriptions.contains(commitment.description)) {
                descriptions.add(commitment.description);
            }
        }
        adapter = new CommitmentList(Commitments.this, ids, names, descriptions, project);
        //commitment_list = (ListView)findViewById(R.id.commitment_list);
        commitment_list.setAdapter(adapter);

        // Configure the list view
        ListView listView = (ListView) findViewById(R.id.friends_listview);
        listView.setAdapter(adapter);
    }

}
