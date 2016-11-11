package itesm.mx.commitment_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewProject extends AppCompatActivity {
    ListView list;
    TextView inviteMember;
    ArrayList <String> memberList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        list = (ListView)findViewById(R.id.new_project_list);
        inviteMember = (TextView) findViewById(R.id.input_member_email);


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

    }
}
