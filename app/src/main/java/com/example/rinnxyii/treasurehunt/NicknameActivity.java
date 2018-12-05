package com.example.rinnxyii.treasurehunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NicknameActivity extends Activity {

    EditText editTextNickname;
    Button buttonGo;
    SharedPreferences sp;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        editTextNickname = (EditText) findViewById(R.id.editTextNickname);
        buttonGo = (Button) findViewById(R.id.buttonGO);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = sp.edit();

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
