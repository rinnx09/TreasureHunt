package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;


public class ShakeActivity extends AppCompatActivity implements ShakeDetector.Listener {
    int shakecount = 1;
    private int missionShakeNum;
    private Boolean timesup = false;
    private Boolean start = false;
    private TextView textviewShake, textviewresult;
    private Button btnstart, btngiveup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        //  Log.e("TAG","Message");
        textviewShake = (TextView) findViewById(R.id.textViewShake);
        textviewresult = (TextView) findViewById(R.id.textViewresult3);
        btnstart = (Button) findViewById(R.id.buttonstart3);
        btngiveup = (Button) findViewById(R.id.buttonGiveup3);

        Intent intent = getIntent();
        missionShakeNum = intent.getIntExtra(HomeActivity.MISSION_VALUE, 0);
        textviewShake.setText("Mission: Shake " + String.valueOf(missionShakeNum) + " times");
        //    Toast.makeText(getApplicationContext(), String.valueOf(missionShakeNum), Toast.LENGTH_LONG).show();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector shakedetector = new ShakeDetector(this);
        shakedetector.start(sensorManager);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnstart.getText().equals("Complete")) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("RESULT", true);
                    returnIntent.putExtra("MARK", shakecount);
                    returnIntent.putExtra("type","SHAKE");
                    setResult(Activity.RESULT_OK, returnIntent);
                    shakecount = 1;
                    finish();
                } else {
                    start = true;
                    shakecount = 1;
                    timesup = false;
                    Toast.makeText(getApplicationContext(), "start counting...", Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 10 seconds
                            if (!btnstart.getText().equals("Complete")) {
                                timesup = true;

                                btnstart.setText("Try again");
                                textviewresult.setText("You have shaked " + String.valueOf(shakecount) + " times");
                            }
                        }
                    }, 10000);  //the time is in miliseconds
                }
            }
        });


        btngiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("RESULT", false);
                returnIntent.putExtra("MARK", 0);
                returnIntent.putExtra("type","SHAKE");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void hearShake() {
        //do any custom business logic
        if (start) {
            if (timesup == false) {
                if (shakecount < missionShakeNum) {
                    if (shakecount == 1) {
                        Toast.makeText(this, "Keep Shaking", Toast.LENGTH_LONG).show();
                    }
                    shakecount = shakecount + 1;
                } else {
                    btnstart.setText("Complete");
                    textviewresult.setText("You have shaked " + String.valueOf(shakecount) + " times");
                    btngiveup.setEnabled(false);
                    start = false;
                }
            }else{
                start = false;
            }
        }

    }
}
