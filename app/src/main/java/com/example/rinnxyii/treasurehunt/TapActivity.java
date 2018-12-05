package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TapActivity extends AppCompatActivity {
    private int Tapcount = 0;
    private int missionTapNum;
    private Boolean timesup = false;
    private TextView textviewTap, textviewresult;
    private Button btngiveup, btnstart;
    private Boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap);
        textviewTap = (TextView) findViewById(R.id.textViewTap);
        textviewresult = (TextView) findViewById(R.id.textViewresult2);
        btnstart = (Button) findViewById(R.id.buttonstart2);
        btngiveup = (Button) findViewById(R.id.buttonGiveup2);

        Intent intent = getIntent();
        missionTapNum = intent.getIntExtra(HomeActivity.MISSION_VALUE, 0);
        textviewTap.setText("Mission: Tap " + String.valueOf(missionTapNum) + " times");
        // Toast.makeText(getApplicationContext(), String.valueOf(missionTapNum), Toast.LENGTH_LONG).show();
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!start) {
                    if (btnstart.getText().equals("Complete")) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("RESULT", true);
                        returnIntent.putExtra("MARK", Tapcount);
                        returnIntent.putExtra("type","TAP");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        start = true;
                        Tapcount = 0;
                        timesup = false;
                        Toast.makeText(getApplicationContext(), "start counting...", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after 10 seconds
                                if (!btnstart.getText().equals("Complete")) {
                                    timesup = true;
                                    start = false;
                                    btnstart.setText("Try again");
                                    textviewresult.setText("You have tapped " + String.valueOf(Tapcount) + " times");
                                }
                            }
                        }, 10000);  //the time is in miliseconds
                    }
                }


            }
        });

        btngiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("RESULT", false);
                returnIntent.putExtra("MARK", Tapcount);
                returnIntent.putExtra("type","TAP");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public void screenTapped(View view) {
        if (start) {
            if (timesup == false) {
                if (Tapcount < missionTapNum) {
                    if (Tapcount == 0) {
                        Toast.makeText(getApplicationContext(), "Keep Tapping", Toast.LENGTH_LONG).show();
                    }
                    Tapcount = Tapcount + 1;

                } else {
                    btnstart.setText("Complete");
                    textviewresult.setText("You have tapped " + String.valueOf(Tapcount) + " times");
                    btngiveup.setEnabled(false);
                    start = false;
                }
            }
        }

    }
}
