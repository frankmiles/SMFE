package com.freeoda.franktirkey.smfe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvGreeting;
    Button btnGreeting;
    String username =" ";
    public static final String myPrefFile = "com.franktirkey.freeoda.prefFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvGreeting = findViewById(R.id.tvGreeting);
        btnGreeting = findViewById(R.id.btnGreet);
        SharedPreferences pref = getSharedPreferences(myPrefFile,MODE_PRIVATE);
        tvGreeting.setText("WELCOME "+pref.getString("user", " ")+
                " TO SMFE (STUDY MATERIAL FOR ENGINEERING) HOPE! THIS WILL HELP YOU TO DO BETTER IN YOUR ACADEMICS");

        btnGreeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SelectBranch.class));
            }
        });

    }

}
