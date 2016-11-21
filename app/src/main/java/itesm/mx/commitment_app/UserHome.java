package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

public class UserHome extends AppCompatActivity {

    String [] projectname = {"Project 1", "Project 2", "Project 3"};
    int [] imageId = {R.drawable.logo2, R.drawable.logo2, R.drawable.logo2};
    GridView grid;

    Button add_project;
    MyApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        UserHomeGrid adapter = new UserHomeGrid(UserHome.this, projectname, imageId);
        grid = (GridView) findViewById(R.id.user_home_grid);
        grid.setAdapter(adapter);
        context = (MyApplication) getApplicationContext();

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
