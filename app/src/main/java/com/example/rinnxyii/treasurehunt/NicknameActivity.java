package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class NicknameActivity extends Activity {

    EditText editTextNickname;
    Button buttonGo;
    SharedPreferences sp;
    SharedPreferences.Editor mEditor;
    ImageButton imageButton1, imageButton2, imageButton3, imageButton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        buttonGo = (Button) findViewById(R.id.buttonGO);

        imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imageButton4 = (ImageButton) findViewById(R.id.imageButton4);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = sp.edit();

        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putString(getString(R.string.preference_profilepic),"1");
                mEditor.commit();
            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putString(getString(R.string.preference_profilepic), "2");
                mEditor.commit();
            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putString(getString(R.string.preference_profilepic), "3");
                mEditor.commit();
            }
        });

        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putString(getString(R.string.preference_profilepic), "4");
                mEditor.commit();
            }
        });

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextNickname.getText().toString();

                mEditor.putString(getString(R.string.preference_nickname), name);
                mEditor.commit();

                Intent intent = new Intent(NicknameActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}
