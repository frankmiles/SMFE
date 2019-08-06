package com.freeoda.franktirkey.smfe;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

public class ApplicationClass extends Application {
    public static final String APPLICATION_ID = "9BDC094E-E5BB-70DE-FF63-69DAE6EBBD00";
    public static final String API_KEY = "B6ACFDC2-15FC-4EDB-84B7-60B459EACDD2";
    public static final String SERVER_URL = "https://api.backendless.com";


        public static final String DEFAULT_CHANNEL = "chat";

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );


    }
}
