package itesm.mx.commitment_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class UserHome extends AppCompatActivity {

    String [] projectname = {"Project 1", "Project 2", "Project 3"};
    int [] imageId = {R.drawable.logo2, R.drawable.logo2, R.drawable.logo2};
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        UserHomeGrid adapter = new UserHomeGrid(UserHome.this, projectname, imageId);
        grid = (GridView) findViewById(R.id.user_home_grid);
        grid.setAdapter(adapter);
    }

}
