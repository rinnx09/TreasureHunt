package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScanQRcodeActivity extends AppCompatActivity {
    //qr code scanner object
    private IntentIntegrator qrScan;
    private TextView textResult;
    private Button btnstart, btngiveup;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        btnstart = (Button) findViewById(R.id.buttonstart4);
        btngiveup = (Button) findViewById(R.id.buttonGiveup4);
        textResult = (TextView) findViewById(R.id.textViewQRresult);
        //intializing scan object
        qrScan = new IntentIntegrator(this);

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!btnstart.getText().equals("Complete")) {
                    //initiating the qr code scan
                    qrScan.initiateScan();
                }

            }
        });

        btngiveup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("RESULT", false);
                returnIntent.putExtra("MARK", 0);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    textResult.setText(obj.getString("access_time") + " " + obj.getString("access_date") + " " + obj.getString("ID"));

                    SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat sdfT = new SimpleDateFormat("HH:mm");
                    String dateDuration[] = obj.getString("access_date").split("-");
                    String timeDuration[] = obj.getString("access_time").split("-");
                    String strcurrentDate = sdfD.format(System.currentTimeMillis());
                    String strcurrentTime = sdfT.format(System.currentTimeMillis());
                    try {
                        Date startdate = sdfD.parse(dateDuration[0]);
                        Date enddate = sdfD.parse(dateDuration[1]);
                        Date starttime = sdfT.parse(timeDuration[0]);
                        Date endtime = sdfT.parse(timeDuration[1]);
                        Date currentDate = sdfD.parse(strcurrentDate);
                        Date currentTime = sdfT.parse(strcurrentTime);

                        if ((currentDate.after(startdate) && currentDate.before(enddate)) || currentDate.equals(startdate) || currentDate.equals(enddate)) {
                            if ((currentTime.after(starttime) && currentTime.before(endtime)) || currentTime.equals(starttime) || currentTime.equals(endtime)) {
                               Intent intent=getIntent();
                              String id=intent.getStringExtra("ID");
                                textResult.setText(obj.getString("ID")+" "+id);

                                sp = PreferenceManager.getDefaultSharedPreferences(this);
                                editor = sp.edit();

                                editor.putString("ID",id);
                                editor.commit();

                                sp.getString("ID", "");


                            }else{
                                Toast.makeText(this,"Must scan it within the event duration time", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(this,"Must scan it within the event duration day", Toast.LENGTH_LONG).show();
                        }
                    } catch (ParseException e) {
                      //  e.printStackTrace();
                        Toast.makeText(this,"This QR code is Invalid", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
