package com.freeoda.franktirkey.smfe;

import android.Manifest;
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

        //Menu------------------------
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayUseLogoEnabled(true);

        //---------------------------------------------

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

                deviceClearance = true;
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText( MainActivity.this, "Error registering " + fault.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        //-------------------------------------

        CountDownTimer count = new CountDownTimer(5000,1000){
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

}
