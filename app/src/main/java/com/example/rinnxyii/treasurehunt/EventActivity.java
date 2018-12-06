package com.example.rinnxyii.treasurehunt;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView imageViewProfilePicture;
    TextView textViewNickname, textViewScore;

    ListView listView;
    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    Events event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setNavHeader();

        //ListView
        event = new Events();
        listView = (ListView) findViewById(R.id.listView);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Events");
        list =new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,R.layout.events_info,R.id.EventsInfo,list);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    event = ds.getValue(Events.class);
                    event.setEvent_name(ds.getKey());
                    list.add(event.getEvent_name());

                }
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                        builder.setCancelable(true);
                        builder.setTitle(event.getEvent_name());

                        String eventDetails = "Testing in progress";

                        builder.setMessage(eventDetails);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listView.showContextMenu();
                            }
                        });
                        builder.show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(upIntent)
                        .startActivities();
            } else {
                NavUtils.navigateUpTo(this, upIntent);
            }
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
            Intent intent = new Intent (EventActivity.this, NotificationsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
                break;

            case R.id.nav_map:
                Intent intentMap = new Intent(EventActivity.this, MapActivity.class);
                startActivity(intentMap);
                break;

            case R.id.nav_rank:
                Intent intentRank = new Intent(EventActivity.this, RankActivity.class);
                startActivity(intentRank);
                break;
        }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
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
