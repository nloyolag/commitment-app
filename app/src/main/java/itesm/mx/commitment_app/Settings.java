package itesm.mx.commitment_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by luis on 22/11/16.
 */

public class Settings extends AppCompatActivity {


    ListView listView;
    String [] items = {"Change Project","Project Settings", "User Settings", "Logout"};
    int [] colors = {0,0,0, Color.RED};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                switch (position) {
                    case 0:
                        startActivity(new Intent(Settings.this, UserHome.class));
                        break;
                    case 1:
                        startActivity(new Intent(Settings.this, ProjectSettings.class));
                        break;
                    case 2:
                        startActivity(new Intent(Settings.this, UserSettings.class));
                        break;
                    case 3: /*logout*/
                        FirebaseAuth.getInstance().signOut();
                        break;
                }

            }
        };

        SettingsList adapter = new SettingsList(Settings.this, items, colors, listener);
        listView = (ListView) findViewById(R.id.list_settings);
        listView.setAdapter(adapter);
    }
}
