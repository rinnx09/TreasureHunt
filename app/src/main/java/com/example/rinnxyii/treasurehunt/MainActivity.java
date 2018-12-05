package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    Button buttonStart;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button)findViewById(R.id.buttonStart);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = sp.getString(getString(R.string.preference_nickname), "");

                if (nickname.equals("")){
                    goToNickName();

                }else{
                    goToHome();
                }
            }
        });
    }

    public void goToHome(){
        Intent intentHome = new Intent (MainActivity.this, HomeActivity.class);
        startActivity(intentHome);
    }

    public void goToNickName(){
        Intent intentNickname = new Intent(MainActivity.this, NicknameActivity.class);
        startActivity(intentNickname);
    }
}
