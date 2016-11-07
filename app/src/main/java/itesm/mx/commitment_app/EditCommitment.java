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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class EditCommitment extends AppCompatActivity {

    private String name;
    private String description;
    private String id;
    private String project;

    private EditText name_field;
    private EditText description_field;
    private EditText id_field;
    private Button submit_button;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    MyApplication context;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_commitment);

        context = (MyApplication) getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        project = context.getProject();

        name_field = findViewById(R.id.name);
        description_field = findViewById(R.id.description);
        id_field = findViewById(R.id.id);
        submit_button = findViewById(R.id.submit);

        intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            name = intent.getStringExtra("name");
            description = intent.getStringExtra("description");
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
                }

                if (no_errors) {
                    Commitment commitment = new Commitment(name, description);
                    commitment.setId(id);
                    mDatabase.child("projects").child(project).child("commitments").child(id).setValue(commitment, new Firebase.CompletionListener() {
                        @Override
                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                            if (firebaseError != null) {
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

        });
    }

    private boolean isValidField(String value) {
        if (value != null && value.length() > 0) {
            return true;
        }
        return false;
    }
}
