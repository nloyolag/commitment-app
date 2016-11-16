package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Notifications extends AppCompatActivity {
    ListView list;

    Integer [] notificationsImage = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    String[] notificationTitles = {"Reminder", "Reminder", "Warning"};
    String[] notificationText = {"Please do Survey 1 in the next 24 hours", "Please do Survey 1 in the next 36 hours", "You still need to add you Email Adress!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_notifications);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_dashboard) {
                    startActivity(new Intent(Notifications.this, Dashboard.class));
                }
                if (tabId == R.id.tab_surveys) {
                    startActivity(new Intent(Notifications.this, Surveys.class));

                }
                if (tabId == R.id.tab_commitments) {
                    startActivity(new Intent(Notifications.this, Commitments.class));

                }
                if (tabId == R.id.tab_team) {
                    startActivity(new Intent(Notifications.this, Team.class));

                }

            }
        });


        NotificationList adapter = new NotificationList(Notifications.this, notificationsImage, notificationTitles, notificationText);
        list = (ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(), UserSettings.class));
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    protected void onRestart(){
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_notifications);
    }

}
