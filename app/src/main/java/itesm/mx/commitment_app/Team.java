package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class Team extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);


        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_team);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_dashboard) {
                    startActivity(new Intent(Team.this, Dashboard.class));
                }
                if (tabId == R.id.tab_surveys) {
                    startActivity(new Intent(Team.this, Surveys.class));

                }
                if (tabId == R.id.tab_commitments) {
                    startActivity(new Intent(Team.this, Commitments.class));
                }

                if (tabId == R.id.tab_notifications) {
                    startActivity(new Intent(Team.this, Notifications.class));

                }
            }
        });
    }

    protected void onRestart(){
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_team);
    }
}
