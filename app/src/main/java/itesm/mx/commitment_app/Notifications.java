package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    protected void onRestart(){
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_notifications);
    }

}
