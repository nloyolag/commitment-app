package itesm.mx.commitment_app;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;

public class Dashboard extends AppCompatActivity {
    public ProgressBar myProgressbar;
    public int myProgress;

    MyApplication context;
    String project;
    DatabaseReference mDatabase;

    /*
    Long tsLong = System.currentTimeMillis()/1000;
    String ts = tsLong.toString();
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        context = (MyApplication) getApplicationContext();
        project = context.getProject();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (project==null)
            onCreate(savedInstanceState);
        if(project!=null) {
            mDatabase.child("projects").child(project).child("commitments").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Commitment commitment = dataSnapshot.getValue(Commitment.class);
                    Calendar cal = Calendar.getInstance();
                    TimeZone tz = cal.getTimeZone();//get your local time zone.
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                    sdf.setTimeZone(tz);//set time zone.
                    String localTime = sdf.format(new Date(Long.parseLong(commitment.survey_time) * 1000));
                    Date date = new Date();
                    try {
                        date = sdf.parse(localTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar currentDate = new GregorianCalendar();
                    int year1 = currentDate.get(Calendar.YEAR);
                    int week1 = currentDate.get(Calendar.WEEK_OF_YEAR);

                    Calendar surveyDate = new GregorianCalendar();
                    surveyDate.setTime(date);
                    int year2 = surveyDate.get(Calendar.YEAR);
                    int week2 = surveyDate.get(Calendar.WEEK_OF_YEAR);

                    if (year1 > year2 || (year1 == year2 && week1 > week2)) {
                        Long tsLong = System.currentTimeMillis() / 1000;
                        String ts = tsLong.toString();
                        final HashMap<String, Survey> surveys = commitment.surveys;
                        final String commitment_id = commitment.id;
                        mDatabase.child("projects").child(project).child("commitments").child(commitment.id).child("survey_time").setValue(ts);
                        mDatabase.child("projects").child(project).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap<String, Survey> new_surveys = new HashMap<String, Survey>();
                                ArrayList<String> users = (ArrayList<String>) dataSnapshot.getValue();
                                for (String from : users) {
                                    for (String to : users) {
                                        if (!from.equals(to)) {
                                            String uid = UUID.randomUUID().toString();
                                            Survey survey = new Survey(from, to, -1, uid);
                                            new_surveys.put(uid, survey);
                                        }
                                    }
                                }
                                new_surveys.putAll(surveys);
                                mDatabase.child("projects").child(project).child("commitments").child(commitment_id).child("surveys").setValue(new_surveys);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_dashboard);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_surveys) {
                    startActivity(new Intent(Dashboard.this, Surveys.class));

                }
                if (tabId == R.id.tab_commitments) {
                    startActivity(new Intent(Dashboard.this, Commitments.class));

                }
                if (tabId == R.id.tab_team) {
                    startActivity(new Intent(Dashboard.this, Team.class));

                }
                if (tabId == R.id.tab_notifications) {
                    startActivity(new Intent(Dashboard.this, Notifications.class));

                }

            }
        });

        //Creates ViewPager & TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Personal"));
        tabLayout.addTab(tabLayout.newTab().setText("Team"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new PersonalDashboard();
                    case 1:
                        return new TeamDashboard();
                    default:
                        return null;
                }
            }

        };

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onRestart() {
        super.onRestart();
        final BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setDefaultTab(R.id.tab_dashboard);

    }
}
