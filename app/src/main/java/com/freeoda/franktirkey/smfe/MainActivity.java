package com.freeoda.franktirkey.smfe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    TextView tvGreeting;
    Button btnGreeting;
    String username =" ",buttonGreeting = " ";
    boolean deviceClearance = false,timer = false ;
    public static final String myPrefFile = "com.franktirkey.freeoda.prefFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //--------------------------------------------------Firebase Analytics
//         Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
//        -------------------------------------------------

        tvGreeting = findViewById(R.id.tvGreeting);
        btnGreeting = findViewById(R.id.btnGreet);
        buttonGreeting = btnGreeting.getText().toString();
        SharedPreferences pref = getSharedPreferences(myPrefFile,MODE_PRIVATE);
        tvGreeting.setText("WELCOME "+pref.getString("user", " ")+
                " TO SMFE (STUDY MATERIAL FOR ENGINEERING) HOPE! THIS WILL HELP YOU TO DO BETTER IN YOUR ACADEMICS");


        //------------------------------------- BackendLess and FireBase Integration and device register--

        List<String> channels = new ArrayList<String>();
        channels.add( "default" ); // channel name for testing
        Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
            @Override
            public void handleResponse(DeviceRegistrationResult response) {
                Toast.makeText( MainActivity.this, "Device registered!",
                        Toast.LENGTH_LONG).show();
                deviceClearance = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText( MainActivity.this, "Error registering " + fault.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        //-------------------------------------

        CountDownTimer count = new CountDownTimer(10000,1000){
            public void onTick(long millisUntilFinished){
                timer = false;
                btnGreeting.setBackground(getResources().getDrawable(R.drawable.button_fade));
                btnGreeting.setText("Reciving Key");
            }

            @Override
            public void onFinish() {
                if(deviceClearance) {
                    btnGreeting.setText(buttonGreeting);
                    btnGreeting.setBackground(getResources().getDrawable(R.drawable.buttons));
                    timer = true;
                }
                else{
                    Toast.makeText(MainActivity.this,"Failed to get device Clearance",Toast.LENGTH_SHORT).show();
                }
            }
        }.start();

        btnGreeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String st = "MainActivity_btn_continue";
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"btnGreeting");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Continue");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                if(deviceClearance && timer) {
                    startActivity(new Intent(MainActivity.this, SelectBranch.class));
                }
                else {
                    Toast.makeText(MainActivity.this,"Failed to Register Device, Check Internet!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
