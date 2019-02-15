package com.freeoda.franktirkey.smfe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

/*
        jUST AN LOGIN SCREEN AND SQL DATABASE INTEGRATION ON THIS ACTIVITY
        -> to selectBranch
 */

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etUsername,etPassword;
    CheckBox cbTermsAndCondition;

    SharedPreferences pref;
    protected String spUsername,spPassword;
    protected Boolean spTc;
    private static final String sharedVault = "com.freeoda.franktirkey.smfe";

    private String userName = "", password = "";
    private boolean tc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //*********************************************
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.drawable.icon);
        actionBar.setTitle("FrankMiles");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //*****************************************

        btnLogin = findViewById(R.id.btnLogin);
        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        cbTermsAndCondition = findViewById(R.id.cbTermsAndCondition);

        pref = getSharedPreferences(sharedVault,MODE_PRIVATE);
        spUsername = pref.getString("username","");
        spPassword = pref.getString("password","");
        etUsername.setText(spUsername);
        etPassword.setText(spPassword);
        cbTermsAndCondition.setChecked(spTc = pref.getBoolean("tc",false));

        cbTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tc = cbTermsAndCondition.isChecked();
                SharedPreferences.Editor editor = getSharedPreferences(sharedVault,MODE_PRIVATE).edit();
                editor.putBoolean("tc",cbTermsAndCondition.isChecked());
                editor.apply();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();

                if(checkCredentials(userName,password)) {
                    Intent intent = new Intent(MainActivity.this, SelectBranch.class);
                    SharedPreferences.Editor editor = getSharedPreferences(sharedVault,MODE_PRIVATE).edit();
                    editor.putString("username",etUsername.getText().toString().trim());
                    editor.putString("password",etPassword.getText().toString().trim());
                    editor.putBoolean("tc",true);
                    editor.apply();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"Check the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }
    private boolean checkCredentials(String un,String pass){
        if(un.equalsIgnoreCase(pass) && !un.isEmpty() && cbTermsAndCondition.isChecked()){
            tc = pref.getBoolean("tc",false);
            return tc;
        }
        else{
            cbTermsAndCondition.setChecked(false);
        }
        return false;
    }
}
