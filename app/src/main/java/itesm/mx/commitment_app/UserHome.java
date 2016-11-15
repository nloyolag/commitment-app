package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserHome extends AppCompatActivity {

    ArrayList<String> project_ids = new ArrayList<String>();
    ArrayList<String> project_names = new ArrayList<String>();
    int image_id = R.drawable.logo2;
    ArrayList<Integer> image_ids = new ArrayList<Integer>();
    GridView grid;

    Button add_project;
    MyApplication context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        grid = (GridView) findViewById(R.id.user_home_grid);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = mAuth.getCurrentUser();
        context = (MyApplication) getApplicationContext();

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = project_ids.get(i);
                context.setProject(id);
                Intent intent = new Intent(UserHome.this, Dashboard.class);
                startActivity(intent);
            }
        });

        mDatabase.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TeamModel team = dataSnapshot.getValue(TeamModel.class);
                if (team.users.contains(user.getUid()) && !project_ids.contains(team.id)) {
                    project_ids.add(team.id);
                    project_names.add(team.name);
                    image_ids.add(image_id);
                }
                UserHomeGrid adapter = new UserHomeGrid(UserHome.this, project_names, project_ids, image_ids);
                grid.setAdapter(adapter);
                grid.invalidateViews();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TeamModel team = dataSnapshot.getValue(TeamModel.class);
                int location = project_ids.indexOf(team.id);
                project_names.set(location, team.name);
                UserHomeGrid adapter = new UserHomeGrid(UserHome.this, project_names, project_ids, image_ids);
                grid.setAdapter(adapter);
                grid.invalidateViews();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TeamModel team = dataSnapshot.getValue(TeamModel.class);
                int location = project_ids.indexOf(team.id);
                project_ids.remove(location);
                project_names.remove(location);
                image_ids.remove(location);
                UserHomeGrid adapter = new UserHomeGrid(UserHome.this, project_names, project_ids, image_ids);
                grid.setAdapter(adapter);
                grid.invalidateViews();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        add_project = (Button) findViewById(R.id.add_project);

        add_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserHome.this, NewProject.class);
                startActivity(intent);
            }
        });

    }

}
