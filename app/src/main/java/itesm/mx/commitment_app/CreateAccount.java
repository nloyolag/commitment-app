package itesm.mx.commitment_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccount extends AppCompatActivity {

    private String name;
    private String username;
    private String password;
    private String confirm_password;
    private String email;

    private EditText name_field;
    private EditText username_field;
    private EditText password_field;
    private EditText confirm_password_field;
    private EditText email_field;
    private Button submit_button;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        context = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name_field = (EditText) findViewById(R.id.edit_name);
        username_field = (EditText) findViewById(R.id.username);
        password_field = (EditText) findViewById(R.id.password);
        confirm_password_field = (EditText) findViewById(R.id.confirm_password_field);
        email_field = (EditText) findViewById(R.id.email);
        submit_button = (Button) findViewById(R.id.submit);

        TextView sign_in = (TextView) findViewById(R.id.link_login);
        sign_in.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccount.this, Login.class);
                startActivity(intent);
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = name_field.getText().toString();
                username = username_field.getText().toString();
                password = password_field.getText().toString();
                confirm_password = confirm_password_field.getText().toString();
                email = email_field.getText().toString();

                Boolean no_errors = true;

                if (!isValidField(name)) {
                    name_field.setError("Invalid Name");
                    no_errors = false;
                }

                if (!isValidField(username)) {
                    username_field.setError("Invalid Username");
                    no_errors = false;
                }

                if (!isValidEmail(email)) {
                    email_field.setError("Invalid Email");
                    no_errors = false;
                }

                if (!isValidField(password)) {
                    password_field.setError("Invalid Password");
                    no_errors = false;
                }

                if (!password.equals(confirm_password)) {
                    confirm_password_field.setError("The specified passwords do not match");
                    no_errors = false;
                }

                if (no_errors) {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(CreateAccount.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(context, "An error has ocurred when creating your account",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        onAuthSuccess(task.getResult().getUser(), username, name);
                                    }
                                }
                            });
                }


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(CreateAccount.this, UserHome.class));
            finish();
        }
    }

    private void onAuthSuccess(FirebaseUser user, String username, String name) {
        writeNewUser(user.getUid(), name, username, user.getEmail());
        startActivity(new Intent(CreateAccount.this, UserHome.class));
        finish();
    }

    private void writeNewUser(String userId, String name, String username, String email) {
        User user = new User(name, username, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidField(String value) {
        if (value != null && value.length() > 0) {
            return true;
        }
        return false;
    }

}
