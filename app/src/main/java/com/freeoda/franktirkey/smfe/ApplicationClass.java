package com.freeoda.franktirkey.smfe;

import android.app.Application;

import com.backendless.Backendless;

public class ApplicationClass extends Application {
    public static final String APPLICATION_ID = "9BDC094E-E5BB-70DE-FF63-69DAE6EBBD00";
    public static final String API_KEY = "06F74842-C689-9D32-FFFF-C1217F5A9900";
    public static final String SERVER_URL = "https://api.backendless.com";

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );
    }
}
