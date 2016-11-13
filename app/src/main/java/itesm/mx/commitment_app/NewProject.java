package itesm.mx.commitment_app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class NewProject extends AppCompatActivity {

//    private String name;
//    private String id;
//
//    private EditText name_field;
//    private Button submit_button;
//    private DatabaseReference mDatabase;
//
//    MyApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

//        context = (MyApplication) getApplicationContext();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        name_field = findViewById(R.id.name);
//        submit_button = findViewById(R.id.name);
//
//        submit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                name = name_field.getText().toString();
//
//                Boolean no_errors = true;
//
//                if (!isValidField(name)) {
//                    name_field.setError("Invalid Name");
//                    no_errors = false;
//                }
//
//                id = UUID.randomUUID().toString();
//
//                if (no_errors) {
//                    String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                    ArrayList<String> users = new ArrayList<String>();
//                    users.add(user);
//                    TeamModel team = new TeamModel(name, id, users);
//                    mDatabase.child("projects").child(id).setValue(team, new Firebase.CompletionListener() {
//                        @Override
//                        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
//                            if (firebaseError != null) {
//                                Toast.makeText(context, "An error ocurred while submitting the team",
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                context.setProject(id);
//                                startActivity(new Intent(NewProject.this, Dashboard.class));
//                                finish();
//                            }
//                        }
//                    });
//                }
//            }
//        });

    }

//    private boolean isValidField(String value) {
//        if (value != null && value.length() > 0) {
//            return true;
//        }
//        return false;
//    }
}
