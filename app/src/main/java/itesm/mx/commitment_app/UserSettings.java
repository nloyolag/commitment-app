package itesm.mx.commitment_app;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserSettings extends AppCompatActivity {

    ListView listView;
    String [] items = {"Change Project", "Logout"};
    int [] colors = {0, Color.RED};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                switch (position) {
                    case 0: startActivity(new Intent(UserSettings.this, UserHome.class)); break;
                    case 1: /*logout*/ FirebaseAuth.getInstance().signOut(); break;
                }

            }
        };

        UserSettingsList adapter = new UserSettingsList(UserSettings.this, items, colors, listener);
        listView = (ListView) findViewById(R.id.list_settings);
        listView.setAdapter(adapter);

    }
}
