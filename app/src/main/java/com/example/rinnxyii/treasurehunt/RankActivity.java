package com.example.rinnxyii.treasurehunt;

import android.app.ProgressDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    ImageView imageViewProfilePicture;
    TextView textViewNickname, textViewScore,textfirst,textsecond,textthird,textscore;
    private ProgressDialog Dialog;
    private List<Rank> rank = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Dialog = new ProgressDialog(RankActivity.this);
        textfirst=(TextView)findViewById(R.id.textViewChampion);
        textsecond=(TextView)findViewById(R.id.textViewSecondPlace);
        textthird=(TextView)findViewById(R.id.textViewThirdPlace);
        textscore=(TextView)findViewById(R.id.textViewUserPoint);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       setNavHeader();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        int score=sp.getInt("SCORE",0);
        textscore.append(" "+String.valueOf(score));

        Dialog.setMessage("Loading...");
        Dialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rankRef = database.getReference();

        rankRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot rankSnapshot = dataSnapshot.child("Rank");
                Iterable<DataSnapshot> rankChildren = rankSnapshot.getChildren();


                for (DataSnapshot ranks : rankChildren) {
                    String name="";
                    String score="";
                    if(ranks.child("Name").exists()&&ranks.child("Score").exists()){
                        name=ranks.child("Name").getValue().toString();
                        score=ranks.child("Score").getValue().toString();
                    }

                    Rank r = new Rank(ranks.getKey(),name,score);

                rank.add(r);
                }

                Dialog.dismiss();
                for(int a=0;a<rank.size();a++){
                    if(rank.get(a).getPlace().equals("1")){
                        textfirst.setText(rank.get(a).getName()+" "+rank.get(a).getScore());
                    }else if(rank.get(a).getPlace().equals("2")){
                        textsecond.setText(rank.get(a).getName()+" "+rank.get(a).getScore());
                    }else if(rank.get(a).getPlace().equals("3")){
                        textthird.setText(rank.get(a).getName()+" "+rank.get(a).getScore());
                    }
                }
                rank.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notifications) {
            Intent intent = new Intent (RankActivity.this, NotificationsActivity.class);
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

            case R.id.nav_event:
                Intent intentEvent = new Intent(RankActivity.this, EventActivity.class);
                startActivity(intentEvent);
                break;

            case R.id.nav_map:
                Intent intentMap = new Intent(RankActivity.this, MapActivity.class);
                startActivity(intentMap);
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
    @Override
    protected void onResume() {
        super.onResume();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        textViewScore.setText("Score: "+String.valueOf(sp.getInt("SCORE",0)));

    }
}
