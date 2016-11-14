package itesm.mx.commitment_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NewProject extends AppCompatActivity {
    ListView list;
    TextView inviteMember;
    ArrayList <String> memberList = new ArrayList<>();

    private String name;
    private String id;

    private EditText name_field;
    private Button submit_button;
    private DatabaseReference mDatabase;
    private Map<String, User> user_list;

    MyApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        list = (ListView)findViewById(R.id.new_project_list);
        inviteMember = (TextView) findViewById(R.id.input_member_email);
        user_list = new HashMap<String, User>();

        inviteMember.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                memberList.add(String.valueOf(inviteMember.getText()));
                NewProjectList adapter = new NewProjectList(NewProject.this, memberList);
                list.setAdapter(adapter);
                inviteMember.setText(inviteMember.getHint());
                return false;
            }
        });

        context = (MyApplication) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name_field = (EditText) findViewById(R.id.input_project_name);
        submit_button = (Button) findViewById(R.id.btn_create);

        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    String key = userSnapshot.getKey();
                    user_list.put(key, user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = name_field.getText().toString();

                Boolean no_errors = true;

                if (!isValidField(name)) {
                    name_field.setError("Invalid Name");
                    no_errors = false;
                }

                id = UUID.randomUUID().toString();

                if (no_errors) {
                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    ArrayList<String> users = new ArrayList<String>();
                    users.add(user);
                    ArrayList<String> emails = new ArrayList<String>();
                    for (int i = 0; i < list.getCount(); i++) {
                        emails.add((String) list.getItemAtPosition(i));
                    }
                    for (Map.Entry<String, User> e : user_list.entrySet()) {
                        Log.d("Project", e.getKey());
                        User current_user = e.getValue();
                        if (emails.contains(current_user.email)) {
                            users.add(e.getKey());
                        }
                    }

                    TeamModel team = new TeamModel(name, id, users);
                    for (String val : users){
                        Log.d("User", val);
                    }
                    mDatabase.child("projects").child(id).setValue(team, new DatabaseReference.CompletionListener() {

                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Toast.makeText(context, "An error ocurred while submitting the team",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                context.setProject(id);
                                startActivity(new Intent(NewProject.this, Dashboard.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean isValidField(String value) {
        if (value != null && value.length() > 0) {
            return true;
        }
        return false;
    }
}
