package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
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

        mDatabase.child("projects").child(project).child("commitments").addChildEventListener(new ValueEventListener() {
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

            // ...
        });

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
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
                    startActivity(new Intent(getApplicationContext(), Commitments.class));

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


    private void populateListView() {
        for (Commitment commitment : commitmentsList) {
            if (!names.contains(commitment.name)) {
                names.add(commitment.name);
            }
            if (!descriptions.contains(commitment.description)) {
                descriptions.add(commitment.description);
            }
        }
        adapter = new CommitmentList(Commitments.this, names, descriptions);
        commitment_list = (ListView)findViewById(R.id.commitment_list);
        commitment_list.setAdapter(adapter);

        // Configure the list view
        ListView listView = (ListView) findViewById(R.id.friends_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent commitment_intent = new Intent(Commitments.this, EditCommitment.class);
                commitment_intent.putExtra("id", commitmentsList.get(position).id);
                commitment_intent.putExtra("name", commitmentsList.get(position).name);
                commitment_intent.putExtra("description", commitmentsList.get(position).description);
                startActivity(commitment_intent);
            }
        });
    }

}
