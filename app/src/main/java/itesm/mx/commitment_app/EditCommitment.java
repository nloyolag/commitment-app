package itesm.mx.commitment_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class EditCommitment extends AppCompatActivity {

    private String name;
    private String description;
    private String id;
    private String project;
    private String survey_time;
    private HashMap<String, Survey> surveys;

    private EditText name_field;
    private EditText description_field;
    private EditText id_field;
    private Button submit_button;

    private DatabaseReference mDatabase;

    MyApplication context;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_commitment);

        context = (MyApplication) getApplicationContext();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        project = context.getProject();

        name_field = (EditText) findViewById(R.id.name_field);
        description_field = (EditText) findViewById(R.id.description);
        id_field = (EditText) findViewById(R.id.id);
        id_field.setVisibility(View.GONE);
        submit_button = (Button) findViewById(R.id.submit_button);

        intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            description = intent.getStringExtra("description");
            surveys = (HashMap<String, Survey>)intent.getSerializableExtra("surveys");
            survey_time = intent.getStringExtra("survey_time");
            id_field.setText(id);
            name_field.setText(name);
            description_field.setText(description);
        }

        submit_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                name = name_field.getText().toString();
                description = description_field.getText().toString();
                id = id_field.getText().toString();

                Boolean no_errors = true;

                if (!isValidField(name)) {
                    name_field.setError("Invalid Name");
                    no_errors = false;
                }

                if (!isValidField(description)) {
                    description_field.setError("Invalid Description");
                    no_errors = false;
                }

                if (!isValidField(id)) {
                    id = UUID.randomUUID().toString();
                    surveys = null;
                }

                if (no_errors) {
                    if (surveys == null) {
                        mDatabase.child("projects").child(project).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                surveys = new HashMap<String, Survey>();
                                ArrayList<String> users = (ArrayList<String>) dataSnapshot.getValue();
                                for (String from : users) {
                                    for (String to : users) {
                                        if (!from.equals(to)) {
                                            String uid = UUID.randomUUID().toString();
                                            Survey survey = new Survey(from, to, -1, uid);
                                            surveys.put(uid, survey);
                                        }
                                    }
                                }
                                Commitment commitment = new Commitment(name, description);
                                commitment.setId(id);
                                commitment.setSurveys(surveys);
                                Long tsLong = System.currentTimeMillis() / 1000;
                                String ts = tsLong.toString();
                                commitment.setSurveyTime(ts);
                                mDatabase.child("projects").child(project).child("commitments").child(id).setValue(commitment, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(context, "An error while submitting the commitment",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            startActivity(new Intent(EditCommitment.this, Commitments.class));
                                            finish();
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Commitment commitment = new Commitment(name, description);
                        commitment.setId(id);
                        commitment.setSurveys(surveys);
                        commitment.setSurveyTime(survey_time);
                        mDatabase.child("projects").child(project).child("commitments").child(id).setValue(commitment, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(context, "An error while submitting the commitment",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(EditCommitment.this, Commitments.class));
                                    finish();
                                }
                            }
                        });
                    }
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
