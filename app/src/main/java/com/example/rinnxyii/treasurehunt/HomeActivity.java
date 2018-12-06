package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar = null;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView imageViewProfilePicture;
    TextView textViewNickname, textViewScore;

    private static final String TAG = "1";
    private static final int request_code = 100;
    private TextView text, textviewResult;
    private Button btnmission, btnQR;
    private String missionToPlay = "";
    public static final String MISSION_VALUE = "MISSIONVALUE";
    private ProgressDialog Dialog;
    private List<Mission> mission = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNavHeader();

        btnmission = (Button) findViewById(R.id.mission);
        btnQR = (Button) findViewById(R.id.buttonQR);
        textviewResult = (TextView) findViewById(R.id.tresult);
        text = (TextView) findViewById(R.id.textViewMission);
        Dialog = new ProgressDialog(HomeActivity.this);

        Dialog.setMessage("Loading...");
        Dialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference missionRef = database.getReference();

        missionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot missionSnapshot = dataSnapshot.child("Missions");
                Iterable<DataSnapshot> missionChildren = missionSnapshot.getChildren();


                for (DataSnapshot missions : missionChildren) {

                    String type = missions.child("mission_Type").getValue().toString();
                    String value = missions.child("mission_Value").getValue().toString();
                    String eventMission;
                    String ID = "";
                    if (missions.child("event_mission").exists()) {
                        eventMission = missions.child("event_mission").getValue().toString();
                    } else {
                        eventMission = null;
                    }
                    if (missions.hasChild("QR_ID")) {
                        ID = missions.child("QR_ID").getValue().toString();
                    }
                    Mission m = new Mission(missions.getKey(), type, value, eventMission);
                    m.setID(ID);
                    mission.add(m);
                }
                Dialog.hide();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT);
            }
        });
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(HomeActivity.this, ScanQRcodeActivity.class);
                startActivityForResult(intent, request_code);


            }
        });
        btnmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Mission> m2 = new ArrayList<>();
                for (int x = 0; x < mission.size(); x++) {
                    if (mission.get(x).getEventMission().equals("")) {
                        m2.add(mission.get(x));
                    }
                }
                Random rand = new Random();
                int index = rand.nextInt(m2.size());
                missionToPlay = m2.get(index).getMissionIndex();

                for (int m = 0; m < mission.size(); m++) {
                    if (mission.get(m).getMissionIndex().equals(missionToPlay)) {
                        if (mission.get(m).getMissionType().equals("Shaking")) {
                            Intent intent = new Intent(HomeActivity.this, ShakeActivity.class);
                            intent.putExtra(MISSION_VALUE, Integer.parseInt(mission.get(m).getMissionValue()));
                            startActivityForResult(intent, request_code);
                            break;

                        } else if (mission.get(m).getMissionType().equals("Tapping")) {
                            Intent intent = new Intent(HomeActivity.this, TapActivity.class);
                            intent.putExtra(MISSION_VALUE, Integer.parseInt(mission.get(m).getMissionValue()));
                            startActivityForResult(intent, request_code);
                            break;

                        } else if (mission.get(m).getMissionType().equals("Wefie")) {
                            Intent intent = new Intent(HomeActivity.this, WefieActivity.class);
                            intent.putExtra(MISSION_VALUE, Integer.parseInt(mission.get(m).getMissionValue()));
                            startActivityForResult(intent, request_code);

                            break;
                        }
                    }
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_event:
                Intent intentEvent = new Intent(HomeActivity.this, EventActivity.class);
                startActivity(intentEvent);
                break;

            case R.id.nav_map:
                Intent intentMap = new Intent(HomeActivity.this, MapActivity.class);
                startActivity(intentMap);
                break;

            case R.id.nav_rank:
                Intent intentRank = new Intent(HomeActivity.this, RankActivity.class);
                startActivity(intentRank);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
         /*    sp = PreferenceManager.getDefaultSharedPreferences(this);
                                editor = sp.edit();
                                editor.putString("ID",id);
                                editor.commit();
                                sp.getString("ID", "");*/
        if (request_code == requestCode) {
            if (Activity.RESULT_OK == RESULT_OK) {
                Boolean mission = data.getBooleanExtra("RESULT", false);
                String type = data.getStringExtra("type");
                int mark = data.getIntExtra("MARK", 0);
                if (!type.equals("SCANQR") && !type.equals("WEFIE")) {
                    if (mission == true) {
                        text.setText("Mission Complete");
                        textviewResult.setText("Well Done");
                    } else {
                        text.setText("Mission Failed");
                        textviewResult.setText("Your score is " + mark);
                    }
                } else if (type.equals("WEFIE")) {
                    if (mission == true) {
                        text.setText("Mission Complete");
                        textviewResult.setText("Congration! you get new friend");
                    } else {
                        text.setText("Mission Failed");
                        textviewResult.setText("");
                    }
                } else {
                    if (mission == true) {
                        text.setText("Mission Complete");
                        textviewResult.setText("Enjoy the event");
                    } else {
                        text.setText("Mission Failed");
                        textviewResult.setText("");
                    }
                }

            }
        }
    }

    public void setNavHeader(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

        View headView = navigationView.getHeaderView(0);
        imageViewProfilePicture = headView.findViewById(R.id.imageViewProfilePicture);
        textViewNickname = headView.findViewById(R.id.textViewNickname);
        textViewScore = headView.findViewById(R.id.textViewScore);

        String nickname = sp.getString(getString(R.string.preference_nickname),"");

        if (!nickname.equals("")){
            textViewNickname.setText(nickname);
        }

        textViewScore.setText("Score: 120");

        String picNo = sp.getString(getString(R.string.preference_profilepic),"");

        if (picNo.equals("1")){
            imageViewProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl_1));
        }else   if (picNo.equals("2")){
            imageViewProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl_2));
        }else   if (picNo.equals("3")){
            imageViewProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy_1));
        }else   if (picNo.equals("4")){
            imageViewProfilePicture.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy_2));
        }

    }
}