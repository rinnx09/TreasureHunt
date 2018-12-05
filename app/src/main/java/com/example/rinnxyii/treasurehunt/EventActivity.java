package com.example.rinnxyii.treasurehunt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView imageViewProfilePicture;
    TextView textViewNickname, textViewScore;


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

            case R.id.nav_user_profile:
                Intent intentUserProfile = new Intent(EventActivity.this, UserProfileActivity.class);
                startActivity(intentUserProfile);
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
        textViewNickname.setText(nickname);

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
