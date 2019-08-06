package com.freeoda.franktirkey.smfe;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    TextView tvGreeting;
    Button btnGreeting,btnChat;
    String username ="Error 554/SPrefViolate";
    String buttonGreeting = " ";
    boolean deviceClearance = false,timer = false ;
    public static final String myPrefFile = "com.franktirkey.freeoda.prefFile";

    private static int devRegError = 0;
    public static boolean devRegistraton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //Menu------------------------
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayUseLogoEnabled(true);

        //---------------------------------------------

        //---------------------------Firebase Analytics
//         Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
//        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
//        -------------------------------------------------

        tvGreeting = findViewById(R.id.tvGreeting);
        btnGreeting = findViewById(R.id.btnGreet);
        btnChat = findViewById(R.id.btnChat);

        buttonGreeting = btnGreeting.getText().toString();

        setUserFromBackendless();

        tvGreeting.setText("WELCOME "+username+
                " TO SMFE (STUDY MATERIAL FOR ENGINEERING) HOPE! " +
                "THIS WILL HELP YOU TO DO BETTER IN YOUR ACADEMICS"
                +"\n Version: 9.2.0 Release");


        //------------------------------------- BackendLess and FireBase Integration and device register--

//        List<String> channels = new ArrayList<String>();
//        channels.add( "default" ); // channel name for testing
//            Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
//                @Override
//                public void handleResponse(DeviceRegistrationResult response) {
//
//                    deviceClearance = true;
//                    Toast.makeText( MainActivity.this, "Device Registered! WELCOME",
//                            Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void handleFault(BackendlessFault fault) {
//
//                        Toast.makeText( MainActivity.this, "Error registering " + fault.getMessage().trim(),
//                                Toast.LENGTH_LONG).show();
//                }
//            });
        devRegistraton = regDevice();
        //-------------------------------------

        if(devRegistraton){
            devRegistered();
        }

        btnGreeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                String st = "MainActivity_btn_continue";
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID,"btnGreeting");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Continue");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
                regDevice();
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
                if(deviceClearance) {
                    startActivity(new Intent(MainActivity.this, SelectBranch.class));
                }
                else {
                    Toast.makeText(MainActivity.this,"Failed to Register Device, Check Internet!",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,StartChatActivity.class);
                intent.putExtra("name", username);
                startActivity(intent);
            }
        });

    }

    public boolean regDevice(){

        final boolean[] regStatus = {false};
        List<String> channels = new ArrayList<String>();
        channels.add( "default" ); // channel name for testing

        Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() {
            @Override
            public void handleResponse(DeviceRegistrationResult response) {

                deviceClearance = true;
                Toast.makeText( MainActivity.this, "Device Registered! WELCOME",
                        Toast.LENGTH_SHORT).show();
                boolean rg = true;

                regStatus[0] = rg;
                devRegistered();

            }

            @Override
            public void handleFault(BackendlessFault fault) {

                if(devRegError < 5){
                    Toast.makeText( MainActivity.this, "Error registering " + fault.getMessage().trim(),
                            Toast.LENGTH_LONG).show();
                }
                else{

                    CountDownTimer ct = new CountDownTimer(5000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                            final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                            boolean isConnected;

                            isConnected = activeNetwork != null &&
                                    activeNetwork.isConnectedOrConnecting();

                            if(!isConnected){Toast.makeText( MainActivity.this, "Turn on Internet ",
                                    Toast.LENGTH_SHORT).show();}
                            regStatus[0] = false;

                        }

                        @Override
                        public void onFinish() {
                            regDevice();
                            devRegError++;
                        }
                    };
                }


            }
        });
        return regStatus[0];
    }

    public void devRegistered(){

        CountDownTimer count = new CountDownTimer(5000,1000){
            public void onTick(long millisUntilFinished){
                timer = false;
                btnGreeting.setBackground(getResources().getDrawable(R.drawable.button_fade));
                btnGreeting.setText("Getting API Key");
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
                    regDevice();
                }
            }
        }.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.feedback:
                Toast.makeText(MainActivity.this,"feedback Clicked",Toast.LENGTH_SHORT).show();

                //----------------------------feedback Intent

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,"franktirkey@gmail.com");
                startActivity(Intent.createChooser(intent,"Send Mail to" + " FrankMiles"));

                //---------------------------------------------

                break;
            case R.id.downloadUpdate:

                Toast.makeText(MainActivity.this,"Checking for updates...",Toast.LENGTH_LONG).show();

                ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

                    CountDownTimer count = new CountDownTimer(10000,1000){
                        public void onTick(long millisUntilFinished){


                        }

                        @Override
                        public void onFinish() {
                            Toast.makeText(MainActivity.this,"Downloading see notification bar",Toast.LENGTH_LONG).show();
                            trigerUpdateDownload();
                        }
                    }.start();
                }

                else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                    Toast.makeText(MainActivity.this,"Not Connected to Internet",Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //---------------------Downloading Update Stuff
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            DownloadManager downloadManager = (DownloadManager) getSystemService(MainActivity.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse("https://backendlessappcontent.com/9BDC094E-E5BB-70DE-FF63-69DAE6EBBD00/E453D360-7CB0-0699-FF8D-2AB0E7481700/files/SMFE.apk");
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("SMFE");
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SMFE.apk");
            long downloadID = downloadManager.enqueue(request);

        }
        else{

            Toast.makeText(MainActivity.this,"Please allow the permission",Toast.LENGTH_SHORT).show();
        }
    }

    public void trigerUpdateDownload(){

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


    }
    //-------------------------------------------------------------

    private void setUserFromBackendless(){

        SharedPreferences sharedPreferences =
                getSharedPreferences(myPrefFile,MODE_PRIVATE);
        username = sharedPreferences.getString("name","");

    }

}
